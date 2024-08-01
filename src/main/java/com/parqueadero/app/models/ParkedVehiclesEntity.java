package com.parqueadero.app.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parked_vehicles")
public class ParkedVehiclesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLotEntity parkingLotEntity;

    @Column(name = "car_plate", length = 6, nullable = false)
    private String carPlate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "time_value")
    private Long timeValue;

    @Embedded
    private Audit audit;
    
}
