package swp.habitforge.habitforge.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GuestController {

    @Autowired private GuestRepository guestRepository;
    @Autowired private GuestService guestService;
}
