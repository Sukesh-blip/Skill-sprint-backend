package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

@RestController
public class HomeController {

  @GetMapping("/")
  public Map<String, Object> getHomeInfo() {
    Map<String, Object> info = new HashMap<>();
    info.put("description",
        "A high-performance platform for developers to practice coding challenges and track their progress.");
    info.put("features", Arrays.asList(
        "Real-time submissions",
        "Role-based access",
        "Solved tracking",
        "Global Leaderboard (Upcoming)"));
    info.put("slogan", "Race to the top of the leaderboard!");
    return info;
  }
}
