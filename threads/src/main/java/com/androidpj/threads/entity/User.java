package com.androidpj.threads.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;

	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false, unique = true)

	private String nickName;
	@Column(nullable = false)
	private String bio;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false, unique = true)

	private String username;
	@Column(nullable = false, unique = false)

	private String password;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Post> posts;

	public User(String nickName) {
		this.nickName = nickName;
	}
}
