package com.bjc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force=true)
@Entity
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private final long id;

	private String version;

	private String name;
	
}
