package com.example.Backend.service;

import com.example.Backend.entity.Customer;
import com.example.Backend.entity.User;
import com.example.Backend.entity.UserCustomerRelation;
import com.example.Backend.repository.CustomerRepository;
import com.example.Backend.repository.UserCustomerRelationRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.example.Backend.util.AuthContext;

/**
 * 用户客户关联业务逻辑服务类
 */
@Service
@Transactional
public class UserCustomerRelationService {
    
    @Autowired
    private UserCustomerRelationRepository userCustomerRelationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * 为用户分配客户管理权限
     */
    public UserCustomerRelation assignCustomerToUser(String username, Long customerId) {
        // RBAC: EMPLOYEE 禁止；AUDITOR/BOSS 允许
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权分配客户");
        }
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        // 检查客户是否存在
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("客户不存在"));
        
        // 检查是否已经关联
        if (userCustomerRelationRepository.existsByUserIdAndCustomerId(user.getId(), customerId)) {
            throw new RuntimeException("用户已经管理该客户");
        }
        
        // 创建关联关系
        UserCustomerRelation relation = new UserCustomerRelation();
        relation.setUser(user);
        relation.setCustomer(customer);
        
        return userCustomerRelationRepository.save(relation);
    }
    
    /**
     * 取消用户的客户管理权限
     */
    public void unassignCustomerFromUser(String username, Long customerId) {
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权取消客户权限");
        }
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        if (!userCustomerRelationRepository.existsByUserIdAndCustomerId(user.getId(), customerId)) {
            throw new RuntimeException("用户没有管理该客户的权限");
        }
        userCustomerRelationRepository.deleteByUserIdAndCustomerId(user.getId(), customerId);
    }
    
    /**
     * 获取用户管理的所有客户
     */
    public List<Customer> getUserCustomers(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userCustomerRelationRepository.findByUserId(user.getId())
            .stream()
            .map(UserCustomerRelation::getCustomer)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 获取管理特定客户的所有用户
     */
    public List<User> getCustomerUsers(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("客户不存在");
        }
        
        return userCustomerRelationRepository.findByCustomerId(customerId)
            .stream()
            .map(UserCustomerRelation::getUser)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 检查用户是否有权限管理客户
     */
    public boolean hasUserCustomerPermission(String username, Long customerId) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userCustomerRelationRepository.existsByUserIdAndCustomerId(user.getId(), customerId);
    }
    
    /**
     * 获取用户客户关联关系
     */
    public Optional<UserCustomerRelation> getUserCustomerRelation(String username, Long customerId) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userCustomerRelationRepository.findByUserIdAndCustomerId(user.getId(), customerId);
    }
    
    /**
     * 获取用户的所有客户关联
     */
    public List<UserCustomerRelation> getUserCustomerRelations(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userCustomerRelationRepository.findByUserId(user.getId());
    }
    
    /**
     * 获取客户的所有用户关联
     */
    public List<UserCustomerRelation> getCustomerUserRelations(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("客户不存在");
        }
        return userCustomerRelationRepository.findByCustomerId(customerId);
    }
    
    /**
     * 删除用户的所有客户关联
     */
    public void deleteUserCustomerRelations(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        userCustomerRelationRepository.deleteByUserId(user.getId());
    }
    
    /**
     * 删除客户的所有用户关联
     */
    public void deleteCustomerUserRelations(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("客户不存在");
        }
        userCustomerRelationRepository.deleteByCustomerId(customerId);
    }
    
    /**
     * 批量分配客户给用户
     */
    public List<UserCustomerRelation> assignCustomersToUser(String username, List<Long> customerIds) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        List<UserCustomerRelation> relations = new java.util.ArrayList<>();
        
        for (Long customerId : customerIds) {
            // 检查客户是否存在
            Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("客户不存在: " + customerId));
            
            // 检查是否已经关联
            if (!userCustomerRelationRepository.existsByUserIdAndCustomerId(user.getId(), customerId)) {
                UserCustomerRelation relation = new UserCustomerRelation();
                relation.setUser(user);
                relation.setCustomer(customer);
                relations.add(userCustomerRelationRepository.save(relation));
            }
        }
        
        return relations;
    }
    
    /**
     * 批量取消用户的客户权限
     */
    public void unassignCustomersFromUser(String username, List<Long> customerIds) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        for (Long customerId : customerIds) {
            if (userCustomerRelationRepository.existsByUserIdAndCustomerId(user.getId(), customerId)) {
                userCustomerRelationRepository.deleteByUserIdAndCustomerId(user.getId(), customerId);
            }
        }
    }
    
    /**
     * 获取用户管理的客户数量
     */
    public long getUserCustomerCount(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userCustomerRelationRepository.findByUserId(user.getId()).size();
    }
    
    /**
     * 获取管理客户的用户数量
     */
    public long getCustomerUserCount(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("客户不存在");
        }
        return userCustomerRelationRepository.findByCustomerId(customerId).size();
    }
    
    /**
     * 检查用户是否管理任何客户
     */
    public boolean hasUserAnyCustomers(String username) {
        // 通过用户名查找用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        return userCustomerRelationRepository.findByUserId(user.getId()).size() > 0;
    }
    
    /**
     * 检查客户是否被任何用户管理
     */
    public boolean isCustomerManagedByAnyUser(Long customerId) {
        return userCustomerRelationRepository.findByCustomerId(customerId).size() > 0;
    }
}