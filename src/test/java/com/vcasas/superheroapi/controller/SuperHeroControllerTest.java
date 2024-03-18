package com.vcasas.superheroapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcasas.superheroapi.entity.SuperHero;
import com.vcasas.superheroapi.service.SuperHeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuperHeroController.class)
@AutoConfigureMockMvc(addFilters = false)

class SuperHeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SuperHeroService service;

    private SuperHero superHero;

    @BeforeEach
    void setUp() {
        superHero = new SuperHero(1L, "Batman");
    }

    @SuppressWarnings("null")
    @Test
    void getSuperHeroesContainingWord_shouldReturnPageOfSuperHeroes() throws Exception {
        PageImpl<SuperHero> page = new PageImpl<>(List.of(superHero));
        when(service.getHeroesContainingWord(any(), any())).thenReturn(page);

        mockMvc.perform(get("/superheroes/search")  
                        .param("word", "man")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Batman"));
    }

    @SuppressWarnings("null")
    @Test
    void getSuperHeroesContainingWord_shouldReturnEmptyPageWhenNoResultsFound() throws Exception {
        PageImpl<SuperHero> emptyPage = new PageImpl<>(List.of());
        when(service.getHeroesContainingWord(any(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/superheroes/search")
                        .param("word", "nonexistent")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getSuperHeroes_shouldReturnListOfSuperHeroes() throws Exception {
        when(service.getAllSuperHeroes()).thenReturn(List.of(superHero));

        mockMvc.perform(get("/superheroes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Batman"));
    }

    @SuppressWarnings("null")
    @Test
    void addSuperHero_shouldAddSuperHero() throws Exception {
        when(service.saveSuperHero(any())).thenReturn(superHero);

        mockMvc.perform(post("/superheroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(superHero)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Batman"));
    }

    @Test
    void getSuperHero_shouldReturnSuperHero() throws Exception {
        when(service.getSuperHeroById(eq(1L))).thenReturn(superHero);

        mockMvc.perform(get("/superheroes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Batman"));
    }

    @Test
    void getSuperHero_shouldReturnNotFoundWhenSuperHeroDoesNotExist() throws Exception {
        when(service.getSuperHeroById(eq(2L))).thenReturn(null);

        mockMvc.perform(get("/superheroes/2"))
                .andExpect(status().isNotFound());
    }

    @SuppressWarnings("null")
    @Test
    void updateSuperHero_shouldUpdateSuperHero() throws Exception {
        SuperHero updatedSuperHero = new SuperHero(1L, "Batman Updated");
        when(service.getSuperHeroById(eq(1L))).thenReturn(superHero);
        when(service.updateSuperHero(any())).thenReturn(updatedSuperHero);

        mockMvc.perform(put("/superheroes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSuperHero)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Batman Updated"));
    }

    @SuppressWarnings("null")
    @Test
    void updateSuperHero_shouldReturnNotFoundWhenSuperHeroDoesNotExist() throws Exception {
        SuperHero updatedSuperHero = new SuperHero(2L, "Batman Updated");
        when(service.getSuperHeroById(eq(2L))).thenReturn(null);

        mockMvc.perform(put("/superheroes/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSuperHero)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSuperHero_shouldDeleteSuperHero() throws Exception {
        when(service.getSuperHeroById(eq(1L))).thenReturn(superHero);

        mockMvc.perform(delete("/superheroes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSuperHero_shouldReturnNotFoundWhenSuperHeroDoesNotExist() throws Exception {
        when(service.getSuperHeroById(eq(2L))).thenReturn(null);

        mockMvc.perform(delete("/superheroes/2"))
                .andExpect(status().isNotFound());
    }
}