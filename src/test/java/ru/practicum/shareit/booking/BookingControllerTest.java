package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @BeforeEach
    void setUp() {
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
}