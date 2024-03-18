package com.vcasas.superheroapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vcasas.superheroapi.entity.SuperHero;
import com.vcasas.superheroapi.repository.SuperHeroJpaRepository;


@Service
public class SuperHeroService {
    private SuperHeroJpaRepository repository;

    public SuperHeroService(SuperHeroJpaRepository repository) {
        this.repository = repository;
    }

    @Cacheable("superheroesContainingWord")
    public Page<SuperHero> getHeroesContainingWord(String word, Pageable pageable) {
        Page<SuperHero> superHeroes = repository.findByNameContainingIgnoreCase(word, pageable);
        if (superHeroes.isEmpty()) {
            return null;
        } else {
            return superHeroes;
        }
    }

    @Cacheable("allSuperheroes")
    public List<SuperHero> getAllSuperHeroes() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public SuperHero saveSuperHero(SuperHero superHero) {
        return repository.save(superHero);
    }

    public SuperHero getSuperHeroById(long id) {
        Optional<SuperHero> superHero = repository.findById(id);
		if (superHero.isPresent()) {
			return superHero.get();
		} else {
			return null;
		}
    }

    @SuppressWarnings("null")
    public SuperHero updateSuperHero(SuperHero superHero) {
        return repository.save(superHero);
    }

    public void deleteSuperHeroById(long id) {
        repository.deleteById(id);;
    }
}