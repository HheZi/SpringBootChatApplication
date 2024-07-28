package com.chat_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chat_app.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReposiory extends CrudRepository<User, Integer>{
	public Optional<User> findByUsername(String username);
}
