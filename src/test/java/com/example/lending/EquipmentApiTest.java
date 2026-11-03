package com.example.lending;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestClockConfig.class)
@Transactional
class EquipmentApiTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 備品一覧を取得できる() throws Exception {
        mockMvc.perform(get("/api/equipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].name").value("ノートPC A"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    void カテゴリで絞り込める() throws Exception {
        mockMvc.perform(get("/api/equipments").param("category", "PC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("ノートPC A"))
                .andExpect(jsonPath("$[1].name").value("ノートPC B"));
    }

    @Test
    void 存在しないカテゴリを指定すると空配列が返る() throws Exception {
        mockMvc.perform(get("/api/equipments").param("category", "UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void 存在しない備品は404() throws Exception {
        mockMvc.perform(get("/api/equipments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void 貸出APIは201を返す() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"equipmentId": 1, "borrower": "tanaka", "days": 7}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dueOn").value("2026-10-22"));
    }

    @Test
    void 貸出中の備品を借りようとすると409() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"equipmentId": 4, "borrower": "tanaka", "days": 7}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("NOT_AVAILABLE"));
    }

    @Test
    void 入力不備は400() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"equipmentId": 1, "days": 7}
                                """))
                .andExpect(status().isBadRequest());
    }
}
