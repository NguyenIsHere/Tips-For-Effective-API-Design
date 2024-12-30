package com.example.demo;

import com.example.demo.model.OrderRequest;
import com.example.demo.repository.OrderRequestRepository;
import com.example.demo.service.OrderRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderRequestServiceTest {

    @Mock
    private OrderRequestRepository orderRequestRepository;

    @InjectMocks
    private OrderRequestService orderRequestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOrders() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderRequestId("1");
        List<OrderRequest> orders = List.of(orderRequest);
        when(orderRequestRepository.findAll()).thenReturn(orders);

        // Act
        List<OrderRequest> result = orderRequestService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getOrderRequestId());
        verify(orderRequestRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrderById_OrderExists() {
        // Arrange
        String orderRequestId = "1";
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderRequestId(orderRequestId);
        when(orderRequestRepository.findById(orderRequestId)).thenReturn(Optional.of(orderRequest));

        // Act
        OrderRequest result = orderRequestService.getOrderById(orderRequestId);

        // Assert
        assertNotNull(result);
        assertEquals(orderRequestId, result.getOrderRequestId());
        verify(orderRequestRepository, times(1)).findById(orderRequestId);
    }

    @Test
    public void testGetOrderById_OrderNotFound() {
        // Arrange
        String orderRequestId = "1";
        when(orderRequestRepository.findById(orderRequestId)).thenReturn(Optional.empty());

        // Act
        OrderRequest result = orderRequestService.getOrderById(orderRequestId);

        // Assert
        assertNull(result);
        verify(orderRequestRepository, times(1)).findById(orderRequestId);
    }

    @Test
    public void testUpdateOrderStatus() {
        // Arrange
        String orderRequestId = "1";
        Map<String, Object> statusUpdate = Map.of("status", "DELIVERED");
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderRequestId(orderRequestId);
        orderRequest.setStatus("PENDING");
        when(orderRequestRepository.findById(orderRequestId)).thenReturn(Optional.of(orderRequest));
        when(orderRequestRepository.save(any(OrderRequest.class))).thenReturn(orderRequest);

        // Act
        String result = orderRequestService.updateOrderStatus(orderRequestId, statusUpdate);

        // Assert
        assertEquals("Order status updated successfully", result);
        assertEquals("DELIVERED", orderRequest.getStatus());
        verify(orderRequestRepository, times(1)).findById(orderRequestId);
        verify(orderRequestRepository, times(1)).save(orderRequest);
    }

    @Test
    public void testEditOrder_OrderExists() {
        // Arrange
        String orderRequestId = "1";
        OrderRequest existingOrder = new OrderRequest();
        existingOrder.setOrderRequestId(orderRequestId);

        OrderRequest updatedOrder = new OrderRequest();
        updatedOrder.setUserId("User123");
        updatedOrder.setAmount(1000);
        updatedOrder.setStatus("SHIPPED");

        when(orderRequestRepository.findById(orderRequestId)).thenReturn(Optional.of(existingOrder));
        when(orderRequestRepository.save(any(OrderRequest.class))).thenReturn(existingOrder);

        // Act
        String result = orderRequestService.editOrder(orderRequestId, updatedOrder);

        // Assert
        assertEquals("Order updated successfully", result);
        assertEquals("User123", existingOrder.getUserId());
        assertEquals(1000, existingOrder.getAmount());
        assertEquals("SHIPPED", existingOrder.getStatus());
        verify(orderRequestRepository, times(1)).findById(orderRequestId);
        verify(orderRequestRepository, times(1)).save(existingOrder);
    }

    @Test
    public void testEditOrder_OrderNotFound() {
        // Arrange
        String orderRequestId = "1";
        OrderRequest updatedOrder = new OrderRequest();
        when(orderRequestRepository.findById(orderRequestId)).thenReturn(Optional.empty());

        // Act
        String result = orderRequestService.editOrder(orderRequestId, updatedOrder);

        // Assert
        assertEquals("Order not found", result);
        verify(orderRequestRepository, times(1)).findById(orderRequestId);
        verify(orderRequestRepository, never()).save(any(OrderRequest.class));
    }

    @Test
    public void testDeleteOrder() {
        // Arrange
        String orderRequestId = "1";
        doNothing().when(orderRequestRepository).deleteById(orderRequestId);

        // Act
        String result = orderRequestService.deleteOrder(orderRequestId);

        // Assert
        assertEquals("Order deleted successfully", result);
        verify(orderRequestRepository, times(1)).deleteById(orderRequestId);
    }

    @Test
    public void testGetOrdersByUserId() {
        // Arrange
        String userId = "User123";
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        List<OrderRequest> orders = List.of(orderRequest);
        Page<OrderRequest> page = new PageImpl<>(orders, PageRequest.of(0, 10), orders.size());
        when(orderRequestRepository.findByUserId(userId, PageRequest.of(0, 10))).thenReturn(page);

        // Act
        Page<OrderRequest> result = orderRequestService.getOrdersByUserId(userId, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userId, result.getContent().get(0).getUserId());
        verify(orderRequestRepository, times(1)).findByUserId(userId, PageRequest.of(0, 10));
    }
}
