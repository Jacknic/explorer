package com.jacknic.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacknic.util.Response;
import com.jacknic.util.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Api(description = "文件夹操作", tags = "folder")
@RestController
@RequestMapping("/api/folder")
public class FolderController {

    @Autowired
    ObjectMapper json;

    @ApiOperation(value = "创建文件夹")
    @GetMapping("/create")
    public String list() throws JsonProcessingException {
        ResultBody resultBody = Response.ok();
        String[] names = {"jacknic", "Tom", "Bob"};
        HashMap<String, Object> data = new HashMap<>();
        data.put("users", names);
        resultBody.setData(data);
        return json.writeValueAsString(resultBody);
    }


}
