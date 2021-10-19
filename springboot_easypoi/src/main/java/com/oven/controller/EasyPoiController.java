package com.oven.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.oven.vo.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/easyPoi")
public class EasyPoiController {

    @RequestMapping(value = "/exportMemberList", method = RequestMethod.GET)
    public void exportMemberList(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        List<Member> memberList = new ArrayList<>();
        memberList.add(new Member(1L, "zhangsan", "123", "张三", new Date(), "15712341234", "ddd", 1));
        memberList.add(new Member(2L, "lisi", "123", "李四", new Date(), "15712341234", "ddd", 1));
        memberList.add(new Member(3L, "wangwu", "123", "王五", new Date(), "15712341234", "ddd", 1));
        memberList.add(new Member(4L, "zhaoliu", "123", "赵六", new Date(), "15712341234", "ddd", 1));

        ExportParams params = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        map.put(NormalExcelConstants.DATA_LIST, memberList);
        map.put(NormalExcelConstants.CLASS, Member.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "memberList");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ResponseBody
    @RequestMapping(value = "/importMemberList", method = RequestMethod.POST)
    public String importMemberList(@RequestPart("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        try {
            List<Member> list = ExcelImportUtil.importExcel(file.getInputStream(), Member.class, params);
            return "导入成功";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "导入失败";
    }

}