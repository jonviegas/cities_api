package com.jonservices.citiesapi.repositories;

import com.jonservices.citiesapi.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CityRepository extends AbstractRepository<City> {

    @Query(value = "SELECT c FROM City c JOIN c.state s WHERE s.uf = UPPER(:uf)")
    Page<City> findByState(String uf, Pageable page);

    @Query(value = "SELECT ((SELECT lat_lon FROM cidade WHERE id=?1) <@> (SELECT lat_lon FROM cidade WHERE id=?2)) as distance", nativeQuery = true)
    BigDecimal distanceByPoints(final Long cityId1, final Long cityId2);

    @Query(value = "SELECT earth_distance(ll_to_earth(?1,?2), ll_to_earth(?3,?4)) as distance", nativeQuery = true)
    BigDecimal distanceByCube(final BigDecimal cityLatitude1, final BigDecimal cityLongitude1, final BigDecimal cityLatitude2, final BigDecimal cityLongitude2);
}
