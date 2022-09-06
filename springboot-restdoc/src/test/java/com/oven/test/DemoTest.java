package com.oven.test;

import com.oven.controller.DemoController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
public class DemoTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/snippets");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("test", responseFields(
                        fieldWithPath("message").description("测试接口")
                )));
    }

    @Test
    public void getById() throws Exception {
        this.mockMvc.perform(get("/getById").param("id", "1"))
                .andExpect(status().isOk())
                .andDo(document("getById", requestParameters(
                        parameterWithName("id").description("用户主键")
                )));
    }

    @Test
    public void getAll() throws Exception {
        this.mockMvc.perform(post("/getAll")
                .param("uname", "admin")
                .param("age", "18"))
                .andExpect(status().isOk())
                .andDo(document("getAll", requestParameters(
                        parameterWithName("uname").description("用户名"),
                        parameterWithName("age").description("年龄"))));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(get("/delete/{id}", 1))
                .andExpect(status().isOk())
                .andDo(document("delete", pathParameters(
                        parameterWithName("id").description("用户主键")
                )));
    }

}
