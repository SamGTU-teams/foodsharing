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
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.parser.model.PostContext;
import ru.rassafel.foodsharing.parser.model.RawPost;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.EventDeserializer;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.PhotoSizesTypeDeserializer;
import ru.rassafel.foodsharing.vkparser.model.vk.deserializer.WallpostAttachmentTypeDeserializer;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class RawPostMapperTest {
    RawPostMapper mapper = RawPostMapper.INSTANCE;

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

        testMapper(source);
    }

    @SneakyThrows
    @Test
    void jsonTest() {
        String json = "{\"type\":\"wall_post_new\",\"object\":{\"id\":2,\"from_id\":-208377052,\"owner_id\":-208377052,\"date\":1642700507,\"marked_as_ads\":0,\"post_type\":\"post\",\"text\":\"Яблоки мск улица арбат раздаю всем нуждающимся\",\"can_edit\":1,\"created_by\":146072345,\"can_delete\":1,\"attachments\":[{\"type\":\"photo\",\"photo\":{\"album_id\":-7,\"date\":1642700507,\"id\":457239018,\"owner_id\":-208377052,\"access_key\":\"f97642906a0e5f9049\",\"post_id\":2,\"sizes\":[{\"height\":73,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=130x73&quality=96&sign=221c9933798783fdc84d7d06adf9725f&c_uniq_tag=YtG_UAWfaIFDkpYZgWCM-Vhgms7jiMyCazwSTURuKzQ&type=album\",\"type\":\"m\",\"width\":130},{\"height\":87,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=130x87&quality=96&crop=156,0,1687,1129&sign=3e9cc1ca1e660259496e332523f1ff51&c_uniq_tag=0txhiLJqs5IRJk4KMRC92qhQbN4uz4vx08VarCesoSM&type=album\",\"type\":\"o\",\"width\":130},{\"height\":133,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=200x133&quality=96&crop=151,0,1698,1129&sign=3d316c07ea47c32b292e24046aac9a7c&c_uniq_tag=ZFQiepxLvusbqKbjeyGGi8gJGmSlWA6YTnRh0qNVlu0&type=album\",\"type\":\"p\",\"width\":200},{\"height\":213,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=320x213&quality=96&crop=152,0,1696,1129&sign=a16b5df024cf75155a0abe240a7045c8&c_uniq_tag=VSXS1Namsy1KNLdPdpg9SIB2Z8LAUW-u18ZQxVRrccw&type=album\",\"type\":\"q\",\"width\":320},{\"height\":340,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=510x340&quality=96&crop=153,0,1693,1129&sign=d9ae6b8373085e88c00c9449f3a51cf4&c_uniq_tag=mcw8gqra87fQGaKYshUVAX_FMDaMYROlHeaQg51Kq7k&type=album\",\"type\":\"r\",\"width\":510},{\"height\":42,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=75x42&quality=96&sign=432ffac86da81c26550193849e84574f&c_uniq_tag=scwQbYGovJ6Ki5mLFbyyWGSixrYI-vCe-tMRE5YDCK4&type=album\",\"type\":\"s\",\"width\":75},{\"height\":1129,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=2000x1129&quality=96&sign=9ed4ec44f7874c9895c064bb92ea6bed&c_uniq_tag=nXzH95IgjvYyJ49uyWiIGdO2ZqVexeQ0vWepPRINOPE&type=album\",\"type\":\"w\",\"width\":2000},{\"height\":341,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=604x341&quality=96&sign=a1f56c9680b9b360f03f35d52eae3e33&c_uniq_tag=bS9G_Xi63XP2tzE_vYnqKxIdBUgDSCeftgQf2IwQDI8&type=album\",\"type\":\"x\",\"width\":604},{\"height\":456,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=807x456&quality=96&sign=dabc3823781432d78c0c84f3f36726be&c_uniq_tag=KKHfHl1H60nuFpOaUnX5Sd7v55ExYirjEijNMmQ4Z3c&type=album\",\"type\":\"y\",\"width\":807},{\"height\":723,\"url\":\"https://sun9-51.userapi.com/impg/3q3eo6rYgEl7q2llAT0nKMdN1XpKDtjAkfS7iQ/2B0j6oo8vJ4.jpg?size=1280x723&quality=96&sign=452b90bf754218a79436cdcb44f936d9&c_uniq_tag=WGOOLd9ywsrX4UgN3pr1PX7AJQ0n2l7R1dki_WZ5LAg&type=album\",\"type\":\"z\",\"width\":1280}],\"text\":\"\",\"user_id\":100,\"has_tags\":false}}],\"geo\":{\"type\":\"point\",\"coordinates\":\"53.20642269124 50.198534691559\",\"place\":{\"created\":0,\"id\":0,\"is_deleted\":false,\"latitude\":0,\"longitude\":0,\"title\":\"улица Дыбенко, Самара\",\"total_checkins\":0,\"updated\":0,\"country\":\"Россия\",\"city\":\"Самара\"},\"showmap\":1},\"comments\":{\"count\":0},\"is_favorite\":false,\"donut\":{\"is_donut\":false},\"short_text_rate\":0.8,\"hash\":\"Pw3Nn4MWggG1ET6ZH4VGDVhJUpCp\"},\"group_id\":208377052,\"event_id\":\"60261a6a51b0d00306f40a47f143595009d24f2b\"}";

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
            .json()
            .deserializers(
                new EventDeserializer(),
                new PhotoSizesTypeDeserializer(),
                new WallpostAttachmentTypeDeserializer()
            ).build();

        Wallpost source = objectMapper.readValue(json, CallbackMessage.class).getWallpost();

        testMapper(source);
    }

    private RawPost newRawPost(String text, long epochSecond, double lat,
                               double lon, String url,
                               String... attachments) {
        RawPost expected = new RawPost();
        expected.setText(text);
        expected.setDate(LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC));
        GeoPoint point = new GeoPoint();
        point.setLat(lat);
        point.setLon(lon);
        PostContext context = new PostContext();
        context.setPoint(point);
        context.setAttachments(Arrays.asList(attachments));

        expected.setContext(context);
        expected.setUrl(url);
        return expected;
    }

    private void testMapper(Wallpost source, RawPost expected) {
        RawPost actual = mapper.map(source);

        assertThat(actual)
            .isNotNull()
            .isNotSameAs(expected)
            .isEqualTo(expected);
    }

    private void testMapper(Wallpost source) {
        RawPost expected = newRawPost("Яблоки мск улица арбат раздаю всем нуждающимся",
            1642700507, 53.20642269124d, 50.198534691559d,
            "https://vk.com/wall-208377052_2", "https://vk.com/photo-208377052_457239018");
        testMapper(source, expected);
    }
}
