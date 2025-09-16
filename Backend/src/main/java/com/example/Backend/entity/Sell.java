package com.example.Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 报单实体类
 * 对应数据库中的 sells 表
 */
@Entity
@Table(name = "sells")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sell {
    
    /**
     * 主键ID - 自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 报单类型
     */
    @NotBlank(message = "报单类型不能为空")
    @Column(name = "sell_kind", nullable = false, length = 50)
    private String sellKind;
    
    /**
     * 报单人名称
     */
    @NotBlank(message = "报单人名称不能为空")
    @Column(name = "seller_name", nullable = false, length = 100)
    private String sellerName;
    
    /**
     * 报单日期
     */
    @NotNull(message = "报单日期不能为空")
    @Column(name = "sell_date", nullable = false)
    private LocalDate sellDate;
    
    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    
    /**
     * 产品数量
     */
    @NotNull(message = "产品数量不能为空")
    @DecimalMin(value = "1", message = "产品数量必须大于0")
    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    /**
     * 产品规格（仅“支”或“盒”）
     */
    @NotBlank(message = "产品规格不能为空")
    @Pattern(regexp = "^(支|盒)$", message = "产品规格只能为“支”或“盒”")
    @Column(name = "product_spec", nullable = false, length = 10)
    private String productSpec;
    
    /**
     * 产品报价
     */
    @NotNull(message = "产品报价不能为空")
    @DecimalMin(value = "0.0", message = "产品报价不能为负数")
    @Column(name = "product_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;
    
    /**
     * 总价格
     */
    @NotNull(message = "总价格不能为空")
    @DecimalMin(value = "0.0", message = "总价格不能为负数")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    /**
     * 客户公司
     */
    @Column(name = "customer_company", length = 200)
    private String customerCompany;
    
    /**
     * 客户姓名
     */
    @NotBlank(message = "客户姓名不能为空")
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    /**
     * 客户地址
     */
    @NotBlank(message = "客户地址不能为空")
    @Column(name = "customer_address", nullable = false, length = 500)
    private String customerAddress;
    
    /**
     * 客户电话
     */
    @NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;
    
    /**
     * 客户省份
     */
    @NotBlank(message = "客户省份不能为空")
    @Column(name = "customer_province", nullable = false, length = 50)
    private String customerProvince;
    
    /**
     * 支付方式
     */
    @Column(name = "pay_method", length = 100)
    private String payMethod;
    
    /**
     * 是否已打款
     */
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    /**
     * 打款截图URL
     * 存储图片的URL地址，支持本地文件或云存储
     * 格式示例：https://your-domain.com/uploads/payment/abc123.jpg
     */
    @Column(name = "payment_screenshot_url", length = 500)
    private String paymentScreenshotUrl;

    /**
     * 是否有效
     * false表示无效，true表示有效
     * 默认无效，需后期更改为有效
     */
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = false;
    
    /**
     * 创建时间 - 自动设置，不可更新
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间 - 自动更新
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

