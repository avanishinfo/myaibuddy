package dev.avanish.my_ai_buddy.matches;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {

}
