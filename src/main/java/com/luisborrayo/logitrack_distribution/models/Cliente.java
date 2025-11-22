package com.luisborrayo.logitrack_distribution.models;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.security.PublicKey;

@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cleintes_id")
    private Long ClienteId;

    @Column(name = "full_name", nullable = false)
    private String Nombre;

    @Column(name = "tax_id", unique = true)
    private String taxId;

    @Column(nullable = false, unique = true)
    private String correo;


    private String direccion;

    private Boolean activo = true;

    public Cliente() {}

    public Cliente(String nombre, String correo, String direccion) {
        this.Nombre = nombre;
        this.direccion = direccion;
        this.activo = true;
    }

    public Long getClienteId() {
        return ClienteId;
    }

    public void setClienteId(Long customerId) {
        this.ClienteId = customerId;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String fullName) {
        this.Nombre = fullName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String email) {
        this.correo = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String address) {
        this.direccion = address;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean active) {
        this.activo = active;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "ClienteId=" + ClienteId +
                ", Nombre='" + Nombre + '\'' +
                ", taxId='" + taxId + '\'' +
                ", correo='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                '}';
    }
}
