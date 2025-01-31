package com.wipro.wipromart.service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.wipromart.controller.ProductController;
import com.wipro.wipromart.entity.Product;
import com.wipro.wipromart.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddProduct() throws Exception {
        // Mock product
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setProductPrice(1500.00);

        // Mock service behavior

        // Perform POST request
        mockMvc.perform(post("/product/save")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.productPrice").value(1500.00));
    }

    @Test
    public void testFetchProductById() throws Exception {
        // Mock product
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setProductPrice(1500.00);

        // Mock service behavior
        when(productService.getProductById(1L)).thenReturn(product);

        // Perform GET request
        mockMvc.perform(get("/product/get/{productId}", 1L)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.productPrice").value(1500.00));
    }

    @Test
    public void testFetchAllProducts() throws Exception {
        // Mock products
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setProductName("Laptop");
        product1.setProductPrice(1500.00);

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setProductName("Mobile");
        product2.setProductPrice(800.00);

        List<Product> products = Arrays.asList(product1, product2);

        // Mock service behavior
        when(productService.getAllProducts()).thenReturn(products);

        // Perform GET request
        mockMvc.perform(get("/product/get/all")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Laptop"))
                .andExpect(jsonPath("$[0].productPrice").value(1500.00))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].productName").value("Mobile"))
                .andExpect(jsonPath("$[1].productPrice").value(800.00));
    }
}

