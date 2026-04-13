package com.wigell.cinema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Entitet för lokal (salong).
// Innehåller namn, max antal gäster, teknisk utrustning och baspris.
// En bokning avser alltid en hel salong.
// @JsonIgnore på screenings/bookings förhindrar cirkulär serialisering.
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int maxGuests;

    private String technicalEquipment;

    private double basePrice;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Screening> screenings = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    public Room() {
    }

    public Room(String name, int maxGuests, String technicalEquipment, double basePrice) {
        this.name = name;
        this.maxGuests = maxGuests;
        this.technicalEquipment = technicalEquipment;
        this.basePrice = basePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getTechnicalEquipment() {
        return technicalEquipment;
    }

    public void setTechnicalEquipment(String technicalEquipment) {
        this.technicalEquipment = technicalEquipment;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

}