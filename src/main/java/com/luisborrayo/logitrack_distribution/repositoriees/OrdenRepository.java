package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.models.Orden;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class OrdenRepository extends BaseRepository<Orden, Long>{
    @Override
    protected Class<Orden> entity() {
        return Orden.class;
    }

    public List<Orden> findByCustomerId(Long customerId) {
        return entityManager.createQuery(
                        "SELECT o FROM Orden o WHERE o.cliente.ClienteId = :customerId ORDER BY o.fechaOrden DESC", Orden.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public List<Orden> findByStatus(String status) {
        return entityManager.createQuery(
                        "SELECT o FROM Orden o WHERE o.estado = :status ORDER BY o.fechaOrden DESC", Orden.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Orden> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return entityManager.createQuery(
                        "SELECT o FROM Orden o WHERE o.fechaOrden BETWEEN :startDate AND :endDate ORDER BY o.fechaOrden DESC", Orden.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public BigDecimal getTotalDebtByCustomer(Long customerId) {
        try {
            TypedQuery<BigDecimal> query = entityManager.createQuery(
                    "SELECT SUM(o.montototal) - COALESCE(SUM(p.amount), 0) " +
                            "FROM Orden o LEFT JOIN o.pagos p " +
                            "WHERE o.cliente.ClienteId = :customerId AND o.estado <> 'Cancelled'", BigDecimal.class);            query.setParameter("customerId", customerId);
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public List<Orden> findIncompleteOrders() {
        return entityManager.createQuery(
                        "SELECT o FROM Orden o WHERE o.estado IN ('Pending', 'Processing') ORDER BY o.fechaOrden ASC", Orden.class)
                .getResultList();
    }



}
