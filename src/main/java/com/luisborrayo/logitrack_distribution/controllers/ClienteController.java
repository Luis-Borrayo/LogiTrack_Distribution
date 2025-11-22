package com.luisborrayo.logitrack_distribution.controllers;

import com.luisborrayo.logitrack_distribution.models.Cliente;
import com.luisborrayo.logitrack_distribution.services.ClienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/cleintes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteController {
    @Inject
    private ClienteService customerService;

    @GET
    public Response getAllCustomers() {
        List<Cliente> customers = customerService.getAll();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/active")
    public Response getActiveCustomers() {
        List<Cliente> customers = customerService.getActiveCustomers();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/tax-id/{taxId}")
    public Response getCustomerByTaxId(@PathParam("taxId") String taxId) {
        Optional<Cliente> customer = customerService.findByTaxId(taxId);
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente no encontrado")
                    .build();
        }
        return Response.ok(customer.get()).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        Optional<Cliente> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente no encontrado")
                    .build();
        }
        return Response.ok(customer.get()).build();
    }

    @POST
    public Response createCustomer(Cliente customer) {
        // Validaciones
        if (customer.getNombre() == null || customer.getNombre().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El nombre completo es requerido")
                    .build();
        }

        if (customer.getCorreo() == null || customer.getCorreo().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El email es requerido")
                    .build();
        }

        if (customer.getActivo() == null) {
            customer.setActivo(true);
        }

        Optional<Cliente> savedCustomer = customerService.save(customer);
        if (savedCustomer.isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("El NIT o email ya existe")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(savedCustomer.get())
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Cliente customer) {
        Optional<Cliente> existingCustomer = customerService.findById(id);
        if (existingCustomer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente no encontrado")
                    .build();
        }

        Cliente customerToUpdate = existingCustomer.get();
        customerToUpdate.setNombre(customer.getNombre());
        customerToUpdate.setTaxId(customer.getTaxId());
        customerToUpdate.setCorreo(customer.getCorreo());
        customerToUpdate.setDireccion(customer.getDireccion());
        customerToUpdate.setActivo(customer.getActivo());

        Optional<Cliente> updatedCustomer = customerService.save(customerToUpdate);
        if (updatedCustomer.isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("El NIT o email ya existe")
                    .build();
        }

        return Response.ok(updatedCustomer.get()).build();
    }

    @PATCH
    @Path("/{id}/deactivate")
    public Response deactivateCustomer(@PathParam("id") Long id) {
        Optional<Cliente> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente no encontrado")
                    .build();
        }

        customerService.deactivateCustomer(id);
        return Response.ok().entity("Cliente desactivado").build();
    }

    @PATCH
    @Path("/{id}/activate")
    public Response activateCustomer(@PathParam("id") Long id) {
        Optional<Cliente> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente no encontrado")
                    .build();
        }

        customerService.activateCustomer(id);
        return Response.ok().entity("Cliente activado").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        Optional<Cliente> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente no encontrado")
                    .build();
        }

        customerService.delete(customer.get());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
