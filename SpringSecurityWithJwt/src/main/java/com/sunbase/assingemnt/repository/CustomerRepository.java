package com.sunbase.assingemnt.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sunbase.assingemnt.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	public Optional<Customer> findByEmail(String email);
	Page<Customer> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email, Pageable pageable);
}
