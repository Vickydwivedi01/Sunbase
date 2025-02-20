package com.sunbase.assingemnt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sunbase.assingemnt.model.Customer;
import com.sunbase.assingemnt.repository.CustomerRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Optional<Customer> opt= customerRepository.findByEmail(username);
		if(opt.isPresent()) {
			Customer customer= opt.get();
			List<GrantedAuthority> authorities= new ArrayList<>();
			return new User(customer.getEmail(), customer.getPassword(), authorities);
		}else
			throw new BadCredentialsException("User Details not found with this username: "+username);
		
		
		
		
		
	}

}

