package com.bjc.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;

import com.bjc.model.*;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
	
	boolean existsUserRoleByUserId(Long id);
	
	List<UserRole> findByUserIdAndUnitId(Long userId, Long UnitId);
	
	List<UserRole> findByUserIdAndUnitIdAndRoleId(Long userId, Long unitId, Long roleId);
	
	List<UserRole> findByUserAndUnitAndRoleAndIdIsNot(User user, Unit unit, Role role, Long userRoleId);
	
	List<UserRole> findByUserIdAndUnitIdAndFromDateTimeIsBefore(Long userId, Long UnitId, LocalDateTime time_a);
	
	default
	List<UserRole> findByUserIdUnitIdAndTimeStamp(Long userId, Long UnitId, LocalDateTime time){
		List<UserRole> aa = findByUserIdAndUnitIdAndFromDateTimeIsBefore(userId, UnitId, time.plusSeconds(1)); 
		return aa.stream().filter(us -> us.getToDate() == null || us.getToDate().isAfter(time.toLocalDate())).collect(Collectors.toList());
	}
}