package com.globo.application.controllers;

import com.globo.application.dtos.MovieDto;
import com.globo.application.dtos.VoteDto;
import com.globo.application.models.MovieModel;
import com.globo.application.models.UserModel;
import com.globo.application.models.VoteModel;
import com.globo.application.services.MovieService;
import com.globo.application.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    private UUID movieId;
    private MovieModel movieModel;
    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        movieId = UUID.randomUUID();
        movieModel = MovieModel.builder().build();
        movieDto = MovieDto.builder().build();
    }

    @Test
    void testGetMovieList() {
        Page<MovieModel> moviePage = new PageImpl<>(Collections.singletonList(movieModel));
        when(movieService.findAll(any(), any(Pageable.class))).thenReturn(moviePage);

        ResponseEntity<Page<MovieModel>> response = movieController.getMovieList(null, Pageable.unpaged());

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testGetMovieById() {
        when(movieService.findByMovieId(movieId)).thenReturn(Optional.of(movieModel));

        ResponseEntity<Object> response = movieController.getMovieById(movieId);

        assertNotNull(response.getBody());
        verify(movieService).findByMovieId(movieId);
    }

    @Test
    void testAddMovie() {
        doNothing().when(movieService).save(any(MovieModel.class));

        ResponseEntity<Object> response = movieController.addMovie(movieModel);

        assertEquals(200, response.getStatusCodeValue());
        verify(movieService).save(movieModel);
    }

    @Test
    void testVoteMovie() {
        VoteDto voteDto = VoteDto.builder().build();
        VoteModel voteModel = VoteModel.builder().build();
        voteModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        voteModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        when(userService.getLoggedInUsername(request)).thenReturn(new UserModel());
        when(movieService.findByMovieId(movieId)).thenReturn(Optional.of(movieModel));
        doNothing().when(movieService).save(any(VoteModel.class));

        ResponseEntity<Object> response = movieController.voteMovie(movieId, voteDto, request);

        assertEquals(200, response.getStatusCodeValue());
        verify(movieService).save(any(VoteModel.class));
    }

    @Test
    void testDeleteMovie() {
        when(movieService.findByMovieId(movieId)).thenReturn(Optional.of(movieModel));
        doNothing().when(movieService).delete(movieModel);

        ResponseEntity<Object> response = movieController.deleteMovie(movieId);

        assertEquals(200, response.getStatusCodeValue());
        verify(movieService).delete(movieModel);
    }

    @Test
    void testDeleteMovie_NotFound() {
        when(movieService.findByMovieId(movieId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = movieController.deleteMovie(movieId);

        assertEquals(404, response.getStatusCodeValue());
    }
}
