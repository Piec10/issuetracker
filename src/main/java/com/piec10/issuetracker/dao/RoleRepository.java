package com.piec10.issuetracker.dao;

import com.piec10.issuetracker.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findByName(String name);
}
