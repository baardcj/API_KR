package com.bjc.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjc.data.UnitRepository;
import com.bjc.model.Unit;

@RestController
@RequestMapping(path = "/units", produces = "application/json")
public class UnitController {
	
	private UnitRepository unit_repo;
	
	public UnitController(UnitRepository unit_repo) {
		this.unit_repo = unit_repo;
	}
	
	
	/*
	 * REQUIREMENT 3
	 * List all units
	 */
	@GetMapping
	public Iterable<Unit> getUnits() {
		return unit_repo.findAll();
	}
}
