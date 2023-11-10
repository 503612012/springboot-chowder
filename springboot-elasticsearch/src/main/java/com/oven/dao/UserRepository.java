package com.oven.dao;

import com.oven.vo.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, Integer> {
}
