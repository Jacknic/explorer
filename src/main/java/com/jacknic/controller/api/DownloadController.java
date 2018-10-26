package com.jacknic.controller.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "download", description = "文件下载")
@RestController
@RequestMapping("/api/download")
public class DownloadController {
}
