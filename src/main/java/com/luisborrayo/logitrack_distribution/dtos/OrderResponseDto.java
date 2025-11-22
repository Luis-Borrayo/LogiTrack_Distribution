package com.luisborrayo.logitrack_distribution.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    private Long orderId;
    private Long clienteId;
    private String NombreCliente;
    private LocalDateTime fechaorden;
    private String estado;
    private BigDecimal montototal;
    private List<OrdenItemDto> items;
    private BigDecimal paidAmount;
    private BigDecimal montopendiente;

    public OrderResponseDto() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getCustomerId() { return clienteId; }
    public void setCustomerId(Long customerId) { this.clienteId = customerId; }
    public String getCustomerName() { return NombreCliente; }
    public void setCustomerName(String customerName) { this.NombreCliente = customerName; }
    public LocalDateTime getOrderDate() { return fechaorden; }
    public void setOrderDate(LocalDateTime orderDate) { this.fechaorden = orderDate; }
    public String getStatus() { return estado; }
    public void setStatus(String status) { this.estado = status; }
    public BigDecimal getTotalAmount() { return montototal; }
    public void setTotalAmount(BigDecimal totalAmount) { this.montototal = totalAmount; }
    public List<OrdenItemDto> getItems() { return items; }
    public void setItems(List<OrdenItemDto> items) { this.items = items; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getPendingAmount() { return montopendiente; }
    public void setPendingAmount(BigDecimal pendingAmount) { this.montopendiente = pendingAmount; }
}
