package com.chat_app.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.chat_app.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReposiory extends CrudRepository<User, Integer>, ListCrudRepository<User, Integer>{
	public Optional<User> findByUsername(String username);
	
	public List<User> getByUsernameIn(List<String> usernames);
	
	public List<User> findByUsernameIsStartingWithIgnoreCase(String username, Limit limit);
}
