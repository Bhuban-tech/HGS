package org.example.hamrogharsewa.repository;

import org.example.hamrogharsewa.model.Role;
import org.example.hamrogharsewa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<User, String> {

    List<User> findByRole(Role role);

    // Example pending providers (custom logic)
    List<User> findByRoleAndActiveFalse(Role role);

    default List<User> findPendingProviders() {
        return findByRoleAndActiveFalse(Role.SERVICE_PROVIDER);
    }
}
