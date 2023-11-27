package ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.AspectDto;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HandlerMetricAnotation {
    private final MeterRegistry meterRegistry;
    private Counter counterMain;
    private Counter counterError;
    private Timer timer;

    @Pointcut("execution(@Metric * *.*(..))")
    public void anotationMethod() {
    }

    @Pointcut("execution(* (@Metric *).*(..))")
    void methodOfAnnotatedClass() {
    }

    @Pointcut("methodOfAnnotatedClass()||anotationMethod()")
    private void pointCutClass() {
    }

    @Around("@annotation(Logging)")
    public Object arroundAllMethodsLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String method = getNameClassAndMethod(proceedingJoinPoint).getNameMethod();
        String nameClass = getNameClassAndMethod(proceedingJoinPoint).getNameClass();
        log.info("AOP " + nameClass + "  " + method + " start method");
        Object returnObject = executeProceedingJP(proceedingJoinPoint, nameClass, method);
        log.info("AOP " + nameClass + "  " + method + " end method");
        return returnObject;
    }

    @Around("pointCutClass()")
    public Object arroundAllMethodsClass(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String method = getNameClassAndMethod(proceedingJoinPoint).getNameMethod();
        String nameClass = getNameClassAndMethod(proceedingJoinPoint).getNameClass();
        counterMain = Counter.builder(nameClass).description("counter " + method).tag("method", method).tag("customRMetric", "").register(meterRegistry);
        counterMain.increment();
        timer = Timer.builder(nameClass + "_timer").tag("method", method).tag("customMetric", "").register(meterRegistry);
        Instant start = Instant.now();
        Object returnObject = executeProceedingJP(proceedingJoinPoint, nameClass, method);
        Duration duration = Duration.between(start, Instant.now());
        timer.record(duration);
        return returnObject;
    }

    private AspectDto getNameClassAndMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String method = signature.getMethod().getName();
        String nameClass = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        Metric metric = signature.getMethod().getAnnotation(Metric.class);
        String nameMethod = "";
        if (metric != null) {
            nameMethod = !metric.label().equals("") ? metric.label() : method;
            nameClass = !metric.nameMetric().equals("") ? metric.nameMetric() : nameClass;
        }
        nameClass = nameClass.substring(nameClass.lastIndexOf(".") + 1);
        nameMethod = method.substring(method.lastIndexOf(".") + 1);
        return new AspectDto(nameMethod, nameClass);
    }

    private Object executeProceedingJP(ProceedingJoinPoint proceedingJoinPoin, String nameMetric, String labelMetric) throws Throwable {
        Object returnObject;
        try {
            returnObject = proceedingJoinPoin.proceed();
        }
        catch (Throwable e){
            counterError = Counter.builder("errorSocialNetwork").description("counterError " + nameMetric).tag("clasAndMethod", labelMetric + " " + nameMetric).register(meterRegistry);
            counterError.increment();
            log.error("ERROR HandlerMetricAnotation method arroundAllMethodsCreatMetricsCount " + nameMetric + "  " + labelMetric + " " + e);
            throw e;
        }
        return returnObject;
    }
}
