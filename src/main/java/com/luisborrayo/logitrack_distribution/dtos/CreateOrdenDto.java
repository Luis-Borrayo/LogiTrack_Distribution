package com.luisborrayo.logitrack_distribution.dtos;

import java.util.List;

public class CreateOrdenDto {
    private Long clienteId;
    private List<OrdenItemSoliDto> items;

    public CreateOrdenDto() {}

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long customerId) { this.clienteId = customerId; }
    public List<OrdenItemSoliDto> getItems() { return items; }
    public void setItems(List<OrdenItemSoliDto> items) { this.items = items; }
}
