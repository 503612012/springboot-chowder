package com.oven.dao.oracle;

import com.oven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleUserDao extends JpaRepository<User, Integer> {
}
