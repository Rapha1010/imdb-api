package com.globo.application.services.impl;

import com.globo.application.dtos.MovieDto;
import com.globo.application.models.MovieModel;
import com.globo.application.models.VoteModel;
import com.globo.application.repositories.MovieRepository;
import com.globo.application.repositories.VoteRepository;
import com.globo.application.services.MovieService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    VoteRepository voteRepository;

    @Override
    public Page<MovieModel> findAll(Specification<MovieModel> spec, Pageable pageable) {
        return movieRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<MovieModel> findByMovieId(UUID movieId) {
        return movieRepository.findByMovieId(movieId);
    }

    @Override
    public void delete(MovieModel movieModel) {
        movieRepository.delete(movieModel);
    }

    @Override
    public void save(MovieModel movieModel) {
        movieRepository.save(movieModel);
    }

    @Override
    public void save(VoteModel voteModel) {
        voteRepository.save(voteModel);
    }

    @Override
    public boolean existsByMovieName(String movieName) {
        return movieRepository.existsByMovieName(movieName);
    }

}
