package com.example.grpc

import com.example.grpc.service.GreeterService
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreeterServiceTest {
    
    @get:Rule
    val grpcCleanup = GrpcCleanupRule()
    
    private lateinit var blockingStub: GreeterGrpc.GreeterBlockingStub
    private val serverName = InProcessServerBuilder.generateName()
    
    @BeforeEach
    fun setUp() {
        // In-process 서버 생성 및 시작
        val server = InProcessServerBuilder
            .forName(serverName)
            .directExecutor()
            .addService(GreeterService())
            .build()
            .start()
            
        grpcCleanup.register(server)
        
        // In-process 채널 생성
        val channel = InProcessChannelBuilder
            .forName(serverName)
            .directExecutor()
            .build()
            
        grpcCleanup.register(channel)
        
        // 블로킹 스텁 생성
        blockingStub = GreeterGrpc.newBlockingStub(channel)
    }
    
    @Test
    fun `sayHello should return greeting message`() {
        // Given
        val request = HelloRequest.newBuilder()
            .setName("Test")
            .build()
            
        // When
        val response = blockingStub
            .withDeadlineAfter(5, TimeUnit.SECONDS)
            .sayHello(request)
            
        // Then
        assert(response.message.contains("Hello Test!"))
        assert(response.message.contains("Server time:"))
    }
    
    @Test
    fun `sayHello with empty name should return greeting`() {
        // Given
        val request = HelloRequest.newBuilder()
            .setName("")
            .build()
            
        // When
        val response = blockingStub
            .withDeadlineAfter(5, TimeUnit.SECONDS)
            .sayHello(request)
            
        // Then
        assert(response.message.contains("Hello !"))
    }
    
    @Test
    fun `sayHelloStream should return multiple messages`() {
        // Given
        val request = HelloRequest.newBuilder()
            .setName("Stream Test")
            .build()
            
        // When
        val responses = blockingStub
            .withDeadlineAfter(10, TimeUnit.SECONDS)
            .sayHelloStream(request)
            .asSequence()
            .toList()
            
        // Then
        assertEquals(5, responses.size)
        responses.forEachIndexed { index, reply ->
            assert(reply.message.contains("[${index + 1}]"))
            assert(reply.message.contains("Hello Stream Test!"))
            assert(reply.message.contains("Streaming at:"))
        }
    }
}
