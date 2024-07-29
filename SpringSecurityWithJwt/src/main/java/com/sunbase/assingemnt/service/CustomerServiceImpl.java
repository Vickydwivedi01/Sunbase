package com.sunbase.assingemnt.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sunbase.assingemnt.exception.CustomerException;
import com.sunbase.assingemnt.model.Customer;
import com.sunbase.assingemnt.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	@Override
	public Customer registerCustomer(Customer customer) throws CustomerException {	
		return customerRepository.save(customer);	
	}

	@Override
	public Customer getCustomerDetailsByEmail(String email)throws CustomerException {	
		return customerRepository.findByEmail(email).orElseThrow(() -> new CustomerException("Customer Not found with Email: "+email));
	}

	@Override
	public Customer updateCustomer(Integer id, Customer customerDetails) throws CustomerException {
		Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found for this id :: " + id));
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setStreet(customerDetails.getStreet());
        customer.setAddress(customerDetails.getAddress());
        customer.setCity(customerDetails.getCity());
        customer.setState(customerDetails.getState());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        return customerRepository.save(customer);
	}

	

	@Override
	public void deleteCustomer(Integer id) throws CustomerException {
		Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found for this id :: " + id));
        customerRepository.delete(customer);
	}

	@Override
    public Page<Customer> searchCustomers(String keyword, Pageable pageable) throws CustomerException {
        return customerRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(
                keyword, keyword, keyword, pageable);
    }
	@Override
	public Page<Customer> getAllCustomers(Pageable pageable) throws CustomerException {
		return customerRepository.findAll(pageable);
	}

	@Override
	public Customer getCustomerById(Integer id) throws CustomerException {
		return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found for this id :: " + id));
	}

}
