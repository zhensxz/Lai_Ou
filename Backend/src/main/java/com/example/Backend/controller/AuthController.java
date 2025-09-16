package com.example.Backend.controller;

import com.example.Backend.entity.User;
import com.example.Backend.entity.UserRole;
import com.example.Backend.service.UserService;
import com.example.Backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
        User user = userOpt.get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("role", user.getRole().name());
        String token = jwtUtil.generateToken(user.getUsername(), claims);
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        Map<String, Object> u = new HashMap<>();
        u.put("id", user.getId());
        u.put("username", user.getUsername());
        u.put("role", user.getRole());
        resp.put("user", u);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("未携带令牌");
        }
        String token = authHeader.substring(7);
        try {
            Claims c = jwtUtil.parse(token);
            Map<String, Object> resp = new HashMap<>();
            resp.put("username", c.getSubject());
            resp.put("uid", c.get("uid"));
            resp.put("role", c.get("role"));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("令牌无效");
        }
    }
}


