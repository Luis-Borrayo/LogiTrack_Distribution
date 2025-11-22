package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.dtos.PagosDto;
import com.luisborrayo.logitrack_distribution.models.Order;
import com.luisborrayo.logitrack_distribution.models.Payment;
import com.luisborrayo.logitrack_distribution.repositoriees.OrdenRepository;
import com.luisborrayo.logitrack_distribution.repositoriees.PagosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PagosService {
    @Inject
    private PagosRepository pagosRepository;

    @Inject
    private OrdenRepository ordenRepository;

    @Inject
    private OrdenService ordenService;

    public List<Payment> getAll() {
        return pagosRepository.getAll();
    }

    public Optional<Payment> findById(Long id) {
        return pagosRepository.findById(id);
    }

    public List<Payment> findByOrderId(Long orderId) {
        return pagosRepository.findByOrderId(orderId);
    }

    @Transactional
    public Optional<Payment> processPayment(PagosDto paymentDto) {
        Optional<Order> orderOpt = ordenRepository.findById(paymentDto.getOrderId());
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();

        if (paymentDto.getAmount() == null || paymentDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.empty();
        }
        BigDecimal totalPaid = pagosRepository.getTotalPaidByOrder(order.getOrderId());
        BigDecimal pendingAmount = order.getTotalAmount().subtract(totalPaid);

        if (paymentDto.getAmount().compareTo(pendingAmount) > 0) {
            return Optional.empty();
        }

        if (paymentDto.getMethod() == null ||
                !List.of("Cash", "Card", "Transfer").contains(paymentDto.getMethod())) {
            return Optional.empty();
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(paymentDto.getAmount());
        payment.setMethod(paymentDto.getMethod());

        Optional<Payment> savedPayment = pagosRepository.save(payment);

        if (savedPayment.isPresent()) {
            BigDecimal newTotalPaid = totalPaid.add(paymentDto.getAmount());
            if (newTotalPaid.compareTo(order.getTotalAmount()) >= 0 &&
                    !"Orden Completada".equals(order.getStatus())) {
                ordenService.updateOrderStatus(order.getOrderId(), "Orden Completada");
            }
        }

        return savedPayment;
    }

    public PagosDto toPaymentDto(Payment payment) {
        PagosDto dto = new PagosDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(payment.getOrder().getOrderId());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        return dto;
    }

    public BigDecimal getTotalPaidByOrder(Long orderId) {
        return pagosRepository.getTotalPaidByOrder(orderId);
    }
}
