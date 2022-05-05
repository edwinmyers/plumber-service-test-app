package com.example.plumberservice.entity;

import com.example.plumberservice.dto.PlumberDto;
import com.example.plumberservice.exception.PlumberUnavailableException;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Plumber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plumber_generator")
    @SequenceGenerator(name = "plumber_generator", sequenceName = "plumber_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NonNull
    private String name;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plumber")
    private List<House> houses = new ArrayList<>();

    public PlumberDto toDto() {
        return new PlumberDto(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Plumber plumber = (Plumber) o;
        return id != null && Objects.equals(id, plumber.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addHouse(House house) {
        if (this.houses.size() >= 5) {
            throw new PlumberUnavailableException("plumber is unavailable currently as he serves maximum amount of houses for the moment");
        }
        house.setPlumber(this);
        this.houses.add(house);
    }

    public void removeHouse(House house) {
        house.setPlumber(null);
        this.houses.remove(house);
    }

    public void removeHouseById(Long houseId) {
        this.houses.removeIf(h -> h.getId().equals(houseId));
    }
}
