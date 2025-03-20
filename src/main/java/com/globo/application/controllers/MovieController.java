package com.globo.application.controllers;

import com.globo.application.dtos.MovieDto;
import com.globo.application.dtos.UserDto;
import com.globo.application.dtos.VoteDto;
import com.globo.application.models.MovieModel;
import com.globo.application.models.UserModel;
import com.globo.application.models.VoteModel;
import com.globo.application.services.MovieService;
import com.globo.application.services.UserService;
import com.globo.application.specifications.MovieSpecification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity<Page<MovieModel>> getMovieList(MovieSpecification spec, @PageableDefault(size = 10, sort = "movieName", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(movieService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovieById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(movieService.findByMovieId(id).get());
    }

    @PostMapping("/")
    public ResponseEntity<Object> addMovie(@RequestBody MovieModel movieModel) {
        movieService.save(movieModel);
        return ResponseEntity.ok().body(movieModel);
    }

    @PostMapping("/vote/{movieId}")
    public ResponseEntity<Object> voteMovie(@PathVariable UUID movieId, @RequestBody VoteDto voteDto, HttpServletRequest request) {
        var loggedUser = userService.getLoggedInUsername(request);
        var movieModel = movieService.findByMovieId(movieId).get();

        var voteModel = new VoteModel();
        BeanUtils.copyProperties(voteDto, voteModel);
        voteModel.setUser(loggedUser);
        voteModel.setMovie(movieModel);
        voteModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        voteModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        movieService.save(voteModel);

        return ResponseEntity.ok().body(voteModel);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Object> deleteMovie(@PathVariable UUID movieId) {
        Optional<MovieModel> movieModelOptional = movieService.findByMovieId(movieId);

        if (movieModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");

        var movieModel = movieModelOptional.get();
        movieService.delete(movieModel);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Object> updateMovie(@PathVariable UUID movieId, @RequestBody MovieDto movieDto) {
        Optional<MovieModel> movieModelOptional = movieService.findByMovieId(movieId);

        if (movieModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");

        var movieModel = new MovieModel();
        BeanUtils.copyProperties(movieDto, movieModel);
        movieModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        movieModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        movieModel.setMovieId(movieId);

        System.out.println(movieModel.getMovieId());
        System.out.println(movieModel.getMovieName());

        movieService.save(movieModel);
        return  ResponseEntity.status(HttpStatus.OK).body(movieDto);
    }

}
