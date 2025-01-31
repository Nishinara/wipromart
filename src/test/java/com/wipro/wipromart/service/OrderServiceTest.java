package com.wipro.wipromart.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.entity.Order;
import com.wipro.wipromart.entity.OrderItem;
import com.wipro.wipromart.entity.Product;
import com.wipro.wipromart.exception.ResourceNotFoundException;
import com.wipro.wipromart.repository.OrderRepository;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveOrder_Success() {
        // Mock customer
        long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        // Mock order items
        OrderItem item1 = new OrderItem();
        item1.setProductId(101L);
        item1.setQty(2);

        OrderItem item2 = new OrderItem();
        item2.setProductId(102L);
        item2.setQty(3);

        // Mock product details
        Product product1 = new Product();
        product1.setProductId(101L);
        product1.setProductPrice(50.0);

        Product product2 = new Product();
        product2.setProductId(102L);
        product2.setProductPrice(30.0);

        // Mock order
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(Arrays.asList(item1, item2));

        // Mock external service calls
        when(customerService.getCustomerById(customerId)).thenReturn(customer);
        when(productService.getProductById(101L)).thenReturn(product1);
        when(productService.getProductById(102L)).thenReturn(product2);

        // Mock repository call
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Call the method
        Order savedOrder = orderService.saveOrder(order);

        // Verify the results
        assertNotNull(savedOrder);
        assertEquals(190.0, savedOrder.getOrderAmount()); // 2*50 + 3*30 = 190
        assertEquals("Success", savedOrder.getOrderStatus());
        assertNotNull(savedOrder.getOrderDate());

        // Verify interactions
        verify(customerService, times(1)).getCustomerById(customerId);
        verify(productService, times(1)).getProductById(101L);
        verify(productService, times(1)).getProductById(102L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testGetOrderDetails_OrderFound() {
        // Mock order
        Order order = new Order();
        order.setOrderId(1);

        // Mock repository call
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // Call the method
        Order retrievedOrder = orderService.getOrderDetails(1);

        // Verify the results
        assertNotNull(retrievedOrder);
        assertEquals(1, retrievedOrder.getOrderId());

        // Verify interactions
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    public void testGetOrderDetails_OrderNotFound() {
        // Mock repository call
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // Call the method and verify exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderDetails(1);
        });

        assertEquals("Order not found", exception.getMessage());

        // Verify interactions
        verify(orderRepository, times(1)).findById(1);
    }
}
