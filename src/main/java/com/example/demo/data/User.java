package com.example.demo.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.UUID;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "user_Sequence")
	@SequenceGenerator(name = "user_Sequence", sequenceName = "USER_SEQ")
	private Long id;

	private UserState state;

	private String name;

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
