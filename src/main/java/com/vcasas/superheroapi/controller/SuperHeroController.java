package com.vcasas.superheroapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vcasas.superheroapi.annotation.ExecutionTime;
import com.vcasas.superheroapi.entity.SuperHero;
import com.vcasas.superheroapi.exception.SuperHeroeException;
import com.vcasas.superheroapi.service.SuperHeroService;
import com.vcasas.superheroapi.exception.RestPreconditions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/superheroes")
@Tag(name = "SuperHeroes", description = "SuperHeroe-related operations")
public class SuperHeroController {
    @Autowired
    SuperHeroService service;

    @GetMapping("/search")
    @Operation(summary = "Gets the SuperHeroes containing a given word",
            description = "The word must be used in the name of the searched SuperHeroes")
    @ApiResponse(responseCode = "404", description = "Not matching SuperHeroes")
    @ExecutionTime
    public ResponseEntity<Page<SuperHero>> getSuperHeroesContainingWord(
            @RequestParam(defaultValue = "") String word,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SuperHero> superheroes = service.getHeroesContainingWord(word, PageRequest.of(page, size));
            return new ResponseEntity<>(RestPreconditions.checkFoundResults(superheroes), HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new SuperHeroeException(e);
        }
    }   

    @GetMapping
    @ExecutionTime
    @Operation(summary = "Lists SuperHeroes")
    public ResponseEntity<List<SuperHero>> getSuperHeroes(){
        try {
            return new ResponseEntity<>(service.getAllSuperHeroes(), HttpStatus.OK);
        }
        catch (Exception e) {
            throw new SuperHeroeException(e);  
        }
    }

    @PostMapping
    @Operation(summary = "Adds SuperHero")
    public ResponseEntity<SuperHero> addSuperHero(@RequestBody @Valid SuperHero superHero) {
        try {
            service.saveSuperHero(superHero);
            return new ResponseEntity<>(superHero, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new SuperHeroeException(e);  
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets SuperHero by ID", 
            description= "SuperHero must exist")
    @ApiResponse(responseCode = "404", description = "SuperHero Not found")
	public ResponseEntity<SuperHero> getSuperHero(@PathVariable("id") @Valid long id) {
		try {
            SuperHero superHero = service.getSuperHeroById(id);
            if (superHero != null) {
            	return new ResponseEntity<>(superHero, HttpStatus.OK);
            } else {
            	throw new SuperHeroeException(id);
            }
        } catch (RuntimeException e) {
            throw new SuperHeroeException(e);  
        }
	}

    @PutMapping("/{id}")
    @Operation(summary = "Updates SuperHero by ID", 
            description= "SuperHero must exist")
    public ResponseEntity<SuperHero> updateSuperHero(@PathVariable("id") long id, @RequestBody @Valid SuperHero superHero) {
		try {
            SuperHero superHeroData = service.getSuperHeroById(id);
            if (superHeroData != null) {
            	superHeroData.setName(superHero.getName());
            	return new ResponseEntity<>(service.updateSuperHero(superHeroData), HttpStatus.OK);
            } else {
            	throw new SuperHeroeException(id);
            }
        } catch (RuntimeException e) {
            throw new SuperHeroeException(e);  
        }
	}

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes SuperHero by ID", 
            description= "SuperHero must exist")
    public ResponseEntity<HttpStatus> deleteSuperHero(@PathVariable("id") long id) {
        try {
            SuperHero superHero = service.getSuperHeroById(id);
            if (superHero != null) {
            	service.deleteSuperHeroById(id);
            	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
            	throw new SuperHeroeException(id);
            }
        } catch (RuntimeException e) {
            throw new SuperHeroeException(e);  
        }
    }
}
