package com.thepet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Note {
    private Long id;
    private Long petId;
    private String type;
    private LocalDate date;
    private String note;
}
