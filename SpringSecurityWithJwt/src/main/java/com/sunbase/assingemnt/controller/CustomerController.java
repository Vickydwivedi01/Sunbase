package com.sunbase.assingemnt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbase.assingemnt.model.Customer;
import com.sunbase.assingemnt.service.CustomerService;
import com.sunbase.assingemnt.service.RemoteApiService;


@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
    private RemoteApiService remoteApiService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	

	
	@PostMapping("/customers")
	public ResponseEntity<Customer> saveCustomerHandler(@RequestBody Customer customer){
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		Customer registeredCustomer= customerService.registerCustomer(customer);
		return new ResponseEntity<>(registeredCustomer,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/customers/{email}")
	public ResponseEntity<Customer> getCustomerByEmailHandler(@PathVariable("email") String email){	
		Customer customer= customerService.getCustomerDetailsByEmail(email);
		return new ResponseEntity<>(customer,HttpStatus.ACCEPTED);	
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customerDetails) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDetails));
    }

    @GetMapping("/customers")
    public ResponseEntity<Page<Customer>> getAllCustomers(Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
	
    @GetMapping("/customers/search")
    public ResponseEntity<Page<Customer>> searchCustomers(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<Customer> customers = customerService.searchCustomers(keyword, pageable);
        return ResponseEntity.ok(customers);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
  
    @PostMapping("/fetch-and-save-customers")
    public ResponseEntity<String> fetchAndSaveCustomers() {
        remoteApiService.fetchAndSaveCustomers();
        return ResponseEntity.ok("Customers fetched and saved successfully");
    }
}
