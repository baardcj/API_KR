package com.bjc.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bjc.model.User;

public interface UserRepository extends CrudRepository<User, Long> {	
	
	@Query("Select distinct ur.user from UserRole ur where ur.unit is not null AND ur.role is not null AND ur.fromDateTime is not null")
	List<User> getUserWithValidUserRole(); 
	
}