package com.rbilling.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email"),@UniqueConstraint(columnNames = "username")  })
public class User {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long id;
	
	private String username;

	@NotBlank
	@Size(max = 300)
	@Email
	private String email;

	@NotBlank
	@Size(max = 250)
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	private String raw_password;
	
	public String getRaw_password() {
		return raw_password;
	}

	public void setRaw_password(String raw_password) {
		this.raw_password = raw_password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User(@NotBlank @Size(max = 300) @Email String email,@NotBlank @Size(max = 250) String password,String username,String raw_password) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
		this.raw_password=raw_password;
	}

	public User(Long id, @NotBlank @Size(max = 300) @Email String email, @NotBlank @Size(max = 250) String password,String username,Set<Role> roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.username = username;
		this.roles = roles;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

}
