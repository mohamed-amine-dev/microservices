package com.realestate.property.repository;

import com.realestate.property.entity.Property;
import com.realestate.property.entity.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
    List<Property> findByOwnerId(Long ownerId);
    
    List<Property> findByStatus(PropertyStatus status);
    
    List<Property> findByCity(String city);
    
    @Query("SELECT p FROM Property p WHERE " +
           "(:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:country IS NULL OR LOWER(p.country) LIKE LOWER(CONCAT('%', :country, '%'))) AND " +
           "(:minPrice IS NULL OR p.pricePerMonth >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.pricePerMonth <= :maxPrice) AND " +
           "(:bedrooms IS NULL OR p.bedroomCount >= :bedrooms) AND " +
           "(:status IS NULL OR p.status = :status)")
    List<Property> searchProperties(
            @Param("city") String city,
            @Param("country") String country,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("bedrooms") Integer bedrooms,
            @Param("status") PropertyStatus status
    );
}
