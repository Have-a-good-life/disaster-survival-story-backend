package com.team1.main.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BestReaction {

	@Id
	public int bestReactionId;
	public String bestReactionName;
	public String bestReactionDesc;

}
