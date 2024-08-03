package com.chat_app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chat_app.model.websocket.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, String>{

}
