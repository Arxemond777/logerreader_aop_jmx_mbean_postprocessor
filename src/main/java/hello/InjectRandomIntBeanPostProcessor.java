package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class InjectRandomIntBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectRandomIntBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        LOGGER.debug("postProcessBeforeInitialization::beanName = {}, beanClass = {}", beanName, bean.getClass().getSimpleName());

        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectRandomInt.class)) {
                LOGGER.warn("has InjectRandomInt postProcessBeforeInitialization::beanName = {}, beanClass = {}, field = {}", beanName, bean.getClass().getSimpleName(), field.getName());

                field.setAccessible(true);
                InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
                ReflectionUtils.setField(field, bean, getRandomIntInRange(annotation.min(), annotation.max()));
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private int getRandomIntInRange(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}