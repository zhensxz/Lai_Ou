package com.example.Backend.repository;

import com.example.Backend.entity.UserProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户产品关联数据访问接口
 */
@Repository
public interface UserProductRelationRepository extends JpaRepository<UserProductRelation, Long> {
    
    /**
     * 查询用户的所有产品关联
     */
    List<UserProductRelation> findByUserId(Long userId);
    
    /**
     * 查询产品的所有用户关联
     */
    List<UserProductRelation> findByProductId(Long productId);
    
    /**
     * 查询特定用户和产品的关联
     */
    Optional<UserProductRelation> findByUserIdAndProductId(Long userId, Long productId);
    
    /**
     * 检查用户是否有权限管理产品
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    
    /**
     * 删除用户和产品的关联
     */
    void deleteByUserIdAndProductId(Long userId, Long productId);
    
    /**
     * 删除用户的所有产品关联
     */
    void deleteByUserId(Long userId);
    
    /**
     * 删除产品的所有用户关联
     */
    void deleteByProductId(Long productId);
}