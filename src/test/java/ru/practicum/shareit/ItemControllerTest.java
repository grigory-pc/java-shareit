//package ru.practicum.shareit;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.practicum.shareit.item.dto.CommentDto;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.ItemShortDto;
//import ru.practicum.shareit.item.service.ItemService;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//class ItemControllerTest {
//    @Mock
//    private ItemService itemService;
//    @InjectMocks
//    private ItemController controller;
//    @Autowired
//    ObjectMapper mapper = new ObjectMapper();
//    private MockMvc mvc;
//    private ItemShortDto itemShortDto;
//    private ItemDto itemDto;
//    private CommentDto commentDto;
//    private String baseUrl;
//
//    @BeforeEach
//    void setUp() {
//        baseUrl = "/items";
//
//        mvc = MockMvcBuilders
//                .standaloneSetup(controller)
//                .build();
//
//        itemShortDto = ItemShortDto.builder()
//                .id(1L)
//                .name("itemTest")
//                .description("Test Item")
//                .available("true")
//                .build();
//
//        itemDto = ItemDto.builder()
//                .id(1L)
//                .name("itemTest")
//                .description("Test Item")
//                .available("true")
//                .build();
//
//        commentDto = CommentDto.builder()
//                .id(1L)
//                .text("test comment")
//                .authorName("user")
//                .build();
//    }
//
//    @DisplayName("GIVEN an item dto " +
//            "WHEN add item controller method called " +
//            "THEN return status OK and returned item have name as in mock")
//    @Test
//    void Test1_shouldReturnItemWhenAdd() throws Exception {
//        long userId = 1L;
//
//        when(itemService.addNewItem(anyLong(), any(ItemShortDto.class)))
//                .thenReturn(itemShortDto);
//
//        mvc.perform(post(baseUrl)
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(mapper.writeValueAsString(itemShortDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.description", is(itemShortDto.getDescription())));
//    }
//
//    @DisplayName("GIVEN  " +
//            "WHEN get all items controller method called " +
//            "THEN return status OK and description field of returned item in [0] index same as in mock")
//    @Test
//    void Test2_shouldReturnAllItems() throws Exception {
//        long userId = 1L;
//
//        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
//                .thenReturn(List.of(itemDto));
//
//        mvc.perform(get(baseUrl)
//                        .header("X-Sharer-User-Id", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(1)))
//                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));
//    }
//
//    @DisplayName("GIVEN an user id and item id " +
//            "WHEN get by id controller method called " +
//            "THEN return status OK and returned item have name as in mock")
//    @Test
//    void Test3_shouldReturnItemById() throws Exception {
//        long userId = 1L;
//        long itemId = 1L;
//
//        when(itemService.getItemDtoById(anyLong(), anyLong()))
//                .thenReturn(itemDto);
//
//        mvc.perform(get(baseUrl + "/{itemId}", itemId)
//                        .header("X-Sharer-User-Id", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(itemDto.getName())));
//    }
//
//    @DisplayName("GIVEN a text, from id and size " +
//            "WHEN search by text controller method called " +
//            "THEN return status OK and description field of returned item in [0] index same as in mock")
//    @Test
//    void Test4_shouldReturnItemsByText() throws Exception {
//        String text = "test";
//
//        when(itemService.searchItemByText(anyString(), anyInt(), anyInt()))
//                .thenReturn(List.of(itemShortDto));
//
//        mvc.perform(get(baseUrl + "/search")
//                        .param("text", text))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(1)))
//                .andExpect(jsonPath("$[0].description", is(itemShortDto.getDescription())));
//    }
//
//
//    @DisplayName("GIVEN a comment dto " +
//            "WHEN add comment controller method called " +
//            "THEN return status OK and returned comment have text as in mock")
//    @Test
//    void Test5_shouldReturnCommentWhenAdd() throws Exception {
//        long userId = 1L;
//        long itemId = 1L;
//
//        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class)))
//                .thenReturn(commentDto);
//
//        mvc.perform(post(baseUrl + "/{itemId}/comment", itemId)
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(mapper.writeValueAsString(commentDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.text", is(commentDto.getText())));
//    }
//
//    @DisplayName("GIVEN an user id, item id and item dto " +
//            "WHEN update item controller method called " +
//            "THEN return status OK and returned item have description as in mock")
//    @Test
//    void Test6_shouldReturnItemWhenUpdate() throws Exception {
//        long userId = 1L;
//        long itemId = 1L;
//
//        when(itemService.updateItem(anyLong(), anyLong(), any(ItemShortDto.class)))
//                .thenReturn(itemShortDto);
//
//        mvc.perform(patch(baseUrl + "/{itemId}", itemId)
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(mapper.writeValueAsString(itemShortDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.description", is(itemShortDto.getDescription())));
//    }
//
//    @DisplayName("GIVEN an user id and item id " +
//            "WHEN delete by id controller method called " +
//            "THEN return status OK and delete service method called 1 time")
//    @Test
//    void Test7_shouldDeleteItem() throws Exception {
//        long userId = 1L;
//        long itemId = 1L;
//
//        mvc.perform(delete(baseUrl + "/{itemId}", itemId)
//                        .header("X-Sharer-User-Id", userId))
//                .andExpect(status().isOk());
//
//        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
//    }
//}