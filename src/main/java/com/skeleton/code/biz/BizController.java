package com.skeleton.code.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/biz")
@RequiredArgsConstructor
@RestController
public class BizController {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user")
    public ResponseEntity<String> user() {
        return ResponseEntity.ok("user");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("admin");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    @GetMapping("/viewer")
    public ResponseEntity<String> viewer() {
        return ResponseEntity.ok("viewer");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/manager")
    public ResponseEntity<String> manager() {
        return ResponseEntity.ok("manager");
    }
}
