package com.scriptum.backend.domain.repositories;

import com.scriptum.backend.domain.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    User save(User user);

}
