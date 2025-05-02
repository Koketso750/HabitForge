// NotificationController.java
package swp.habitforge.habitforge.notification;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.user.User;

import java.util.Date;

@Controller
public class NotificationController {

    @Autowired private NotificationService notificationService;
    @Autowired private NotificationRepository notificationRepository;

    @PostMapping("/notification/mark-as-read/{id}")
    public String markNotificationAsRead(
            @PathVariable("id") Integer notificationId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in.");
            return "redirect:/login/user";
        }

        try {
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid notification ID"));

            if (!notification.getUser().getUserId().equals(loggedInUser.getUserId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
                return "redirect:/user/dashboard";
            }

            notification.setRead(true);
            notification.setUpdatedAt(new Date());
            notificationRepository.save(notification);

            redirectAttributes.addFlashAttribute("success", "Notification marked as read.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating notification: " + e.getMessage());
        }

        return "redirect:/user/dashboard";
    }
}