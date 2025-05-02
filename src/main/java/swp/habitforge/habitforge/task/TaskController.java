package swp.habitforge.habitforge.task;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.notification.Notification;
import swp.habitforge.habitforge.notification.NotificationRepository;
import swp.habitforge.habitforge.progress.Progress;
import swp.habitforge.habitforge.progress.ProgressRepository;
import swp.habitforge.habitforge.user.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @PostMapping("/task/create")
    public String createTask(
            @RequestParam String taskName,
            @RequestParam String taskDescription,
            @RequestParam String dueDate,
            @RequestParam String habitType,
            @RequestParam Integer habitFrequency,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to add tasks.");
            return "redirect:/login/user";
        }

        try {
            LocalDate localDueDate = LocalDate.parse(dueDate);
            Date utilDueDate = Date.from(localDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Save task
            Task task = new Task();
            task.setTaskName(taskName);
            task.setTaskDescription(taskDescription);
            task.setDueDate(utilDueDate);
            task.setHabitType(habitType);
            task.setHabitFrequency(habitFrequency);
            task.setCompleted(false);
            task.setCreatedAt(new Date());
            task.setUpdatedAt(new Date());
            task.setUser(loggedInUser);

            Task savedTask = taskRepository.save(task);

            // Save notification
            Notification notification = new Notification();
            notification.setNotificationText("New task added: " + taskName);
            notification.setNotificationDate(new Date());
            notification.setCreatedAt(new Date());
            notification.setUpdatedAt(new Date());
            notification.setUser(loggedInUser);
            notification.setTask(savedTask);
            notification.setRead(false);

            notificationRepository.save(notification);

            // Save progress (initial value)
            Progress progress = new Progress();
            progress.setProgressDate(new Date());
            progress.setProgressValue(0); // Initial progress
            progress.setCreatedAt(new Date());
            progress.setUpdatedAt(new Date());
            progress.setUser(loggedInUser);
            progress.setTask(savedTask);

            progressRepository.save(progress);

            redirectAttributes.addFlashAttribute("success", "Task, notification, and progress initialized!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save task data: " + e.getMessage());
        }

        return "redirect:/user/dashboard";
    }

    @PostMapping("/task/complete/{id}")
    public String completeTask(
            @PathVariable("id") Integer taskId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to complete tasks.");
            return "redirect:/login/user";
        }

        try {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));

            if (!task.getUser().getUserId().equals(loggedInUser.getUserId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
                return "redirect:/user/dashboard";
            }

            task.setCompleted(true);
            task.setUpdatedAt(new Date());
            taskRepository.save(task);

            // Update progress
            Progress progress = progressRepository.findByTask(task)
                    .orElseThrow(() -> new IllegalArgumentException("Progress record not found"));

            progress.setProgressValue(100); // 100% complete
            progress.setUpdatedAt(new Date());
            progressRepository.save(progress);

            redirectAttributes.addFlashAttribute("success", "Task marked as completed!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error completing task: " + e.getMessage());
        }

        return "redirect:/user/dashboard";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(
            @PathVariable("id") Integer taskId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to delete tasks.");
            return "redirect:/login/user";
        }

        try {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));

            if (!task.getUser().getUserId().equals(loggedInUser.getUserId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
                return "redirect:/user/dashboard";
            }

            // Delete associated progress records first
            progressRepository.deleteByTask(task);

            // Delete associated notifications
            notificationRepository.deleteByTask(task);

            // Then delete the task
            taskRepository.delete(task);

            redirectAttributes.addFlashAttribute("success", "Task deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting task: " + e.getMessage());
        }

        return "redirect:/user/dashboard";
    }
}
