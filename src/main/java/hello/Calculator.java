package hello;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

@ManagedResource(
        objectName = "Examples:type=JMX,name=Calculator",
        description = "A calculator to demonstrate JMX in the SpringFramework")
@Component
public class Calculator {
    @InjectRandomInt
    private int value1;

    @InjectRandomInt(min = 100, max = 200)
    private int value2;

    private String name;

    @ManagedAttribute(description = "Calculator name")
    public String getName() {
        return name;
    }

    @ManagedAttribute(description = "Calculator name")
    public void setName(String name) {
        this.name = name;
    }

    @ManagedAttribute(description = "Calculator value1")
    public int getValue1() {
        return value1;
    }

    @ManagedAttribute(description = "Calculator value1")
    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }
}
