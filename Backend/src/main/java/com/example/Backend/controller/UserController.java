package com.example.Backend.controller;

import com.example.Backend.entity.User;
import com.example.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户管理控制器
 * 提供用户CRUD操作和表管理功能
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // ========== 用户CRUD操作 ==========
    
    /**
     * 创建用户
     */
    @PostMapping
    public Map<String, Object> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "用户创建成功");
            result.put("data", savedUser);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "用户创建失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public Map<String, Object> getAllUsers() {
        try {
            List<User> users = userService.findAllUsers();
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", users);
            result.put("count", users.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取用户列表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findById(id);
            Map<String, Object> result = new HashMap<>();
            if (user.isPresent()) {
                result.put("status", "success");
                result.put("data", user.get());
            } else {
                result.put("status", "error");
                result.put("message", "用户不存在，ID: " + id);
            }
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取用户失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 根据用户名获取用户
     */
    @GetMapping("/username/{username}")
    public Map<String, Object> getUserByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.findByUsername(username);
            Map<String, Object> result = new HashMap<>();
            if (user.isPresent()) {
                result.put("status", "success");
                result.put("data", user.get());
            } else {
                result.put("status", "error");
                result.put("message", "用户不存在，用户名: " + username);
            }
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取用户失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id); // 确保ID一致
            User updatedUser = userService.updateUser(user);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "用户更新成功");
            result.put("data", updatedUser);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "用户更新失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "用户删除成功");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "用户删除失败: " + e.getMessage());
            return result;
        }
    }
    
    // ========== 用户表管理操作 ==========
    
    /**
     * 检查用户表是否存在
     */
    @GetMapping("/{id}/tables/check")
    public Map<String, Object> checkUserTables(@PathVariable Long id) {
        try {
            boolean exists = userService.checkUserTablesExist(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("userId", id);
            result.put("tablesExist", exists);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "检查用户表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 重新创建用户表
     */
    @PostMapping("/{id}/tables/recreate")
    public Map<String, Object> recreateUserTables(@PathVariable Long id) {
        try {
            userService.recreateUserTables(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "用户表重新创建成功");
            result.put("userId", id);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "重新创建用户表失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取用户表名列表
     */
    @GetMapping("/{id}/tables")
    public Map<String, Object> getUserTableNames(@PathVariable Long id) {
        try {
            List<String> tableNames = userService.getUserTableNames(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("userId", id);
            result.put("tableNames", tableNames);
            result.put("count", tableNames.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "获取用户表名失败: " + e.getMessage());
            return result;
        }
    }
}
