package com.example.Backend.controller;

import com.example.Backend.service.DynamicDataService;
import com.example.Backend.service.DynamicTableService;
import com.example.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据控制器
 * 处理用户动态表的数据操作
 */
@RestController
@RequestMapping("/api/dynamic")
public class DynamicDataController {
    
    @Autowired
    private DynamicDataService dynamicDataService;
    
    @Autowired
    private DynamicTableService dynamicTableService;
    
    @Autowired
    private UserService userService;
    
    // ========== 客户数据操作 ==========
    
    /**
     * 添加客户
     */
    @PostMapping("/{userId}/customers")
    public Map<String, Object> addCustomer(@PathVariable Long userId, @RequestBody Map<String, Object> customerData) {
        try {
            Long customerId = dynamicDataService.addCustomer(userId, customerData);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "客户添加成功");
            result.put("customerId", customerId);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "客户添加失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取用户的所有客户
     */
    @GetMapping("/{userId}/customers")
    public Map<String, Object> getCustomers(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> customers = dynamicDataService.getCustomers(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", customers);
            result.put("count", customers.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取客户列表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据ID获取客户
     */
    @GetMapping("/{userId}/customers/{customerId}")
    public Map<String, Object> getCustomerById(@PathVariable Long userId, @PathVariable Long customerId) {
        try {
            Map<String, Object> customer = dynamicDataService.getCustomerById(userId, customerId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", customer);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取客户信息失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 更新客户信息
     */
    @PutMapping("/{userId}/customers/{customerId}")
    public Map<String, Object> updateCustomer(@PathVariable Long userId, @PathVariable Long customerId, 
                                            @RequestBody Map<String, Object> customerData) {
        try {
            int updated = dynamicDataService.updateCustomer(userId, customerId, customerData);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "客户更新成功");
            result.put("updatedCount", updated);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "客户更新失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 删除客户
     */
    @DeleteMapping("/{userId}/customers/{customerId}")
    public Map<String, Object> deleteCustomer(@PathVariable Long userId, @PathVariable Long customerId) {
        try {
            int deleted = dynamicDataService.deleteCustomer(userId, customerId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "客户删除成功");
            result.put("deletedCount", deleted);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "客户删除失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据多个条件模糊查询客户
     */
    @GetMapping("/{userId}/customers/search")
    public Map<String, Object> searchCustomers(@PathVariable Long userId, 
                                             @RequestParam(required = false) String customerName,
                                             @RequestParam(required = false) String phone,
                                             @RequestParam(required = false) String address,
                                             @RequestParam(required = false) String company) {
        try {
            Map<String, Object> searchParams = new HashMap<>();
            if (customerName != null && !customerName.trim().isEmpty()) {
                searchParams.put("customerName", customerName);
            }
            if (phone != null && !phone.trim().isEmpty()) {
                searchParams.put("phone", phone);
            }
            if (address != null && !address.trim().isEmpty()) {
                searchParams.put("address", address);
            }
            if (company != null && !company.trim().isEmpty()) {
                searchParams.put("company", company);
            }
            
            List<Map<String, Object>> customers = dynamicDataService.searchCustomers(userId, searchParams);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", customers);
            result.put("count", customers.size());
            result.put("searchParams", searchParams);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "客户搜索失败: " + e.getMessage());
            return result;
        }
    }
    
    // ========== 产品数据操作 ==========
    
    /**
     * 添加产品
     */
    @PostMapping("/{userId}/products")
    public Map<String, Object> addProduct(@PathVariable Long userId, @RequestBody Map<String, Object> productData) {
        try {
            Long productId = dynamicDataService.addProduct(userId, productData);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "产品添加成功");
            result.put("productId", productId);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品添加失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取用户的所有产品
     */
    @GetMapping("/{userId}/products")
    public Map<String, Object> getProducts(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> products = dynamicDataService.getProducts(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取产品列表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据ID获取产品
     */
    @GetMapping("/{userId}/products/{productId}")
    public Map<String, Object> getProductById(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            Map<String, Object> product = dynamicDataService.getProductById(userId, productId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", product);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取产品信息失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 更新产品信息
     */
    @PutMapping("/{userId}/products/{productId}")
    public Map<String, Object> updateProduct(@PathVariable Long userId, @PathVariable Long productId, 
                                           @RequestBody Map<String, Object> productData) {
        try {
            int updated = dynamicDataService.updateProduct(userId, productId, productData);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "产品更新成功");
            result.put("updatedCount", updated);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品更新失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 删除产品
     */
    @DeleteMapping("/{userId}/products/{productId}")
    public Map<String, Object> deleteProduct(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            int deleted = dynamicDataService.deleteProduct(userId, productId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "产品删除成功");
            result.put("deletedCount", deleted);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品删除失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取用户的所有产品
     */
    @GetMapping("/{userId}/products")
    public Map<String, Object> getProducts(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> products = dynamicDataService.getProducts(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取产品列表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据产品名称查询（模糊匹配）
     */
    @GetMapping("/{userId}/products/search")
    public Map<String, Object> getProductsByName(@PathVariable Long userId, @RequestParam String name) {
        try {
            List<Map<String, Object>> products = dynamicDataService.getProductsByName(userId, name);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            result.put("searchKeyword", name);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品搜索失败: " + e.getMessage());
            return result;
        }
    }
    
    // ========== 统计信息 ==========
    
    /**
     * 获取用户数据统计
     */
    @GetMapping("/{userId}/statistics")
    public Map<String, Object> getUserStatistics(@PathVariable Long userId) {
        try {
            Map<String, Object> statistics = dynamicDataService.getUserStatistics(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", statistics);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取统计数据失败: " + e.getMessage());
            return result;
        }
    }
    
    // ========== 表管理 ==========
    
    /**
     * 检查用户表是否存在
     */
    @GetMapping("/{userId}/tables/exists")
    public Map<String, Object> checkUserTablesExist(@PathVariable Long userId) {
        try {
            boolean exists = userService.checkUserTablesExist(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("exists", exists);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "检查表存在性失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 重新创建用户表
     */
    @PostMapping("/{userId}/tables/recreate")
    public Map<String, Object> recreateUserTables(@PathVariable Long userId) {
        try {
            userService.recreateUserTables(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "用户表重新创建成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "重新创建表失败: " + e.getMessage());
            return result;
        }
    }
}
