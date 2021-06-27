package com.github.mkopylec.furiouscinema.infrastructure.resilience4j

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.ofRetryRegistry
import io.github.resilience4j.retry.RetryRegistry
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class ResilienceProvider(
    meter: MeterRegistry,
    private val circuitBreaker: CircuitBreakerRegistry,
    private val retry: RetryRegistry
) {
    init {
        ofCircuitBreakerRegistry(circuitBreaker).bindTo(meter)
        ofRetryRegistry(retry).bindTo(meter)
    }

    suspend fun <T> execute(operationName: String, operation: suspend () -> T): T =
        retry.retry(operationName).executeSuspendFunction {
            circuitBreaker.circuitBreaker(operationName).executeSuspendFunction(operation)
        }
}
