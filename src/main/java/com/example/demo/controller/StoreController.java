package com.example.demo.controller;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    StoreService storeService;

    @GetMapping("")
    public List<StoreSummaryDTO> storeSummaries(@RequestParam(value = "id", required = false) String[] storeIds){
        return storeService.getStoreSummaryDTOS(storeIds);
    }

    @GetMapping("/{storeId}")
    public StoreSummaryDTO storeSummary(@PathVariable String storeId){
        return storeService.getStoreSummaryDTO(storeId);
    }

}
