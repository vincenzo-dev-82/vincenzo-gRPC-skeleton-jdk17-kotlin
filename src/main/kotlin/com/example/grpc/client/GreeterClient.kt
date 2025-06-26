package com.example.grpc.client

import com.example.grpc.GreeterGrpc
import com.example.grpc.HelloRequest
import com.example.grpc.HelloReply
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch

class GreeterClient(
    private val host: String,
    private val port: Int
) {
    private val channel: ManagedChannel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build()
        
    private val blockingStub: GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)
    private val asyncStub: GreeterGrpc.GreeterStub = GreeterGrpc.newStub(channel)
    
    fun greet(name: String) {
        println("Calling sayHello with name: $name")
        
        val request = HelloRequest.newBuilder()
            .setName(name)
            .build()
            
        try {
            val response = blockingStub.sayHello(request)
            println("Response: ${response.message}")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    
    fun greetStream(name: String) {
        println("Calling sayHelloStream with name: $name")
        
        val request = HelloRequest.newBuilder()
            .setName(name)
            .build()
            
        val latch = CountDownLatch(1)
        
        val responseObserver = object : StreamObserver<HelloReply> {
            override fun onNext(reply: HelloReply) {
                println("Streaming response: ${reply.message}")
            }
            
            override fun onError(t: Throwable) {
                println("Error in stream: ${t.message}")
                latch.countDown()
            }
            
            override fun onCompleted() {
                println("Stream completed")
                latch.countDown()
            }
        }
        
        try {
            asyncStub.sayHelloStream(request, responseObserver)
            latch.await(10, TimeUnit.SECONDS)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
        println("Client shutdown complete")
    }
}
