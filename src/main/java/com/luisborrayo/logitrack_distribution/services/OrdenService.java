package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.dtos.CreateOrdenDto;
import com.luisborrayo.logitrack_distribution.dtos.OrdenItemDto;
import com.luisborrayo.logitrack_distribution.dtos.OrdenItemSoliDto;
import com.luisborrayo.logitrack_distribution.dtos.OrderResponseDto;
import com.luisborrayo.logitrack_distribution.models.Cliente;
import com.luisborrayo.logitrack_distribution.models.Orden;
import com.luisborrayo.logitrack_distribution.models.OrdenItem;
import com.luisborrayo.logitrack_distribution.models.Producto;
import com.luisborrayo.logitrack_distribution.repositoriees.OrdenRepository;
import com.luisborrayo.logitrack_distribution.repositoriees.PagosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrdenService {
    @Inject
    private OrdenRepository ordenRepository;

    @Inject
    private ClienteService customerService;

    @Inject
    private ProductoService productService;

    @Inject
    private PagosRepository paymentRepository;

    public List<Orden> getAll() {
        return ordenRepository.getAll();
    }

    public Optional<Orden> findById(Long id) {
        return ordenRepository.findById(id);
    }

    public List<Orden> findByCustomerId(Long customerId) {
        return ordenRepository.findByCustomerId(customerId);
    }

    public List<Orden> findByStatus(String status) {
        return ordenRepository.findByStatus(status);
    }

    public List<Orden> findIncompleteOrders() {
        return ordenRepository.findIncompleteOrders();
    }

    @Transactional
    public Optional<Orden> createOrder(CreateOrdenDto orderDto) {
        // Validar que el cliente exista y esté activo
        Optional<Cliente> customer = customerService.findById(orderDto.getCustomerId());
        if (customer.isEmpty() || !customerService.isCustomerActive(orderDto.getCustomerId())) {
            return Optional.empty();
        }

        // Crear la orden
        Orden order = new Orden();
        order.setCliente(customer.get());
        order.setCliente("Pending");

        // Procesar items
        List<OrdenItem> items = new ArrayList<>();
        for (OrdenItemSoliDto itemDto : orderDto.getItems()) {
            Optional<Producto> product = productService.findById(itemDto.getProductId());
            if (product.isEmpty() || !product.get().getActivo()) {
                return Optional.empty(); // Producto no encontrado o inactivo
            }

            if (itemDto.getQuantity() <= 0) {
                return Optional.empty(); // Cantidad inválida
            }

            OrdenItem orderItem = new OrdenItem();
            orderItem.setProducto(product.get());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.get().getPrecio());
            orderItem.calculateSubtotal();

            items.add(orderItem);
            order.addItem(orderItem);
        }

        // Calcular total
        order.calculateTotal();

        // Guardar la orden
        return ordenRepository.save(order);
    }

    @Transactional
    public Optional<Orden> updateOrderStatus(Long orderId, String status) {
        Optional<Orden> orderOpt = ordenRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Orden order = orderOpt.get();
        order.setEstado(status);
        return ordenRepository.save(order);
    }

    public OrderResponseDto toOrderResponseDto(Orden order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCliente().getClienteId());
        dto.setCustomerName(order.getCliente().getNombre());
        dto.setOrderDate(order.getFechaOrden());
        dto.setStatus(order.getEstado());
        dto.setTotalAmount(order.getMontototal());

        // Convertir items a DTO
        List<OrdenItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrdenItemDto(
                        item.getProducto().getProductId(),
                        item.getProducto().getNombre(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        // Calcular montos de pago
        BigDecimal paidAmount = paymentRepository.getTotalPaidByOrder(order.getOrderId());
        dto.setPaidAmount(paidAmount);
        dto.setPendingAmount(order.getMontototal().subtract(paidAmount));

        return dto;
    }

    public BigDecimal getTotalDebtByCustomer(Long customerId) {
        return ordenRepository.getTotalDebtByCustomer(customerId);
    }
}
