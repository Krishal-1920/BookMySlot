package com.example.BookMySlot.repository;

import com.example.BookMySlot.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Booking findByUserUserIdAndSlotSlotId(String userId, String slotId);

    List<Booking> findAllByUserUserId(String userId);

}
