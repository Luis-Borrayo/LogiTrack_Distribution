package com.luisborrayo.logitrack_distribution.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Cliente cliente;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime fechaOrden = LocalDateTime.now();

    @Column(nullable = false)
    private String estado = "Pendiente";

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal montototal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Pagos> pagos = new ArrayList<>();

    public Orden() {}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente customer) {
        this.cliente = customer;
    }

    public LocalDateTime getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDateTime orderDate) {
        this.fechaOrden = orderDate;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String status) {
        this.estado = status;
    }

    public BigDecimal getMontototal() {
        return montototal;
    }

    public void setMontototal(BigDecimal totalAmount) {
        this.montototal = totalAmount;
    }

    public List<OrdenItem> getItems() {
        return items;
    }

    public void setItems(List<OrdenItem> items) {
        this.items = items;
    }

    public List<Pagos> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pagos> payments) {
        this.pagos = pagos;
    }

    public void addItem(OrdenItem item) {
        items.add(item);
        item.setOrden(this);
    }

    public void removeItem(OrdenItem item) {
        items.remove(item);
        item.setOrden(null);
    }

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        this.montototal = items.stream()
                .map(OrdenItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("orderId=").append(orderId);
        sb.append(", customer=").append(cliente != null ? cliente.getClienteId() : null);
        sb.append(", orderDate=").append(fechaOrden);
        sb.append(", status='").append(estado).append('\'');
        sb.append(", totalAmount=").append(montototal);
        sb.append(", itemsCount=").append(items != null ? items.size() : 0);
        sb.append('}');
        return sb.toString();
    }
}
