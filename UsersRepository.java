package com.example.profile_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.profile_management.model.Users;

public interface UsersRepository extends JpaRepository<Users, String>{
}