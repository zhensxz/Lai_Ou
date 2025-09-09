package com.example.Backend.repository;

import com.example.Backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户数据访问接口
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * 根据客户姓名模糊查询
     */
    List<Customer> findByCustomerNameContaining(String customerName);
    
    /**
     * 根据电话查询客户
     */
    Optional<Customer> findByPhone(String phone);
    
    /**
     * 根据公司名称模糊查询
     */
    List<Customer> findByCompanyContaining(String company);
    
    /**
     * 根据省份查询客户
     */
    List<Customer> findByProvince(String province);
    
    /**
     * 多字段模糊查询
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "(:customerName IS NULL OR c.customerName LIKE %:customerName%) AND " +
           "(:phone IS NULL OR c.phone LIKE %:phone%) AND " +
           "(:address IS NULL OR c.address LIKE %:address%) AND " +
           "(:company IS NULL OR c.company LIKE %:company%) AND " +
           "(:province IS NULL OR c.province LIKE %:province%)")
    List<Customer> searchCustomers(@Param("customerName") String customerName,
                                  @Param("phone") String phone,
                                  @Param("address") String address,
                                  @Param("company") String company,
                                  @Param("province") String province);
    
    /**
     * 查询管理特定客户的所有用户
     */
    @Query("SELECT c.users FROM Customer c WHERE c.id = :customerId")
    List<Object> findUsersByCustomerId(@Param("customerId") Long customerId);
}