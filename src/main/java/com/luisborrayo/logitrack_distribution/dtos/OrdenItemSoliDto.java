package com.luisborrayo.logitrack_distribution.dtos;

public class OrdenItemSoliDto {
    private Long productId;
    private Integer quantity;

    // Constructors
    public OrdenItemSoliDto() {}

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
