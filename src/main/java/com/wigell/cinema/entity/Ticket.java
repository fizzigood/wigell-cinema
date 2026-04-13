package com.wigell.cinema.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entitet för biljett.
// Representerar en köpt biljett till en föreställning.
// Priset lagras i både SEK och USD. Köpdatum sätts automatiskt vid köp.
@Entity
@Table(
        name = "ticket",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ticket_screening_seat",
                columnNames = {"screening_id", "seat_number"}
        )
)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    private double priceSek;

    private double priceUsd;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    public Ticket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public double getPriceSek() {
        return priceSek;
    }

    public void setPriceSek(double priceSek) {
        this.priceSek = priceSek;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(double priceUsd) {
        this.priceUsd = priceUsd;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}