package com.forum.booking.service;

import com.forum.booking.dto.*;
import com.forum.booking.entity.Booking;
import com.forum.booking.entity.Company;
import com.forum.booking.repository.BookingRepository;
import com.forum.booking.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CompanyRepository companyRepository;

    private static final LocalDate FORUM_DATE = LocalDate.of(2026, 4, 24);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(18, 0);
    private static final int SLOT_DURATION_MINUTES = 30;

    @Transactional(readOnly = true)
    public List<CompanyDto> getAllCompaniesWithSlots() {
        // Получаем ВСЕ забронированные слоты (общие для всех)
        List<String> allBookedSlots = bookingRepository.findAllBookedTimeSlots(FORUM_DATE);
        
        // Получаем информацию о бронированиях компаний (какая компания кем забронирована)
        List<Booking> allBookings = bookingRepository.findByBookingDateOrderByTimeSlotAsc(FORUM_DATE);
        Map<Long, Booking> companyBookingMap = new HashMap<>();
        for (Booking booking : allBookings) {
            companyBookingMap.put(booking.getCompany().getId(), booking);
        }

        List<Company> companies = companyRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        List<CompanyDto> result = new ArrayList<>();

        for (Company company : companies) {
            CompanyDto dto = convertToDto(company);
            
            // Проверяем, забронирована ли эта компания
            Booking booking = companyBookingMap.get(company.getId());
            if (booking != null) {
                dto.setIsBooked(true);
                dto.setBookedTime(booking.getTimeSlot());
                dto.setBookedBy(booking.getFirstName() + " " + booking.getLastName());
            } else {
                dto.setIsBooked(false);
                dto.setBookedTime(null);
                dto.setBookedBy(null);
            }

            // Используем общие забронированные слоты для всех
            dto.setBookedSlots(allBookedSlots);
            dto.setAvailableSlots(generateTimeSlots(allBookedSlots));

            result.add(dto);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public CompanyDto getCompanyWithSlots(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Компания не найдена"));

        CompanyDto dto = convertToDto(company);
        
        // Получаем ВСЕ забронированные слоты
        List<String> allBookedSlots = bookingRepository.findAllBookedTimeSlots(FORUM_DATE);
        
        // Проверяем, забронирована ли эта компания
        Booking booking = bookingRepository.findByBookingDateOrderByTimeSlotAsc(FORUM_DATE)
            .stream()
            .filter(b -> b.getCompany().getId().equals(companyId))
            .findFirst()
            .orElse(null);
            
        if (booking != null) {
            dto.setIsBooked(true);
            dto.setBookedTime(booking.getTimeSlot());
            dto.setBookedBy(booking.getFirstName() + " " + booking.getLastName());
        } else {
            dto.setIsBooked(false);
            dto.setBookedTime(null);
            dto.setBookedBy(null);
        }
        
        dto.setBookedSlots(allBookedSlots);
        dto.setAvailableSlots(generateTimeSlots(allBookedSlots));

        return dto;
    }

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request) {
        // Проверка, что дата - день форума
        if (!request.getBookingDate().equals(FORUM_DATE)) {
            throw new RuntimeException("Бронирование доступно только на 24 апреля 2026");
        }

        // Проверка существования компании
        Company company = companyRepository.findById(request.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Компания не найдена"));

        // Проверка, что компания ещё не забронирована
        boolean isCompanyBooked = bookingRepository.existsByCompanyIdAndBookingDate(
            request.getCompanyId(),
            request.getBookingDate()
        );
        
        if (isCompanyBooked) {
            throw new RuntimeException("Эта компания уже забронирована");
        }

        // Проверка, что слот не занят (общий для всех!)
        boolean isSlotTaken = bookingRepository.existsByBookingDateAndTimeSlot(
            request.getBookingDate(),
            request.getTimeSlot()
        );

        if (isSlotTaken) {
            throw new RuntimeException("Этот временной слот уже забронирован");
        }

        // Создание бронирования
        Booking booking = Booking.builder()
            .company(company)
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phone(request.getPhone())
            .email(request.getEmail())
            .bookingDate(request.getBookingDate())
            .timeSlot(request.getTimeSlot())
            .build();

        Booking saved = bookingRepository.save(booking);

        return convertToResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(this::convertToResponseDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByCompany(Long companyId) {
        return bookingRepository.findByCompanyIdOrderByBookingDateDescTimeSlotDesc(companyId)
            .stream()
            .map(this::convertToResponseDto)
            .toList();
    }

    private CompanyDto convertToDto(Company company) {
        return CompanyDto.builder()
            .id(company.getId())
            .name(company.getName())
            .description(company.getDescription())
            .logoUrl(company.getLogoUrl())
            .isActive(company.getIsActive())
            .createdAt(company.getCreatedAt())
            .isBooked(false)
            .build();
    }

    private BookingResponseDto convertToResponseDto(Booking booking) {
        return BookingResponseDto.builder()
            .id(booking.getId())
            .companyId(booking.getCompany().getId())
            .companyName(booking.getCompany().getName())
            .firstName(booking.getFirstName())
            .lastName(booking.getLastName())
            .phone(booking.getPhone())
            .email(booking.getEmail())
            .bookingDate(booking.getBookingDate())
            .timeSlot(booking.getTimeSlot())
            .createdAt(booking.getCreatedAt())
            .build();
    }

    private List<TimeSlotDto> generateTimeSlots(List<String> bookedSlots) {
        List<TimeSlotDto> slots = new ArrayList<>();
        LocalTime currentTime = START_TIME;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!currentTime.isAfter(END_TIME)) {
            String timeStr = currentTime.format(formatter);
            boolean isAvailable = !bookedSlots.contains(timeStr);

            slots.add(TimeSlotDto.builder()
                .time(timeStr)
                .isAvailable(isAvailable)
                .build());

            currentTime = currentTime.plusMinutes(SLOT_DURATION_MINUTES);
        }

        return slots;
    }

    public LocalDate getForumDate() {
        return FORUM_DATE;
    }
}
