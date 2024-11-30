package com.oven.jsonpath;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Test implements CommandLineRunner {

    @Override
    public void run(String... args) {
        String str = "{\n" +
                "    \"store\": {\n" +
                "        \"book\": [\n" +
                "            {\n" +
                "                \"category\": \"reference\",\n" +
                "                \"author\": \"Nigel Rees\",\n" +
                "                \"title\": \"Sayings of the Century\",\n" +
                "                \"price\": 8.95\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"Evelyn Waugh\",\n" +
                "                \"title\": \"Sword of Honour\",\n" +
                "                \"price\": 12.99\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"Herman Melville\",\n" +
                "                \"title\": \"Moby Dick\",\n" +
                "                \"isbn\": \"0-553-21311-3\",\n" +
                "                \"price\": 8.99\n" +
                "            },\n" +
                "            {\n" +
                "                \"category\": \"fiction\",\n" +
                "                \"author\": \"J. R. R. Tolkien\",\n" +
                "                \"title\": \"The Lord of the Rings\",\n" +
                "                \"isbn\": \"0-395-19395-8\",\n" +
                "                \"price\": 22.99\n" +
                "            }\n" +
                "        ],\n" +
                "        \"bicycle\": {\n" +
                "            \"color\": \"red\",\n" +
                "            \"price\": 19.95\n" +
                "        }\n" +
                "    },\n" +
                "    \"expensive\": 10\n" +
                "}";
        List<String> authors = JsonPath.read(str, "$.store.book[*].author");
        log.info("所有book节点的author字段信息：{}", authors);
        String title = JsonPath.read(str, "$['store']['book'][0]['title']");
        log.info("第一本书的title字段111：{}", title);
        String title2 = JsonPath.read(str, "$.store.book[0].title");
        log.info("第一本书的title字段222：{}", title2);
        Integer number = JsonPath.read(str, "$.store.book.length()");
        log.info("所有book的数量：{}", number);
        List<Map<String, Object>> expensiveBooks = JsonPath.read(str, "$.store.book[?(@.price > 10)]");
        log.info("价格大于10的book：{}", expensiveBooks);
    }

}