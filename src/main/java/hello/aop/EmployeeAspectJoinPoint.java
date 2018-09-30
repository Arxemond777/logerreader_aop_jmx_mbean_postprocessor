package hello.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// TODO https://www.journaldev.com/2583/spring-aop-example-tutorial-aspect-advice-pointcut-joinpoint-annotations

@Aspect
@Component
public class EmployeeAspectJoinPoint {

//    @Before("execution(public void hello.model..set*(*))")
//    public void loggingAdvice(JoinPoint joinPoint){
//        System.out.println("Before running loggingAdvice on method="+joinPoint.toString());
//
//        System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
//
//    }
//
//    //Advice arguments, will be applied to bean methods with single String argument
//    @Before("args(name1)")
//    public void logStringArguments(String name1){
//        System.out.println("String argument passed="+name1);
//    }
}
