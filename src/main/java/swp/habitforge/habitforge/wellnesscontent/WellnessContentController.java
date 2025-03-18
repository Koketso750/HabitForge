package swp.habitforge.habitforge.wellnesscontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WellnessContentController {

    @Autowired private WellnessContentRepository wellnessContentRepository;
    @Autowired private WellnessContentService wellnessContentService;
}
