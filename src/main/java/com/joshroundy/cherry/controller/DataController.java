package com.joshroundy.cherry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

    @GetMapping("/user")
    public String getUser() {
        return null;
    }
    @GetMapping("/date")
    public String getDate() {
        return null;
    }
    @GetMapping("/meal")
    public String getMeal() {
        return null;
    }
    @GetMapping("/meal-item")
    public String getMealItem() {
        return null;
    }

    @PostMapping("/date")
    public String setDate() {
        return null;
    }
    @PostMapping("/meal")
    public String setMeal() {
        return null;
    }
    @PostMapping("/meal-item")
    public String setMealItem() {
        return null;
    }

}
