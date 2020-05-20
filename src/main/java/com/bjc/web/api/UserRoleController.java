package com.bjc.web.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.bjc.data.*;
import com.bjc.model.*;
import com.bjc.web.api.exception.*;
import com.bjc.web.api.jsonpayload.UserRoleTemplate;

@RestController
@RequestMapping(path = "/userRoles", produces = "application/json")
public class UserRoleController {

	private UserRoleRepository userRole_repo;
	private UserRepository user_repo; 
	private UnitRepository unit_repo;
	private RoleRepository role_repo; 

	public UserRoleController(UserRoleRepository userRole_repo,  UserRepository user_repo, UnitRepository unit_repo, RoleRepository role_repo) {
		this.userRole_repo = userRole_repo;
		this.user_repo = user_repo;
		this.unit_repo = unit_repo; 
		this.role_repo = role_repo; 
	}

	
	/*
	 * REQUIREMENT 5 
	 * List all user roles (both currently valid and invalid) for a given user id and unit id
	 */
	@GetMapping(params = { "userId", "unitId" })
	public Iterable<UserRole> getUserRolesByUserIdAndUnitId(@RequestParam("userId") long paramUserId,
			@RequestParam("unitId") long paramUnitId) {
		return userRole_repo.findByUserIdAndUnitId(paramUserId, paramUnitId);
	}

	
	/*
	 * REQUIREMENT 6 
	 * List only valid user roles for a given user id and unit id at a given timestamp
	 */
	@GetMapping(params = { "userId", "unitId", "timeStamp" })
	public Iterable<UserRole> getValidUserRolesByUserIdAndUnitIdAndTimeStamp(@RequestParam("userId") long paramUserId,
			@RequestParam("unitId") long paramUnitId, @RequestParam("timeStamp") String paramTimeStamp) {
		
		LocalDateTime locDateTime = LocalDateTime.parse(paramTimeStamp);
		return userRole_repo.findByUserIdUnitIdAndTimeStamp(paramUserId, paramUnitId, locDateTime);
	}
	
	
	/*
	 * REQUIREMENT 10 
	 * Create a new user role for a given user id, unit id, role id, an optional valid from timestamp
	 * (if not specified, default to the current date and time) and an optional valid to timestamp 
	 * (if not specified, default to no timestamp). If a valid to timestamp is specified, 
	 * it must be after the valid from timestamp (or the current date and time if valid from timestamp is not specified in the request). 
	 * At most one user role for a given combination of user id, unit id and role id can be valid at any point in time.
	 */
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public UserRole createNewUserRole(@RequestBody UserRoleTemplate urTemp) {
		
		if(urTemp.getUserId() == null)
			throw new IncompletePayloadException("userId", "Long"); 
		
		if(urTemp.getUnitId() == null)
			throw new IncompletePayloadException("unitId", "Long"); 

		if (urTemp.getRoleId() == null)
			throw new IncompletePayloadException("roleId", "Long");
		
		
		Long userId = Long.valueOf(urTemp.getUserId());
		Long unitId = Long.valueOf(urTemp.getUnitId());
		Long roleId = Long.valueOf(urTemp.getRoleId());
		
		if(!user_repo.existsById(userId))
			throw new EntityNotFoundException("user", userId);
		
		if(!unit_repo.existsById(unitId))
			throw new EntityNotFoundException("unit", unitId);
		
		if(!role_repo.existsById(roleId))
			throw new EntityNotFoundException("role", roleId);

		
		LocalDateTime fromDateTime = (urTemp.getFromDateTime() == null) ? LocalDateTime.now()
				: urTemp.getFromDateTime();

		LocalDate toDate = (urTemp.getToDate() == null) ? null
				: urTemp.getToDate();
		
		if(toDate != null && fromDateTime.isAfter(toDate.atStartOfDay()))	
			throw new IncompatibleValuesException(fromDateTime, toDate);  
				
		
		List<UserRole> userRoles = userRole_repo.findByUserIdAndUnitIdAndRoleId(userId, unitId, roleId);
		
		if(OverlappingPeriodsInUserRole(fromDateTime, toDate, userRoles))
			throw new UserHasUserRoleException(userId);
		
		
		UserRole newUserRole = new UserRole();
		newUserRole.setUser(user_repo.findById(userId).get());
		newUserRole.setUnit(unit_repo.findById(unitId).get());
		newUserRole.setRole(role_repo.findById(unitId).get());
		newUserRole.setFromDateTime(fromDateTime);
		newUserRole.setToDate(toDate);
		
		return userRole_repo.save(newUserRole);
	}
	
	
	/*
	 * REQUIREMENT 11 
	 * Update an existing user role. Only the valid from and valid to timestamps can be changed. 
	 * The valid from timestamp, if specified, must be a timestamp (a user role must always have a valid from timestamp). 
	 * The requirement that the valid to timestamp, if specified, must come after the valid from timestamp must be enforced, 
	 * and an update that would cause two user roles for the same user id, unit id and role id to be valid 
	 * at the same time must be rejected
	 */
	@PatchMapping(path = "/{userRoleId}", consumes = "application/json")
	public UserRole updateUserRole(@PathVariable("userRoleId") Long paramUserRoleId,
			@RequestBody UserRoleTemplate urTemp) {

		if (!userRole_repo.existsById(paramUserRoleId))
			throw new EntityNotFoundException("UserRole", paramUserRoleId);

		if (urTemp.getFromDateTime() == null && urTemp.getToDate() == null)
			throw new IncompletePayloadException("fromDateTime' and/or 'toDate", "localDateTime and DateTime");

		
		UserRole userRole = userRole_repo.findById(paramUserRoleId).get();

		LocalDateTime fromDateTime = (urTemp.getFromDateTime() != null) ? urTemp.getFromDateTime()
				: userRole.getFromDateTime();
		LocalDate toDate = (urTemp.getToDate() != null) ? urTemp.getToDate() : userRole.getToDate();

		if (toDate != null && fromDateTime.isAfter(toDate.atStartOfDay()))
			throw new IncompatibleValuesException(fromDateTime, toDate);

		
		List<UserRole> userRoles = userRole_repo.findByUserAndUnitAndRoleAndIdIsNot(userRole.getUser(),
				userRole.getUnit(), userRole.getRole(), paramUserRoleId);

		if (OverlappingPeriodsInUserRole(fromDateTime, toDate, userRoles))
			throw new UserHasUserRoleException(paramUserRoleId);

		userRole.setFromDateTime(fromDateTime);
		userRole.setToDate(toDate);
		return userRole_repo.save(userRole);
	}
	
	
	/*
	 * REQUIREMENT 12 
	 * Delete an existing user role.
	 */
	@DeleteMapping(path = "/{userRoleId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteUserRoles(@PathVariable("userRoleId") Long pathUserRoleId) {
		
		if (!userRole_repo.existsById(pathUserRoleId)) 
			throw new EntityNotFoundException("User role", pathUserRoleId);
		
		userRole_repo.deleteById(pathUserRoleId);
	}
	
	
	private boolean OverlappingPeriodsInUserRole(LocalDateTime fromDateTime, LocalDate toDate, List<UserRole> userRoles) {
		
		for (UserRole ur : userRoles) {
			if ((ur.getToDate() == null || fromDateTime.isBefore(ur.getToDate().atStartOfDay()))
					&& (toDate == null || toDate.atStartOfDay().isAfter(ur.getFromDateTime()))) {
				return true;
			}
		}
		return false; 
	}
	
}