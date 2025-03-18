package swp.habitforge.habitforge.firebase;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.IOException;
import java.util.Optional;
@Service
public class FirebaseStorageService {

    public String uploadProfilePicture(MultipartFile file, String username) throws IOException {
        // Get a reference to Firebase storage
        Bucket bucket = StorageClient.getInstance().bucket();

        // Generate a unique file name using the username
        String fileName = "profile-pictures/" + username + ".jpg"; // Modify this based on your requirements

        // Upload the file to Firebase Storage
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

        // Return the file's public URL
        return "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName() + "/o/"
                + fileName.replace("/", "%2F") + "?alt=media";
    }
}
