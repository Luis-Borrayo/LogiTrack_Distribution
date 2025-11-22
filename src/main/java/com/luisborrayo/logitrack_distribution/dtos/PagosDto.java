package com.luisborrayo.logitrack_distribution.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagosDto {
    private Long paymentId;
    private Long orderId;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String method;

    public PagosDto() {}

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
