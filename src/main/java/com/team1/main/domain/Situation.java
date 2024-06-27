package com.team1.main.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Situation {
	@Id
	private Integer situationId;

	private String situationName;

	private String situationDesc;
}
