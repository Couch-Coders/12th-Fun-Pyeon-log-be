package com.example.demo.controller;

import com.example.demo.entity.KeywordContent;
import com.example.demo.entity.StoreKeyword;
import com.example.demo.entity.StoreSummary;
import com.example.demo.repository.StoreSummaryRepository;
import com.example.demo.service.StoreService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StoreController.class)
class StoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StoreService storeService;

    @MockBean
    StoreSummaryRepository storeSummaryRepository;

    @BeforeEach
    public void before(){
        StoreSummary storeSummary = StoreSummary.builder()
                .storeId("store-1")
                .starRate(4.0)
                .reviewCount(203l)
                .storeKeywords(new ArrayList<>())
                .build();
        storeSummaryRepository.save(storeSummary);
    }

    @Test
    @Transactional
    public void storeSummary() throws Exception {
        mockMvc.perform(get("/stores/store-1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}