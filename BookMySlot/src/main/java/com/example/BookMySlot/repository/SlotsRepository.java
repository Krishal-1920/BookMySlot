package com.example.BookMySlot.repository;

import com.example.BookMySlot.entity.Slots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotsRepository extends JpaRepository<Slots, String> {

}
