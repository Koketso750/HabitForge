package swp.habitforge.habitforge.feedback;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.habitforge.habitforge.user.User;
import swp.habitforge.habitforge.wellnesscontent.WellnessContent;
import swp.habitforge.habitforge.wellnesscontent.WellnessContentRepository;

import java.util.Date;
import java.util.Optional;

@Controller
public class FeedbackController {

    @Autowired private FeedbackRepository feedbackRepository;
    @Autowired private FeedbackService feedbackService;
    @Autowired private WellnessContentRepository wellnessContentRepository;

    @PostMapping("/feedback/submit/{contentId}")
    public String submitFeedback(@PathVariable("contentId") Long contentId,
                                 @RequestParam("rating") int rating,
                                 @RequestParam(value = "feedbackText", required = false) String feedbackText,
                                 HttpSession session) {
        // Create a new Feedback entity
        Feedback feedback = new Feedback();

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        WellnessContent content = wellnessContentRepository.findWellnessContentById(contentId);
        feedback.setContent(content);
        feedback.setRating(rating);
        feedback.setFeedbackText(feedbackText);
        feedback.setCreatedAt(new Date());
        feedback.setUser(loggedInUser);

        // Save it
        feedbackRepository.save(feedback);

        return "redirect:/user/dashboard"; // Redirect back to the dashboard page
    }

}
