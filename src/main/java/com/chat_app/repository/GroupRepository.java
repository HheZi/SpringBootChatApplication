package com.chat_app.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat_app.model.websocket.Group;

@Repository
public interface GroupRepository extends MongoRepository<Group, String>{

	@Query("{usersName: {$contains: :usersName}}")
	List<Group> findByUsersName(@Param("usersName") String usersName);
	
}
