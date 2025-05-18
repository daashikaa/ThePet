package com.thepet.repositories;

import com.thepet.model.Note;
import com.thepet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByPetId(Long petId);
    List<Note> findByPetIdAndTimeAfter(Long petId, LocalDateTime time);

    @Query("SELECT DISTINCT n.pet FROM Note n WHERE n.time >= :weekAgo")
    List<Pet> findPetsWithNotesLastWeek(@Param("weekAgo") LocalDateTime weekAgo);
}