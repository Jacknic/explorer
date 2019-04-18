package com.jacknic

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * 应用入口
 * @author Jacknic
 */
@SpringBootApplication
class ExplorerApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(ExplorerApplication::class.java, *args)
}

