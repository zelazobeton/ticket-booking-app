package com.zelazobeton.ticketbooking.shared;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;


@Transactional(readOnly = true)
public abstract class AbstractRepository<T extends BaseEntity> implements GenericRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public AbstractRepository() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T load(final Long id) {
        T entity = this.entityManager.find(this.clazz, id);
        return entity;
    }

    @Override
    public T save(final T aggregate) {
        this.singleItemSave(aggregate);
        this.entityManager.flush();
        return aggregate;
    }

    @Override
    public void saveAll(final Collection<T> aggregates) {
        for (T aggregate : aggregates) {
            this.singleItemSave(aggregate);
        }
        this.entityManager.flush();
    }

    @Override
    public void remove(final Long id) {
        final T entity = this.load(id);
        this.entityManager.remove(entity);
        this.entityManager.flush();
    }

    private void singleItemSave(final T aggregate) {
        if (!this.entityManager.contains(aggregate)) {
            this.entityManager.persist(aggregate);
        }
        this.entityManager.lock(aggregate, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    protected void flush() {
        this.entityManager.flush();
    }

    protected TypedQuery<T> getJPQLQuery(String jpqlQuery) {
        return this.entityManager.createQuery(jpqlQuery, this.clazz);
    }
}
