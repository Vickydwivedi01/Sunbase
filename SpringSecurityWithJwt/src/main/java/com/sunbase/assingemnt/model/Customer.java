package com.sunbase.assingemnt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer custId;
	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private String state;
	private String phone;
	private String email;
	private String address;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	


}
