package com.chat_app.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chat_app.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("users")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails{
	
	private static final long serialVersionUID = -1359234059078546653L;

	@Id
	@EqualsAndHashCode.Exclude
	private Integer id;

	private String email;

	private String username;

	private String password;
	
	@Column("is_non_locked")
	@Builder.Default
	private Boolean isNonLocked = true;
	
	@Builder.Default
	private Status status = Status.OFFLINE;

	private String avatar;
	
	@CreatedDate
	@Column("created_at")
	private Instant createdAt;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isNonLocked;
	}
}
