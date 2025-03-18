package swp.habitforge.habitforge.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        // Load the firebase-config.json file from the resources directory.
        InputStream serviceAccount = new ClassPathResource("habitforge-5230e-firebase-adminsdk-fbsvc-36d6f865cb.json").getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("habitforge-5230e.firebasestorage.app") // Add your storage bucket name here
                .build();

        return FirebaseApp.initializeApp(options);
    }
}