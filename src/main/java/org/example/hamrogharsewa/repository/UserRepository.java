package org.example.hamrogharsewa.repository;

import org.example.hamrogharsewa.model.Role;
import org.example.hamrogharsewa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndActiveFalse(Role role);

    List<User> findByRoleAndApprovedFalse(Role role);

    List<User> findByRoleAndApprovedTrueAndActiveTrue(Role role);

    List<User> findByServiceCategoryIdAndRoleAndApprovedTrueAndActiveTrue(String serviceCategoryId, Role role);

    boolean existsByEmail(String email);
}
