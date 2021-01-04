package me.dktsudgg.demostudyrestapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    // 롬복 동작을 학습하기 위한 테스트 1
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Demo Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    // 롬복 동작을 학습하기 위한 테스트 2
    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    //    @CsvSource({
//            "0, 0, true",
//            "100, 0, false",
//            "0, 100, false"
//    })
    // 참고: https://velog.io/@ashesglow/%ED%8C%8C%EB%9D%BC%EB%AF%B8%ED%84%B0-%ED%85%8C%EC%8A%A4%ED%8A%B8%EB%A5%BC-%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95-Junit5-JUnitParams
    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @MethodSource("parametersForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] parametersForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false},
                new Object[]{100, 200, false}
        };
    }

    @ParameterizedTest(name = "{index} => location={0}, isOffline={1}")
    @MethodSource("parametersForTestOffline")
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[]{"강남", true},
                new Object[]{null, false},
                new Object[]{"      ", false},
        };
    }

}