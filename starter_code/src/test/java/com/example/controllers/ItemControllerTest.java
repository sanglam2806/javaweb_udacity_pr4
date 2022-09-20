package com.example.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.SareetaApplication;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SareetaApplication.class)
public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository;

    @BeforeEach
    public void initEachTest() {
        itemRepository = mock(ItemRepository.class);
        itemController = new ItemController(itemRepository);
    }

    @Test
    public void getItemListTest(){
        List<Item> itemList = new ArrayList<>();

        itemList.add(createItem1());
        itemList.add(createItem2());
        itemList.add(createItem3());

        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        assertEquals(response.getBody().get(0), itemList.get(0));
        assertEquals(response.getBody().get(1), itemList.get(1));
        assertEquals(response.getBody().get(2), itemList.get(2));
    }

    @Test
    public void getItemByIdTest() {
        Item item2 = createItem2();
        when(itemRepository.findById(item2.getId())).thenReturn(Optional.of(item2));

        ResponseEntity<Item> response = itemController.getItemById(item2.getId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        equalProperties(item2, response.getBody());
    }

    @Test
    public void getItemByNameTest() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(createItem3());
        itemList.add(createItem4());
        when(itemRepository.findByName("Item3")).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item3");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 2);
        equalProperties(response.getBody().get(0), itemList.get(0));
        equalProperties(response.getBody().get(1), itemList.get(1));
    }
    private Item createItem1() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Item decription1");
        item.setName("Item1");
        item.setPrice(new BigDecimal(10));

        return item;
    }
    private Item createItem2() {
        Item item = new Item();
        item.setId(2L);
        item.setDescription("Item decription2");
        item.setName("Item2");
        item.setPrice(new BigDecimal(20));

        return item;
    }
    private Item createItem3() {
        Item item = new Item();
        item.setId(3L);
        item.setDescription("Item decription3");
        item.setName("Item3");
        item.setPrice(new BigDecimal(30));

        return item;
    }
    private Item createItem4() {
        Item item = new Item();
        item.setId(4L);
        item.setDescription("Item decription4");
        item.setName("Item3");
        item.setPrice(new BigDecimal(40));

        return item;
    }

    private void equalProperties (Item item, Item itemCompare) {
        assertEquals(item.getId(), itemCompare.getId());
        assertEquals(item.getName(), itemCompare.getName());
        assertEquals(item.getPrice(), itemCompare.getPrice());
        assertEquals(item.getDescription(), itemCompare.getDescription());
    }
}
