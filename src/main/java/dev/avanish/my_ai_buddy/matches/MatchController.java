package dev.avanish.my_ai_buddy.matches;

import dev.avanish.my_ai_buddy.conversations.Conversation;
import dev.avanish.my_ai_buddy.conversations.ConversationRepository;
import dev.avanish.my_ai_buddy.profile.Profile;
import dev.avanish.my_ai_buddy.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MatchController {

    private final MatchRepository matchRepository;
    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;

    public MatchController(MatchRepository matchRepository, ConversationRepository conversationRepository, ProfileRepository profileRepository) {
        this.matchRepository = matchRepository;
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
    }

    public record CreateMatchRequest(String profileId){}

    @CrossOrigin(origins = "*")
    @PostMapping("/matches")
    public Match createNewMatch(@RequestBody CreateMatchRequest request){
        Profile profile = profileRepository.findById(request.profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Profile Id"));

        // TODO: 10-04-2025 make sure thre are no existing conversatin with profile already.


        Conversation conversation = new Conversation(
                UUID.randomUUID().toString() ,
                request.profileId,
                new ArrayList<>()
        );
        conversationRepository.save(conversation);
        Match match=  new Match(
                UUID.randomUUID().toString(),
                profile,
                conversation.id());

        matchRepository.save(match);
        return match;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/matches")
    public List<Match> getAllMatches(){
        return matchRepository.findAll();
    }
}
