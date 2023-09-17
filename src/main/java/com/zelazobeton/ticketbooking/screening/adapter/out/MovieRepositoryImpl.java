package com.zelazobeton.ticketbooking.screening.adapter.out;

import com.zelazobeton.ticketbooking.screening.application.port.out.MovieRepository;
import com.zelazobeton.ticketbooking.screening.model.Movie;
import com.zelazobeton.ticketbooking.shared.AbstractRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
class MovieRepositoryImpl extends AbstractRepository<Movie> implements MovieRepository {

}
