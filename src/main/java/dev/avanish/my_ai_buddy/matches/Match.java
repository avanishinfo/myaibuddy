package dev.avanish.my_ai_buddy.matches;

import dev.avanish.my_ai_buddy.profile.Profile;

public record Match(
        String id,
        Profile profile,
        String conversationId
) {
}
