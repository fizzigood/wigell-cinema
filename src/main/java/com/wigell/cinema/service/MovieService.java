package com.wigell.cinema.service;

import com.wigell.cinema.config.ResourceNotFoundException;
import com.wigell.cinema.entity.Movie;
import com.wigell.cinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Tjänst för filmhantering.
// Filmer innehåller titel, genre, åldersgräns och längd i minuter.
@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Hämtar alla filmer
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Hämtar en film via ID. Kastar 404 om filmen inte finns
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film hittades inte med id: " + id));
    }

    // Skapar en ny film och loggar händelsen
    @Transactional
    public Movie createMovie(Movie movie) {
        Movie saved = movieRepository.save(movie);
        logger.info("Admin skapade film '{}' (id={})", saved.getTitle(), saved.getId());
        return saved;
    }

    // Tar bort en film och loggar händelsen. Kastar 404 om filmen inte finns
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
        logger.info("Admin tog bort film '{}' (id={})", movie.getTitle(), id);
    }

}