package com.oven.controller;

import com.oven.config.HBaseClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    private static final String TABLE_NAME = "user";
    private static final String TABLE_FAMILY_1 = "pet";
    private static final String TABLE_FAMILY_2 = "car";

    @Resource
    private HBaseClient hBaseClient;

    @RequestMapping("/createTable")
    public String createTable() {
        try {
            hBaseClient.createTable(TABLE_NAME, TABLE_FAMILY_1, TABLE_FAMILY_2);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/deleteTable")
    public String deleteTable() {
        try {
            hBaseClient.deleteTable(TABLE_NAME);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/add")
    public String add() {
        try {
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_1, "name", "金毛");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_1, "age", "2");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_1, "sex", "男");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_2, "brand", "宝马");
            hBaseClient.insertOrUpdate(TABLE_NAME, "1", TABLE_FAMILY_2, "price", "100W");

            hBaseClient.insertOrUpdate(TABLE_NAME, "2", TABLE_FAMILY_1, "name", "英短");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/delete")
    public String delete(String id) {
        try {
            hBaseClient.deleteRow(TABLE_NAME, id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/deleteColumnFamily")
    public String deleteColumnFamily(String id) {
        try {
            hBaseClient.deleteColumnFamily(TABLE_NAME, id, TABLE_FAMILY_2);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/deleteColumn")
    public String deleteColumn(String id) {
        try {
            hBaseClient.deleteColumn(TABLE_NAME, id, TABLE_FAMILY_1, "age");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/getValue")
    public String getValue(String id) {
        try {
            return hBaseClient.getValue(TABLE_NAME, id, TABLE_FAMILY_1, "name");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/getById")
    public String getById(String id) {
        try {
            return hBaseClient.selectOneRow(TABLE_NAME, id, TABLE_FAMILY_1);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
