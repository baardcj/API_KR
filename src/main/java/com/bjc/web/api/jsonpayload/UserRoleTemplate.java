package com.bjc.web.api.jsonpayload;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserRoleTemplate {

	private long id;

	private String version = "1";

	private String userId;

	private String unitId;

	private String roleId;

	private LocalDateTime fromDateTime;

	private LocalDate toDate;
} 
