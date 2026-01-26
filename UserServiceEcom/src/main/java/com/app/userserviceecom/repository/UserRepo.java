package com.app.userserviceecom.repository;

import com.app.userserviceecom.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends MongoRepository<User,String> {
}
