package com.example.plumberservice.controller;

import com.example.plumberservice.dto.HirePlumberRequest;
import com.example.plumberservice.dto.HouseDto;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.dto.SaveHouseRequest;
import com.example.plumberservice.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HouseController {

    private final HouseService houseService;

    @PostMapping("/houses/{houseId}/bind")
    public ResponseEntity<Void> bindPlumber(@PathVariable Long houseId, @RequestParam Long plumberId) {
        houseService.bindPlumber(houseId, plumberId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/houses/{houseId}/unbind")
    public ResponseEntity<Void> unbindPlumber(@PathVariable Long houseId, @RequestParam Long plumberId) {
        houseService.unbindPlumber(houseId, plumberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/houses/{id}/plumber")
    public ResponseEntity<PlumberDto> getHousePlumberInfo(@PathVariable Long id) {
        return ResponseEntity.ok(houseService.getPlumber(id));
    }

    @GetMapping("/houses/{id}")
    public ResponseEntity<HouseDto> getHouse(@PathVariable Long id) {
        return ResponseEntity.ok(houseService.getHouse(id));
    }

    @PostMapping("/houses")
    public ResponseEntity<HouseDto> hirePlumber(@RequestBody SaveHouseRequest saveHouseRequest) {
        return ResponseEntity.ok(houseService.saveHouse(saveHouseRequest));
    }

    @DeleteMapping("/houses/{id}")
    public ResponseEntity<Boolean> removeHouse(@PathVariable Long id) {
        return ResponseEntity.ok(houseService.removeHouse(id));
    }
}
