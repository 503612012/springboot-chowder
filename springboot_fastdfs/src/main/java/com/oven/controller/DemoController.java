package com.oven.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.oven.utils.FastdfsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Controller
public class DemoController {

    @Resource
    private FastdfsUtils fastdfsUtils;

    @RequestMapping("/")
    public String index() {
        return "upload";
    }

    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件！");
            return "redirect:/";
        }
        try {
            StorePath path = fastdfsUtils.upload(file);
            redirectAttributes.addFlashAttribute("message", "上传【" + file.getOriginalFilename() + "】成功!");
            redirectAttributes.addFlashAttribute("path", "http://172.16.188.194:8888/" + path.getFullPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

}
