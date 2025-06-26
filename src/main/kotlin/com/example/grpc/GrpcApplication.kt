package com.example.grpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcApplication

fun main(args: Array<String>) {
    // 클라이언트 모드로 실행하는 경우
    if (args.isNotEmpty() && args[0] == "client") {
        runClient()
    } else {
        // 서버 모드로 실행
        runApplication<GrpcApplication>(*args)
    }
}

private fun runClient() {
    println("Starting gRPC client...")
    val client = com.example.grpc.client.GreeterClient("localhost", 9090)
    
    println("\n=== Unary Call ===")
    client.greet("World")
    
    println("\n=== Streaming Call ===")
    client.greetStream("Kotlin")
    
    println("\n=== Multiple Requests ===")
    listOf("Alice", "Bob", "Charlie").forEach { name ->
        client.greet(name)
        Thread.sleep(500)
    }
    
    client.shutdown()
}
