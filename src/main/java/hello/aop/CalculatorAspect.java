package hello.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// TODO https://www.journaldev.com/2583/spring-aop-example-tutorial-aspect-advice-pointcut-joinpoint-annotations

// weaving : It is the process of linking aspects with other objects to create the advised proxy objects
// Weaving: COMPILE, LOAD TIME or RUNTIME

@Aspect // aspect: module of class which implement aop-logic
@Component
public class CalculatorAspect {
    public static final Logger LOGGER = LoggerFactory.getLogger(CalculatorAspect.class);

    // TODO without POINTCUT
//    @Before // Advice: before/after/instead of
//            ("execution(public String getName())") // Join Point:  A join point is the specific point in the application such
//                                                   // as METHOD EXECUTION, EXCEPTION HANDLING, CHANGING object VARIABLE etc.
//    public void getNameAdvice(){
//        LOGGER.warn("CalculatorAspect: Executing Advice on getName()");
//    }
//
//
//
//    // Target: *hello.*.get*()
//    @Before("execution(* hello.*.get*())")
//    public void getAllAdvice(){
//        LOGGER.warn("CalculatorAspect: Service method getter called"); // Introduction: change/adding/replace the structure of a class
//    }

    // TODO with pointcut
    @Before("getNamePointcut()")
    public void loggingAdvice(){
        System.out.println("Executing loggingAdvice on getName()");
    }

    @Before("getNamePointcut()")
    public void secondAdvice(){
        System.out.println("Executing secondAdvice on getName()");
    }

    // Pointcut - is a set of Joint Point
    // Pointcut = {Join_Point_1, Join_Point_2, Join_Point_3}
    @Pointcut("execution(public String getName())")
    public void getNamePointcut(){}

    @Before("allMethodsPointcut()")
    public void allServiceMethodsAdvice(){
        System.out.println("Before executing service method");
    }

    //Pointcut to execute on all the methods of classes in a package
    @Pointcut("within(hello.*)")
    public void allMethodsPointcut(){}

}

