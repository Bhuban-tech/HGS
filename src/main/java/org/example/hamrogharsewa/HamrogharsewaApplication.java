package org.example.hamrogharsewa;

import org.example.hamrogharsewa.model.Role;
import org.example.hamrogharsewa.model.User;
import org.example.hamrogharsewa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class HamrogharsewaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HamrogharsewaApplication.class, args);
    }

    @Component
    class DataInitializer implements CommandLineRunner {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

        @Value("${superadmin.email}")
        private String email;

        @Value("${superadmin.username}")
        private String username;

        @Value("${superadmin.password}")
        private String password;

        @Value("${superadmin.profile}")
        private String profile;

        @Override
        public void run(String... args) {
            try {
                List<User> superAdmins = userRepository.findByRole(Role.SUPERADMIN);

                if (superAdmins.isEmpty()) {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUserName(username);

                    // If the password looks like a BCrypt hash, save it directly
                    if (password.startsWith("$2a$") || password.startsWith("$2b$")) {
                        newUser.setPassword(password);
                    } else {
                        newUser.setPassword(passwordEncoder.encode(password));
                    }

                    newUser.setRole(Role.SUPERADMIN);
                    newUser.setActive(true);
                    newUser.setApproved(true);

                    userRepository.save(newUser);
                    System.out.println("SUPERADMIN created successfully.");
                }

            } catch (Exception e) {
                System.err.println("Error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
