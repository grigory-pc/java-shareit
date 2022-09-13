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
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.service.UserService;
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
//class UserControllerTest {
//    @Mock
//    private UserService userService;
//    @InjectMocks
//    private UserController controller;
//    @Autowired
//    ObjectMapper mapper = new ObjectMapper();
//
//    private MockMvc mvc;
//    private UserDto userDto;
//    private String baseUrl;
//
//    @BeforeEach
//    void setUp() {
//        baseUrl = "/users";
//
//        mvc = MockMvcBuilders
//                .standaloneSetup(controller)
//                .build();
//
//        userDto = UserDto.builder()
//                .id(1L)
//                .email("user1@test.ru")
//                .name("test")
//                .build();
//    }
//
//    @DisplayName("GIVEN an user id " +
//            "WHEN get by id controller method called " +
//            "THEN return status OK and returned user have email as in mock")
//    @Test
//    void Test1_shouldReturnUserById() throws Exception {
//        long userId = 1L;
//
//        when(userService.getUserById(anyLong()))
//                .thenReturn(userDto);
//
//        mvc.perform(get(baseUrl + "/{id}", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//
//    @DisplayName("GIVEN  " +
//            "WHEN get all users controller method called " +
//            "THEN return status OK and email field of returned user in [0] index same as in mock")
//    @Test
//    void Test2_shouldReturnAllUsers() throws Exception {
//        long userId = 1L;
//
//        when(userService.getUserAll())
//                .thenReturn(List.of(userDto));
//
//        mvc.perform(get(baseUrl))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(1)))
//                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
//    }
//
//    @DisplayName("GIVEN an user dto " +
//            "WHEN add user controller method called " +
//            "THEN return status OK and returned user have email as in mock")
//    @Test
//    void Test3_shouldReturnUserWhenAdd() throws Exception {
//        long userId = 1L;
//
//        when(userService.add(any(UserDto.class)))
//                .thenReturn(userDto);
//
//        mvc.perform(post(baseUrl)
//                        .header("X-Sharer-User-Id", userId)
//                        .contentType("application/json")
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//
//    @DisplayName("GIVEN an user id and user dto " +
//            "WHEN update user controller method called " +
//            "THEN return status OK and returned user have email as in mock")
//    @Test
//    void Test6_shouldReturnUserWhenUpdate() throws Exception {
//        long userId = 1L;
//
//        when(userService.update(anyLong(), any(UserDto.class)))
//                .thenReturn(userDto);
//
//        mvc.perform(patch(baseUrl + "/{userId}", userId)
//                        .contentType("application/json")
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//
//    @DisplayName("GIVEN an user id  " +
//            "WHEN delete by id controller method called " +
//            "THEN return status OK and delete service method called 1 time")
//    @Test
//    void Test7_shouldDeleteItem() throws Exception {
//        long userId = 1L;
//
//        mvc.perform(delete(baseUrl + "/{userId}", userId))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).delete(anyLong());
//    }
//}