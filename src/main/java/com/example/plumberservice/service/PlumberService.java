package com.example.plumberservice.service;

import com.example.plumberservice.dto.*;
import com.example.plumberservice.entity.House;
import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.exception.EntityNotFoundException;
import com.example.plumberservice.repository.HouseRepository;
import com.example.plumberservice.repository.PlumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlumberService {
    private final PlumberRepository plumberRepository;
    private final HouseRepository houseRepository;

    public PlumberDto getPlumber(Long id) {
        return plumberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("plumber not found")).toDto();
    }

    public PlumberDto hirePlumber(HirePlumberRequest hirePlumberRequest) {
        return plumberRepository.save(new Plumber(hirePlumberRequest.getPlumberName())).toDto();
    }

    public boolean firePlumber(Long id) {
        houseRepository.unbindPlumber(id);
        plumberRepository.deleteById(id);

        return !plumberRepository.existsById(id);
    }

    public GetPlumberHousesResponse getPlumberHouses(Long plumberId) {
        val plumber = plumberRepository.findById(plumberId).orElseThrow(() ->
                new EntityNotFoundException(String.format("plumber with id %d not found", plumberId)));
        val houseAddresses = plumber.getHouses().stream()
                .map(House::getAddress)
                .collect(Collectors.toList());

        return new GetPlumberHousesResponse(plumber.getName(), houseAddresses);
    }
}
