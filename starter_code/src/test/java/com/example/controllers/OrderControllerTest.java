package com.example.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.SareetaApplication;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SareetaApplication.class)
public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    public void initEachTest() {
        userRepository = mock(UserRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderController = new OrderController(userRepository, orderRepository);
    }

    @Test
    public void submitTest() {
        Cart cart = createCart();
        UserOrder userOrder = createUserOrder();
        when(userRepository.findByUsername(cart.getUser().getUsername())).thenReturn(cart.getUser());

        ResponseEntity<UserOrder> response = orderController.submit(cart.getUser().getUsername());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        compareOrder(response.getBody(), userOrder);

    }

    @Test
    public void getOrdersForUser() {
        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(createUserOrder());

        Cart cart = createCart();
        UserOrder userOrder = createUserOrder();
        when(userRepository.findByUsername(cart.getUser().getUsername())).thenReturn(cart.getUser());
        when(orderRepository.findByUser(cart.getUser())).thenReturn(userOrderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(cart.getUser().getUsername());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 1);
        compareOrder(response.getBody().get(0), userOrder);

    }

    private void compareOrder(UserOrder userOrder1, UserOrder userOrder2) {
        assertEquals(userOrder1.getUser().getUsername(), userOrder2.getUser().getUsername());
        assertEquals(userOrder1.getItems().size(), userOrder2.getItems().size());
        assertEquals(userOrder1.getTotal(), userOrder2.getTotal());
        assertEquals(userOrder1.getItems().get(0).getPrice(), userOrder2.getItems().get(0).getPrice());
    }

    private Cart createCart() {
        User user = new User();
        user.setId(1);
        user.setUsername("Mitsu");
        user.setPassword("1234");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(createItem());
        cart.setTotal(new BigDecimal(10000));

        user.setCart(cart);
        return cart;
    }

    private UserOrder createUserOrder() {
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(createCart().getUser());
        userOrder.createFromCart(createCart());
        userOrder.setItems(new ArrayList<>());
        userOrder.getItems().add(createItem());
        userOrder.setTotal(new BigDecimal(10000));

        return userOrder;
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Item decription1");
        item.setName("Item1");
        item.setPrice(new BigDecimal(10));

        return item;
    }
}
