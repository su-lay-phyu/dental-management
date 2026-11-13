package com.dental_management.demo;

import com.dental_management.demo.model.entities.User;
import com.dental_management.demo.model.entities.RoleName;
import com.dental_management.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		return args -> {

			String adminEmail = "admin@gmail.com";

			if (!userRepository.existsByEmail(adminEmail)) {

				User admin = new User();

				admin.setName("Admin");
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode("Admin@123"));
				admin.setPhone("09123456789");
				admin.setRole(RoleName.ROLE_ADMIN);
				admin.setEmailVerified(true);

				userRepository.save(admin);

				System.out.println("Default Admin Created Successfully!");

			} else {
				System.out.println("Default Admin Already Exists!");
			}
		};
	}
}