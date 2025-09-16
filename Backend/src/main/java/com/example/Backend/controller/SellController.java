package com.example.Backend.controller;

import com.example.Backend.entity.Sell;
import com.example.Backend.service.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 报单管理控制器
 */
@RestController
@RequestMapping("/api/sells")
@CrossOrigin(origins = "*")
public class SellController {
    
    @Autowired
    private SellService sellService;
    
    /**
     * 创建报单
     */
    @PostMapping
    public ResponseEntity<?> createSell(@Valid @RequestBody Sell sell) {
        try {
            Sell createdSell = sellService.createSell(
                sell.getSellerName(), sell.getSellDate(), sell.getProductName(),
                sell.getProductQuantity(), sell.getProductPrice(), sell.getTotalPrice(),
                sell.getCustomerCompany(), sell.getCustomerName(), sell.getCustomerAddress(),
                sell.getCustomerPhone(), sell.getCustomerProvince(), sell.getSellKind(), 
                sell.getPaymentScreenshotUrl(), sell.getProductSpec(), sell.getPayMethod()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据ID获取报单
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSellById(@PathVariable Long id) {
        try {
            Optional<Sell> sell = sellService.findById(id);
            if (sell.isPresent()) {
                return ResponseEntity.ok(sell.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 更新报单信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSell(@PathVariable Long id, @Valid @RequestBody Sell sell) {
        try {
            Sell updatedSell = sellService.updateSell(
                id, sell.getSellerName(), sell.getSellDate(), sell.getProductName(),
                sell.getProductQuantity(), sell.getProductPrice(), sell.getTotalPrice(),
                sell.getCustomerCompany(), sell.getCustomerName(), sell.getCustomerAddress(),
                sell.getCustomerPhone(), sell.getCustomerProvince(), sell.getSellKind(), 
                sell.getPaymentScreenshotUrl(), sell.getProductSpec(), sell.getPayMethod()
            );
            return ResponseEntity.ok(updatedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 更新报单打款状态
     */
    @PutMapping("/{id}/payment")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id, @RequestParam Boolean isPaid) {
        try {
            Sell updatedSell = sellService.updatePaymentStatus(id, isPaid);
            return ResponseEntity.ok(updatedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 更新报单有效性状态
     */
    @PutMapping("/{id}/validity")
    public ResponseEntity<?> updateValidityStatus(@PathVariable Long id, @RequestParam Boolean isValid) {
        try {
            Sell updatedSell = sellService.updateValidityStatus(id, isValid);
            return ResponseEntity.ok(updatedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 审核报单（设置为有效）
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveSell(@PathVariable Long id) {
        try {
            Sell approvedSell = sellService.approveSell(id);
            return ResponseEntity.ok(approvedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 拒绝报单（设置为无效）
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectSell(@PathVariable Long id) {
        try {
            Sell rejectedSell = sellService.rejectSell(id);
            return ResponseEntity.ok(rejectedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 标记为已打款
     */
    @PutMapping("/{id}/mark-paid")
    public ResponseEntity<?> markAsPaid(@PathVariable Long id) {
        try {
            Sell updatedSell = sellService.markAsPaid(id);
            return ResponseEntity.ok(updatedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 标记为未打款
     */
    @PutMapping("/{id}/mark-unpaid")
    public ResponseEntity<?> markAsUnpaid(@PathVariable Long id) {
        try {
            Sell updatedSell = sellService.markAsUnpaid(id);
            return ResponseEntity.ok(updatedSell);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 删除报单
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSell(@PathVariable Long id) {
        try {
            sellService.deleteSell(id);
            return ResponseEntity.ok("报单删除成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取所有报单
     */
    @GetMapping
    public ResponseEntity<?> getAllSells() {
        try {
            List<Sell> sells = sellService.getAllSells();
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据报单人姓名查询
     */
    @GetMapping("/search/seller")
    public ResponseEntity<?> findBySellerName(@RequestParam String sellerName) {
        try {
            List<Sell> sells = sellService.findBySellerName(sellerName);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据报单日期查询
     */
    @GetMapping("/search/date")
    public ResponseEntity<?> findBySellDate(@RequestParam String sellDate) {
        try {
            LocalDate date = LocalDate.parse(sellDate);
            List<Sell> sells = sellService.findBySellDate(date);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据报单日期范围查询
     */
    @GetMapping("/search/date-range")
    public ResponseEntity<?> findBySellDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Sell> sells = sellService.findBySellDateRange(start, end);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据产品名称查询
     */
    @GetMapping("/search/product")
    public ResponseEntity<?> findByProductName(@RequestParam String productName) {
        try {
            List<Sell> sells = sellService.findByProductName(productName);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据客户姓名查询
     */
    @GetMapping("/search/customer-name")
    public ResponseEntity<?> findByCustomerName(@RequestParam String customerName) {
        try {
            List<Sell> sells = sellService.findByCustomerName(customerName);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据客户公司查询
     */
    @GetMapping("/search/customer-company")
    public ResponseEntity<?> findByCustomerCompany(@RequestParam String customerCompany) {
        try {
            List<Sell> sells = sellService.findByCustomerCompany(customerCompany);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据客户省份查询
     */
    @GetMapping("/search/customer-province")
    public ResponseEntity<?> findByCustomerProvince(@RequestParam String customerProvince) {
        try {
            List<Sell> sells = sellService.findByCustomerProvince(customerProvince);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据报单类型模糊查询
     */
    @GetMapping("/search/sell-kind")
    public ResponseEntity<?> findBySellKindContaining(@RequestParam String sellKind) {
        try {
            List<Sell> sells = sellService.findBySellKindContaining(sellKind);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据支付方式模糊查询
     */
    @GetMapping("/search/pay-method")
    public ResponseEntity<?> findByPayMethodContaining(@RequestParam String payMethod) {
        try {
            List<Sell> sells = sellService.findByPayMethodContaining(payMethod);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 查询已打款的报单
     */
    @GetMapping("/paid")
    public ResponseEntity<?> getPaidSells() {
        try {
            List<Sell> sells = sellService.getPaidSells();
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 查询未打款的报单
     */
    @GetMapping("/unpaid")
    public ResponseEntity<?> getUnpaidSells() {
        try {
            List<Sell> sells = sellService.getUnpaidSells();
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 查询有效的报单
     */
    @GetMapping("/valid")
    public ResponseEntity<?> getValidSells() {
        try {
            List<Sell> sells = sellService.getValidSells();
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 查询无效的报单
     */
    @GetMapping("/invalid")
    public ResponseEntity<?> getInvalidSells() {
        try {
            List<Sell> sells = sellService.getInvalidSells();
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据总价格范围查询
     */
    @GetMapping("/search/price-range")
    public ResponseEntity<?> findByTotalPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        try {
            List<Sell> sells = sellService.findByTotalPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 多字段综合查询
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchSells(@RequestParam(required = false) String sellerName,
                                    @RequestParam(required = false) String productName,
                                    @RequestParam(required = false) String productSpec,  // 新增
                                    @RequestParam(required = false) String customerName,
                                    @RequestParam(required = false) String customerCompany,
                                    @RequestParam(required = false) String customerProvince,
                                    @RequestParam(required = false) String sellKind,  // 新增
                                    @RequestParam(required = false) String payMethod,  // 新增
                                    @RequestParam(required = false) String startDate,
                                    @RequestParam(required = false) String endDate,
                                    @RequestParam(required = false) Double minTotalPrice,
                                    @RequestParam(required = false) Double maxTotalPrice,
                                    @RequestParam(required = false) Boolean isPaid,
                                    @RequestParam(required = false) Boolean isValid) {
        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
            
            // 转换Double为BigDecimal
            BigDecimal minPrice = minTotalPrice != null ? BigDecimal.valueOf(minTotalPrice) : null;
            BigDecimal maxPrice = maxTotalPrice != null ? BigDecimal.valueOf(maxTotalPrice) : null;
            
            List<Sell> sells = sellService.searchSells(sellerName, productName, productSpec,  // 新增
                                                    customerName, customerCompany, customerProvince, sellKind, payMethod,
                                                    start, end, minPrice, maxPrice,
                                                    isPaid, isValid);
            return ResponseEntity.ok(sells);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取报单总数
     */
    @GetMapping("/count")
    public ResponseEntity<?> getSellCount() {
        try {
            long count = sellService.getSellCount();
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}