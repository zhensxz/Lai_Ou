package com.example.Backend.repository;

import com.example.Backend.entity.UserCustomerRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户客户关联数据访问接口
 */
@Repository
public interface UserCustomerRelationRepository extends JpaRepository<UserCustomerRelation, Long> {
    
    /**
     * 查询用户的所有客户关联
     */
    List<UserCustomerRelation> findByUserId(Long userId);
    
    /**
     * 查询客户的所有用户关联
     */
    List<UserCustomerRelation> findByCustomerId(Long customerId);
    
    /**
     * 查询特定用户和客户的关联
     */
    Optional<UserCustomerRelation> findByUserIdAndCustomerId(Long userId, Long customerId);
    
    /**
     * 检查用户是否有权限管理客户
     */
    boolean existsByUserIdAndCustomerId(Long userId, Long customerId);
    
    /**
     * 删除用户和客户的关联
     */
    void deleteByUserIdAndCustomerId(Long userId, Long customerId);
    
    /**
     * 删除用户的所有客户关联
     */
    void deleteByUserId(Long userId);
    
    /**
     * 删除客户的所有用户关联
     */
    void deleteByCustomerId(Long customerId);
}