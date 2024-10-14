package io.github.yulily.ab.model.repository;

import io.github.yulily.ab.model.User;
import org.springframework.data.repository.CrudRepository;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLoginId(String loginId);
}
