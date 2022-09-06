package com.oven.controller;

import com.oven.utils.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
public class DemoController {

    @javax.annotation.Resource
    private FileUtils fileUtils;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileUtils
                .list()
                .map(path -> MvcUriComponentsBuilder
                        .fromMethodName(DemoController.class, "download", path.getFileName().toString())
                        .build()
                        .toString())
                .collect(Collectors.toList()));
        return "upload";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Resource file = fileUtils.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            redirectAttributes.addFlashAttribute("message", "请选择文件!");
            return "redirect:/";
        }
        fileUtils.upload(file);
        redirectAttributes.addFlashAttribute("message", "上传【" + file.getOriginalFilename() + "】成功!");
        return "redirect:/";
    }

}
