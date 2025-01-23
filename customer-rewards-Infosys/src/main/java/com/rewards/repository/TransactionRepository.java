package com.rewards.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rewards.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.customer.id = ?1 AND t.date BETWEEN ?2 AND ?3")
    List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
