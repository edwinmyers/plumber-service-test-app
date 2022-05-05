package com.example.plumberservice.plumber;


import com.example.plumberservice.dto.GetPlumberHousesResponse;
import com.example.plumberservice.dto.HirePlumberRequest;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.entity.House;
import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.repository.HouseRepository;
import com.example.plumberservice.repository.PlumberRepository;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlumberIntegrationTest {

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
    void getPlumber_returnsPlumberDetails() {
        ResponseEntity<PlumberDto> response = restTemplate.getForEntity(API + "/plumbers/1", PlumberDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Vasya");
    }

    @Test
    @Order(2)
    void postPlumber_hiresPlumberAndReturnsDetails() {
        val anton = new HirePlumberRequest("Anton");
        ResponseEntity<PlumberDto> response = restTemplate.postForEntity(API + "/plumbers/", anton, PlumberDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Anton");
    }

    @Test
    @Order(3)
    void getPlumberHouses_returnsHousesServedByPlumber() {
        val response =
                restTemplate.getForEntity(API + "/plumbers/1/houses", GetPlumberHousesResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getHouses()).isNotEmpty();
    }

    @Test
    @Order(4)
    void deletePlumber_firesPlumberAndUnbindsHouses() {
        restTemplate.delete(API + "/plumbers/1");

        assertThat(plumberRepository.existsById(1L)).isFalse();
        assertThat(houseRepository.findAll()).isNotEmpty();
        assertThat(houseRepository.existsByPlumberId(1L)).isFalse();
    }
}
