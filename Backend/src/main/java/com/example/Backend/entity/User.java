package com.example.Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 * 对应数据库中的 users 表
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 50, message = "用户名长度必须在1-50个字符之间")
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少6个字符")
    @Column(name = "password", nullable = false)
    private String password;
    
    /**
     * 用户角色
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role = UserRole.EMPLOYEE;
    
    // ========== 多对多关联到产品 ==========
    
    /**
     * 用户管理的产品列表
     * 多对多关联，通过中间表 user_product_relations 实现
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_product_relations",           // 中间表名
        joinColumns = @JoinColumn(name = "user_id"),        // 当前实体在中间表中的外键
        inverseJoinColumns = @JoinColumn(name = "product_id") // 关联实体在中间表中的外键
    )
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    // ========== 多对多关联到客户 ==========
    
    /**
     * 用户管理的客户列表
     * 多对多关联，通过中间表 user_customer_relations 实现
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_customer_relations",           // 中间表名
        joinColumns = @JoinColumn(name = "user_id"),        // 当前实体在中间表中的外键
        inverseJoinColumns = @JoinColumn(name = "customer_id") // 关联实体在中间表中的外键
    )
    @JsonIgnore
    private List<Customer> customers = new ArrayList<>();
}