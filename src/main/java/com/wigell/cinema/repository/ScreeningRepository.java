package com.wigell.cinema.repository;

import com.wigell.cinema.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findByMovieIdAndDateTimeBetween(Long movieId, LocalDateTime start, LocalDateTime end);
    List<Screening> findByMovieId(Long movieId);
    List<Screening> findByRoomId(Long roomId);

}