package com.example.Backend.repository;

import com.example.Backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * 产品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据产品名称模糊查询
     */
    List<Product> findByNameContaining(String name);
    
    /**
     * 根据产品名称精确查询
     */
    Optional<Product> findByName(String name);
    
    /**
     * 根据生产公司名称模糊查询
     */
    List<Product> findByManufacturerContaining(String manufacturer);
    
    /**
     * 根据生产公司名称精确查询
     */
    List<Product> findByManufacturer(String manufacturer);
    
    /**
     * 查询管理特定产品的所有用户
     */
    @Query("SELECT p.users FROM Product p WHERE p.id = :productId")
    List<Object> findUsersByProductId(@Param("productId") Long productId);
    
    /**
     * 查询没有设置过期日期的产品
     */
    @Query("SELECT p FROM Product p WHERE p.expiryDate IS NULL")
    List<Product> findProductsWithoutExpiry();
}