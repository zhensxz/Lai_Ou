package com.example.Backend.controller;

import com.example.Backend.entity.Customer;
import com.example.Backend.entity.User;
import com.example.Backend.entity.UserCustomerRelation;
import com.example.Backend.service.UserCustomerRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户客户关联管理控制器
 */
@RestController
@RequestMapping("/api/user-customer-relations")
@CrossOrigin(origins = "*")
public class UserCustomerRelationController {
    
    @Autowired
    private UserCustomerRelationService userCustomerRelationService;
    
    /**
     * 为用户分配客户管理权限
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignCustomerToUser(@RequestParam String username, @RequestParam Long customerId) {
        try {
            UserCustomerRelation relation = userCustomerRelationService.assignCustomerToUser(username, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(relation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 取消用户的客户管理权限
     */
    @DeleteMapping("/unassign")
    public ResponseEntity<?> unassignCustomerFromUser(@RequestParam String username, @RequestParam Long customerId) {
        try {
            userCustomerRelationService.unassignCustomerFromUser(username, customerId);
            return ResponseEntity.ok("客户权限取消成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户管理的所有客户
     */
    @GetMapping("/user/{username}/customers")
    public ResponseEntity<?> getUserCustomers(@PathVariable String username) {
        try {
            List<Customer> customers = userCustomerRelationService.getUserCustomers(username);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取管理特定客户的所有用户
     */
    @GetMapping("/customer/{customerId}/users")
    public ResponseEntity<?> getCustomerUsers(@PathVariable Long customerId) {
        try {
            List<User> users = userCustomerRelationService.getCustomerUsers(customerId);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 检查用户是否有权限管理客户
     */
    @GetMapping("/check-permission")
    public ResponseEntity<?> hasUserCustomerPermission(@RequestParam String username, @RequestParam Long customerId) {
        try {
            boolean hasPermission = userCustomerRelationService.hasUserCustomerPermission(username, customerId);
            return ResponseEntity.ok(hasPermission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户客户关联关系
     */
    @GetMapping("/relation")
    public ResponseEntity<?> getUserCustomerRelation(@RequestParam String username, @RequestParam Long customerId) {
        try {
            return userCustomerRelationService.getUserCustomerRelation(username, customerId)
                .map(relation -> ResponseEntity.ok(relation))
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户的所有客户关联
     */
    @GetMapping("/user/{username}/relations")
    public ResponseEntity<?> getUserCustomerRelations(@PathVariable String username) {
        try {
            List<UserCustomerRelation> relations = userCustomerRelationService.getUserCustomerRelations(username);
            return ResponseEntity.ok(relations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取客户的所有用户关联
     */
    @GetMapping("/customer/{customerId}/relations")
    public ResponseEntity<?> getCustomerUserRelations(@PathVariable Long customerId) {
        try {
            List<UserCustomerRelation> relations = userCustomerRelationService.getCustomerUserRelations(customerId);
            return ResponseEntity.ok(relations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 删除用户的所有客户关联
     */
    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUserCustomerRelations(@PathVariable String username) {
        try {
            userCustomerRelationService.deleteUserCustomerRelations(username);
            return ResponseEntity.ok("用户的所有客户关联已删除");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 删除客户的所有用户关联
     */
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteCustomerUserRelations(@PathVariable Long customerId) {
        try {
            userCustomerRelationService.deleteCustomerUserRelations(customerId);
            return ResponseEntity.ok("客户的所有用户关联已删除");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 批量分配客户给用户
     */
    @PostMapping("/batch-assign")
    public ResponseEntity<?> assignCustomersToUser(@RequestParam String username, @RequestBody List<Long> customerIds) {
        try {
            List<UserCustomerRelation> relations = userCustomerRelationService.assignCustomersToUser(username, customerIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(relations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 批量取消用户的客户权限
     */
    @DeleteMapping("/batch-unassign")
    public ResponseEntity<?> unassignCustomersFromUser(@RequestParam String username, @RequestBody List<Long> customerIds) {
        try {
            userCustomerRelationService.unassignCustomersFromUser(username, customerIds);
            return ResponseEntity.ok("批量取消客户权限成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户管理的客户数量
     */
    @GetMapping("/user/{username}/count")
    public ResponseEntity<?> getUserCustomerCount(@PathVariable String username) {
        try {
            long count = userCustomerRelationService.getUserCustomerCount(username);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取管理客户的用户数量
     */
    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<?> getCustomerUserCount(@PathVariable Long customerId) {
        try {
            long count = userCustomerRelationService.getCustomerUserCount(customerId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 检查用户是否管理任何客户
     */
    @GetMapping("/user/{username}/has-any")
    public ResponseEntity<?> hasUserAnyCustomers(@PathVariable String username) {
        try {
            boolean hasAny = userCustomerRelationService.hasUserAnyCustomers(username);
            return ResponseEntity.ok(hasAny);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 检查客户是否被任何用户管理
     */
    @GetMapping("/customer/{customerId}/is-managed")
    public ResponseEntity<?> isCustomerManagedByAnyUser(@PathVariable Long customerId) {
        try {
            boolean isManaged = userCustomerRelationService.isCustomerManagedByAnyUser(customerId);
            return ResponseEntity.ok(isManaged);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}