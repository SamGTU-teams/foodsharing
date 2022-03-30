package ru.rassafel.foodsharing.analyzer.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPost;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.service.GeoLuceneAnalyzerService;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(
    bindings = {
        @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.consumer.queue}"),
            exchange = @Exchange(value = "${spring.rabbitmq.consumer.exchange}", type = ExchangeTypes.FANOUT)
        )
    }
)
public class PostRabbitListener {
    private final RabbitTemplate template;
    private final LuceneRepository luceneRepository;
    private final ProductLuceneAnalyzerService productsAnalyzer;
    private final GeoLuceneAnalyzerService geoAnalyzer;
    private final ProductMapper mapper;

    @RabbitHandler
    public void handle(RawPost rawPost) {
        FoodPost result = new FoodPost();

        LuceneIndexedString postText = luceneRepository.add(rawPost.getText());

        result.setProducts(productsAnalyzer.parseProducts(rawPost, postText)
            .map(Pair::getFirst)
            .map(mapper::entityToDto)
            .collect(Collectors.toList()));

        geoAnalyzer.parseGeoPoint(rawPost, postText)
            .ifPresent(result::setPoint);

        result.setText(rawPost.getText());
        result.setUrl(rawPost.getUrl());
        result.setAttachments(rawPost.getContext().getAttachments());
        result.setDate(rawPost.getDate());

        template.convertAndSend(result);

        luceneRepository.delete(postText);
    }
}
