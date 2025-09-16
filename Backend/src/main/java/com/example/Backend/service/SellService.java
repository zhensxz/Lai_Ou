package com.example.Backend.service;

import com.example.Backend.entity.Sell;
import com.example.Backend.repository.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.example.Backend.util.AuthContext;

/**
 * 报单业务逻辑服务类
 */
@Service
@Transactional
public class SellService {
    
    @Autowired
    private SellRepository sellRepository;
    
    public Sell createSell(String sellerName, LocalDate sellDate, String productName,
                        Integer productQuantity, BigDecimal productPrice, BigDecimal totalPrice,
                        String customerCompany, String customerName, String customerAddress,
                        String customerPhone, String customerProvince, String sellKind, String paymentScreenshotUrl, String productSpec, String payMethod) {

        if (productSpec == null || !productSpec.matches("^(支|盒)$")) {
            throw new RuntimeException("产品规格只能为\"支\"或\"盒\"");
        }

        Sell sell = new Sell();
        sell.setSellerName(sellerName);
        sell.setSellDate(sellDate);
        sell.setProductName(productName);
        sell.setProductQuantity(productQuantity);
        sell.setProductPrice(productPrice);
        sell.setTotalPrice(totalPrice);
        sell.setCustomerCompany(customerCompany);
        sell.setCustomerName(customerName);
        sell.setCustomerAddress(customerAddress);
        sell.setCustomerPhone(customerPhone);
        sell.setCustomerProvince(customerProvince);
        sell.setSellKind(sellKind);
        sell.setPaymentScreenshotUrl(paymentScreenshotUrl);
        sell.setProductSpec(productSpec);
        sell.setPayMethod(payMethod);  // 添加支付方式
        sell.setIsPaid(false);
        sell.setIsValid(false);
        return sellRepository.save(sell);
    }
    
    /**
     * 根据ID查找报单
     */
    public Optional<Sell> findById(Long id) {
        return sellRepository.findById(id);
    }
    
    public Sell updateSell(Long id, String sellerName, LocalDate sellDate, String productName,
                        Integer productQuantity, BigDecimal productPrice, BigDecimal totalPrice,
                        String customerCompany, String customerName, String customerAddress,
                        String customerPhone, String customerProvince, String sellKind, String paymentScreenshotUrl, String productSpec, String payMethod) {

        Sell sell = sellRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("报单不存在"));

        if (sell.getIsValid()) {
            throw new RuntimeException("已审核的报单不能修改");
        }

        // RBAC: EMPLOYEE 只能修改自己创建的报单
        String role = AuthContext.getRole();
        String currentUsername = AuthContext.getUsername();
        if ("EMPLOYEE".equals(role)) {
            if (currentUsername == null || !currentUsername.equals(sell.getSellerName())) {
                throw new RuntimeException("无权修改该报单");
            }
        }

        if (productSpec == null || !productSpec.matches("^(支|盒)$")) {
            throw new RuntimeException("产品规格只能为\"支\"或\"盒\"");
        }

        sell.setSellerName(sellerName);
        sell.setSellDate(sellDate);
        sell.setProductName(productName);
        sell.setProductQuantity(productQuantity);
        sell.setProductPrice(productPrice);
        sell.setTotalPrice(totalPrice);
        sell.setCustomerCompany(customerCompany);
        sell.setCustomerName(customerName);
        sell.setCustomerAddress(customerAddress);
        sell.setCustomerPhone(customerPhone);
        sell.setCustomerProvince(customerProvince);
        sell.setSellKind(sellKind);
        sell.setPaymentScreenshotUrl(paymentScreenshotUrl);
        sell.setProductSpec(productSpec);
        sell.setPayMethod(payMethod);  // 添加支付方式
        return sellRepository.save(sell);
    }
    
    /**
     * 更新报单打款状态
     */
    public Sell updatePaymentStatus(Long id, Boolean isPaid) {
        Sell sell = sellRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("报单不存在"));
        
        sell.setIsPaid(isPaid);
        return sellRepository.save(sell);
    }
    
    /**
     * 更新报单有效性状态
     */
    public Sell updateValidityStatus(Long id, Boolean isValid) {
        Sell sell = sellRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("报单不存在"));
        
        sell.setIsValid(isValid);
        return sellRepository.save(sell);
    }
    
    /**
     * 审核报单（设置为有效）
     */
    public Sell approveSell(Long id) {
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权审核报单");
        }
        return updateValidityStatus(id, true);
    }
    
    /**
     * 拒绝报单（设置为无效）
     */
    public Sell rejectSell(Long id) {
        String role = AuthContext.getRole();
        if ("EMPLOYEE".equals(role)) {
            throw new RuntimeException("无权审核报单");
        }
        return updateValidityStatus(id, false);
    }
    
    /**
     * 标记为已打款
     */
    public Sell markAsPaid(Long id) {
        String role = AuthContext.getRole();
        if (!"BOSS".equals(role)) {
            throw new RuntimeException("无权标记打款");
        }
        return updatePaymentStatus(id, true);
    }
    
    /**
     * 标记为未打款
     */
    public Sell markAsUnpaid(Long id) {
        String role = AuthContext.getRole();
        if (!"BOSS".equals(role)) {
            throw new RuntimeException("无权标记打款");
        }
        return updatePaymentStatus(id, false);
    }
    
    /**
     * 删除报单
     */
    public void deleteSell(Long id) {
        if (!sellRepository.existsById(id)) {
            throw new RuntimeException("报单不存在");
        }
        Sell sell = sellRepository.findById(id).orElseThrow();
        if (sell.getIsValid()) {
            throw new RuntimeException("已审核的报单不能删除");
        }
        String role = AuthContext.getRole();
        String currentUsername = AuthContext.getUsername();
        if ("EMPLOYEE".equals(role)) {
            if (currentUsername == null || !currentUsername.equals(sell.getSellerName())) {
                throw new RuntimeException("无权删除该报单");
            }
        }
        sellRepository.deleteById(id);
    }
    
    /**
     * 获取所有报单
     */
    public List<Sell> getAllSells() {
        String role = AuthContext.getRole();
        String currentUsername = AuthContext.getUsername();
        if ("EMPLOYEE".equals(role) && currentUsername != null) {
            return sellRepository.findBySellerNameContaining(currentUsername);
        }
        return sellRepository.findAll();
    }
    
    /**
     * 根据报单人姓名查询
     */
    public List<Sell> findBySellerName(String sellerName) {
        return sellRepository.findBySellerNameContaining(sellerName);
    }
    
    /**
     * 根据报单日期查询
     */
    public List<Sell> findBySellDate(LocalDate sellDate) {
        return sellRepository.findBySellDate(sellDate);
    }
    
    /**
     * 根据报单日期范围查询
     */
    public List<Sell> findBySellDateRange(LocalDate startDate, LocalDate endDate) {
        return sellRepository.findBySellDateBetween(startDate, endDate);
    }
    
    /**
     * 根据产品名称查询
     */
    public List<Sell> findByProductName(String productName) {
        return sellRepository.findByProductNameContaining(productName);
    }
    
    /**
     * 根据客户姓名查询
     */
    public List<Sell> findByCustomerName(String customerName) {
        return sellRepository.findByCustomerNameContaining(customerName);
    }
    
    /**
     * 根据客户公司查询
     */
    public List<Sell> findByCustomerCompany(String customerCompany) {
        return sellRepository.findByCustomerCompanyContaining(customerCompany);
    }
    
    /**
     * 根据客户省份查询
     */
    public List<Sell> findByCustomerProvince(String customerProvince) {
        return sellRepository.findByCustomerProvince(customerProvince);
    }
    
    /**
     * 查询已打款的报单
     */
    public List<Sell> getPaidSells() {
        return sellRepository.findByIsPaidTrue();
    }
    
    /**
     * 查询未打款的报单
     */
    public List<Sell> getUnpaidSells() {
        return sellRepository.findByIsPaidFalse();
    }
    
    /**
     * 查询有效的报单
     */
    public List<Sell> getValidSells() {
        return sellRepository.findByIsValidTrue();
    }
    
    /**
     * 查询无效的报单
     */
    public List<Sell> getInvalidSells() {
        return sellRepository.findByIsValidFalse();
    }
    
    /**
     * 根据总价格范围查询
     */
    public List<Sell> findByTotalPriceRange(Double minPrice, Double maxPrice) {
        return sellRepository.findByTotalPriceBetween(minPrice, maxPrice);
    }
    
    /**
     * 根据报单类型模糊查询
     */
    public List<Sell> findBySellKindContaining(String sellKind) {
        return sellRepository.findBySellKindContaining(sellKind);
    }
    
    /**
     * 根据支付方式模糊查询
     */
    public List<Sell> findByPayMethodContaining(String payMethod) {
        return sellRepository.findByPayMethodContaining(payMethod);
    }
    
    /**
     * 多字段综合查询
     */
    public List<Sell> searchSells(String sellerName, String productName, String productSpec,
                                 String customerName, String customerCompany, String customerProvince,
                                 String sellKind, String payMethod, LocalDate startDate, LocalDate endDate,
                                 BigDecimal minTotalPrice, BigDecimal maxTotalPrice,
                                 Boolean isPaid, Boolean isValid) {
        return sellRepository.searchSells(sellerName, productName, productSpec,
                                        customerName, customerCompany, customerProvince, sellKind, payMethod,
                                        startDate, endDate, minTotalPrice, maxTotalPrice,
                                        isPaid, isValid);
    }
    
    /**
     * 检查报单是否存在
     */
    public boolean existsById(Long id) {
        return sellRepository.existsById(id);
    }
    
    /**
     * 获取报单总数
     */
    public long getSellCount() {
        return sellRepository.count();
    }
}