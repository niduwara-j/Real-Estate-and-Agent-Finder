package com.dilshan.realestate.repository;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Property;
import com.dilshan.realestate.model.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByAgent(Agent agent);
    List<Property> findByStatus(String status);
    List<Property> findByPropertyType(PropertyType propertyType);
    List<Property> findByCityIgnoreCase(String city);

    @Query("SELECT p FROM Property p WHERE " +
           "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:type IS NULL OR p.propertyType = :type) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Property> searchProperties(@Param("keyword") String keyword,
                                    @Param("type") PropertyType type,
                                    @Param("minPrice") Double minPrice,
                                    @Param("maxPrice") Double maxPrice);
}
