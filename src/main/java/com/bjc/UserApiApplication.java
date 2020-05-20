package com.bjc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bjc.data.*;
import com.bjc.model.*;

@SpringBootApplication
public class UserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner dataLoader(UserRepository repo, UnitRepository un_repo, RoleRepository ro_repo, UserRoleRepository us_repo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				
				User us_1 = new User(1, "1", "Alice");
				User us_2 = new User(2, "2", "Bob");
				User us_3 = new User(3, "1", "Eve");
				repo.save(us_1);
				repo.save(us_2);
				repo.save(us_3);
				
				Unit un_11 = new Unit(1, "2", "Kreftregisteret");
				Unit un_12 = new Unit(2, "1", "Akershus universitetssykehus HF");
				Unit un_13 = new Unit(3, "2", "Sørlandet sykehus HF");
				Unit un_14 = new Unit(4, "2", "Vestre Viken HF");
				un_repo.save(un_11);
				un_repo.save(un_12);
				un_repo.save(un_13);
				un_repo.save(un_14);
				
				Role r_101 = new Role(1, "1", "User administration");
				Role r_102 = new Role(2, "2", "Endoscopist administration");
				Role r_103 = new Role(3, "1", "Report colonoscopy capacity");
				Role r_104 = new Role(4, "2", "Send invitations");
				Role r_105 = new Role(5, "1", "View statistics");
				ro_repo.save(r_101);
				ro_repo.save(r_102);
				ro_repo.save(r_103);
				ro_repo.save(r_104);
				ro_repo.save(r_105);
				
				UserRole ur_1001 = new UserRole(1, "1", us_1, un_11, r_101, LocalDateTime.parse("2019-01-02T00:00:00"), LocalDate.of(2019, 12, 31));
				UserRole ur_1002 = new UserRole(2, "2", us_1, un_11, r_104, LocalDateTime.parse("2019-01-02T00:00:00"), LocalDate.of(2019, 12, 31));
				UserRole ur_1003 = new UserRole(3, "1", us_1, un_11, r_105, LocalDateTime.parse("2019-06-11T00:00:00"), LocalDate.of(2019, 12, 31));
				UserRole ur_1004 = new UserRole(4, "2", us_2, un_12, r_101, LocalDateTime.parse("2020-01-28T00:00:00"), null);
				UserRole ur_1005 = new UserRole(5, "1", us_2, un_12, r_105, LocalDateTime.parse("2020-01-28T00:00:00"), null);
				UserRole ur_1006 = new UserRole(6, "1", us_2, un_14, r_101, LocalDateTime.parse("2020-01-28T00:00:00"), null);
				UserRole ur_1007 = new UserRole(7, "1", us_2, un_14, r_102, LocalDateTime.parse("2020-01-28T00:00:00"), null);
				UserRole ur_1008 = new UserRole(8, "1", us_1, un_11, r_101, LocalDateTime.parse("2020-02-01T07:00:00"), null);
				UserRole ur_1009 = new UserRole(9, "1", us_1, un_11, r_104, LocalDateTime.parse("2020-02-01T07:00:00"), null);
				
				us_repo.save(ur_1001);
				us_repo.save(ur_1002);
				us_repo.save(ur_1003);
				us_repo.save(ur_1004);
				us_repo.save(ur_1005);
				us_repo.save(ur_1006);
				us_repo.save(ur_1007);
				us_repo.save(ur_1008);
				us_repo.save(ur_1009);
			}
		};
	}

}
