package com.team1.main.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ReactionMap {
	@Id
	public int reactionMapId;
	public int situationId;
	public int bestReactionId;

}
