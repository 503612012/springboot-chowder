package com.oven.dao.mysql;

import com.oven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MysqlUserDao extends JpaRepository<User, Integer> {
}
