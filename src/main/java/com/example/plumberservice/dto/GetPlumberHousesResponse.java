package com.example.plumberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetPlumberHousesResponse {

    private String plumberName;
    private List<String> houses;

}
