package com.zelazobeton.ticketbooking.screening.infrastructure;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
}
