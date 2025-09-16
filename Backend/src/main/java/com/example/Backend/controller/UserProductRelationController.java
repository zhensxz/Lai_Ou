package com.example.Backend.controller;

import com.example.Backend.entity.Product;
import com.example.Backend.entity.User;
import com.example.Backend.entity.UserProductRelation;
import com.example.Backend.service.UserProductRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户产品关联管理控制器
 */
@RestController
@RequestMapping("/api/user-product-relations")
@CrossOrigin(origins = "*")
public class UserProductRelationController {
    
    @Autowired
    private UserProductRelationService userProductRelationService;
    
    /**
     * 为用户分配产品管理权限
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignProductToUser(@RequestParam String username, @RequestParam Long productId) {
        try {
            UserProductRelation relation = userProductRelationService.assignProductToUser(username, productId);
            return ResponseEntity.status(HttpStatus.CREATED).body(relation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 取消用户的产品管理权限
     */
    @DeleteMapping("/unassign")
    public ResponseEntity<?> unassignProductFromUser(@RequestParam String username, @RequestParam Long productId) {
        try {
            userProductRelationService.unassignProductFromUser(username, productId);
            return ResponseEntity.ok("产品权限取消成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户管理的所有产品
     */
    @GetMapping("/user/{username}/products")
    public ResponseEntity<?> getUserProducts(@PathVariable String username) {
        try {
            List<Product> products = userProductRelationService.getUserProducts(username);
            return ResponseEntity.ok(products);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取管理特定产品的所有用户
     */
    @GetMapping("/product/{productId}/users")
    public ResponseEntity<?> getProductUsers(@PathVariable Long productId) {
        try {
            List<User> users = userProductRelationService.getProductUsers(productId);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 检查用户是否有权限管理产品
     */
    @GetMapping("/check-permission")
    public ResponseEntity<?> hasUserProductPermission(@RequestParam String username, @RequestParam Long productId) {
        try {
            boolean hasPermission = userProductRelationService.hasUserProductPermission(username, productId);
            return ResponseEntity.ok(hasPermission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户产品关联关系
     */
    @GetMapping("/relation")
    public ResponseEntity<?> getUserProductRelation(@RequestParam String username, @RequestParam Long productId) {
        try {
            return userProductRelationService.getUserProductRelation(username, productId)
                .map(relation -> ResponseEntity.ok(relation))
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户的所有产品关联
     */
    @GetMapping("/user/{username}/relations")
    public ResponseEntity<?> getUserProductRelations(@PathVariable String username) {
        try {
            List<UserProductRelation> relations = userProductRelationService.getUserProductRelations(username);
            return ResponseEntity.ok(relations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取产品的所有用户关联
     */
    @GetMapping("/product/{productId}/relations")
    public ResponseEntity<?> getProductUserRelations(@PathVariable Long productId) {
        try {
            List<UserProductRelation> relations = userProductRelationService.getProductUserRelations(productId);
            return ResponseEntity.ok(relations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 删除用户的所有产品关联
     */
    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUserProductRelations(@PathVariable String username) {
        try {
            userProductRelationService.deleteUserProductRelations(username);
            return ResponseEntity.ok("用户的所有产品关联已删除");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 删除产品的所有用户关联
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteProductUserRelations(@PathVariable Long productId) {
        try {
            userProductRelationService.deleteProductUserRelations(productId);
            return ResponseEntity.ok("产品的所有用户关联已删除");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 批量分配产品给用户
     */
    @PostMapping("/batch-assign")
    public ResponseEntity<?> assignProductsToUser(@RequestParam String username, @RequestBody List<Long> productIds) {
        try {
            List<UserProductRelation> relations = userProductRelationService.assignProductsToUser(username, productIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(relations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 批量取消用户的产品权限
     */
    @DeleteMapping("/batch-unassign")
    public ResponseEntity<?> unassignProductsFromUser(@RequestParam String username, @RequestBody List<Long> productIds) {
        try {
            userProductRelationService.unassignProductsFromUser(username, productIds);
            return ResponseEntity.ok("批量取消产品权限成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取用户管理的产品数量
     */
    @GetMapping("/user/{username}/count")
    public ResponseEntity<?> getUserProductCount(@PathVariable String username) {
        try {
            long count = userProductRelationService.getUserProductCount(username);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取管理产品的用户数量
     */
    @GetMapping("/product/{productId}/count")
    public ResponseEntity<?> getProductUserCount(@PathVariable Long productId) {
        try {
            long count = userProductRelationService.getProductUserCount(productId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 检查用户是否管理任何产品
     */
    @GetMapping("/user/{username}/has-any")
    public ResponseEntity<?> hasUserAnyProducts(@PathVariable String username) {
        try {
            boolean hasAny = userProductRelationService.hasUserAnyProducts(username);
            return ResponseEntity.ok(hasAny);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 检查产品是否被任何用户管理
     */
    @GetMapping("/product/{productId}/is-managed")
    public ResponseEntity<?> isProductManagedByAnyUser(@PathVariable Long productId) {
        try {
            boolean isManaged = userProductRelationService.isProductManagedByAnyUser(productId);
            return ResponseEntity.ok(isManaged);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}