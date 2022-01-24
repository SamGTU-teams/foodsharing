package ru.rassafel.foodsharing.vkparser.model.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.FullAccessGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.WithoutAccessGroup;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
@SpringBootTest(classes = VkGroupMapperImpl.class)
class VkGroupMapperTest {
    @Autowired
    VkGroupMapper mapper;

    @Test
    void mapFullAccess() {
        FullAccessGroup source = new FullAccessGroup();
        source.setGroupId(1);
        source.setAccessToken("Test token");
        source.setSecretKey("Test secret");


        VkGroup expected = new VkGroup();
        expected.setGroupId(1);
        expected.setAccessToken("Test token");
        expected.setSecretKey("Test secret");

        VkGroup actual = mapper.map(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void mapWithoutAccess() {
        WithoutAccessGroup source = new WithoutAccessGroup();
        source.setGroupId(1);
        source.setConfirmationCode("Test code");
        source.setSecretKey("Test secret");

        VkGroup expected = new VkGroup();
        expected.setGroupId(1);
        expected.setConfirmationCode("Test code");
        expected.setSecretKey("Test secret");

        VkGroup actual = mapper.map(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(source)
            .isNotSameAs(expected)
            .isEqualToComparingFieldByField(expected);
    }
}
