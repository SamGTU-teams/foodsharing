package ru.rassafel.foodsharing.ibot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.ibot.controller.IBotController;
import ru.rassafel.foodsharing.ibot.model.dto.RequestFoodPostDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration-test")
class IBotApplicationIntegrationTest {
    @Autowired
    IBotController controller;

    @Test
    void findNearbyPostWithBetweenTime() {
        RequestFoodPostDto request = new RequestFoodPostDto();
        request.setAfter(LocalDateTime.of(2022, 5, 3, 0, 0));
        request.setPoint(new GeoPoint(53.205273, 50.1311675));
        request.setProducts(List.of(new ProductDto(12L, "")));
        request.setRange(3000);

        List<FoodPostDto> posts = controller.findNearbyPosts(request);
        System.out.println(posts);

        assertThat(posts)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void findNearbyPostAll() {
        RequestFoodPostDto request = new RequestFoodPostDto();
        request.setAfter(LocalDateTime.of(2022, 5, 2, 0, 0));
        request.setPoint(new GeoPoint(53.205273, 50.1311675));
        request.setProducts(List.of(new ProductDto(12L, "")));
        request.setRange(3000);

        List<FoodPostDto> posts = controller.findNearbyPosts(request);
        System.out.println(posts);

        assertThat(posts)
            .isNotNull()
            .isNotEmpty()
            .hasSize(2);
    }

    @Test
    void findNearbyPostsWithProduct() {
        RequestFoodPostDto request = new RequestFoodPostDto();
        request.setAfter(LocalDateTime.of(2022, 5, 2, 0, 0));
        request.setPoint(new GeoPoint(53.205273, 50.1311675));
        request.setProducts(List.of(new ProductDto(13L, "")));
        request.setRange(3000);

        List<FoodPostDto> posts = controller.findNearbyPosts(request);
        System.out.println(posts);

        assertThat(posts)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void findNearbyPostsWithProductNotMatch() {
        RequestFoodPostDto request = new RequestFoodPostDto();
        request.setAfter(LocalDateTime.of(2022, 5, 2, 0, 0));
        request.setPoint(new GeoPoint(53.205273, 50.1311675));
        request.setProducts(List.of(new ProductDto(10L, "")));
        request.setRange(3000);

        List<FoodPostDto> posts = controller.findNearbyPosts(request);
        System.out.println(posts);

        assertThat(posts)
            .isNotNull()
            .isEmpty();
    }

    @Test
    void findNearbyPostsNotMatchGeo() {
        RequestFoodPostDto request = new RequestFoodPostDto();
        request.setAfter(LocalDateTime.of(2022, 5, 2, 0, 0));
        request.setPoint(new GeoPoint(53, 50));
        request.setProducts(List.of(new ProductDto(12L, "")));
        request.setRange(3000);

        List<FoodPostDto> posts = controller.findNearbyPosts(request);
        System.out.println(posts);

        assertThat(posts)
            .isNotNull()
            .isEmpty();
    }
}
