package com.example.container;

import com.example.annotation.Autowired;
import com.example.annotation.Component;
import com.example.annotation.Start;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IOCContainer {

    private final Set<Object> beans;

    public IOCContainer(Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    /**
     * start di beans, Start annotation
     */
    public void start() {
        beans.forEach(bean -> Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Start.class))
                .forEach(method -> {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        exception.printStackTrace();
                    }
                })
        );
    }

    /**
     * create container get all package, Component annotation
     */
    public static IOCContainer createContainer(final String packageName) {
        return new Reflections(packageName, Scanners.SubTypes.filterResultsBy(filter -> true))
                .getSubTypesOf(Object.class)
                .stream()
                .filter(targetClass -> targetClass.isAnnotationPresent(Component.class))
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), IOCContainer::new));
    }

    /**
     * create beans
     */
    private Set<Object> createBeans(final Set<Class<?>> classes)  {
        return classes.stream()
                .map(clazz -> {
                    try {
                        return clazz.getDeclaredConstructor();
                    } catch (NoSuchMethodException exception) {
                        throw new RuntimeException(exception.getMessage());
                    }
                })
                .peek(constructor -> constructor.setAccessible(true))
                .map(constructor -> {
                    try {
                        return constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                        throw new RuntimeException(exception.getMessage());
                    }
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * setting bean field, Autowired annotation
     */
    private void setFields(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .forEach(field -> setFields(bean, field));
    }
    private void setFields(final Object bean, final Field field){
        final Class<?> fieldType = field.getType();
        field.setAccessible(true);
        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(matchBean -> {
                    try {
                        field.set(bean, matchBean);
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(exception.getMessage());
                    }
                });
    }
}
