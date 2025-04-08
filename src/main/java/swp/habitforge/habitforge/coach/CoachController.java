package swp.habitforge.habitforge.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.firebase.FirebaseStorageService;
import swp.habitforge.habitforge.user.User;
import swp.habitforge.habitforge.user.UserRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class CoachController {

    @Autowired private CoachRepository coachRepository;
    @Autowired private CoachService coachService;
    @Autowired private FirebaseStorageService firebaseStorageService;

    @GetMapping("/coach/sign/up")
    public String goToCoachSignUp(){
        return "coach-sign-up";
    }

    @GetMapping("/login")
    public String goToLogin(){
        return "login";
    }

    @GetMapping("/admin_shauntel")
  public String manageCoaches (Model model){
        List<Coach>listCoaches=coachService.listAll();
        model.addAttribute("listCoaches",listCoaches);
        return "admin_shauntel";
    }

    @GetMapping("coach_form")
    public String showAddCoachForm(Model model){
        model.addAttribute("coach", new Coach());
      return "coach_form";
    }


    @GetMapping("/coach/register")
    public String showCoachCreatedForm ( Model model ){
        model.addAttribute("coach", new Coach());
        return "/coach/register";
    }

    //need mapping for adding new coach

    @PostMapping("/coach/register")
    public String processCoachSignUp(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String url,
            @RequestParam String email,
            @RequestParam String password,
         //   @RequestParam MultipartFile profilePicture,
            @RequestParam String bio,
            @RequestParam String expertise,
            RedirectAttributes redirectAttributes) throws IOException {

        // Check if email already exists
        if (coachService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists. Please log in.");

          return "redirect:/coach/register";
        }

        // Create a new Coach object
        Coach coach = new Coach();
        coach.setName(name);
        coach.setSurname(surname);
        coach.setEmail(email);
        coach.setPassword(password);

        //upload picture to firebase
       // String profilePictureUrl = firebaseStorageService.uploadProfilePicture(profilePicture, name + " " + surname);

        coach.setProfilePicture(url);
        coach.setBio(bio);
        coach.setExpertise(expertise);
        coach.setCreatedAt(new Date());
        coach.setUpdatedAt(new Date());

        // Save coach to database
        coachRepository.save(coach);

        // initially redirected to "redirect:/login";
        //i will redirect to /coach/register for now
        redirectAttributes.addFlashAttribute("success", "Coach registration successful!.");
        return "redirect:/coach/register";
    }



}


