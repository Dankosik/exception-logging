package ru.dankos.exceptionlogging.aspect

import mu.KLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class LogExceptionsAspect {

    @Pointcut("execution(public * *(..))")
    fun publicMethod() {
    }

    @Pointcut("within(@ru.dankos.exceptionlogging.annotation.LogExceptions *)")
    fun logExceptionsAnnotation() {
    }

    @Around("publicMethod() && logExceptionsAnnotation()")
    fun logExceptionForClass(joinPoint: ProceedingJoinPoint): Any? = try {
        joinPoint.proceed()
    } catch (exception: Exception) {
        logger.error(exception) { "${exception.message}" }
        throw exception
    }

    @AfterThrowing(
        pointcut = "@annotation(ru.dankos.exceptionlogging.annotation.LogExceptions)",
        throwing = "exception"
    )
    fun logExceptionByMethod(exception: Exception) = logger.error(exception) { "${exception.message}" }

    private companion object : KLogging()
}