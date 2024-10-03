package com.beyond.easycheck.reservationroom.ui.requestbody;

import com.beyond.easycheck.reservationroom.infrastructure.entity.PaymentStatus;
import com.beyond.easycheck.reservationroom.infrastructure.entity.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationRoomUpdateRequest {

    @NotNull(message = "체크인 날짜를 지정해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkinDate;

    @NotNull(message = "체크아웃 날짜를 지정해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkoutDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @NotNull
    @Min(value = 0, message = "price must be greater than or equal to 0")
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
