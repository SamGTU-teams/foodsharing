package ru.rassafel.foodsharing.ibot.model.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class StringListConverterTest {
    private StringListConverter converter;

    @BeforeEach
    void init() {
        this.converter = new StringListConverter();
    }

    @Test
    void convertToDatabaseColumn() {
        String expected = "Attachment1 Attachment2 Attachment3";
        List<String> strings = Arrays.asList("Attachment1", "Attachment2", "Attachment3");
        String actual = converter.convertToDatabaseColumn(strings);
        assertThat(actual)
            .isNotNull()
            .isNotBlank()
            .isEqualTo(expected);
    }

    @Test
    void convertToEntityAttribute() {
        List<String> expected = Arrays.asList("Attachment1", "Attachment2", "Attachment3");
        String string = "Attachment1 Attachment2 Attachment3";
        List<String> actual = converter.convertToEntityAttribute(string);
        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .doesNotContainNull()
            .isEqualTo(expected);
    }
}
