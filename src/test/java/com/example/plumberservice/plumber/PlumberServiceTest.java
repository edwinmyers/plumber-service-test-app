package com.example.plumberservice.plumber;

import com.example.plumberservice.dto.GetPlumberHousesResponse;
import com.example.plumberservice.dto.HirePlumberRequest;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.entity.House;
import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.repository.HouseRepository;
import com.example.plumberservice.repository.PlumberRepository;
import com.example.plumberservice.service.PlumberService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PlumberServiceTest {

    @Mock
    private PlumberRepository plumberRepository;
    @Mock
    private HouseRepository houseRepository;

    private PlumberService plumberService;

    @BeforeEach
    void setUp() {
        plumberService = new PlumberService(plumberRepository, houseRepository);
    }

    @Test
    void getPlumber_returnsPlumberInfo() {
        given(plumberRepository.findById(1L)).willReturn(Optional.of(new Plumber("Vasya")));

        PlumberDto plumber = plumberService.getPlumber(1L);

        assertThat(plumber).isNotNull();
        assertThat(plumber.getName()).isEqualTo("Vasya");
    }

    @Test
    void hirePlumber_savesPlumberAndReturnsPlumberInfo() {
        val vasya = new Plumber("Vasya");
        given(plumberRepository.save(any())).willReturn(vasya);

        PlumberDto plumber = plumberService.hirePlumber(new HirePlumberRequest(vasya.getName()));

        assertThat(plumber).isNotNull();
        assertThat(plumber.getName()).isEqualTo("Vasya");
    }

    @Test
    void getPlumberHouses_returnsHousesServedByPlumber() {
        val vasya = new Plumber("Vasya");
        vasya.addHouse(new House("fake street 123"));
        given(plumberRepository.findById(1L)).willReturn(Optional.of(vasya));

        GetPlumberHousesResponse plumberHousesResponse = plumberService.getPlumberHouses(1L);

        assertThat(plumberHousesResponse).isNotNull();
        assertThat(plumberHousesResponse.getPlumberName()).isEqualTo("Vasya");
        assertThat(plumberHousesResponse.getHouses()).isNotEmpty();
    }
}
