package com.example.plumberservice.controller;

import com.example.plumberservice.dto.GetPlumberHousesResponse;
import com.example.plumberservice.dto.HirePlumberRequest;
import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.service.PlumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PlumberController {

    private final PlumberService plumberService;

    @GetMapping("/plumbers/{id}")
    public ResponseEntity<PlumberDto> getPlumberInfo(@PathVariable Long id) {
        return ResponseEntity.ok(plumberService.getPlumber(id));
    }

    @PostMapping("/plumbers")
    public ResponseEntity<PlumberDto> hirePlumber(@RequestBody HirePlumberRequest plumberRequest) {
        return ResponseEntity.ok(plumberService.hirePlumber(plumberRequest));
    }


    @DeleteMapping("/plumbers/{id}")
    public ResponseEntity<Boolean> firePlumber(@PathVariable Long id) {
        return ResponseEntity.ok(plumberService.firePlumber(id));
    }

    @GetMapping("/plumbers/{id}/houses")
    public ResponseEntity<GetPlumberHousesResponse> getPlumberHouses(@PathVariable Long id) {
        return ResponseEntity.ok(plumberService.getPlumberHouses(id));
    }
}
