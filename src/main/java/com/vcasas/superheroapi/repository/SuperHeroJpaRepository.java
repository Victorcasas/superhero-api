package com.vcasas.superheroapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcasas.superheroapi.entity.SuperHero;

@Repository
public interface SuperHeroJpaRepository extends JpaRepository<SuperHero, Long> {   
     
    Page<SuperHero> findByNameContainingIgnoreCase(String word, Pageable pageable);
}