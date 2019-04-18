package com.jacknic.config

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


/**
 * Swagger 文档配置
 * @author Jacknic
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis {
                    it?.apply {
                        val declaringClass = declaringClass()
                        if (declaringClass == BasicErrorController::class.java) {
                            return@apis false
                        }
                        if (declaringClass.isAnnotationPresent(RestController::class.java)) {
                            return@apis true
                        }
                        isAnnotatedWith(ResponseBody::class.java)
                    }
                    false
                }
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("文件管理器restful API 接口文档")
                .version("1.0")
                .build()
    }
}