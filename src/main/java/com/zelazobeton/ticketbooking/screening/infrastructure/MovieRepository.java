package com.zelazobeton.ticketbooking.screening.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zelazobeton.ticketbooking.screening.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
