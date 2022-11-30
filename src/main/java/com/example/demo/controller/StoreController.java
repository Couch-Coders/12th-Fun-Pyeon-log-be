package com.example.demo.controller;

import com.example.demo.dto.StoreDTO;
import com.example.demo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    StoreService storeService;

    @GetMapping("")
    public List<StoreDTO> storeSummaries(@RequestParam(value = "id", required = false) String[] storeIds){
        return storeService.getStoreSummaries(storeIds);
    }

}
