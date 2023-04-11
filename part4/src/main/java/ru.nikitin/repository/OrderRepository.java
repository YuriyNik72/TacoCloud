package ru.nikitin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nikitin.entity.TacoOrder;

import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    TacoOrder save(TacoOrder order);

    List<TacoOrder> findByDeliveryZip(String deliveryZip);
}
