package com.wigell.cinema.repository;

import com.wigell.cinema.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomerId(Long customerId);
    List<Ticket> findByScreeningId(Long screeningId);
    long countByScreeningId(Long screeningId);
    boolean existsByScreeningIdAndSeatNumber(Long screeningId, Integer seatNumber);

}