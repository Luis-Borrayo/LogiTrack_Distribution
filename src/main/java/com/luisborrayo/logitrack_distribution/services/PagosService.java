package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.dtos.PagosDto;
import com.luisborrayo.logitrack_distribution.models.Orden;
import com.luisborrayo.logitrack_distribution.models.Pagos;
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

    public List<Pagos> getAll() {
        return pagosRepository.getAll();
    }

    public Optional<Pagos> findById(Long id) {
        return pagosRepository.findById(id);
    }

    public List<Pagos> findByOrderId(Long orderId) {
        return pagosRepository.findByOrderId(orderId);
    }

    @Transactional
    public Optional<Pagos> processPayment(PagosDto paymentDto) {
        // Validar que la orden exista
        Optional<Orden> orderOpt = ordenRepository.findById(paymentDto.getOrderId());
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Orden order = orderOpt.get();

        // Validar que el monto no sea negativo o cero
        if (paymentDto.getAmount() == null || paymentDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.empty();
        }

        // Calcular total pagado hasta ahora
        BigDecimal totalPaid = pagosRepository.getTotalPaidByOrder(order.getOrderId());
        BigDecimal pendingAmount = order.getMontototal().subtract(totalPaid);

        // Validar que el pago no exceda el monto pendiente
        if (paymentDto.getAmount().compareTo(pendingAmount) > 0) {
            return Optional.empty();
        }

        // Validar método de pago
        if (paymentDto.getMethod() == null ||
                !List.of("Cash", "Card", "Transfer").contains(paymentDto.getMethod())) {
            return Optional.empty();
        }

        // Crear y guardar el pago
        Pagos payment = new Pagos();
        payment.setOrder(order);
        payment.setAmount(paymentDto.getAmount());
        payment.setMethod(paymentDto.getMethod());

        Optional<Pagos> savedPayment = pagosRepository.save(payment);

        // Actualizar estado de la orden si está completamente pagada
        if (savedPayment.isPresent()) {
            BigDecimal newTotalPaid = totalPaid.add(paymentDto.getAmount());
            if (newTotalPaid.compareTo(order.getMontototal()) >= 0 &&
                    !"Completed".equals(order.getEstado())) {
                ordenService.updateOrderStatus(order.getOrderId(), "Completed");
            }
        }

        return savedPayment;
    }

    public PagosDto toPaymentDto(Pagos payment) {
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
