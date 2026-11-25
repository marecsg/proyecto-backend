package pe.edu.upeu.LabManager.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        String json = System.getenv("FIREBASE_CREDENTIALS");

        GoogleCredentials credentials;

        if (json != null && !json.isBlank()) {
            credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(
                            json.getBytes(StandardCharsets.UTF_8)
                    )
            );
        } else {
            credentials = GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}