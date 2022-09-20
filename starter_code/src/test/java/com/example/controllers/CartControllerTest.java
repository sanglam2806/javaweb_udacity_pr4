package com.example.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.SareetaApplication;
import com.example.demo.model.persistence.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest(classes = SareetaApplication.class)
public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ItemRepository itemRepository;

    @BeforeEach
    public void initEachTest() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        itemRepository = mock(ItemRepository.class);
        cartController = new CartController(userRepository, cartRepository, itemRepository);
    }

    @Test
    public void addToCartTest() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(createUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem()));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals(modifyCartRequest.getQuantity(), response.getBody().getItems().size());
        assertEquals(modifyCartRequest.getUsername(), response.getBody().getUser().getUsername());
        assertEquals(modifyCartRequest.getItemId(),1);
    }

    @Test
    public void removeFromcartTest() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(createUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem()));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals(0, response.getBody().getItems().size());
        assertEquals(modifyCartRequest.getUsername(), response.getBody().getUser().getUsername());
    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("Mitsu");
        return modifyCartRequest;
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Mitsu");
        user.setPassword("1234");

        Cart cart = new Cart();
        cart.setUser(user);

        user.setCart(cart);
        return user;
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
