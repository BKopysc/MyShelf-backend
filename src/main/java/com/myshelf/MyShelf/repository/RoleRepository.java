package com.myshelf.MyShelf.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.myshelf.MyShelf.models.ERole;
import com.myshelf.MyShelf.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(ERole name);
}
