package swp.habitforge.habitforge.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class GuestController {

    @Autowired
    private GuestRepository guestRepository;

    @GetMapping("/summary/report")
    public String showSummaryReport(Model model) {
        List<SummaryReportProjection> report = guestRepository.getSummaryReport();
        model.addAttribute("report", report);
        return "summary_report";
    }

    @GetMapping("/wellness/content/feedback/report")
    public String showWellnessFeedbackReport(Model model) {
        List<WellnessFeedbackProjection> report = guestRepository.getWellnessFeedbackReport();
        model.addAttribute("report", report);
        return "wellness_feedback_report";
    }
}