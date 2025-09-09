package com.example.Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品总表实体类
 * 包含所有用户的产品数据
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    /**
     * 主键ID - 自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 生产公司
     */
    @Column(name = "manufacturer", length = 200)
    private String manufacturer;
    
    /**
     * 产品描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * 产品底价
     */
    @DecimalMin(value = "0.0", message = "产品底价不能为负数")
    @Column(name = "base_price", precision = 10, scale = 2, nullable = true)
    private BigDecimal basePrice;
    
    /**
     * 库存数量
     */
    @NotNull(message = "库存数量不能为空")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
    
    /**
     * 产品效期（精确到月）
     */
    @Column(name = "expiry_date", nullable = true)
    private java.time.YearMonth expiryDate;
    
    // ========== 多对多关联到用户 ==========
    
    /**
     * 管理该产品的用户列表
     * mappedBy = "products" 表示关联关系由User实体的products字段维护
     * 这是多对多关系的"被拥有方"
     */
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}