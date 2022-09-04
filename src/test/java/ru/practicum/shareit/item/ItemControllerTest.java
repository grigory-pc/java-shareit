package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.service.ItemService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController controller;
    @Autowired
    ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemShortDto itemShortDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemShortDto = ItemShortDto.builder()
                .id(1L)
                .name("itemTest")
                .description("Test Item")
                .available("true")
                .build();
    }

    @DisplayName("GIVEN an item dto " +
            "WHEN add item controller method called " +
            "THEN return status OK and returned item have name as in mock")
    @Test
    void Test1_shouldReturnItemWhenAdd() throws Exception {
        long userId = 1L;

        when(itemService.addNewItem(anyLong(), any(ItemShortDto.class)))
                .thenReturn(itemShortDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemShortDto.getDescription())));
    }
}