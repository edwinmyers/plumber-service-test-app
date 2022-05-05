package com.example.plumberservice.plumber;

import com.example.plumberservice.entity.Plumber;
import com.example.plumberservice.exception.EntityNotFoundException;
import com.example.plumberservice.repository.PlumberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PlumberRepositoryTest {

    @Autowired
    private PlumberRepository plumberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void getPlumber_returnsPlumber() {
        entityManager.persistAndFlush(new Plumber("Vasya"));

        Plumber plumber = plumberRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("plumber not found"));
        assertThat(plumber.getName()).isEqualTo("Vasya");
    }

}
