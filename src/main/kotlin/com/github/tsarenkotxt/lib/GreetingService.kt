package com.github.tsarenkotxt.lib

import org.springframework.stereotype.Service

/**
 * The Greeting service.
 */
@Service
class GreetingService {

    fun greeting(name: String?): String = "Hello, ${name ?: "World"}!"
}
