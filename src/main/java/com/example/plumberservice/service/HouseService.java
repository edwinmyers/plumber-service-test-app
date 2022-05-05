package com.example.plumberservice.service;

import com.example.plumberservice.dto.HouseDto;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.dto.SaveHouseRequest;
import com.example.plumberservice.entity.House;
import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.exception.EntityNotFoundException;
import com.example.plumberservice.repository.HouseRepository;
import com.example.plumberservice.repository.PlumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;


@Service
@Transactional
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final PlumberRepository plumberRepository;

    public void bindPlumber(Long houseId, Long plumberId) {

        val house = houseRepository.findById(houseId).orElseThrow(() ->
                new EntityNotFoundException(format("house with id : %d not found", houseId)));
        val plumber = plumberRepository.findById(plumberId).orElseThrow(() ->
                new EntityNotFoundException(format("plumber with id : %d not found", plumberId)));
        plumber.addHouse(house);
    }

    public void unbindPlumber(Long houseId, Long plumberId) {
        val house = houseRepository.findById(houseId).orElseThrow(() ->
                new EntityNotFoundException(format("house with id : %d not found", houseId)));
        val plumber = plumberRepository.findById(plumberId).orElseThrow(() ->
                new EntityNotFoundException(format("plumber with id : %d not found", plumberId)));
        plumber.removeHouse(house);
    }

    public PlumberDto getPlumber(Long houseId) {
        return houseRepository.findById(houseId).map(House::getPlumber)
                .map(Plumber::toDto)
                .orElseThrow(() -> new EntityNotFoundException(format("house with id %d not found", houseId)));
    }

    public HouseDto getHouse(Long houseId) {
        return houseRepository.findById(houseId).map(House::toDto)
                .orElseThrow(() -> new EntityNotFoundException(format("house with id %d not found", houseId)));
    }

    public HouseDto saveHouse(SaveHouseRequest houseRequest) {
        return houseRepository.save(new House(houseRequest.getAddress())).toDto();
    }

    public Boolean removeHouse(Long houseId) {
        val house = houseRepository.findById(houseId).orElse(null);
        if (house != null) {
            house.getPlumber().removeHouse(house);
        }
        houseRepository.deleteById(houseId);
        return houseRepository.existsById(houseId);
    }
}
