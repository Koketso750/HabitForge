package swp.habitforge.habitforge.coach;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.feedback.Feedback;
import swp.habitforge.habitforge.feedback.FeedbackService;
import swp.habitforge.habitforge.wellnesscontent.WellnessContent;
import swp.habitforge.habitforge.wellnesscontent.WellnessContentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class CoachController {

    @Autowired private CoachRepository coachRepository;
    @Autowired private CoachService coachService;
    @Autowired private WellnessContentService wellnessContentService;
    @Autowired private FeedbackService feedbackService;


    // Directory where profile pictures will be stored
    private final String UPLOAD_DIR = "src/main/resources/static/images/coaches/";

    @GetMapping("/coach/sign/up")
    public String goToCoachSignUp(){
        return "coach-sign-up";
    }

    @GetMapping("/login/coach")
    public String goToLogin(){
        return "login_coach";
    }

    @GetMapping("/admin_shauntel")
    public String manageCoaches(Model model){
        List<Coach> listCoaches = coachService.listAll();
        model.addAttribute("listCoaches", listCoaches);
        return "admin_shauntel";
    }

    @GetMapping("/coach_form")
    public String showAddCoachForm(Model model){
        model.addAttribute("coach", new Coach());
        return "coach_form";
    }

    @GetMapping("/coach/register")
    public String showCoachCreatedForm(Model model){
        model.addAttribute("coach", new Coach());
        return "login_coach";
    }

    @PostMapping("/coach/register")
    public String processCoachSignUp(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam MultipartFile profilePicture,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String bio,
            @RequestParam String expertise,
            RedirectAttributes redirectAttributes) throws IOException {

        if (coachService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists. Please log in.");
            return "redirect:/coach/register";
        }

        Coach coach = new Coach();
        coach.setName(name);
        coach.setSurname(surname);
        coach.setEmail(email);
        coach.setPassword(password);
        coach.setBio(bio);
        coach.setExpertise(expertise);
        coach.setCreatedAt(new Date());
        coach.setUpdatedAt(new Date());

        // Save coach first to get ID
        Coach savedCoach = coachRepository.save(coach);

        // Handle profile picture upload
        if (!profilePicture.isEmpty()) {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileExtension = getFileExtension(profilePicture.getOriginalFilename());
            String fileName = "coach_" + savedCoach.getCoachId() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, profilePicture.getBytes());

            String dbFilePath = "/images/coaches/" + fileName;
            savedCoach.setProfilePicture(dbFilePath);
            coachRepository.save(savedCoach);
        }

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login/coach";
    }

    // NEW: Helper method to get file extension
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

    // ===========================
    // NEW LOGIN LOGIC FOR COACHES
    // ===========================

    @PostMapping("/login/coach/dashboard")
    public String processCoachLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<Coach> coach = coachRepository.findByEmail(email);

        // Check if the coach is not found
        if (coach.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Email not found. Please check your email or sign up.");
            return "redirect:/login/coach";
        }

        // Check if the password is correct
        if (!coach.get().getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect password. Please try again.");
            return "redirect:/login/coach";
        }

        // Store coach in session
        session.setAttribute("loggedInCoach", coach.get());

        return "redirect:/coach/dashboard";
    }

    @GetMapping("/coach/dashboard")
    public String showCoachDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Coach loggedInCoach = (Coach) session.getAttribute("loggedInCoach");

        if (loggedInCoach == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access your dashboard.");
            return "redirect:/login/coach";
        }

        List<Feedback> feedbackList = feedbackService.getFeedbackForCoachWithUser(loggedInCoach);
        List<WellnessContent> recentContent = wellnessContentService.getContentByCoach(loggedInCoach);

        // Calculate average ratings
        Map<Long, Double> averageRatings = new HashMap<>();
        for (WellnessContent content : recentContent) {
            List<Feedback> contentFeedback = content.getFeedback();
            if (contentFeedback != null && !contentFeedback.isEmpty()) {
                double avgRating = contentFeedback.stream()
                        .map(Feedback::getRating)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0);
                averageRatings.put(content.getContentId().longValue(), avgRating);
            } else {
                averageRatings.put(content.getContentId().longValue(), 0.0);
            }
        }

        Long contentCount = wellnessContentService.countContentByCoach(loggedInCoach);

        model.addAttribute("coach", loggedInCoach);
        model.addAttribute("recentContent", recentContent);
        model.addAttribute("contentCount", contentCount);
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("averageRatings", averageRatings);

        return "coach_dashboard";
    }

    @PostMapping("/coach/admin/update/'")
    public String updateCoach(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam String bio,
            @RequestParam String expertise,
            @RequestParam(required = false) MultipartFile profilePicture,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        Coach loggedInCoach = (Coach) session.getAttribute("loggedInCoach");

        if (loggedInCoach == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to update your profile.");
            return "redirect:/login/coach";
        }

        // Update coach information
        loggedInCoach.setName(name);
        loggedInCoach.setSurname(surname);
        loggedInCoach.setEmail(email);
        loggedInCoach.setBio(bio);
        loggedInCoach.setExpertise(expertise);
        loggedInCoach.setUpdatedAt(new Date());

        // Handle password change if requested
        if (currentPassword != null && !currentPassword.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
            if (!loggedInCoach.getPassword().equals(currentPassword)) {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
                return "redirect:/coach/dashboard";
            }
            loggedInCoach.setPassword(newPassword);
        }

        // Handle profile picture update if provided
        if (profilePicture != null && !profilePicture.isEmpty()) {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileExtension = getFileExtension(profilePicture.getOriginalFilename());
            String fileName = "coach_" + loggedInCoach.getCoachId() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, profilePicture.getBytes());

            String dbFilePath = "/images/coaches/" + fileName;
            loggedInCoach.setProfilePicture(dbFilePath);
        }

        // Save updated coach
        coachRepository.save(loggedInCoach);

        // Update session
        session.setAttribute("loggedInCoach", loggedInCoach);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/coach/dashboard";
    }


    @PostMapping("/coach/update")
    public String updateCoachProfile(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam String bio,
            @RequestParam String expertise,
            @RequestParam(required = false) MultipartFile profilePicture,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        Coach loggedInCoach = (Coach) session.getAttribute("loggedInCoach");

        if (loggedInCoach == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to update your profile.");
            return "redirect:/login/coach";
        }

        // Update coach information
        loggedInCoach.setName(name);
        loggedInCoach.setSurname(surname);
        loggedInCoach.setEmail(email);
        loggedInCoach.setBio(bio);
        loggedInCoach.setExpertise(expertise);
        loggedInCoach.setUpdatedAt(new Date());

        // Handle password change if requested
        if (currentPassword != null && !currentPassword.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
            if (!loggedInCoach.getPassword().equals(currentPassword)) {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
                return "redirect:/coach/dashboard";
            }
            loggedInCoach.setPassword(newPassword);
        }

        // Handle profile picture update if provided
        if (profilePicture != null && !profilePicture.isEmpty()) {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileExtension = getFileExtension(profilePicture.getOriginalFilename());
            String fileName = "coach_" + loggedInCoach.getCoachId() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, profilePicture.getBytes());

            String dbFilePath = "/images/coaches/" + fileName;
            loggedInCoach.setProfilePicture(dbFilePath);
        }

        // Save updated coach
        coachRepository.save(loggedInCoach);

        // Update session
        session.setAttribute("loggedInCoach", loggedInCoach);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/coach/dashboard";
    }

    @GetMapping("/logout/coach")
    public String logoutCoach(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been successfully logged out.");
        return "redirect:/login/coach";
    }
}