package ru.practicum.shareit;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController controller;
    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemRequestDto itemRequestDto;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "/requests";

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemTest")
                .description("Test Item")
                .available("true")
                .build();

        itemRequestDto = itemRequestDto.builder()
                .id(1L)
                .description("Test item Request")
                .items(List.of(itemDto))
                .build();
    }

    @DisplayName("GIVEN  " +
            "WHEN get all ItemRequest controller method called " +
            "THEN return status OK and description field of returned item request in [0] index same as in mock")
    @Test
    void Test1_shouldReturnAllItemRequest() throws Exception {
        long userId = 2L;

        when(itemRequestService.getAllItemRequest(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get(baseUrl + "/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())));
    }

    @DisplayName("GIVEN an user id  " +
            "WHEN get by user id controller method called " +
            "THEN return status OK and description field of returned item request in [0] index same as in mock")
    @Test
    void Test2_shouldReturnItemRequestByUserId() throws Exception {
        long userId = 1L;

        when(itemRequestService.getItemRequestByUserId(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get(baseUrl)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())));
    }

    @DisplayName("GIVEN a request id " +
            "WHEN get by id controller method called " +
            "THEN return status OK and returned item request have description as in mock")
    @Test
    void Test3_shouldReturnItemRequestById() throws Exception {
        long userId = 2L;
        long requestId = 1L;

        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get(baseUrl + "/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @DisplayName("GIVEN an item request dto " +
            "WHEN add item request controller method called " +
            "THEN return status OK and returned item request have description as in mock")
    @Test
    void Test4_shouldReturnItemRequestWhenAdd() throws Exception {
        long userId = 1L;

        when(itemRequestService.addItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mvc.perform(post(baseUrl)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }
}