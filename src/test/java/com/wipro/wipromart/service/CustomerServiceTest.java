package com.wipro.wipromart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.exception.ResourceNotFoundException;
import com.wipro.wipromart.repository.CustomerRepository;


@SpringBootTest
public class CustomerServiceTest {
	
	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();
	
	@Mock
	private CustomerRepository customertRepository;
	
	@Test
	void testgetCustomerById() {
		
		Customer customer = new Customer();
		customer.setCustomerId(200);
		customer.setFirstName("Nishitha");
		customer.setLastName("Nara");
		customer.setEmail("nishi@gmail.com");
		customer.setMobile("8247618959");
		customer.setCity("hyd");
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customertRepository.findById(200L)).thenReturn(optionalCustomer);
		
		Customer actualCustomer = customerService.getCustomerById(200);
		
		assertEquals("Nishitha",actualCustomer.getFirstName());		
		assertEquals("Nara",actualCustomer.getLastName());		
		
	}
	
	@Test
	void testGetsetCustomerByIdWithException() {
		
		when(customertRepository.findById(200L)).thenThrow(ResourceNotFoundException.class);
				
		assertThrows(ResourceNotFoundException.class, ()-> customerService.getCustomerById(200));		
	}
	
	@Test
	void testSaveCustomer() {
		
		Customer customer = new Customer();
		customer.setCustomerId(200);
		customer.setFirstName("Nishitha");
		customer.setLastName("Nara");
		customer.setEmail("nishi@gmail.com");
		customer.setMobile("8247618959");
		customer.setCity("hyd");
		
		when(customertRepository.save(customer)).thenReturn(customer);
		
		Customer newCustomer = customerService.saveCustomer(customer);
		
		assertEquals(200,newCustomer.getCustomerId());
		assertEquals("Nishitha",newCustomer.getFirstName());
		assertEquals("Nara",newCustomer.getLastName());
		assertEquals("hyd",newCustomer.getCity());		
	}
	
	@Test
	void testGetAllCustomer() {
		
		Customer customer = new Customer();
		customer.setCustomerId(200);
		customer.setFirstName("Nishitha");
		customer.setLastName("Nara");
		customer.setEmail("nishi@gmail.com");
		customer.setMobile("8247618959");
		customer.setCity("hyd");
		
		Customer customer1 = new Customer();
		customer.setCustomerId(300);
		customer.setFirstName("prathyu");
		customer.setLastName("sai");
		customer.setEmail("sai@gmail.com");
		customer.setMobile("8524691125");
		customer.setCity("goa");
		
		Customer customer2 = new Customer();
		customer.setCustomerId(400);
		customer.setFirstName("kris");
		customer.setLastName("pasupu");
		customer.setEmail("kris@gmail.com");
		customer.setMobile("8052461322");
		customer.setCity("pune");
		
		List<Customer> myCustomers = new ArrayList<>();		
		myCustomers.add(customer);
		myCustomers.add(customer1);
		myCustomers.add(customer2);
		
		when(customertRepository.findAll()).thenReturn(myCustomers);
		
		List<Customer> customertList = customerService.getAllCustomers();
		
		assertEquals(myCustomers.size(),customertList.size());
			
	}
	
	@Test
	void testDeleteCustomer() {
		
		Customer customer = new Customer();
		customer.setCustomerId(200);
		customer.setFirstName("Nishitha");
		customer.setLastName("Nara");
		customer.setEmail("nishi@gmail.com");
		customer.setMobile("8247618959");
		customer.setCity("hyd");
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customertRepository.findById(200L)).thenReturn(optionalCustomer);
		
		//when(productRepository.delete(optionalProduct.get()))
		
		doNothing().when(customertRepository).delete(customer);
		
		customerService.deleteCustomer(200); // return type is void 
	    	    
	    verify(customertRepository,times(1)).findById(200L);
	    verify(customertRepository,times(1)).delete(customer);			    
	
	}
		
}


