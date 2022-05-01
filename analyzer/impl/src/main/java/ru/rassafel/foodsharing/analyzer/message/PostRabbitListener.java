package ru.rassafel.foodsharing.analyzer.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.exception.GeoPointParseException;
import ru.rassafel.foodsharing.analyzer.exception.ParseException;
import ru.rassafel.foodsharing.analyzer.exception.ProductParseException;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPost;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.service.GeoLuceneAnalyzerService;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;
import ru.rassafel.foodsharing.parser.model.RawPost;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(
    queues = {"${spring.rabbitmq.raw-post.queue}"}
)
public class PostRabbitListener {
    private final RabbitTemplate template;
    private final LuceneRepository luceneRepository;
    private final ProductLuceneAnalyzerService productService;
    private final GeoLuceneAnalyzerService geoAnalyzer;
    private final ProductMapper mapper;
    private final Validator validator;

    @RabbitHandler
    public void handle(RawPost rawPost) {
        Set<ConstraintViolation<RawPost>> violations = validator.validate(rawPost);
        if (!violations.isEmpty()) {
            log.debug("Validation exception", new ConstraintViolationException(violations));
            return;
        }

        FoodPost result = new FoodPost();
        LuceneIndexedString postText = luceneRepository.add(rawPost.getText().toLowerCase());
        try {
            List<ProductDto> products = Streamable
                .of(productService.parseProducts(rawPost, postText))
                .map(ScoreProduct::getProduct)
                .map(mapper::entityToDto)
                .toList();
            if (products.isEmpty()) {
                log.debug("Post does not contains products: {}", rawPost);
                throw new ProductParseException("Post does not contains products");
            }
            result.setProducts(products);

            Optional<GeoPoint> geoPoint = geoAnalyzer.parseGeoPoint(rawPost, postText);
            if (geoPoint.isEmpty()) {
                log.debug("Post does not contains geopoint: {}", rawPost);
                throw new GeoPointParseException("Post does not contains geopoint");
            }
            result.setPoint(geoPoint.get());

            result.setText(rawPost.getText());
            result.setUrl(rawPost.getUrl());
            result.setAttachments(rawPost.getContext().getAttachments());
            result.setDate(rawPost.getDate());

            template.convertAndSend(result);
        } catch (ParseException ex) {
//            ToDo: Add handler
        } finally {
            luceneRepository.unregister(postText);
        }
    }
}
