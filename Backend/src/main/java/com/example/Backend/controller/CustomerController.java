package com.example.Backend.controller;

import com.example.Backend.entity.Customer;
import com.example.Backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 客户管理控制器
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * 创建客户
     */
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(
                customer.getCustomerName(), customer.getPhone(), customer.getAddress(),
                customer.getCompany(), customer.getProvince()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据ID获取客户
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            Optional<Customer> customer = customerService.findById(id);
            if (customer.isPresent()) {
                return ResponseEntity.ok(customer.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据客户姓名搜索
     */
    @GetMapping("/search/name")
    public ResponseEntity<?> searchCustomersByName(@RequestParam String customerName) {
        try {
            List<Customer> customers = customerService.findByCustomerName(customerName);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    /**
     * 根据公司搜索
     */
    @GetMapping("/search/company")
    public ResponseEntity<?> searchCustomersByCompany(@RequestParam String company) {
        try {
            List<Customer> customers = customerService.findByCompany(company);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 根据省份搜索
     */
    @GetMapping("/search/province")
    public ResponseEntity<?> searchCustomersByProvince(@RequestParam String province) {
        try {
            List<Customer> customers = customerService.findByProvince(province);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 多字段综合搜索
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchCustomers(@RequestParam(required = false) String customerName,
                                           @RequestParam(required = false) String address,
                                           @RequestParam(required = false) String company,
                                           @RequestParam(required = false) String province) {
        try {
            List<Customer> customers = customerService.searchCustomers(customerName, address, 
                                                                      company, province);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 更新客户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(
                id, customer.getCustomerName(), customer.getPhone(), customer.getAddress(),
                customer.getCompany(), customer.getProvince()
            );
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok("客户删除成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取所有客户
     */
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * 获取客户总数
     */
    @GetMapping("/count")
    public ResponseEntity<?> getCustomerCount() {
        try {
            long count = customerService.getCustomerCount();
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}