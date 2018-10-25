package com.jacknic.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "文件夹操作", tags = "文件夹")
@RestController
@RequestMapping("/api/folder")
public class FolderController {

    @ApiOperation(value = "获取文件列表", notes = "getCount更多说明")
    @GetMapping("/create")
    public ModelMap list(ModelMap modelMap) {
        modelMap.addAttribute("name", "Jacknic");
        return modelMap;
    }


}
