package com.github.tsarenkotxt.lib

import org.springframework.stereotype.Service

@Service
class GreetingService {

    fun greeting(name: String?): String = "Hello, ${name ?: "World"}!"
}
