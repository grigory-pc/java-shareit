package ru.practicum.shareit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class StringBooleanSerializerTest {
    @Autowired
    private JacksonTester<ItemDto> jacksonTester;


    @DisplayName("GIVEN an item dto " +
            "WHEN item dto serialize " +
            "THEN available field converted to boolean")
    @Test
    void Test1_shouldConvertAvailableFieldBoolean() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("itemTest")
                .description("Test Item")
                .available("true")
                .build();

        JsonContent<ItemDto> json = jacksonTester.write(itemDto);
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}