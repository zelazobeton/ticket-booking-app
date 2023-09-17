package com.zelazobeton.ticketbooking.shared;

import java.util.Collection;

public interface GenericRepository<T> {

    T load(final Long id);

    T save(final T aggregate);

    void saveAll(final Collection<T> aggregates);

    void remove(final Long id);
}
