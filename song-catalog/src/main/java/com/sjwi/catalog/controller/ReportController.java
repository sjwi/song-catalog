/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.model.LogEntry;
import com.sjwi.catalog.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

  @Autowired UserService userService;

  @GetMapping("/log")
  public ResponseEntity<LogData> structuredLogs(@RequestParam(required = false) Integer limit) {
    return ResponseEntity.ok(new LogData(userService.getLogData(limit == null ? 1000 : limit)));
  }

  @AllArgsConstructor
  @Data
  public class LogData {
    List<LogEntry> data;
  }

  @GetMapping("/user-report")
  public ResponseEntity<List<String>> displayUserReport() {
    return ResponseEntity.ok(
        userService.getAllActiveUsers().stream()
            .map(u -> u.getUsername())
            .collect(Collectors.toList()));
  }
}
