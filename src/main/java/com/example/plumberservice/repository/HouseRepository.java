package com.example.plumberservice.repository;

import com.example.plumberservice.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

    boolean existsByPlumberId(Long id);

    @Modifying
    @Query("update House h set h.plumber = null where h.plumber.id = ?1 ")
    void unbindPlumber(Long plumberId);

    List<House> findAllByPlumberId(Long plumberId);
}
