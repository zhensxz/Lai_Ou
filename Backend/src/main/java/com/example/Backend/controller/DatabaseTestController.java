package com.example.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库测试控制器
 * 用于测试本地MySQL数据库连接和基本功能
 */
@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    /**
     * 测试本地MySQL数据库连接
     */
    @GetMapping("/database")
    public Map<String, Object> testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            result.put("status", "success");
            result.put("message", "本地MySQL数据库连接成功");
            result.put("database", connection.getMetaData().getDatabaseProductName());
            result.put("version", connection.getMetaData().getDatabaseProductVersion());
            result.put("url", connection.getMetaData().getURL());
            result.put("driver", connection.getMetaData().getDriverName());
            result.put("driverVersion", connection.getMetaData().getDriverVersion());
            
            // 测试基本查询
            try (var stmt = connection.createStatement();
                 var rs = stmt.executeQuery("SELECT 1 as test")) {
                if (rs.next()) {
                    result.put("queryTest", "成功");
                }
            }
            
        } catch (SQLException e) {
            result.put("status", "error");
            result.put("message", "本地MySQL数据库连接失败: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
            result.put("sqlState", e.getSQLState());
            result.put("errorCode", e.getErrorCode());
        }
        
        return result;
    }

    /**
     * 应用健康检查端点
     */
    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "Backend Application");
        result.put("database", "Local MySQL Database");
        return result;
    }
    
    /**
     * 数据库详细信息
     */
    @GetMapping("/database-info")
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("databaseType", "MySQL");
        result.put("location", "Local");
        result.put("features", new String[]{
            "本地开发", "快速部署", "易于调试", "成本低廉"
        });
        return result;
    }
}
