package com.vcasas.superheroapi.service;

import com.vcasas.superheroapi.entity.SuperHero;
import com.vcasas.superheroapi.repository.SuperHeroJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class SuperHeroServiceTest {

    @MockBean
    private SuperHeroJpaRepository repository;

    @Autowired
    private SuperHeroService service;

    private SuperHero superHero;

    @BeforeEach
    void setUp() {        
        superHero = new SuperHero(4L, "Batman");
    }

    @SuppressWarnings("null")
    @Test
    void getHeroesContainingWord_shouldReturnPageOfSuperHeroes() {
        Page<SuperHero> page = new PageImpl<>(List.of(superHero));
        when(repository.findByNameContainingIgnoreCase(any(), any())).thenReturn(page);

        Page<SuperHero> result = service.getHeroesContainingWord("man", PageRequest.of(0, 10));

        assertThat(result).isEqualTo(page);
        verify(repository, times(1)).findByNameContainingIgnoreCase(any(), any());
    }

    @SuppressWarnings("null")
    @Test
    void getHeroesContainingWord_shouldReturnNullWhenNoResultsFound() {
        Page<SuperHero> emptyPage = new PageImpl<>(List.of());
        when(repository.findByNameContainingIgnoreCase(any(), any())).thenReturn(emptyPage);

        Page<SuperHero> result = service.getHeroesContainingWord("nonexistent", PageRequest.of(0, 10));

        assertThat(result).isNull();
        verify(repository, times(1)).findByNameContainingIgnoreCase(any(), any());
    }

    @Test
    void getAllSuperHeroes_shouldReturnListOfSuperHeroes() {
        when(repository.findAll()).thenReturn(List.of(superHero));

        List<SuperHero> result = service.getAllSuperHeroes();

        assertThat(result).containsExactly(superHero);
        verify(repository, times(1)).findAll();
    }

    @SuppressWarnings("null")
    @Test
    void saveSuperHero_shouldSaveSuperHero() {
        when(repository.save(any())).thenReturn(superHero);

        SuperHero result = service.saveSuperHero(superHero);

        assertThat(result).isEqualTo(superHero);
        verify(repository, times(1)).save(any());
    }

    @Test
    void getSuperHeroById_shouldReturnSuperHero() {
        when(repository.findById(eq(1L))).thenReturn(Optional.of(superHero));

        SuperHero result = service.getSuperHeroById(1L);

        assertThat(result).isEqualTo(superHero);
        verify(repository, times(1)).findById(eq(1L));
    }

    @Test
    void getSuperHeroById_shouldReturnNullWhenSuperHeroDoesNotExist() {
        when(repository.findById(eq(2L))).thenReturn(Optional.empty());

        SuperHero result = service.getSuperHeroById(2L);

        assertThat(result).isNull();
        verify(repository, times(1)).findById(eq(2L));
    }

    @SuppressWarnings("null")
    @Test
    void updateSuperHero_shouldUpdateSuperHero() {
        SuperHero updatedSuperHero = new SuperHero(4L, "Batman Updated");
        when(repository.save(any())).thenReturn(updatedSuperHero);

        SuperHero result = service.updateSuperHero(updatedSuperHero);

        assertThat(result).isEqualTo(updatedSuperHero);
        verify(repository, times(1)).save(any());
    }

    @Test
    void deleteSuperHeroById_shouldDeleteSuperHero() {
        service.deleteSuperHeroById(1L);

        verify(repository, times(1)).deleteById(eq(1L));
    }
}