package hello;

import hello.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    @Autowired
    private Calculator calculator;

    @RequestMapping("/")
    public String index() {

        Employee employee = new Employee();

        employee.setName("aaaa"+calculator.getValue1());
        System.out.println(employee.getName());

        return "Greetings from Spring Boot! Name : " + calculator.getName() + System.lineSeparator() +
                "value 1: " + calculator.getValue1() + System.lineSeparator()  +
                "value 2: " + calculator.getValue2() + System.lineSeparator();
    }

}
