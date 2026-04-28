package com.cafe.kiosk.service;

import com.cafe.kiosk.model.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private static OrderService instance;
    private final List<Order> orders = new ArrayList<>();

    private OrderService() {}

    public static synchronized OrderService getInstance() {
        if (instance == null) instance = new OrderService();
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public double getTotalSales() {
        return orders.stream().mapToDouble(Order::getTotalPrice).sum();
    }
}