package com.luisborrayo.logitrack_distribution.controllers;

import com.luisborrayo.logitrack_distribution.dtos.PagosDto;
import com.luisborrayo.logitrack_distribution.models.Payment;
import com.luisborrayo.logitrack_distribution.services.PagosService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/pagos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagosController {
    @Inject
    private PagosService PagosService;

    @GET
    public Response getAllPayments() {
        List<Payment> payments = PagosService.getAll();
        List<PagosDto> paymentDtos = payments.stream()
                .map(PagosService::toPaymentDto)
                .collect(Collectors.toList());
        return Response.ok(paymentDtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") Long id) {
        Optional<Payment> payment = PagosService.findById(id);
        if (payment.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pago no encontrado")
                    .build();
        }
        PagosDto paymentDto = PagosService.toPaymentDto(payment.get());
        return Response.ok(paymentDto).build();
    }

    @GET
    @Path("/order/{orderId}")
    public Response getPaymentsByOrder(@PathParam("orderId") Long orderId) {
        List<Payment> payments = PagosService.findByOrderId(orderId);
        List<PagosDto> paymentDtos = payments.stream()
                .map(PagosService::toPaymentDto)
                .collect(Collectors.toList());
        return Response.ok(paymentDtos).build();
    }

    @POST
    public Response processPayment(PagosDto paymentDto) {
        // Validaciones básicas
        if (paymentDto.getOrderId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ID de la orden es requerido")
                    .build();
        }

        if (paymentDto.getAmount() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El monto del pago es requerido")
                    .build();
        }

        if (paymentDto.getMethod() == null || paymentDto.getMethod().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El método de pago es requerido")
                    .build();
        }

        Optional<Payment> processedPayment = PagosService.processPayment(paymentDto);
        if (processedPayment.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo procesar el pago. Verifique la orden, el monto y el método de pago")
                    .build();
        }

        PagosDto responseDto = PagosService.toPaymentDto(processedPayment.get());
        return Response.status(Response.Status.CREATED)
                .entity(responseDto)
                .build();
    }

    @GET
    @Path("/order/{orderId}/total-paid")
    public Response getTotalPaidByOrder(@PathParam("orderId") Long orderId) {
        BigDecimal totalPaid = PagosService.getTotalPaidByOrder(orderId);
        return Response.ok().entity(new TotalPaidResponse(orderId, totalPaid)).build();
    }

    // Clase interna para response
    public static class TotalPaidResponse {
        private Long orderId;
        private BigDecimal totalPaid;

        public TotalPaidResponse(Long orderId, BigDecimal totalPaid) {
            this.orderId = orderId;
            this.totalPaid = totalPaid;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public BigDecimal getTotalPaid() {
            return totalPaid;
        }

        public void setTotalPaid(BigDecimal totalPaid) {
            this.totalPaid = totalPaid;
        }
    }
}
