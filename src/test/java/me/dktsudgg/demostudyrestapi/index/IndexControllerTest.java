package me.dktsudgg.demostudyrestapi.index;

import me.dktsudgg.demostudyrestapi.common.RestDocsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 웹쪽 테스트는 주로 mocking없이 SpringBootTest애노테이션으로 통합테스트를 한다
 * mocking할게 너무 많아서 테스트코드 짜기가 힘들고 관리가 힘들어서..
 */

//@WebMvcTest // 이 애노테이션만 추가해서는 레포지토리는 주입을 못받음. 왜냐면 웹용 slicing test라서 웹용 빈들만 생성하고, 레포지토리를 빈으로 등록해주지 않음.. 레포지토리는 mocking하면됨
@ExtendWith(SpringExtension.class)
@SpringBootTest // 레포지토리도 mocking 안하고 바로 주입받아 쓸 수 있도록 이 애노테이션 사용
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists())
        ;
    }

}