package com.vcasas.superheroapi;

import com.vcasas.superheroapi.entity.SuperHero;
import com.vcasas.superheroapi.repository.SuperHeroJpaRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SuperHeroApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SuperHeroJpaRepository repository;

    private SuperHero superHero;

    @BeforeEach
    void setUp() {
        restTemplate = restTemplate.withBasicAuth("user", "password");
        superHero = new SuperHero("Batman");
        repository.saveAndFlush(superHero);
    }

    @AfterEach
    void clean() {  
        repository.deleteById(5L);
        repository.deleteById(4L);
    }

    @SuppressWarnings({"unchecked", "null" })
    @Test
    @Order(3) 
    void shouldGetSuperHeroesContainingWord() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "http://localhost:" + port + "/superheroes/search?word=man",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> content = (List<Map<String, Object>>) responseBody.get("content");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content).extracting("name").containsExactly("Superman", "Spiderman", "Batman");
    }

    @SuppressWarnings({ "rawtypes", "null", "unchecked" })
    @Test
    @Order(2) 
    void shouldGetSuperHeroes() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/superheroes",
                List.class);
        List<SuperHero> responseBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.size()).isEqualTo(4);
    }
        
    @SuppressWarnings("null")
    @Test
    @Order(4) 
    void shouldGetSuperHeroById() {
        ResponseEntity<SuperHero> response = restTemplate.getForEntity("http://localhost:" + port + "/superheroes/1", SuperHero.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Superman");
    }

    @SuppressWarnings("null")
    @Test
    @Order(5) 
    void shouldCreateSuperHero() throws InterruptedException {
        SuperHero newSuperHero = new SuperHero(5L, "Hulk");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SuperHero> request = new HttpEntity<>(newSuperHero, headers);

        ResponseEntity<SuperHero> response = restTemplate.postForEntity("http://localhost:" + port + "/superheroes", request, SuperHero.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Hulk");
    }

    @SuppressWarnings("null")
    @Test
    @Order(1) 
    void shouldUpdateSuperHero() {
        SuperHero updatedSuperHero = new SuperHero(4L, "Batman Updated");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SuperHero> request = new HttpEntity<>(updatedSuperHero, headers);

        restTemplate.put("http://localhost:" + port + "/superheroes/4", request);

        ResponseEntity<SuperHero> response = restTemplate.getForEntity("http://localhost:" + port + "/superheroes/4", SuperHero.class);


        assertThat(response.getBody().getName()).isEqualTo("Batman Updated");
    }

    @SuppressWarnings("null")
    @Test
    @Order(6) 
    void shouldDeleteSuperHero() throws InterruptedException {
        restTemplate.delete("http://localhost:" + port + "/superheroes/4");
        ResponseEntity<SuperHero> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/superheroes/4",
                SuperHero.class);
    
        // Verificar que se devuelve HttpStatus.NO_CONTENT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getName()).isNull();
    }
}