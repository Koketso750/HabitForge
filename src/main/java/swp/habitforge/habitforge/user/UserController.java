package swp.habitforge.habitforge.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.notification.Notification;
import swp.habitforge.habitforge.notification.NotificationRepository;
import swp.habitforge.habitforge.notification.NotificationService;
import swp.habitforge.habitforge.progress.ProgressRepository;
import swp.habitforge.habitforge.task.Task;
import swp.habitforge.habitforge.task.TaskRepository;
import swp.habitforge.habitforge.task.TaskService;
import swp.habitforge.habitforge.wellnesscontent.WellnessContent;
import swp.habitforge.habitforge.wellnesscontent.WellnessContentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WellnessContentService wellnessContentService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private TaskService taskService;

    @Autowired private TaskRepository taskRepository;

    @Autowired private ProgressRepository progressRepository;

    @Autowired private NotificationRepository notificationRepository;


    // Directory where profile pictures will be stored
    private final String UPLOAD_DIR = "src/main/resources/static/images/profiles/";

    // Display admin panel with list of all users
    @GetMapping("/admin_patience")
    public String manageUsers(Model model) {
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
        return "admin_patience";
    }

    // Show form for adding a new user
    @GetMapping("/user_form")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user_form";
    }


    // Process form submission for creating a new user
    @PostMapping("/user/create")
    public String createUser(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            // Set timestamps
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());

            // Save the user
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin_patience";
    }

    // Show form for editing an existing user
    @GetMapping("/user/update/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            model.addAttribute("user", user);
            return "edit_user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin_patience";
        }
    }

    // Process form submission for updating a user
    @PostMapping("/user/update/{id}")
    public String updateUser(
            @PathVariable("id") Integer id,
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Get existing user data
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

            // Update fields
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setProfilePicture(user.getProfilePicture());

            // Only update password if a new one was provided
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

            // Update timestamp
            existingUser.setUpdatedAt(new Date());

            // Save the updated user
            userRepository.save(existingUser);

            redirectAttributes.addFlashAttribute("success", "User updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin_patience";
    }

    // Delete a user
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin_patience";
    }

    @GetMapping("/user/sign/up")
    public String goToUserSignUp(){
        return "user-sign-up";
    }

    @PostMapping("/user/register")
    public String processUserSignUp(
            @RequestParam String username,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) throws IOException {

        // Check if username or email already exists
        if (userService.usernameExists(username)) {
            redirectAttributes.addFlashAttribute("error", "Username already exists. Please choose another one.");
            return "redirect:/user/sign/up";
        }

        if (userService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists. Please log in.");
            return "redirect:/user/sign/up";
        }

        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password); // In a real app, you should encrypt this password
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // Save user to get the ID
        User savedUser = userRepository.save(user);

        // Handle profile picture upload
        if (!profilePicture.isEmpty()) {
            // Create directory if it doesn't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename using user ID
            String fileExtension = getFileExtension(profilePicture.getOriginalFilename());
            String fileName = "user_" + savedUser.getUserId() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            // Save file to server
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, profilePicture.getBytes());

            // Update user with profile picture path
            String dbFilePath = "/images/profiles/" + fileName; // Path to be stored in database
            savedUser.setProfilePicture(dbFilePath);
            userRepository.save(savedUser);
        }

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login/user";
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

    @GetMapping("/login/user")
    public String showLoginPage() {
        return "login_user";
    }

    @PostMapping("/login/user/dashboard")
    public String processUserLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Find user by email
        User user = userRepository.findByEmail(email);

        // Check if user exists and password matches
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Email not found. Please check your email or sign up.");
            return "redirect:/login/user";
        }

        if (!user.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect password. Please try again.");
            return "redirect:/login/user";
        }

        // Store user in session
        session.setAttribute("loggedInUser", user);

        // Redirect to user dashboard
        return "redirect:/user/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String showUserDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access your dashboard.");
            return "redirect:/login/user";
        }

        // Get user's tasks
        List<Task> tasks = taskService.getTasksByUser(loggedInUser);
        long taskCount = tasks.stream().filter(task -> !task.getCompleted()).count();
        long completedTaskCount = tasks.stream().filter(Task::getCompleted).count();

        // Calculate progress percentage
        int progressPercentage = tasks.isEmpty() ? 0 : (int) ((completedTaskCount * 100) / tasks.size());

        // Get user's notifications
        List<Notification> notifications = notificationService.getUnreadNotificationsByUser(loggedInUser);
        long notificationCount = notifications.size();

        // Get all wellness content from all coaches
        List<WellnessContent> wellnessContent = wellnessContentService.getAllContent();

        // Add all attributes to the model
        model.addAttribute("user", loggedInUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskCount", taskCount);
        model.addAttribute("completedTaskCount", completedTaskCount);
        model.addAttribute("progressPercentage", progressPercentage);
        model.addAttribute("notifications", notifications);
        model.addAttribute("notificationCount", notificationCount);
        model.addAttribute("wellnessContent", wellnessContent);

        return "user_dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Clear session
        session.invalidate();

        redirectAttributes.addFlashAttribute("success", "You have been successfully logged out.");
        return "redirect:/login/user";
    }

    @PostMapping("/user/update")
    public String updateUserProfile(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) MultipartFile profilePicture,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to update your profile.");
            return "redirect:/login/user";
        }

        try {
            User user = userRepository.findById(loggedInUser.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Update basic info
            if (name != null && !name.isEmpty()) user.setName(name);
            if (surname != null && !surname.isEmpty()) user.setSurname(surname);
            if (email != null && !email.isEmpty()) user.setEmail(email);

            // Update password if provided
            if (currentPassword != null && !currentPassword.isEmpty() &&
                    newPassword != null && !newPassword.isEmpty()) {

                if (!user.getPassword().equals(currentPassword)) {
                    redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
                    return "redirect:/user/dashboard#settings";
                }

                user.setPassword(newPassword);
            }

            // Update profile picture if provided
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String fileExtension = getFileExtension(profilePicture.getOriginalFilename());
                String fileName = "user_" + user.getUserId() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, profilePicture.getBytes());
                user.setProfilePicture("/images/profiles/" + fileName);
            }

            user.setUpdatedAt(new Date());
            userRepository.save(user);

            // Update session with new user data
            session.setAttribute("loggedInUser", user);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/user/dashboard#settings";
    }

    @PostMapping("/user/delete")
    public String deleteUserAccount(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to delete your account.");
            return "redirect:/login/user";
        }

        try {
            // First delete all user-related data
            taskRepository.deleteByUser(loggedInUser);
            notificationRepository.deleteByUser(loggedInUser);
            progressRepository.deleteByUser(loggedInUser);

            // Then delete the user
            userRepository.delete(loggedInUser);

            // Clear session
            session.invalidate();

            redirectAttributes.addFlashAttribute("success", "Your account has been deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting account: " + e.getMessage());
            return "redirect:/user/dashboard#settings";
        }

        return "redirect:/";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(
            @RequestParam(name = "email", required = false) String email,
            Model model,
            HttpSession session) {

        // Check if we're coming back to the form with email parameter
        if (email != null && !email.isEmpty()) {
            model.addAttribute("showOtpForm", true);
            model.addAttribute("email", email);
        } else {
            // Clear any existing session attributes
            session.removeAttribute("resetEmail");
            model.addAttribute("showOtpForm", false);
        }

        return "forgot_user_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam String email,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Check if email exists
        User user = userRepository.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Email not found. Please check your email or sign up.");
            return "redirect:/forgot-password";
        }

        try {
            // Send OTP
            passwordResetService.sendPasswordResetOtp(email);

            // Store email in session for verification
            session.setAttribute("resetEmail", email);

            // Redirect with email parameter to show OTP form
            redirectAttributes.addAttribute("email", email);
            return "redirect:/forgot-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send OTP. Please try again later.");
            return "redirect:/forgot-password";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtpAndResetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Validate passwords match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            redirectAttributes.addAttribute("email", email);
            return "redirect:/forgot-password";
        }

        // Verify OTP
        if (!passwordResetService.verifyOtp(email, otp)) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired OTP. Please try again.");
            redirectAttributes.addAttribute("email", email);
            return "redirect:/forgot-password";
        }

        // Update password
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            user.setUpdatedAt(new Date());
            userRepository.save(user);

            // Clear OTP and session
            passwordResetService.clearOtp(email);
            session.removeAttribute("resetEmail");

            redirectAttributes.addFlashAttribute("success", "Password reset successfully. You can now login with your new password.");
            return "redirect:/login/user";
        }

        redirectAttributes.addFlashAttribute("error", "User not found.");
        return "redirect:/forgot-password";
    }
}