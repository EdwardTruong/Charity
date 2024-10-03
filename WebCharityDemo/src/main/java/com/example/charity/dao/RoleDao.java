package com.example.charity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.charity.entity.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer>{

}
