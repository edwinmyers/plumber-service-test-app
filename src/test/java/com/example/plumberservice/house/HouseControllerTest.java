package com.example.plumberservice.house;

import com.example.plumberservice.dto.HirePlumberRequest;
import com.example.plumberservice.dto.HouseDto;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.dto.SaveHouseRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseService houseService;

    @MockBean
    private PlumberService plumberService;

    @Autowired
    private ObjectMapper objectMapper;

    public static String API = "/api/v1";

    @Test
    void bindPlumberToHouse_shouldReturnOk() throws Exception {

        mockMvc.perform(post(API + "/houses/1/bind")
                        .param("plumberId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void bindPlumber_notFound() throws Exception {
        doThrow(new EntityNotFoundException("hey")).when(houseService).bindPlumber(anyLong(), anyLong());

        mockMvc.perform(post(API + "/houses/1/bind")
                        .param("plumberId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void unbindPlumberToHouse_shouldReturnOk() throws Exception {

        mockMvc.perform(post(API + "/houses/1/unbind")
                        .param("plumberId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void unbindPlumber_notFound() throws Exception {
        doThrow(new EntityNotFoundException("hey")).when(houseService).unbindPlumber(anyLong(), anyLong());

        mockMvc.perform(post(API + "/houses/1/unbind")
                        .param("plumberId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHousePlumber_shouldReturnPlumberThatServesGivenHouse() throws Exception {

        given(houseService.getPlumber(anyLong()))
                .willReturn(new PlumberDto("Vasya"));

        mockMvc.perform(get(API + "/houses/1/plumber"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Vasya"));
    }

    @Test
    void getHouse_shouldReturnHouseInfo() throws Exception {

        given(houseService.getHouse(anyLong()))
                .willReturn(new HouseDto("fake street 123", "Vasya"));

        mockMvc.perform(get(API + "/houses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("address").value("fake street 123"))
                .andExpect(jsonPath("plumberName").value("Vasya"));
    }

    @Test
    void postHouse_shouldSaveHouseAndReturnDetails() throws Exception {

        val house = new HouseDto("fake street 123");
        given(houseService.saveHouse(any())).willReturn(house);

        val request = objectMapper.writeValueAsString(new SaveHouseRequest("Vasya"));
        mockMvc.perform(post(API + "/houses").content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("address").value("fake street 123"));
    }

    @Test
    void deleteHouse_shouldDeleteHouseAndUnbindPlumber() throws Exception {

        given(houseService.removeHouse(any())).willReturn(true);

        mockMvc.perform(delete(API + "/houses/1"))
                .andExpect(status().isOk());
    }
}
