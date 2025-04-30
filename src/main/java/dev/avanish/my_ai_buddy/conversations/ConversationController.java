package dev.avanish.my_ai_buddy.conversations;

import dev.avanish.my_ai_buddy.profile.Profile;
import dev.avanish.my_ai_buddy.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RestController
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;
    private final ConversationService conversationService;

    public ConversationController(ConversationRepository conversationRepository, ProfileRepository profileRepository, ConversationService conversationService) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
        this.conversationService = conversationService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest request){
        profileRepository.findById(request.profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile Id"));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString() ,
                request.profileId,
                new ArrayList<>()
        );
        conversationRepository.save(conversation);
        return conversation;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversation(
            @PathVariable String conversationId
    ){
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to find conversation-id" + conversationId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageToConversation(
            @PathVariable String conversationId,
            @RequestBody ChatMessage chatMessage){

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to find conversation-id" + conversationId));

        String matchProfileId = conversation.profileId();

        Profile profile = profileRepository.findById(matchProfileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find profile Id"));

        Profile user = profileRepository.findById(chatMessage.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Author Id"));

        //// TODO: 09-04-2025 need to validate that the author of a message hapens to be only the profile associated with the user

        ChatMessage messageWithTime = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.authorId(),
                LocalDateTime.now()
        );
        conversation.messages().add(messageWithTime);
        //hey LLM send message and figure out next message

        conversationService.generateProfileResponse(conversation, profile, user);

        conversationRepository.save(conversation);

        return conversation;
    }


    public record CreateConversationRequest(
            String profileId
    ){}
}
