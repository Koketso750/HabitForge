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

        return "coach_dashboard";
    }

    @PostMapping("/coach/content/upload")
    public String uploadContent(
            @RequestParam("contentTitle") String contentTitle,
            @RequestParam("contentType") String contentType,
            @RequestParam("contentDescription") String contentDescription,
            @RequestParam("contentUrl") String contentUrl,
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
        content.setContentUrl(contentUrl);


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

            wellnessContentRepository.deleteById(contentId);

            redirectAttributes.addFlashAttribute("success", "Content deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this content.");
        }

        return "redirect:/coach/dashboard";
    }
}