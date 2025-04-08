package swp.habitforge.habitforge.coach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swp.habitforge.habitforge.firebase.FirebaseStorageService;

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
    public String manageCoaches(Model model){
        List<Coach> listCoaches = coachService.listAll();
        model.addAttribute("listCoaches", listCoaches);
        return "admin_shauntel";
    }

    @GetMapping("/coach_form")
    public String showAddCoachForm(Model model){
        model.addAttribute("coach", new Coach());
        return "coach_form";
    }

    @GetMapping("/coach/register")
    public String showCoachCreatedForm(Model model){
        model.addAttribute("coach", new Coach());
        return "coach_form";
    }

    @PostMapping("/coach/register")
    public String processCoachSignUp(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String url,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String bio,
            @RequestParam String expertise,
            RedirectAttributes redirectAttributes) throws IOException {

        if (coachService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists. Please log in.");
            return "redirect:/coach/register";
        }

        Coach coach = new Coach();
        coach.setName(name);
        coach.setSurname(surname);
        coach.setEmail(email);
        coach.setPassword(password);
        coach.setProfilePicture(url);
        coach.setBio(bio);
        coach.setExpertise(expertise);
        coach.setCreatedAt(new Date());
        coach.setUpdatedAt(new Date());

        coachRepository.save(coach);

        redirectAttributes.addFlashAttribute("success", "Coach registration successful!");
        return "redirect:/coach/register";
    }

    // Show form for editing an existing coach
    @GetMapping("/coach/update/{id}")
    public String editCoach(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Coach coach = coachRepository.findByCoachId(id);

            model.addAttribute("coach", coach);
            return "edit_coach";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin_shauntel";
        }
    }

    // Process form submission for updating a coach
    @PostMapping("/coach/update/{id}")
    public String updateCoach(
            @PathVariable("id") Integer id,
            @ModelAttribute("coach") Coach coach,
            RedirectAttributes redirectAttributes) {
        try {
            // Get existing coach data
            Coach existingCoach = coachRepository.findByCoachId(id);

            // Update fields
            existingCoach.setName(coach.getName());
            existingCoach.setSurname(coach.getSurname());
            existingCoach.setEmail(coach.getEmail());
            existingCoach.setProfilePicture(coach.getProfilePicture());
            existingCoach.setBio(coach.getBio());
            existingCoach.setExpertise(coach.getExpertise());

            // Only update password if a new one was provided
            if (coach.getPassword() != null && !coach.getPassword().isEmpty()) {
                existingCoach.setPassword(coach.getPassword());
            }

            // Update timestamp
            existingCoach.setUpdatedAt(new Date());

            // Save the updated coach
            coachRepository.save(existingCoach);

            redirectAttributes.addFlashAttribute("success", "Coach updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating coach: " + e.getMessage());
        }
        return "redirect:/admin_shauntel";
    }

    // Delete a coach
    @GetMapping("/coach/delete/{id}")
    public String deleteCoach(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Coach coach = coachRepository.findByCoachId(id);
            coachRepository.delete(coach);
            redirectAttributes.addFlashAttribute("success", "Coach deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting coach: " + e.getMessage());
        }
        return "redirect:/admin_shauntel";
    }
}