package dev.melvstein.portfolio.api.user.repository;

import dev.melvstein.portfolio.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
