package com.luisborrayo.logitrack_distribution.repositoriees;

import com.luisborrayo.logitrack_distribution.models.Pagos;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PagosRepository extends BaseRepository<Pagos, Long>{
    @Override
    protected Class<Pagos> entity() {
        return Pagos.class;
    }

    public List<Pagos> findByOrderId(Long orderId) {
        return entityManager.createQuery(
                        "SELECT p FROM Pagos p WHERE p.order.orderId = :orderId ORDER BY p.paymentDate DESC", Pagos.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public Optional<Pagos> findLatestByOrder(Long orderId) {
        try {
            TypedQuery<Pagos> query = entityManager.createQuery(
                    "SELECT p FROM Pagos p WHERE p.order.orderId = :orderId ORDER BY p.paymentDate DESC", Pagos.class);
            query.setParameter("orderId", orderId);
            query.setMaxResults(1);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public BigDecimal getTotalPaidByOrder(Long orderId) {
        try {
            TypedQuery<BigDecimal> query = entityManager.createQuery(
                    "SELECT COALESCE(SUM(p.amount), 0) FROM Pagos p WHERE p.order.orderId = :orderId", BigDecimal.class);
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
