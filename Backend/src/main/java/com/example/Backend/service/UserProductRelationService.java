package com.example.Backend.service;

import com.example.Backend.entity.Product;
import com.example.Backend.entity.User;
import com.example.Backend.entity.UserProductRelation;
import com.example.Backend.repository.ProductRepository;
import com.example.Backend.repository.UserProductRelationRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户产品关联业务逻辑服务类
 */
@Service
@Transactional
public class UserProductRelationService {
    
    @Autowired
    private UserProductRelationRepository userProductRelationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * 为用户分配产品管理权限
     */
    public UserProductRelation assignProductToUser(String username, Long productId) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        // 检查产品是否存在
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("产品不存在"));
        
        // 检查是否已经关联
        if (userProductRelationRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new RuntimeException("用户已经管理该产品");
        }
        
        // 创建关联关系
        UserProductRelation relation = new UserProductRelation();
        relation.setUser(user);
        relation.setProduct(product);
        
        return userProductRelationRepository.save(relation);
    }
    
    /**
     * 取消用户的产品管理权限
     */
    public void unassignProductFromUser(String username, Long productId) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        if (!userProductRelationRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new RuntimeException("用户没有管理该产品的权限");
        }
        userProductRelationRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }
    
    /**
     * 获取用户管理的所有产品
     */
    public List<Product> getUserProducts(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userProductRelationRepository.findByUserId(user.getId())
            .stream()
            .map(UserProductRelation::getProduct)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 获取管理特定产品的所有用户
     */
    public List<User> getProductUsers(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("产品不存在");
        }
        
        return userProductRelationRepository.findByProductId(productId)
            .stream()
            .map(UserProductRelation::getUser)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 检查用户是否有权限管理产品
     */
    public boolean hasUserProductPermission(String username, Long productId) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userProductRelationRepository.existsByUserIdAndProductId(user.getId(), productId);
    }
    
    /**
     * 获取用户产品关联关系
     */
    public Optional<UserProductRelation> getUserProductRelation(String username, Long productId) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userProductRelationRepository.findByUserIdAndProductId(user.getId(), productId);
    }
    
    /**
     * 获取用户的所有产品关联
     */
    public List<UserProductRelation> getUserProductRelations(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userProductRelationRepository.findByUserId(user.getId());
    }
    
    /**
     * 获取产品的所有用户关联
     */
    public List<UserProductRelation> getProductUserRelations(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("产品不存在");
        }
        return userProductRelationRepository.findByProductId(productId);
    }
    
    /**
     * 删除用户的所有产品关联
     */
    public void deleteUserProductRelations(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        userProductRelationRepository.deleteByUserId(user.getId());
    }
    
    /**
     * 删除产品的所有用户关联
     */
    public void deleteProductUserRelations(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("产品不存在");
        }
        userProductRelationRepository.deleteByProductId(productId);
    }
    
    /**
     * 批量分配产品给用户
     */
    public List<UserProductRelation> assignProductsToUser(String username, List<Long> productIds) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        List<UserProductRelation> relations = new java.util.ArrayList<>();
        
        for (Long productId : productIds) {
            // 检查产品是否存在
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("产品不存在: " + productId));
            
            // 检查是否已经关联
            if (!userProductRelationRepository.existsByUserIdAndProductId(user.getId(), productId)) {
                UserProductRelation relation = new UserProductRelation();
                relation.setUser(user);
                relation.setProduct(product);
                relations.add(userProductRelationRepository.save(relation));
            }
        }
        
        return relations;
    }
    
    /**
     * 批量取消用户的产品权限
     */
    public void unassignProductsFromUser(String username, List<Long> productIds) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        for (Long productId : productIds) {
            if (userProductRelationRepository.existsByUserIdAndProductId(user.getId(), productId)) {
                userProductRelationRepository.deleteByUserIdAndProductId(user.getId(), productId);
            }
        }
    }
    
    /**
     * 获取用户管理的产品数量
     */
    public long getUserProductCount(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userProductRelationRepository.findByUserId(user.getId()).size();
    }
    
    /**
     * 获取管理产品的用户数量
     */
    public long getProductUserCount(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("产品不存在");
        }
        return userProductRelationRepository.findByProductId(productId).size();
    }
    
    /**
     * 检查用户是否管理任何产品
     */
    public boolean hasUserAnyProducts(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userProductRelationRepository.findByUserId(user.getId()).size() > 0;
    }
    
    /**
     * 检查产品是否被任何用户管理
     */
    public boolean isProductManagedByAnyUser(Long productId) {
        return userProductRelationRepository.findByProductId(productId).size() > 0;
    }
}