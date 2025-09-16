package com.example.Backend.repository;

import com.example.Backend.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 报单数据访问接口
 */
@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {
    
    /**
     * 根据报单人姓名查询
     */
    List<Sell> findBySellerNameContaining(String sellerName);
    
    /**
     * 根据报单日期查询
     */
    List<Sell> findBySellDate(LocalDate sellDate);
    
    /**
     * 根据报单日期范围查询
     */
    List<Sell> findBySellDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据产品名称模糊查询
     */
    List<Sell> findByProductNameContaining(String productName);
    
    /**
     * 根据客户姓名模糊查询
     */
    List<Sell> findByCustomerNameContaining(String customerName);
    
    /**
     * 根据客户公司模糊查询
     */
    List<Sell> findByCustomerCompanyContaining(String customerCompany);

    /**
     * 根据客户公司名称精确查询
     */
    List<Sell> findByCustomerCompany(String customerCompany);

    /**
     * 根据客户省份查询
     */
    List<Sell> findByCustomerProvince(String customerProvince);
    
    /**
     * 根据报单类型模糊查询
     */
    List<Sell> findBySellKindContaining(String sellKind);
    
    /**
     * 根据支付方式模糊查询
     */
    List<Sell> findByPayMethodContaining(String payMethod);
    
    /**
     * 查询已打款的报单
     */
    List<Sell> findByIsPaidTrue();
    
    /**
     * 查询未打款的报单
     */
    List<Sell> findByIsPaidFalse();
    
    /**
     * 查询有效的报单
     */
    List<Sell> findByIsValidTrue();
    
    /**
     * 查询无效的报单
     */
    List<Sell> findByIsValidFalse();
    
       /**
        * 多字段综合查询（所有条件均为可选）
        *
        * 说明：
        * - 文本类字段使用模糊匹配（LIKE %xxx%）
        * - 日期使用闭区间范围筛选（sellDate 在 [startDate, endDate]）
        * - 金额使用闭区间范围筛选（totalPrice 在 [minTotalPrice, maxTotalPrice]）
        * - 布尔字段精确匹配（为 null 则不参与筛选）
        * - 任一入参为 null 时，该条件自动忽略，不影响其他条件
        *
        * 参数含义：
        * - sellerName：报单人名称（模糊）
        * - productName：产品名称（模糊）
        * - customerCompany：客户公司（模糊）
        * - customerName：客户姓名（模糊）
        * - customerPhone：客户电话（模糊）
        * - startDate：报单开始日期（含）
        * - endDate：报单结束日期（含）
        * - minTotalPrice：总价下限（含）
        * - maxTotalPrice：总价上限（含）
        * - isPaid：是否已打款（true/false，null忽略）
        * - isValid：是否有效（true/false，null忽略）
        *
        * 返回：
        * - 满足所有已传入条件的报单列表，按默认顺序返回
        */
    @Query("SELECT s FROM Sell s WHERE s.totalPrice BETWEEN :minPrice AND :maxPrice")
    List<Sell> findByTotalPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT s FROM Sell s WHERE " +
        "(:sellerName IS NULL OR s.sellerName LIKE %:sellerName%) AND " +
        "(:productName IS NULL OR s.productName LIKE %:productName%) AND " +
        "(:productSpec IS NULL OR s.productSpec = :productSpec) AND " +   // 新增
        "(:customerCompany IS NULL OR s.customerCompany LIKE %:customerCompany%) AND " +
        "(:customerName IS NULL OR s.customerName LIKE %:customerName%) AND " +
        "(:customerPhone IS NULL OR s.customerPhone LIKE %:customerPhone%) AND " +
        "(:sellKind IS NULL OR s.sellKind LIKE %:sellKind%) AND " +   // 新增
        "(:payMethod IS NULL OR s.payMethod LIKE %:payMethod%) AND " +   // 新增
        "(:startDate IS NULL OR s.sellDate >= :startDate) AND " +
        "(:endDate IS NULL OR s.sellDate <= :endDate) AND " +
        "(:minTotalPrice IS NULL OR s.totalPrice >= :minTotalPrice) AND " +
        "(:maxTotalPrice IS NULL OR s.totalPrice <= :maxTotalPrice) AND " +
        "(:isPaid IS NULL OR s.isPaid = :isPaid) AND " +
        "(:isValid IS NULL OR s.isValid = :isValid)")
    List<Sell> searchSells(@Param("sellerName") String sellerName,
                        @Param("productName") String productName,
                        @Param("productSpec") String productSpec,          // 新增
                        @Param("customerCompany") String customerCompany,
                        @Param("customerName") String customerName,
                        @Param("customerPhone") String customerPhone,
                        @Param("sellKind") String sellKind,                // 新增
                        @Param("payMethod") String payMethod,              // 新增
                        @Param("startDate") java.time.LocalDate startDate,
                        @Param("endDate") java.time.LocalDate endDate,
                        @Param("minTotalPrice") java.math.BigDecimal minTotalPrice,
                        @Param("maxTotalPrice") java.math.BigDecimal maxTotalPrice,
                        @Param("isPaid") Boolean isPaid,
                        @Param("isValid") Boolean isValid);
}