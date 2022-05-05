package com.example.plumberservice.plumber;

import com.example.plumberservice.dto.GetPlumberHousesResponse;
import com.example.plumberservice.dto.HirePlumberRequest;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.exception.EntityNotFoundException;
import com.example.plumberservice.service.HouseService;
import com.example.plumberservice.service.PlumberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PlumberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlumberService plumberService;
    @MockBean
    private HouseService houseService;
    @Autowired
    private ObjectMapper objectMapper;

    public static String API = "/api/v1";

    @Test
    void getPlumber_shouldReturnPlumberDetails() throws Exception {

        given(plumberService.getPlumber(anyLong())).willReturn(new PlumberDto("Vasya"));

        mockMvc.perform(get(API + "/plumbers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Vasya"));
    }

    @Test
    void getPlumber_notFound() throws Exception {
        given(plumberService.getPlumber(anyLong())).willThrow(new EntityNotFoundException("plumber not found"));

        mockMvc.perform(get(API+ "/plumber/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postPlumber_shouldSavePlumberAndReturnPlumberDetails() throws Exception {

        val vasya = new PlumberDto("Vasya");
        given(plumberService.hirePlumber(any())).willReturn(vasya);

        val request = objectMapper.writeValueAsString(new HirePlumberRequest("Vasya"));
        mockMvc.perform(post(API+ "/plumbers").content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Vasya"));
    }

    @Test
    void deletePlumber_shouldDeletePlumberAndUnbindHouses() throws Exception {

        given(plumberService.firePlumber(any())).willReturn(true);

        mockMvc.perform(delete(API + "/plumbers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getPlumberHouses_shouldReturnHousesServedByPlumber() throws Exception {

        given(plumberService.getPlumberHouses(anyLong()))
                .willReturn(new GetPlumberHousesResponse("Vasya", List.of("Fake street 123")));

        mockMvc.perform(get(API + "/plumbers/1/houses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("plumberName").value("Vasya"))
                .andExpect(jsonPath("houses").value("Fake street 123"));
    }
}
