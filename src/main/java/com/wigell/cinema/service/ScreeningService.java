package com.wigell.cinema.service;

import com.wigell.cinema.config.ResourceNotFoundException;
import com.wigell.cinema.dto.ScreeningRequest;
import com.wigell.cinema.entity.Movie;
import com.wigell.cinema.entity.Room;
import com.wigell.cinema.entity.Screening;
import com.wigell.cinema.repository.ScreeningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// Tjänst för föreställningshantering.
// En föreställning kopplar ihop en film med en lokal vid en given tidpunkt.
// Filtrering på film och datum.
@Service
public class ScreeningService {

    private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    public ScreeningService(ScreeningRepository screeningRepository, MovieService movieService, RoomService roomService) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    // Hämtar alla föreställningar
    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    // Hämtar en föreställning via ID. Kastar 404 om den inte finns
    public Screening getScreeningById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Föreställning hittades inte med id: " + id));
    }

    // Filtrerar föreställningar på film och datum (alla föreställningar för en film på en specifik dag)
    public List<Screening> getScreeningsByMovieAndDate(Long movieId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return screeningRepository.findByMovieIdAndDateTimeBetween(movieId, start, end);
    }

    // Skapar en ny föreställning och loggar händelsen
    @Transactional
    public Screening createScreening(ScreeningRequest request) {
        Movie movie = movieService.getMovieById(request.getMovieId());
        Room room = roomService.getRoomById(request.getRoomId());

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setRoom(room);
        screening.setDateTime(LocalDateTime.parse(request.getDateTime()));
        screening.setTicketPrice(request.getTicketPrice());

        Screening saved = screeningRepository.save(screening);
        logger.info("Admin skapade föreställning för film '{}' i lokal '{}' kl {} (id={})",
                movie.getTitle(), room.getName(), saved.getDateTime(), saved.getId());
        return saved;
    }

    // Tar bort en föreställning och loggar händelsen
    @Transactional
    public void deleteScreening(Long id) {
        Screening screening = getScreeningById(id);
        screeningRepository.delete(screening);
        logger.info("Admin tog bort föreställning id={} för film '{}'", id, screening.getMovie().getTitle());

    }

}