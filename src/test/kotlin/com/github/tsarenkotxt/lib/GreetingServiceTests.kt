package com.github.tsarenkotxt.lib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GreetingServiceTests {

    @Test
    fun greeting() {
        val greetingService = GreetingService()

        assertEquals(greetingService.greeting(null), "Hello, World!")
        assertEquals(greetingService.greeting("Kotlin"), "Hello, Kotlin!")
    }

}
