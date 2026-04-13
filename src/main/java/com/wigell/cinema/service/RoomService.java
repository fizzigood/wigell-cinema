package com.wigell.cinema.service;

import com.wigell.cinema.config.ResourceNotFoundException;
import com.wigell.cinema.entity.Room;
import com.wigell.cinema.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Tjänst för lokalhantering.
// Lokaler (salonger) har namn, max antal gäster, teknisk utrustning och baspris.
@Service
public class RoomService {

    // Logger för att logga händelser
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    // Skapar en referens till RoomRepository för databasinteraktion
    private final RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Hämtar alla lokaler
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Hämtar en lokal via ID. Kastar 404 om lokalen inte finns
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lokal hittades inte med id: " + id));
    }

    // Skapar en ny lokal och loggar händelsen
    @Transactional
    public Room createRoom(Room room) {
        Room saved = roomRepository.save(room);
        logger.info("Admin skapade lokal '{}' (id={})", saved.getName(), saved.getId());
        return saved;
    }

    // Uppdaterar en befintlig lokal (namn, kapacitet, utrustning, pris)
    @Transactional
    public Room updateRoom(Long id, Room updated) {
        Room existing = getRoomById(id);
        existing.setName(updated.getName());
        existing.setMaxGuests(updated.getMaxGuests());
        existing.setTechnicalEquipment(updated.getTechnicalEquipment());
        existing.setBasePrice(updated.getBasePrice());
        Room saved = roomRepository.save(existing);
        logger.info("Admin uppdaterade lokal '{}' (id={})", saved.getName(), saved.getId());
        return saved;
    }

}