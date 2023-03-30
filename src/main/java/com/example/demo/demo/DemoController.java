package com.example.demo.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

  @GetMapping
  public ResponseEntity<String> sayHello() {



    return ResponseEntity.ok("xxx");
  }

  @GetMapping("/test")
  public ResponseEntity<String> x() {
    return ResponseEntity.ok("xxx");
  }
}
