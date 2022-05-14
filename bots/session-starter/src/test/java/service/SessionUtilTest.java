package service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.rassafel.bot.session.util.SessionUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SessionUtilTest {

    private static final Map<Integer, String> MAP = Map.of(
        1, "First",
        2, "Second",
        3, "Third",
        4, "Fourth",
        5, "Fifth",
        6, "Sixth",
        7, "Seventh",
        8, "Eighth",
        9, "Ninth"
    );

    @ParameterizedTest
    @MethodSource
    void testFindValuesByDigitalMessage(String provided, Set<String> expected){
        Set<String> result = SessionUtil.findValuesByMessage(MAP, provided);
        assertThat(result)
            .containsExactlyInAnyOrderElementsOf(expected);
    }

    static List<Arguments> testFindValuesByDigitalMessage(){
        return List.of(
            Arguments.of("1, 2, 3", Set.of("First", "Second", "Third")),
            Arguments.of("1 2, 3 9 4", Set.of("First", "Second", "Third", "Ninth", "Fourth")),
            Arguments.of("1 2 6,2", Set.of("First", "Second", "Sixth")),
            Arguments.of(" 1 2 2", Set.of("First", "Second")),
            Arguments.of(" 1 2,2, 3 4, 5,6 7, 8   9",
                Set.of("First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth")),
            Arguments.of(" 1  ,   3,   1", Set.of("First", "Third"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testFindValuesByDigitalMessageWithInvalidNumber(String provided, String errorMessage) {
        assertThatThrownBy(() -> SessionUtil.findValuesByMessage(MAP, provided))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(errorMessage);

    }

    static List<Arguments> testFindValuesByDigitalMessageWithInvalidNumber() {
        return List.of(
            Arguments.of("1, 2, 29", "Номера 29 в списке нет, повторите попытку"),
            Arguments.of("110, 2, 29", "Номера 110 в списке нет, повторите попытку")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testFindValuesByCharacterMessage(String provided, Set<String> expected){
        Set<String> result = SessionUtil.findValuesByMessage(MAP, provided);
        assertThat(result)
            .containsExactlyInAnyOrderElementsOf(expected);
    }

    static List<Arguments> testFindValuesByCharacterMessage(){
        return List.of(
            Arguments.of("First", Set.of("First")),
            Arguments.of("second", Set.of("second")),
            Arguments.of("THIRD", Set.of("THIRD"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testFindValuesByCharacterMessageWithInvalidName(String provided, String errorMessage) {
        assertThatThrownBy(() -> SessionUtil.findValuesByMessage(MAP, provided))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(errorMessage);

    }

    static List<Arguments> testFindValuesByCharacterMessageWithInvalidName() {
        return List.of(
            Arguments.of("firs_t", "Названия firs_t в списке нет, повторите попытку"),
            Arguments.of("seconddd", "Названия seconddd в списке нет, повторите попытку")
        );
    }
}
