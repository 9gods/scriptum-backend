package com.scriptum.backend.infrastructure.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface IUserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

}
