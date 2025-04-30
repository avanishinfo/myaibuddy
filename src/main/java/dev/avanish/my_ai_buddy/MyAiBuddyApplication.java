package dev.avanish.my_ai_buddy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.avanish.my_ai_buddy.conversations.ConversationRepository;
import dev.avanish.my_ai_buddy.matches.MatchRepository;
import dev.avanish.my_ai_buddy.profile.Gender;
import dev.avanish.my_ai_buddy.profile.Profile;
import dev.avanish.my_ai_buddy.profile.ProfileCreationService;
import dev.avanish.my_ai_buddy.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class  MyAiBuddyApplication implements CommandLineRunner {

	@Autowired
	private ProfileCreationService profileCreationService;

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ConversationRepository conversationRepository;
	@Autowired
	private MatchRepository matchRepository;

	public static void main(String[] args) {
		SpringApplication.run(MyAiBuddyApplication.class, args);
	}

	public void run(String... args){

		profileCreationService.createProfiles(2);
		saveProfilesToDB();

		/*clearAllData();
		saveProfilesToDB();*/
	}

	private void clearAllData() {
		conversationRepository.deleteAll();
		matchRepository.deleteAll();
		profileRepository.deleteAll();
	}

	@Value("#{${myaibuddy.character.user}}")
	private Map<String, String> userProfileProperties;

	public void saveProfilesToDB() {
		Gson gson = new Gson();
		try {
			List<Profile> existingProfiles = gson.fromJson(
					new FileReader("profiles.json"),
					new TypeToken<ArrayList<Profile>>() {}.getType()
			);
			profileRepository.deleteAll();
			profileRepository.saveAll(existingProfiles);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		Profile profile = new Profile(
				userProfileProperties.get("id"),
				userProfileProperties.get("firstName"),
				userProfileProperties.get("lastName"),
				Integer.parseInt(userProfileProperties.get("age")),
				userProfileProperties.get("ethnicity"),
				Gender.valueOf(userProfileProperties.get("gender")),
				userProfileProperties.get("bio"),
				userProfileProperties.get("imageUrl"),
				userProfileProperties.get("myersBriggsPersonalityType")
		);
		System.out.println(userProfileProperties);
		profileRepository.save(profile);

	}
}
