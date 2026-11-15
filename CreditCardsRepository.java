package com.example.profile_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.profile_management.model.CreditCards;

public interface CreditCardsRepository extends JpaRepository<CreditCards, String>{ 
    List<CreditCards> findByUsername(String username);
}
