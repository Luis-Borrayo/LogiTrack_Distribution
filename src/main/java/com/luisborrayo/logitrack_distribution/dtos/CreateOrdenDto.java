package com.luisborrayo.logitrack_distribution.dtos;

import java.util.List;

public class CreateOrdenDto {
    private Long customerId;
    private List<OrdenItemSoliDto> items;

    // Constructors
    public CreateOrdenDto() {}

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public List<OrdenItemSoliDto> getItems() { return items; }
    public void setItems(List<OrdenItemSoliDto> items) { this.items = items; }
}
