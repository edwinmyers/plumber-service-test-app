package com.example.plumberservice.house;

import com.example.plumberservice.dto.HouseDto;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.dto.SaveHouseRequest;
import com.example.plumberservice.entity.House;
import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.exception.PlumberUnavailableException;
import com.example.plumberservice.repository.HouseRepository;
import com.example.plumberservice.repository.PlumberRepository;
import com.example.plumberservice.service.HouseService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

    @Mock
    private PlumberRepository plumberRepository;

    @Mock
    private HouseRepository houseRepository;

    private HouseService houseService;

    @BeforeEach
    void setUp() {
        houseService = new HouseService(houseRepository, plumberRepository);
    }


    @Test
    void bindPlumber_bindsPlumberAndThrowsNoError() {
        given(houseRepository.findById(any())).willReturn(Optional.of(new House("Fake street 123")));
        given(plumberRepository.findById(any())).willReturn(Optional.of(new Plumber("Vasya")));

        assertThatNoException().isThrownBy(() -> houseService.bindPlumber(1L, 1L));
    }

    @Test
    void bindPlumber6thTime_ShouldThrowError() {
        given(houseRepository.findById(any())).willReturn(Optional.of(new House("Fake street 123")));
        val vasya = new Plumber("Vasya");
        for (int i = 0; i < 5; i++) {
            vasya.addHouse(new House("House number i"));
        }
        given(plumberRepository.findById(any())).willReturn(Optional.of(vasya));

        assertThatExceptionOfType(PlumberUnavailableException.class).isThrownBy(() ->
                houseService.bindPlumber(1L, 1L));
    }

    @Test
    void unbindPlumber_unbindsPlumberAndThrowsNoError() {
        given(houseRepository.findById(any())).willReturn(Optional.of(new House("Fake street 123")));
        given(plumberRepository.findById(any())).willReturn(Optional.of(new Plumber("Vasya")));

        assertThatNoException().isThrownBy(() -> houseService.unbindPlumber(1L, 1L));
    }

    @Test
    void getHousePlumber_returnsThisHousesPlumberInfo() {
        val house = new House("fake street 123");
        house.setPlumber(new Plumber("Vasya"));
        given(houseRepository.findById(1L)).willReturn(Optional.of(house));
        PlumberDto plumber = houseService.getPlumber(1L);

        assertThat(plumber).isNotNull();
        assertThat(plumber.getName()).isEqualTo("Vasya");
    }

    @Test
    void getHouse_returnsThisHouseInfo() {
        val house = new House("fake street 123");
        given(houseRepository.findById(1L)).willReturn(Optional.of(house));
        HouseDto houseDto = houseService.getHouse(1L);

        assertThat(houseDto).isNotNull();
        assertThat(houseDto.getAddress()).isEqualTo("fake street 123");
    }

    @Test
    void saveHouse_savesHouseAndReturnsThisHouseInfo() {
        val house = new House("fake street 123");
        given(houseRepository.save(any())).willReturn(house);
        HouseDto houseDto = houseService.saveHouse(new SaveHouseRequest("fake street 123"));

        assertThat(houseDto).isNotNull();
        assertThat(houseDto.getAddress()).isEqualTo("fake street 123");
    }
}
