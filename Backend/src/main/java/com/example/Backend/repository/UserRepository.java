package com.example.Backend.repository;

import com.example.Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 查询用户管理的所有产品
     */
    @Query("SELECT u.products FROM User u WHERE u.id = :userId")
    List<Product> findProductsByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户管理的所有客户
     */
    @Query("SELECT u.customers FROM User u WHERE u.id = :userId")
    List<Customer> findCustomersByUserId(@Param("userId") Long userId);
}