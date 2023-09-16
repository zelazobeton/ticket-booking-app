package com.zelazobeton.ticketbooking.reservation.application.service.impl;

import com.zelazobeton.ticketbooking.ClockConfig;
import com.zelazobeton.ticketbooking.JdbcConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.zelazobeton.ticketbooking.recommendation",
        "com.zelazobeton.ticketbooking.screening", 
        "com.zelazobeton.ticketbooking.reservation"})
@EnableJdbcRepositories(basePackages = {
        "com.zelazobeton.ticketbooking.screening",
        "com.zelazobeton.ticketbooking.reservation"})
@Import({JdbcConfig.class, ClockConfig.class})
public class ReservationTestContext {}
