package dev.avanish.my_ai_buddy.conversations;

import dev.avanish.my_ai_buddy.profile.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
}
