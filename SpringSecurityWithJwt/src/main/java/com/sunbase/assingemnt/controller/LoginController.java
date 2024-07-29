package com.sunbase.assingemnt.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbase.assingemnt.model.Customer;
import com.sunbase.assingemnt.repository.CustomerRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;




@RestController
public class LoginController {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/signIn")
	public ResponseEntity<Customer> getLoggedInCustomerDetailsHandler(Authentication auth) {
		Customer customer = customerRepository.findByEmail(auth.getName())
				.orElseThrow(() -> new BadCredentialsException("Invalid Username or password"));
		return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);

	}
//    @GetMapping("/signIn")
//    public ResponseEntity<?> getLoggedInCustomerDetailsHandler(Authentication auth) {
//        if (auth == null || !auth.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                                 .body(Collections.singletonMap("message", "Invalid Username or password"));
//        }
//
//        Customer customer = customerRepository.findByEmail(auth.getName())
//                .orElseThrow(() -> new BadCredentialsException("Invalid Username or password"));
//
//        String token = generateToken(customer);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//
//        return new ResponseEntity<>(customer, headers, HttpStatus.ACCEPTED);
//    }

    private String generateToken(Customer customer) {
        // Your token generation logic here
        return "generated-jwt-token"; // Replace with actual token generation
    }

}
