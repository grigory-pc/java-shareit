package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController controller;
    @Autowired
    ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private User user;
    private Item item;
    private BookingOutDto bookingOutDto;
    private BookingInDto bookingInDto;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "/bookings";

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        item = Item.builder()
                .id(1L)
                .name("itemTest")
                .description("Test Item")
                .available("true")
                .build();

        user = User.builder()
                .id(1L)
                .email("user1@test.ru")
                .name("test")
                .build();

        bookingOutDto = bookingOutDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, Month.JUNE, 1, 12, 00))
                .end(LocalDateTime.of(2022, Month.DECEMBER, 1, 12, 00))
                .status(Status.WAITING)
                .booker(user)
                .item(item)
                .build();

        bookingInDto = bookingInDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, Month.JUNE, 1, 12, 00))
                .end(LocalDateTime.of(2022, Month.DECEMBER, 1, 12, 00))
                .status(String.valueOf(Status.WAITING))
                .bookerId(1L)
                .itemId(1L)
                .build();

        mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @DisplayName("GIVEN an user id, booking id, approved status " +
            "WHEN update status controller method called " +
            "THEN return OK and returned updated booking have item name as in mock")
    @Test
    void Test1_shouldReturnUpdatedBooking() throws Exception {
        long userId = 1L;
        long bookingId = 1L;

        when(bookingService.updateBookingStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingOutDto);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(bookingOutDto.getItem().getName())));
    }

    @DisplayName("GIVEN an user id and booking id " +
            "WHEN get by id controller method called " +
            "THEN return status OK and returned booking have item name as in mock")
    @Test
    void Test2_shouldReturnBookingById() throws Exception {
        long userId = 1L;
        long bookingId = 1L;

        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingOutDto);

        mvc.perform(get(baseUrl + "/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(bookingOutDto.getItem().getName())));
    }

    @DisplayName("GIVEN a booker id " +
            "WHEN get by booker id controller method called " +
            "THEN return status OK and item name field of returned item in [0] index same as in mock")
    @Test
    void Test3_shouldReturnBookingByBookerId() throws Exception {
        long userId = 2L;

        when(bookingService.getBookingsByBookerId(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOutDto));

        mvc.perform(get(baseUrl)
                        .header("X-Sharer-User-Id", userId))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item.name", is(bookingOutDto.getItem().getName())));
    }

    @DisplayName("GIVEN an owner id " +
            "WHEN get by owner id controller method called " +
            "THEN return status OK and item name field of returned item in [0] index same as in mock")
    @Test
    void Test4_shouldReturnBookingByOwnerId() throws Exception {
        long userId = 1L;

        when(bookingService.getBookingsByOwnerId(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOutDto));

        mvc.perform(get(baseUrl + "/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item.name", is(bookingOutDto.getItem().getName())));
    }

    @DisplayName("GIVEN an booking dto " +
            "WHEN add booking controller method called " +
            "THEN return status OK and returned booking have status as in mock")
    @Test
    void Test5_shouldReturnBookingWhenAdd() throws Exception {
        long userId = 1L;

        when(bookingService.addNewBooking(anyLong(), any(BookingInDto.class)))
                .thenReturn(bookingInDto);

        mvc.perform(post(baseUrl)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingInDto.getStatus())));
    }
}