package swp.habitforge.habitforge.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.habitforge.habitforge.coach.Coach;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired private FeedbackRepository feedbackRepository;


    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getFeedbackForCoach(Coach coach) {
        return feedbackRepository.findByContentCoach(coach);
    }

    public List<Feedback> getFeedbackForCoachWithUser(Coach coach) {
        return feedbackRepository.findByContentCoach(coach); // Make sure this fetches the user association
    }
}
