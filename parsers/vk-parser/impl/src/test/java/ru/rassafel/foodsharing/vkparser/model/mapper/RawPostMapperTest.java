package ru.rassafel.foodsharing.vkparser.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.objects.base.Place;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.photos.PhotoSizesType;
import com.vk.api.sdk.objects.wall.Geo;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.parser.model.dto.PostContext;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.EnumDeserializer;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.EventDeserializer;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.PhotoSizesTypeDeserializer;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.WallpostAttachmentTypeDeserializer;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
@SpringBootTest(classes = {
    RawPostMapperImpl.class,
    GeoMapperImpl.class,
    EventDeserializer.class,
    WallpostAttachmentTypeDeserializer.class,
    PhotoSizesTypeDeserializer.class
})
class RawPostMapperTest {
    @Autowired
    RawPostMapper mapper;

    @Autowired
    List<EnumDeserializer<?>> deserializers;

    @Test
    void convertToDto() {
        List<WallpostAttachment> attachments = List.of(new WallpostAttachment()
            .setType(WallpostAttachmentType.PHOTO)
            .setPhoto(new Photo()
                .setPostId(2)
                .setAlbumId(-7)
                .setDate(1642700507)
                .setId(457239018)
                .setOwnerId(-208377052)
                .setAccessKey("f97642906a0e5f9049")
                .setText("")
                .setUserId(100)
                .setHasTags(false)
                .setSizes(List.of(
                    new PhotoSizes()
                        .setHeight(73)
                        .setWidth(130)
                        .setType(PhotoSizesType.M)
                        .setUrl(URI.create("https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=130x73&quality=96&sign=221c9933798783fdc84d7d06adf9725f&c_uniq_tag=YtG_UAWfaIFDkpYZgWCM-Vhgms7jiMyCazwSTURuKzQ&type=album"))
                ))
            ));

        Wallpost source = new Wallpost();
        source.setId(2);
        source.setOwnerId(-208377052);
        source.setText("Яблоки мск улица арбат раздаю всем нуждающимся");
        source.setDate(1642700507);
        source.setAttachments(attachments);
        source.setGeo(new Geo()
            .setCoordinates("53.20642269124 50.198534691559")
            .setType("point")
            .setPlace(new Place()
                .setId(0)
                .setCreated(0)
                .setLatitude(0f)
                .setLongitude(0f)
                .setTitle("улица Дыбенко, Самара")
                .setCountry("Россия")
                .setCity("Самара")));

        RawPostDto expected = new RawPostDto();
        expected.setText("Яблоки мск улица арбат раздаю всем нуждающимся");
        expected.setDate(LocalDateTime.ofEpochSecond(1642700507, 0, ZoneOffset.UTC));
        GeoPoint point = new GeoPoint();
        point.setLat(53.20642269124d);
        point.setLon(50.198534691559d);
        PostContext context = new PostContext();
        context.setPoint(point);
        context.setAttachments(List.of("https://vk.com/club208377052?z=photo-208377052_457239018%2Fwall-208377052_2"));

        expected.setContext(context);
        expected.setUrl("https://vk.com/club208377052?w=wall-208377052_2");

        RawPostDto actual = mapper.map(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(expected)
            .isEqualTo(expected);
    }

    @SneakyThrows
    @Test
    void jsonTest() {
        String json = "{\n" +
            "  \"type\": \"wall_post_new\",\n" +
            "  \"object\": {\n" +
            "    \"id\": 2,\n" +
            "    \"from_id\": -208377052,\n" +
            "    \"owner_id\": -208377052,\n" +
            "    \"date\": 1642700507,\n" +
            "    \"marked_as_ads\": 0,\n" +
            "    \"post_type\": \"post\",\n" +
            "    \"text\": \"Яблоки мск улица арбат раздаю всем нуждающимся\",\n" +
            "    \"can_edit\": 1,\n" +
            "    \"created_by\": 146072345,\n" +
            "    \"can_delete\": 1,\n" +
            "    \"attachments\": [\n" +
            "      {\n" +
            "        \"type\": \"photo\",\n" +
            "        \"photo\": {\n" +
            "          \"album_id\": -7,\n" +
            "          \"date\": 1642700507,\n" +
            "          \"id\": 457239018,\n" +
            "          \"owner_id\": -208377052,\n" +
            "          \"access_key\": \"f97642906a0e5f9049\",\n" +
            "          \"post_id\": 2,\n" +
            "          \"sizes\": [\n" +
            "            {\n" +
            "              \"height\": 73,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=130x73&quality=96&sign=221c9933798783fdc84d7d06adf9725f&c_uniq_tag=YtG_UAWfaIFDkpYZgWCM-Vhgms7jiMyCazwSTURuKzQ&type=album\",\n" +
            "              \"type\": \"m\",\n" +
            "              \"width\": 130\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 87,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=130x87&quality=96&crop=156,0,1687,1129&sign=3e9cc1ca1e660259496e332523f1ff51&c_uniq_tag=0txhiLJqs5IRJk4KMRC92qhQbN4uz4vx08VarCesoSM&type=album\",\n" +
            "              \"type\": \"o\",\n" +
            "              \"width\": 130\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 133,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=200x133&quality=96&crop=151,0,1698,1129&sign=3d316c07ea47c32b292e24046aac9a7c&c_uniq_tag=ZFQiepxLvusbqKbjeyGGi8gJGmSlWA6YTnRh0qNVlu0&type=album\",\n" +
            "              \"type\": \"p\",\n" +
            "              \"width\": 200\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 213,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=320x213&quality=96&crop=152,0,1696,1129&sign=a16b5df024cf75155a0abe240a7045c8&c_uniq_tag=VSXS1Namsy1KNLdPdpg9SIB2Z8LAUW-u18ZQxVRrccw&type=album\",\n" +
            "              \"type\": \"q\",\n" +
            "              \"width\": 320\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 340,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=510x340&quality=96&crop=153,0,1693,1129&sign=d9ae6b8373085e88c00c9449f3a51cf4&c_uniq_tag=mcw8gqra87fQGaKYshUVAX_FMDaMYROlHeaQg51Kq7k&type=album\",\n" +
            "              \"type\": \"r\",\n" +
            "              \"width\": 510\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 42,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=75x42&quality=96&sign=432ffac86da81c26550193849e84574f&c_uniq_tag=scwQbYGovJ6Ki5mLFbyyWGSixrYI-vCe-tMRE5YDCK4&type=album\",\n" +
            "              \"type\": \"s\",\n" +
            "              \"width\": 75\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 1129,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=2000x1129&quality=96&sign=9ed4ec44f7874c9895c064bb92ea6bed&c_uniq_tag=nXzH95IgjvYyJ49uyWiIGdO2ZqVexeQ0vWepPRINOPE&type=album\",\n" +
            "              \"type\": \"w\",\n" +
            "              \"width\": 2000\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 341,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=604x341&quality=96&sign=a1f56c9680b9b360f03f35d52eae3e33&c_uniq_tag=bS9G_Xi63XP2tzE_vYnqKxIdBUgDSCeftgQf2IwQDI8&type=album\",\n" +
            "              \"type\": \"x\",\n" +
            "              \"width\": 604\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 456,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=807x456&quality=96&sign=dabc3823781432d78c0c84f3f36726be&c_uniq_tag=KKHfHl1H60nuFpOaUnX5Sd7v55ExYirjEijNMmQ4Z3c&type=album\",\n" +
            "              \"type\": \"y\",\n" +
            "              \"width\": 807\n" +
            "            },\n" +
            "            {\n" +
            "              \"height\": 723,\n" +
            "              \"url\": \"https:\\/\\/sun9-51.userapi.com\\/impg\\/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ\\/2B0j6oo8vJ4.jpg?size=1280x723&quality=96&sign=452b90bf754218a79436cdcb44f936d9&c_uniq_tag=WGOOLd9ywsrX4UgN3pr1PX7AJQ0n2l7R1dki_WZ5LAg&type=album\",\n" +
            "              \"type\": \"z\",\n" +
            "              \"width\": 1280\n" +
            "            }\n" +
            "          ],\n" +
            "          \"text\": \"\",\n" +
            "          \"user_id\": 100,\n" +
            "          \"has_tags\": false\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"geo\": {\n" +
            "      \"type\": \"point\",\n" +
            "      \"coordinates\": \"53.20642269124 50.198534691559\",\n" +
            "      \"place\": {\n" +
            "        \"created\": 0,\n" +
            "        \"id\": 0,\n" +
            "        \"is_deleted\": false,\n" +
            "        \"latitude\": 0.000000,\n" +
            "        \"longitude\": 0.000000,\n" +
            "        \"title\": \"улица Дыбенко, Самара\",\n" +
            "        \"total_checkins\": 0,\n" +
            "        \"updated\": 0,\n" +
            "        \"country\": \"Россия\",\n" +
            "        \"city\": \"Самара\"\n" +
            "      },\n" +
            "      \"showmap\": 1\n" +
            "    },\n" +
            "    \"comments\": {\n" +
            "      \"count\": 0\n" +
            "    },\n" +
            "    \"is_favorite\": false,\n" +
            "    \"donut\": {\n" +
            "      \"is_donut\": false\n" +
            "    },\n" +
            "    \"short_text_rate\": 0.800000,\n" +
            "    \"hash\": \"Pw3Nn4MWggG1ET6ZH4VGDVhJUpCp\"\n" +
            "  },\n" +
            "  \"group_id\": 208377052,\n" +
            "  \"event_id\": \"60261a6a51b0d00306f40a47f143595009d24f2b\"\n" +
            "}";

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
            .json()
            .deserializers(deserializers.toArray(new EnumDeserializer<?>[0]))
            .build();

        Wallpost source = objectMapper.readValue(json, CallbackMessage.class).getWallpost();

        RawPostDto expected = new RawPostDto();
        expected.setText("Яблоки мск улица арбат раздаю всем нуждающимся");
        expected.setDate(LocalDateTime.ofEpochSecond(1642700507, 0, ZoneOffset.UTC));
        GeoPoint point = new GeoPoint();
        point.setLat(53.20642269124d);
        point.setLon(50.198534691559d);
        PostContext context = new PostContext();
        context.setPoint(point);
        context.setAttachments(List.of("https://vk.com/club208377052?z=photo-208377052_457239018%2Fwall-208377052_2"));

        expected.setContext(context);
        expected.setUrl("https://vk.com/club208377052?w=wall-208377052_2");

        RawPostDto actual = mapper.map(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(expected)
            .isEqualTo(expected);
    }
}
