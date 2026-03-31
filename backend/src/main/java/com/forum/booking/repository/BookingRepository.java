package com.forum.booking.repository;

import com.forum.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByCompanyIdAndBookingDateAndTimeSlot(
        Long companyId,
        LocalDate bookingDate,
        String timeSlot
    );
    
    List<Booking> findByCompanyIdOrderByBookingDateDescTimeSlotDesc(Long companyId);
    
    List<Booking> findByBookingDateOrderByTimeSlotAsc(LocalDate bookingDate);
    
    @Query("SELECT b.timeSlot FROM Booking b WHERE b.company.id = :companyId AND b.bookingDate = :date ORDER BY b.timeSlot")
    List<String> findBookedTimeSlots(@Param("companyId") Long companyId, @Param("date") LocalDate date);
    
    // Получаем ВСЕ забронированные слоты на дату (общие для всех компаний)
    @Query("SELECT b.timeSlot FROM Booking b WHERE b.bookingDate = :date ORDER BY b.timeSlot")
    List<String> findAllBookedTimeSlots(@Param("date") LocalDate date);
    
    // Проверка, занят ли слот (общая для всех компаний)
    boolean existsByBookingDateAndTimeSlot(LocalDate bookingDate, String timeSlot);
    
    // Проверка, забронирована ли компания на дату (только одна бронь на компанию)
    boolean existsByCompanyIdAndBookingDate(Long companyId, LocalDate bookingDate);
    
    boolean existsByCompanyIdAndBookingDateAndTimeSlot(
        Long companyId,
        LocalDate bookingDate,
        String timeSlot
    );
}
