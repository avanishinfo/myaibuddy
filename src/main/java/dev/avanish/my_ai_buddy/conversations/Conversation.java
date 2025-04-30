package dev.avanish.my_ai_buddy.conversations;

import dev.avanish.my_ai_buddy.profile.Profile;

import java.util.List;

public record Conversation(
        String id,
        String profileId,
        List<ChatMessage> messages
) {
}
