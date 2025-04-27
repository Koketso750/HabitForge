package swp.habitforge.habitforge.wellnesscontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.habitforge.habitforge.coach.Coach;

import java.util.List;

@Service
public class WellnessContentService {

    @Autowired private WellnessContentRepository wellnessContentRepository;

    public List<WellnessContent> getContentByCoach(Coach coach) {
        return wellnessContentRepository.findByCoach(coach);
    }

    public Long countContentByCoach(Coach coach) {
        return wellnessContentRepository.countByCoach(coach);
    }

    public List<WellnessContent> listAll() {
        return (List<WellnessContent>) wellnessContentRepository.findAll();
    }
}
