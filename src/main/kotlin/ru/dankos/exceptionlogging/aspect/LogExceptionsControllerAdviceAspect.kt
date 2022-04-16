package ru.dankos.exceptionlogging.aspect

import mu.KLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class LogExceptionsControllerAdviceAspect {

    @Pointcut("execution(public * *(..))")
    fun publicMethod() {
    }

    @Pointcut("within(@ru.dankos.exceptionlogging.annotation.LogExceptionsControllerAdvice *)")
    fun logExceptionsControllerAdviceAnnotation() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    fun controllerAdviceAnnotation() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    fun restControllerAdviceAnnotation() {
    }

    @Before("publicMethod() && logExceptionsControllerAdviceAnnotation() && (controllerAdviceAnnotation() || restControllerAdviceAnnotation())")
    fun logExceptionForControllerAdvice(joinPoint: JoinPoint) {
        val classesOfMethodArgs = joinPoint.args
        classesOfMethodArgs.filterIsInstance<Exception>().forEach {
            logger.error(it) { "${it.message}" }
        }
    }

    private companion object : KLogging()
}