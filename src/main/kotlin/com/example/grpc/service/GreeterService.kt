package com.example.grpc.service

import com.example.grpc.GreeterGrpc
import com.example.grpc.HelloReply
import com.example.grpc.HelloRequest
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@GrpcService
class GreeterService : GreeterGrpc.GreeterImplBase() {
    
    private val logger = LoggerFactory.getLogger(GreeterService::class.java)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    
    override fun sayHello(
        request: HelloRequest,
        responseObserver: StreamObserver<HelloReply>
    ) {
        logger.info("Received request from: ${request.name}")
        
        val timestamp = LocalDateTime.now().format(formatter)
        val reply = HelloReply.newBuilder()
            .setMessage("Hello ${request.name}! Server time: $timestamp")
            .build()
            
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
        
        logger.info("Sent reply to: ${request.name}")
    }
    
    override fun sayHelloStream(
        request: HelloRequest,
        responseObserver: StreamObserver<HelloReply>
    ) {
        logger.info("Received streaming request from: ${request.name}")
        
        // 5개의 메시지를 스트리밍으로 전송
        for (i in 1..5) {
            val timestamp = LocalDateTime.now().format(formatter)
            val reply = HelloReply.newBuilder()
                .setMessage("[$i] Hello ${request.name}! Streaming at: $timestamp")
                .build()
                
            responseObserver.onNext(reply)
            
            // 1초 간격으로 메시지 전송
            Thread.sleep(1000)
        }
        
        responseObserver.onCompleted()
        logger.info("Completed streaming to: ${request.name}")
    }
}
