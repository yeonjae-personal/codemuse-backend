//package com.lgcns.svcp.prod.ruleengine;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class PerformanceLogger {
//
//    @Around("execution(* com.lgcns.svcp.prod.ruleengine.service.RuleService.evaluateFrom*(..))")
//    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
////        long start = System.currentTimeMillis();
////
////        Object result = joinPoint.proceed();
////
////        long duration = System.currentTimeMillis() - start;
////        String method = joinPoint.getSignature().getName();
////        System.out.println("[PERF] " + method + " executed in " + duration + " ms");
//        
//        long start = System.nanoTime();
//        Object result = joinPoint.proceed();
//        long end = System.nanoTime();
//        System.out.println("[PERF] took " + (end - start)/1_000_000 + " ms");
//
//        return result;
//    }
//}
