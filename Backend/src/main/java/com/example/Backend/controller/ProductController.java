package com.example.Backend.controller;

import com.example.Backend.entity.Product;
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 产品总表控制器
 * 管理所有用户的产品数据
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 创建新产品
     */
    @PostMapping
    public Map<String, Object> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "产品创建成功");
            result.put("data", createdProduct);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品创建失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取所有产品
     */
    @GetMapping
    public Map<String, Object> getAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();
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
    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.findById(id);
            Map<String, Object> result = new HashMap<>();
            if (product.isPresent()) {
                result.put("status", "success");
                result.put("data", product.get());
            } else {
                result.put("status", "error");
                result.put("message", "产品不存在");
            }
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取产品信息失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据名称搜索产品
     */
    @GetMapping("/search")
    public Map<String, Object> searchProducts(@RequestParam String name) {
        try {
            List<Product> products = productService.searchByName(name);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "搜索产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据价格范围查找产品
     */
    @GetMapping("/price-range")
    public Map<String, Object> getProductsByPriceRange(@RequestParam BigDecimal minPrice, 
                                                       @RequestParam BigDecimal maxPrice) {
        try {
            List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "按价格范围查找产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取有库存的产品
     */
    @GetMapping("/in-stock")
    public Map<String, Object> getInStockProducts() {
        try {
            List<Product> products = productService.findInStockProducts();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取有库存产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取缺货的产品
     */
    @GetMapping("/out-of-stock")
    public Map<String, Object> getOutOfStockProducts() {
        try {
            List<Product> products = productService.findOutOfStockProducts();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取缺货产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 更新产品信息
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            product.setId(id);
            Product updatedProduct = productService.updateProduct(product);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "产品更新成功");
            result.put("data", updatedProduct);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品更新失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 更新产品库存
     */
    @PutMapping("/{id}/stock")
    public Map<String, Object> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            Product updatedProduct = productService.updateStock(id, quantity);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "库存更新成功");
            result.put("data", updatedProduct);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "库存更新失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "产品删除成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "产品删除失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取即将过期的产品
     */
    @GetMapping("/expiring")
    public Map<String, Object> getExpiringProducts(@RequestParam(defaultValue = "1") int months) {
        try {
            List<Product> products = productService.findExpiringProducts(months);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            result.put("months", months);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取即将过期产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取已过期的产品
     */
    @GetMapping("/expired")
    public Map<String, Object> getExpiredProducts() {
        try {
            List<Product> products = productService.findExpiredProducts();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取已过期产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取没有效期的产品
     */
    @GetMapping("/no-expiry")
    public Map<String, Object> getProductsWithoutExpiry() {
        try {
            List<Product> products = productService.findProductsWithoutExpiry();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", products);
            result.put("count", products.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取无效期产品失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取产品统计信息
     */
    @GetMapping("/statistics")
    public Map<String, Object> getProductStatistics() {
        try {
            ProductService.ProductStatistics statistics = productService.getProductStatistics();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", statistics);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取统计信息失败: " + e.getMessage());
            return result;
        }
    }
}
