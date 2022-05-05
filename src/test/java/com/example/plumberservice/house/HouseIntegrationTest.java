package com.example.plumberservice.house;

import com.example.plumberservice.dto.HouseDto;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.dto.SaveHouseRequest;
import com.example.plumberservice.entity.House;
import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.repository.HouseRepository;
import com.example.plumberservice.repository.PlumberRepository;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HouseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlumberRepository plumberRepository;

    @Autowired
    private HouseRepository houseRepository;


    public static String API = "/api/v1";

    @BeforeEach
    void setUp() {
        val vasya = new Plumber("Vasya");
        val house = new House("Fake street 123");
        vasya.addHouse(house);
        plumberRepository.save(vasya);
        houseRepository.save(house);
    }


    @Test
    @Order(1)
    void unbindPlumberToHouse_unbindsPlumberFromHouseAndReturns200() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(API + "/houses/1/unbind")
                .queryParam("plumberId", "1");
        val response =
                restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(houseRepository.existsByPlumberId(1L)).isFalse();
    }

    @Test
    @Order(2)
    void bindPlumberToHouse_bindsPlumberToHouseAndReturns200() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(API + "/houses/1/bind")
                .queryParam("plumberId", "1");
        val response =
                restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        val plumberName = houseRepository.findById(1L)
                .map(House::getPlumber)
                .map(Plumber::getName)
                .orElse(null);
        assertThat(plumberName).isEqualTo("Vasya");
    }

    @Test
    @Order(3)
    void getHousePlumber_returnsGivenHousesPlumberInfo() {
        val response =
                restTemplate.getForEntity(API + "/houses/1/plumber", PlumberDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Vasya");
    }


    @Test
    @Order(4)
    void getHouse_returnsGivenHouseInfo() {
        val response =
                restTemplate.getForEntity(API + "/houses/1", HouseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAddress()).isEqualTo("Fake street 123");
        assertThat(response.getBody().getPlumberName()).isEqualTo("Vasya");
    }

    @Test
    @Order(5)
    void postHouse_addsHouseAndReturnsItsAddress() {
        val response =
                restTemplate.postForEntity(API + "/houses", new SaveHouseRequest("fake street 123"), HouseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAddress()).isEqualTo("fake street 123");
    }

    @Test
    @Order(6)
    void deleteHouse_removesHouseAndReturnsNothing() {
        restTemplate.delete(API + "/houses/2", new SaveHouseRequest("fake street 123"), HouseDto.class);
        assertThat(houseRepository.existsById(2L)).isFalse();
    }
}
