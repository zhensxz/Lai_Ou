package com.example.Backend.service;

import com.example.Backend.entity.Product;
import com.example.Backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import com.example.Backend.util.AuthContext;

/**
 * 产品业务逻辑服务类
 */
@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserProductRelationService userProductRelationService;
    
    /**
     * 创建产品
     */
    public Product createProduct(String name, String description, BigDecimal basePrice, Integer stockQuantity, String expiryDate, String manufacturer, String spec) {
        // RBAC: EMPLOYEE 禁止创建
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权创建产品");
        }
        // 当效期不为空时，检查产品名称和效期的组合是否已存在
        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            List<Product> existingProducts = productRepository.findByNameContaining(name);
            for (Product existingProduct : existingProducts) {
                if (existingProduct.getName().equals(name) && 
                    existingProduct.getExpiryDate() != null && 
                    existingProduct.getExpiryDate().equals(expiryDate)) {
                    throw new RuntimeException("该产品已登记");
                }
            }
        }
        
        // 创建产品对象
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setBasePrice(basePrice);
        product.setStockQuantity(stockQuantity);
        product.setExpiryDate(expiryDate);
        product.setManufacturer(manufacturer);
        product.setSpec(spec);
        
        // 保存产品
        return productRepository.save(product);
    }
    
    /**
     * 根据ID查找产品
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * 根据产品名称查找产品
     */
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }
    
    /**
     * 根据产品名称模糊查询
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    
    /**
     * 根据生产公司模糊查询
     */
    public List<Product> searchByManufacturer(String manufacturer) {
        return productRepository.findByManufacturerContaining(manufacturer);
    }
    
    /**
     * 根据生产公司精确查询
     */
    public List<Product> findByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer);
    }
    
    /**
     * 更新产品信息
     */
    public Product updateProduct(Long id, Product product) {
        // RBAC: EMPLOYEE 禁止更新
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权更新产品");
        }
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("产品不存在"));
        
        // 当效期不为空时，检查产品名称和效期的组合是否已存在（排除当前产品）
        if (product.getExpiryDate() != null && !product.getExpiryDate().trim().isEmpty()) {
            List<Product> existingProducts = productRepository.findByNameContaining(product.getName());
            for (Product existing : existingProducts) {
                if (!existing.getId().equals(id) && 
                    existing.getName().equals(product.getName()) && 
                    existing.getExpiryDate() != null && 
                    existing.getExpiryDate().equals(product.getExpiryDate())) {
                    throw new RuntimeException("该产品已登记");
                }
            }
        }
        
        // 更新产品信息
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBasePrice(product.getBasePrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setExpiryDate(product.getExpiryDate());
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setSpec(product.getSpec());
        
        return productRepository.save(existingProduct);
    }
    
    /**
     * 更新产品库存
     */
    public Product updateStock(Long id, Integer newStockQuantity) {
        // RBAC: EMPLOYEE 禁止更新库存
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权更新库存");
        }
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("产品不存在"));
        
        if (newStockQuantity < 0) {
            throw new RuntimeException("库存数量不能为负数");
        }
        
        product.setStockQuantity(newStockQuantity);
        return productRepository.save(product);
    }
    
    /**
     * 删除产品
     */
    public void deleteProduct(Long id) {
        // RBAC: EMPLOYEE 禁止删除
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权删除产品");
        }
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("产品不存在");
        }
        
        // 先删除该产品的所有用户关联关系
        userProductRelationService.deleteProductUserRelations(id);
        
        // 再删除产品本身
        productRepository.deleteById(id);
    }
    
    /**
     * 获取所有产品
     */
    public List<Product> getAllProducts() {
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            String username = com.example.Backend.util.AuthContext.getUsername();
            if (username != null && !username.isEmpty()) {
                return userProductRelationService.getUserProducts(username);
            }
        }
        return productRepository.findAll();
    }
    
    
    /**
     * 检查产品是否存在
     */
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
    
    /**
     * 检查产品名称是否存在
     */
    public boolean existsByName(String name) {
        return productRepository.findByName(name).isPresent();
    }
    
    /**
     * 获取产品总数
     */
    public long getProductCount() {
        return productRepository.count();
    }
}