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
		@Column(name = "user_id")
		private Long userId;
	
		@Column(nullable = false, unique = true)
		private String email;

		@Column(name = "nick_name", nullable = false, unique = true)
		private String nickName;

		private String bio;

		private String image;

		private String phone;
	
		@Column(nullable = false, unique = true)
		private String username;

		@Column(nullable = false)
		private String password;

		@Column(name = "is_active", columnDefinition = "boolean default false")
		private boolean isActive;
	
		@JsonIgnore
		@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
		private Set<Post> posts;
	
		public User(String nickName) {
			this.nickName = nickName;
		}
	}
