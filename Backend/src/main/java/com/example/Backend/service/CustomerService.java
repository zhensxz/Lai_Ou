package com.example.Backend.service;

import com.example.Backend.entity.Customer;
import com.example.Backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Backend.util.AuthContext;
import com.example.Backend.entity.User;
import com.example.Backend.entity.UserCustomerRelation;
import com.example.Backend.repository.UserCustomerRelationRepository;
import com.example.Backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * 客户业务逻辑服务类
 */
@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserCustomerRelationService userCustomerRelationService;

    @Autowired
    private UserCustomerRelationRepository userCustomerRelationRepository;

    @Autowired
    private UserRepository userRepository;
    
    /**
     * 创建客户
     */
    public Customer createCustomer(String customerName, String phone, String address,
                                 String company, String province) {
        
        // 创建客户对象
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCompany(company);
        customer.setProvince(province);
        
        // 保存客户
        Customer saved = customerRepository.save(customer);

        // 如果当前是 EMPLOYEE 或 AUDITOR/BOSS，也可选择自动关联。按规则：EMPLOYEE 创建后自动关联到自己
        String role = AuthContext.getRole();
        Long uid = AuthContext.getUserId();
        if (uid != null && "EMPLOYEE".equals(role)) {
            User user = userRepository.findById(uid).orElse(null);
            if (user != null) {
                UserCustomerRelation rel = new UserCustomerRelation();
                rel.setUser(user);
                rel.setCustomer(saved);
                userCustomerRelationRepository.save(rel);
            }
        }
        return saved;
    }
    
    /**
     * 根据ID查找客户
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    /**
     * 根据客户姓名查找客户
     */
    public List<Customer> findByCustomerName(String customerName) {
        return customerRepository.findByCustomerNameContaining(customerName);
    }
    
    
    /**
     * 根据公司名称查找客户
     */
    public List<Customer> findByCompany(String company) {
        return customerRepository.findByCompanyContaining(company);
    }
    
    /**
     * 根据省份查找客户
     */
    public List<Customer> findByProvince(String province) {
        return customerRepository.findByProvince(province);
    }
    
    
    /**
     * 更新客户信息
     */
    public Customer updateCustomer(Long id, String customerName, String phone, String address,
                                 String company, String province) {
        
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("客户不存在"));
        
        // 权限：EMPLOYEE 仅可更新与自己关联的客户
        String role = AuthContext.getRole();
        Long uid = AuthContext.getUserId();
        if ("EMPLOYEE".equals(role)) {
            if (uid == null || !userCustomerRelationRepository.existsByUserIdAndCustomerId(uid, id)) {
                throw new RuntimeException("无权操作该客户");
            }
        }

        // 更新客户信息
        customer.setCustomerName(customerName);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCompany(company);
        customer.setProvince(province);
        
        return customerRepository.save(customer);
    }
    
    /**
     * 更新客户姓名和电话（部分更新）
     */
    public Customer updateCustomerBasicInfo(Long id, String customerName, String phone) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("客户不存在"));
        
        // 只更新客户姓名和电话
        customer.setCustomerName(customerName);
        customer.setPhone(phone);
        
        return customerRepository.save(customer);
    }
    
    /**
     * 删除客户
     */
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("客户不存在");
        }
        
        // 权限：EMPLOYEE 仅可删除与自己关联的客户
        String role = AuthContext.getRole();
        Long uid = AuthContext.getUserId();
        if ("EMPLOYEE".equals(role)) {
            if (uid == null || !userCustomerRelationRepository.existsByUserIdAndCustomerId(uid, id)) {
                throw new RuntimeException("无权删除该客户");
            }
        }

        // 先删除该客户的所有用户关联关系
        userCustomerRelationService.deleteCustomerUserRelations(id);
        
        // 再删除客户本身
        customerRepository.deleteById(id);
    }
    
    /**
     * 获取所有客户
     */
    public List<Customer> getAllCustomers() {
        String role = AuthContext.getRole();
        Long uid = AuthContext.getUserId();
        if ("EMPLOYEE".equals(role) && uid != null) {
            // 仅返回与自己关联的客户
            return userCustomerRelationService.getUserCustomers(
                userRepository.findById(uid).map(User::getUsername).orElse("")
            );
        }
        return customerRepository.findAll();
    }
    
    /**
     * 多字段综合查询
     */
    public List<Customer> searchCustomers(String customerName, String address,
                                        String company, String province) {
        return customerRepository.searchCustomers(customerName, address, company, province);
    }
    
    /**
     * 根据客户姓名精确查询
     */
    public Optional<Customer> findCustomerByName(String customerName) {
        return customerRepository.findByCustomerNameContaining(customerName)
            .stream()
            .filter(customer -> customer.getCustomerName().equals(customerName))
            .findFirst();
    }
    
    /**
     * 检查客户是否存在
     */
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }
    
    
    /**
     * 获取客户总数
     */
    public long getCustomerCount() {
        return customerRepository.count();
    }
    
    /**
     * 根据省份统计客户数量
     */
    public long countByProvince(String province) {
        return customerRepository.findByProvince(province).size();
    }
    
    
    /**
     * 获取所有省份列表
     */
    public List<String> getAllProvinces() {
        return customerRepository.findAll()
            .stream()
            .map(Customer::getProvince)
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
    }
    
}