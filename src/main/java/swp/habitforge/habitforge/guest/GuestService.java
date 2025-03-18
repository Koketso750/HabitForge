package swp.habitforge.habitforge.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    @Autowired private GuestRepository guestRepository;
}
