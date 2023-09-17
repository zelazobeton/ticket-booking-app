package com.zelazobeton.ticketbooking.screening.application.port.out;

import com.zelazobeton.ticketbooking.shared.GenericRepository;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Movie;

@Repository
public interface MovieRepository extends GenericRepository<Movie> {
}
