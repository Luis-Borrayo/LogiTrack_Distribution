package com.luisborrayo.logitrack_distribution.dtos;

import java.math.BigDecimal;

public class OrdenItemDto {
    private Long productId;
    private String NombreProducto;
    private Integer quantity;
    private BigDecimal PrecioUnic;
    private BigDecimal subtotal;

    public OrdenItemDto() {}

    public OrdenItemDto(Long productId, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        this.productId = productId;
        this.NombreProducto = productName;
        this.quantity = quantity;
        this.PrecioUnic = unitPrice;
        this.subtotal = subtotal;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getNombreProducto() { return NombreProducto; }
    public void setNombreProducto(String productName) { this.NombreProducto = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrecioUnic() { return PrecioUnic; }
    public void setPrecioUnic(BigDecimal unitPrice) { this.PrecioUnic = unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
