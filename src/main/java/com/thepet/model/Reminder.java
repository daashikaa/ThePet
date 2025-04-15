package com.thepet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reminder")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Event is required")
    @Column(name = "event")
    private String event;

    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    @Column(name = "date")
    private LocalDateTime date;

    @NotNull(message = "Reminder date is required")
    @Future(message = "Reminder date must be in the future")
    @Column(name = "counted_date")
    private LocalDateTime countedDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonBackReference
    private Pet pet;
}