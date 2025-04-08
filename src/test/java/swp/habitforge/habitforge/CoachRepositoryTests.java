package swp.habitforge.habitforge;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import swp.habitforge.habitforge.coach.Coach;
import swp.habitforge.habitforge.coach.CoachRepository;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)

public class CoachRepositoryTests {
    @Autowired private CoachRepository repo ;

    @Test
    public void testAddNewCoach () {
        Coach coachDef = new Coach();

        coachDef.setName("Ntokozo");
        coachDef.setSurname("Nkwana");
        coachDef.setProfilePicture("pic_pic_url");
        coachDef.setEmail("nkw.ntok@gmail.com");
        coachDef.setPassword("word123");
        coachDef.setBio("Experienced coach in Meditation.");
        coachDef.setExpertise("Meditation");
        coachDef.setCreatedAt(new Date());
        coachDef.setUpdatedAt(new Date());

        Coach savedCoach = repo.save(coachDef);
        Assertions.assertThat(savedCoach).isNotNull();
        Assertions.assertThat(savedCoach.getCoachId()).isGreaterThan(0);


    }

    @Test
    public void testListAll() {
        Iterable<Coach> coaches = repo.findAll();
        Assertions.assertThat(coaches).hasSizeGreaterThan(0);

        for (Coach coach : coaches) {
            System.out.println(coach);
        }
    }

    @Test
    public void testUpdate() {
        Integer coachId = 1;
        Optional<Coach> optionalCoach = repo.findById(coachId);

        Assertions.assertThat(optionalCoach).isPresent();

        Coach coach = optionalCoach.get();
        coach.setBio("Updated bio for coach.");
        repo.save(coach);

        Coach updatedCoach = repo.findById(coachId).get();
        Assertions.assertThat(updatedCoach.getBio()).isEqualTo("Updated bio for coach.");
    }

    @Test
    public void testGet() {
        Integer coachId = 2;
        Optional<Coach> optionalCoach = repo.findById(coachId);
        Assertions.assertThat(optionalCoach).isPresent();
        System.out.println(optionalCoach.get());
    }

    @Test
    public void testDelete() {
        Integer coachId = 1;
        repo.deleteById(coachId);
        Optional<Coach> optionalCoach = repo.findById(coachId);
        Assertions.assertThat(optionalCoach).isNotPresent();
    }





}
