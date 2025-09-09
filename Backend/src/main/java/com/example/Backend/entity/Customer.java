package com.example.Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户信息实体类
 * 对应数据库中的 customers 表
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company", length = 200)
    private String company;
    
    @NotBlank(message = "客户姓名不能为空")
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    @NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @NotBlank(message = "客户地址不能为空")
    @Column(name = "address", nullable = false, length = 500)
    private String address;
    
    @NotBlank(message = "客户省份不能为空")
    @Column(name = "province", nullable = false, length = 50)
    private String province;
    
    // ========== 多对多关联到用户 ==========
    
    /**
     * 管理该客户的用户列表
     * mappedBy = "customers" 表示关联关系由User实体的customers字段维护
     * 这是多对多关系的"被拥有方"
     */
    @ManyToMany(mappedBy = "customers", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}