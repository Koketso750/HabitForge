package swp.habitforge.habitforge.wellnesscontent;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import swp.habitforge.habitforge.coach.Coach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class WellnessContentController {

    @Autowired
    private WellnessContentRepository wellnessContentRepository;

    @Autowired
    private WellnessContentService wellnessContentService;

    // Directory where content files will be stored
    private final String UPLOAD_DIR = "src/main/resources/static/content/";

    @GetMapping("/coach/content")
    public String viewCoachContent(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Coach loggedInCoach = (Coach) session.getAttribute("loggedInCoach");

        if (loggedInCoach == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access your content.");
            return "redirect:/login/coach";
        }

        List<WellnessContent> contentList = wellnessContentService.getContentByCoach(loggedInCoach);
        model.addAttribute("contentList", contentList);
        model.addAttribute("coach", loggedInCoach);

        return "coach_content";
    }

    @PostMapping("/coach/content/upload")
    public String uploadContent(
            @RequestParam("contentTitle") String contentTitle,
            @RequestParam("contentType") String contentType,
            @RequestParam("contentDescription") String contentDescription,
            @RequestParam("contentFile") MultipartFile contentFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        Coach loggedInCoach = (Coach) session.getAttribute("loggedInCoach");

        if (loggedInCoach == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to upload content.");
            return "redirect:/login/coach";
        }

        // Create wellness content object
        WellnessContent content = new WellnessContent();
        content.setContentTitle(contentTitle);
        content.setContentType(contentType);
        content.setContentDescription(contentDescription);
        content.setCoach(loggedInCoach);
        content.setCreatedAt(new Date());
        content.setUpdatedAt(new Date());

        // Process file upload
        if (!contentFile.isEmpty()) {
            // Create directory if it doesn't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename
            String fileExtension = getFileExtension(contentFile.getOriginalFilename());
            String fileName = "content_" + UUID.randomUUID().toString() + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Save file
            Files.write(filePath, contentFile.getBytes());

            // Set content URL and file type
            String contentUrl = "/content/" + fileName;
            content.setContentUrl(contentUrl);
            content.setContentFileType(fileExtension.substring(1)); // Remove the dot from extension
        }

        // Save content to database
        wellnessContentRepository.save(content);

        redirectAttributes.addFlashAttribute("success", "Content uploaded successfully!");
        return "redirect:/coach/dashboard";
    }

    // Helper method to get file extension
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return filename.substring(dotIndex);
    }

    @GetMapping("/coach/content/delete/{contentId}")
    public String deleteContent(@PathVariable Integer contentId, HttpSession session, RedirectAttributes redirectAttributes) {
        Coach loggedInCoach = (Coach) session.getAttribute("loggedInCoach");

        if (loggedInCoach == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to delete content.");
            return "redirect:/login/coach";
        }

        WellnessContent content = wellnessContentRepository.findById(contentId).orElse(null);

        if (content != null && content.getCoach().getCoachId().equals(loggedInCoach.getCoachId())) {
            // Delete the file if it exists
            if (content.getContentUrl() != null && !content.getContentUrl().isEmpty()) {
                String filePath = "src/main/resources/static" + content.getContentUrl();
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (IOException e) {
                    // Log error but continue with database deletion
                    System.err.println("Could not delete file: " + e.getMessage());
                }
            }

            wellnessContentRepository.deleteById(contentId);
            redirectAttributes.addFlashAttribute("success", "Content deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this content.");
        }

        return "redirect:/coach/dashboard";
    }
}