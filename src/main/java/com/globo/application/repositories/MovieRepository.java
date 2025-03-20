package com.globo.application.repositories;

import com.globo.application.models.MovieModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface MovieRepository extends JpaRepository<MovieModel, UUID>, JpaSpecificationExecutor<MovieModel> {

    boolean existsByMovieName(String movieName);

    Optional<MovieModel> findByMovieId(UUID movieId);

    Page<MovieModel> findAll(Specification<MovieModel> spec, Pageable pageable);
}
