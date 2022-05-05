package com.example.plumberservice.entity;

import com.example.plumberservice.dto.HouseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "house")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "house_id_generator")
    @SequenceGenerator(name = "house_id_generator", sequenceName = "house_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    private String address;

    @ManyToOne
    @JoinColumn(name = "plumber_id")
    private Plumber plumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        House house = (House) o;
        return id != null && Objects.equals(id, house.id);
    }

    public HouseDto toDto() {
        return new HouseDto(this.address, this.plumber != null ? this.plumber.getName() : null);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public House(String address) {
        this.address = address;
    }
}