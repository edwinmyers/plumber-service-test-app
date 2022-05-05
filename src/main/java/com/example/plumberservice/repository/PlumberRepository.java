package com.example.plumberservice.repository;

import com.example.plumberservice.entity.Plumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlumberRepository extends JpaRepository<Plumber, Long> {
    @Query("select p from Plumber p where ?1 in p.houses")
    Plumber findByHouseId(Long houseId);
}
