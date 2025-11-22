package com.luisborrayo.logitrack_distribution.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)  // SIN unique=true
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    private String categoria;

    private Boolean activo = true;

    public Producto() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescription(String description) {
        this.descripcion = description;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal price) {
        this.precio = price;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String category) {
        this.categoria = category;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean active) {
        this.activo = active;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", activo=" + activo +
                '}';
    }
}
