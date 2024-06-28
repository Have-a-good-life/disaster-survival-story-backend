package com.team1.main.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Situation {
	@Id
	public Integer situationId;

	public String situationName;

	public String situationDesc;
}
