package com.bjc.web.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.bjc.data.UserRepository;
import com.bjc.data.UserRoleRepository;
import com.bjc.model.User;
import com.bjc.web.api.exception.*;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {

	private UserRepository user_repo;
	private UserRoleRepository userRole_repo;

	public UserController(UserRepository user_repo, UserRoleRepository userRole_repo) {
		this.user_repo = user_repo;
		this.userRole_repo = userRole_repo;
	}

	
	/*
	 * REQUIREMENT 1 
	 * get all users
	 */
	@GetMapping
	public Iterable<User> getUsers() {
		return user_repo.findAll();
	}
	
	
	/*
	 * REQUIREMENT 2 
	 * My interpretation: 
	 * Get all users which had at some time a (not specified) user role at a (not specified) unit
	 */
	@GetMapping(params = "validUserRole=true")
	public Iterable<User> getUserWithUserRole() {
		return user_repo.getUserWithValidUserRole();
	}
	

	/*
	 * REQUIREMENT 7 
	 * Create a new user
	 */
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public User createNewUser(@RequestBody User userTemplate) {
		
		if(userTemplate.getName() == null || userTemplate.getName() == "")
			throw new IncompletePayloadException("name", "no empty string"); 
		
		User newUser = new User(); 
		newUser.setName(userTemplate.getName());
		return user_repo.save(newUser);
	}

	
	/*
	 * REQUIREMENT 8 
	 * Update an existing user. The version must always be specified. 
	 * If the specified version does not match the resource's current version, 
	 * a response indicating a version mismatch must be returned
	 */
	@PatchMapping(path = "/{userId}", params = "version", consumes = "application/json")
	public User updateUser(@PathVariable("userId") Long paramUserId, 
			@RequestParam("version") long paramVersion, @RequestBody User userTemplate) {
		
		if (!user_repo.existsById(paramUserId)) 
			throw new EntityNotFoundException("User", paramUserId);
				
		User user = user_repo.findById(paramUserId).get();
		Long userVersion = new Long(user.getVersion()); 
		
		if(userVersion!= paramVersion) 
			throw new VersionMisMatchException(paramVersion, paramUserId);
		
		if(userTemplate.getName() == null || userTemplate.getName() == "") 
			throw new IncompletePayloadException("name", "no empty string"); 
		
		user.setName(userTemplate.getName());
		//user.setVersion(user.getVersion()+1);
		return user_repo.save(user);
	}

	
	
	/*
	 * REQUIREMENT 9 
	 * Delete an existing user. A user can only be deleted if there are no user roles for that user
	 */
	@DeleteMapping(path = "/{userId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable("userId") Long pathUserId) {
		
		if (!user_repo.existsById(pathUserId)) 
			throw new EntityNotFoundException("User", pathUserId);

		if (userRole_repo.existsUserRoleByUserId(pathUserId))
			throw new UserHasUserRoleException(pathUserId);
		
		user_repo.deleteById(pathUserId);
	}
	
}
