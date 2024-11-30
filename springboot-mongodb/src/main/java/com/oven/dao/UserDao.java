package com.oven.dao;

import com.oven.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User, Integer> {
}
