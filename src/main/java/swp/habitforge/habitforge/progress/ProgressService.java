package swp.habitforge.habitforge.progress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {

    @Autowired private ProgressRepository progressRepository;
}
