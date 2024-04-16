package org.wsd.app.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.config.SwaggerConfig;


@Log4j2
@RestController
@RequestMapping("/api/filesystem")
@Tag(name = "File System Controller")
@SecurityRequirement(name = SwaggerConfig.BEARER_TOKEN)
public class FileSystemController {

}
