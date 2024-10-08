package com.beyond.easycheck.tickets.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ticket_payment")
public class TicketPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_order_id", nullable = false)
    private TicketOrderEntity ticketOrder;

    @Column(name = "amount",nullable = false)
    private BigDecimal paymentAmount;

    @Column(nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus paymentStatus;

    private String cancelReason;

    private LocalDateTime cancelDate;

    private LocalDateTime paymentDate;

    public static TicketPaymentEntity createPayment(TicketOrderEntity order, BigDecimal amount, String method) {
        TicketPaymentEntity payment = new TicketPaymentEntity();
        payment.ticketOrder = order;
        payment.paymentAmount = amount;
        payment.paymentMethod = method;
        payment.paymentStatus = OrderStatus.PENDING;
        payment.paymentDate = LocalDateTime.now();
        return payment;
    }


    public void cancelPayment(String reason) {
        this.paymentStatus = OrderStatus.CANCELLED;
        this.cancelReason = reason;
        this.cancelDate = LocalDateTime.now();
    }

}
