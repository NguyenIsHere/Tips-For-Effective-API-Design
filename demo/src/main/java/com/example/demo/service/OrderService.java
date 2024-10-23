package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(OrderItem orderItem, User user) throws Exception{

        Order createOrder = new Order();
        Product product = productRepository.findByName(orderItem.getProductName());
        orderItem.setTotalPrice(orderItem.getQuantity()* product.getPrice());

        createOrder.getOrderItems().add(orderItem);
        createOrder.setCreatedAt(new Date());
        createOrder.setDeliveryAddress(user.getAddress());
        createOrder.setUserId(user.getId());
        createOrder.setStatus("Đang đợi lấy hàng");
        double totalPrice = 0;
        for(int i = 0 ; i < createOrder.getOrderItems().size(); i++){
            totalPrice = totalPrice + createOrder.getOrderItems().get(i).getTotalPrice();
        }
        createOrder.setTotalPrice(totalPrice);

        orderRepository.save(createOrder);
        return createOrder;
    }

    public Order updateOrder(String orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if(orderStatus.equals("Đang đợi lấy hàng")
                || orderStatus.equals("Đã giao")
                || orderStatus.equals("Đang giao")){
            order.setStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("Please select a valid order status");
    }

    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    public void cancelOrder(String orderId) throws Exception {
        Order order = findOrderById(orderId);
        if(order.getStatus().equals("Đang đợi lấy hàng")){
            orderRepository.deleteById(orderId);
        }else{
            throw new Exception(order.getStatus());
        }
    }

    public Order findOrderById(String orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(orderId);
        if(optionalOrder.isEmpty()){
            throw new Exception("Order not found");
        }
        return optionalOrder.get();
    }

    public List<Order> getUsersOrder(String userId) throws Exception {
        return orderRepository.findByUserId(userId);
    }

}
