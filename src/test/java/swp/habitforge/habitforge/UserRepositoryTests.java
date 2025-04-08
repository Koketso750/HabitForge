package swp.habitforge.habitforge;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import swp.habitforge.habitforge.user.User;
import swp.habitforge.habitforge.user.UserRepository;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)

public class UserRepositoryTests {

    @Autowired private UserRepository repo;

    @Test
    public void testAddNewUser () {
        User userDef = new User ();
        userDef.setUsername("lebo");
        userDef.setEmail("lebo@gmail.com");
        userDef.setPassword("lebza3425");
        userDef.setName("Lebogang");
        userDef.setSurname("Nkuna");
        userDef.setProfilePicture("image.jpg");
        userDef.setCreatedAt(new Date());
        userDef.setUpdatedAt(new Date());



        User savedUser = repo.save(userDef);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUserId()).isGreaterThan(0);
 }


 @Test
    public void testListAllUsers(){

        Iterable <User> users = repo.findAll();
        Assertions.assertThat(users).hasSizeGreaterThan(0);

        for (User user : users ){

            System.out.println(user);

        }


 }

 @Test
    public void testUpdateUser () {
        Integer userId = 1;
        Optional<User> optionalUser = repo.findById( userId);
        User user = optionalUser.get();

        //update user password

     user.setPassword("456ggg");

     repo.save(user);

     User updatedUser = repo.findById(userId).get();
     Assertions.assertThat(updatedUser.getPassword()).isEqualTo("456ggg");




 }

 @Test
    public void testGetUser () {
        Integer userId = 2 ;
        Optional<User> optionalUser = repo.findById(userId);
     Assertions.assertThat(optionalUser).isPresent();
     System.out.println(optionalUser.get());



 }

 @Test
    public void testDeleteUser () {
        Integer userId = 1 ;
        repo.deleteById(userId);
        Optional <User> optionalUser = repo.findById(userId);
        Assertions.assertThat(optionalUser).isNotPresent();


 }

















}
