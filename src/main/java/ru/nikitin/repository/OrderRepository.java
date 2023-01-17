package ru.nikitin.repository;

import ru.nikitin.entity.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
