package com.proj.backend;

import com.proj.backend.model.Role;
import com.proj.backend.model.RoleType;
import com.proj.backend.repository.RoleRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("DB_DIALECT", dotenv.get("DB_DIALECT"));

        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(RoleType.ADMIN));
                roleRepository.save(new Role(RoleType.DRIVER));
                roleRepository.save(new Role(RoleType.OWNER));
                System.out.println("Roles inicializados en la base de datos.");
            }
        };
    }
}
