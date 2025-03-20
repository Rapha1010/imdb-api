package com.globo.application.services;

import com.globo.application.dtos.MovieDto;
import com.globo.application.models.MovieModel;
import com.globo.application.models.VoteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface MovieService {

    Page<MovieModel> findAll(Specification<MovieModel> spec, Pageable pageable);

    Optional<MovieModel> findByMovieId(UUID movieId);

    void delete(MovieModel movieModel);

    void save(MovieModel movieModel);

    void save(VoteModel voteModel);

    boolean existsByMovieName(String movieName);
}
