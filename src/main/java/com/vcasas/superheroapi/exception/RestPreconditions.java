package com.vcasas.superheroapi.exception;

import org.springframework.data.domain.Page;

import com.vcasas.superheroapi.entity.SuperHero;
    public class RestPreconditions {

        public static Page<SuperHero> checkFoundResults(Page<SuperHero> resource) {
            if (resource == null) {
                throw new SuperHeroeException("No Matching Superheroes");
            }
            return resource;
        }
}