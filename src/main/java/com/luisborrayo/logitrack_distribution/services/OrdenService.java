package com.luisborrayo.logitrack_distribution.services;

import com.luisborrayo.logitrack_distribution.dtos.CreateOrdenDto;
import com.luisborrayo.logitrack_distribution.dtos.OrdenItemDto;
import com.luisborrayo.logitrack_distribution.dtos.OrdenItemSoliDto;
import com.luisborrayo.logitrack_distribution.dtos.OrderResponseDto;
import com.luisborrayo.logitrack_distribution.models.Customer;
import com.luisborrayo.logitrack_distribution.models.Order;
import com.luisborrayo.logitrack_distribution.models.OrderItem;
import com.luisborrayo.logitrack_distribution.models.Product;
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

    public List<Order> getAll() {
        return ordenRepository.getAll();
    }

    public Optional<Order> findById(Long id) {
        return ordenRepository.findById(id);
    }

    public List<Order> findByCustomerId(Long customerId) {
        return ordenRepository.findByCustomerId(customerId);
    }

    public List<Order> findByStatus(String status) {
        return ordenRepository.findByStatus(status);
    }

    public List<Order> findIncompleteOrders() {
        return ordenRepository.findIncompleteOrders();
    }

    @Transactional
    public Optional<Order> createOrder(CreateOrdenDto orderDto) {
        Optional<Customer> customer = customerService.findById(orderDto.getClienteId());
        if (customer.isEmpty() || !customerService.isCustomerActive(orderDto.getClienteId())) {
            return Optional.empty();
        }

        Order order = new Order();
        order.setCustomer(customer.get());
        order.setStatus("Pendiente");

        List<OrderItem> items = new ArrayList<>();
        for (OrdenItemSoliDto itemDto : orderDto.getItems()) {
            Optional<Product> product = productService.findById(itemDto.getProductId());
            if (product.isEmpty() || !product.get().getActive()) {
                return Optional.empty();
            }

            if (itemDto.getQuantity() <= 0) {
                return Optional.empty();
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.get());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.get().getPrice());
            orderItem.calculateSubtotal();

            items.add(orderItem);
            order.addItem(orderItem);
        }

        order.calculateTotal();
        return ordenRepository.save(order);
    }

    @Transactional
    public Optional<Order> updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOpt = ordenRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        order.setStatus(status);
        return ordenRepository.save(order);
    }

    public OrderResponseDto toOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getCustomerId());
        dto.setCustomerName(order.getCustomer().getFullName());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrdenItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrdenItemDto(
                        item.getProductId().getProductId(),
                        item.getProductId().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        BigDecimal paidAmount = paymentRepository.getTotalPaidByOrder(order.getOrderId());
        dto.setPaidAmount(paidAmount);
        dto.setPendingAmount(order.getTotalAmount().subtract(paidAmount));

        return dto;
    }

    public BigDecimal getTotalDebtByCustomer(Long customerId) {
        return ordenRepository.getTotalDebtByCustomer(customerId);
    }
}
