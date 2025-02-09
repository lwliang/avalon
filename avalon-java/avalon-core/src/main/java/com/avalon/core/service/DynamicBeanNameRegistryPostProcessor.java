package com.avalon.core.service;

import com.avalon.core.context.AvalonApplicationContext;
import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/04 20:59
 */
@Component
@Slf4j
public class DynamicBeanNameRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private AvalonApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AvalonApplicationContext) applicationContext;
    }

    @Data
    private static class InheritInfo {
        private String serviceName;
        private String inheritName;
        private Class<?> className; // 当前类
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 遍历 Spring 容器中所有的 BeanDefinition
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        List<InheritInfo> inheritList = new ArrayList<>(); // 需要继承的模型
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);

            try {
                if (ObjectUtils.isNull(beanDefinition.getBeanClassName())) continue;
                Object db = beanDefinition.getAttribute("db");
                if (ObjectUtils.isNotNull(db)) return; // 有数据库说明是动态注册的，不处理

                // 检查是否是 AbstractService 的子类
                Class<?> beanClass = Class.forName(beanDefinition.getBeanClassName());
                if (isAbstractService(beanClass)) {
                    // 创建 Bean 实例以调用 getService 方法
                    AbstractService serviceInstance = (AbstractService) beanClass.getDeclaredConstructor().newInstance();
                    String serviceName = serviceInstance.getServiceName(); // 获取动态名称
                    String inherit = serviceInstance.getInherit();// 获取需要继承的父类
                    if (StringUtils.isEmpty(serviceName)) continue; // null保持原样

                    if (StringUtils.isNotEmpty(inherit)) { // 具有父类，则二次替换
                        InheritInfo inheritInfo = new InheritInfo();
                        inheritInfo.setServiceName(serviceName);
                        inheritInfo.setInheritName(inherit);
                        inheritInfo.setClassName(beanClass);
                        inheritList.add(inheritInfo);
                        continue;
                    }
                    // 如果名称不同，则修改 BeanDefinition 的名称  无继承的情况下
                    if (!beanName.equals(serviceName)) {
                        registry.removeBeanDefinition(beanName); // 移除旧的定义
                        registry.registerBeanDefinition(serviceName, beanDefinition); // 注册新的定义
                        if (log.isDebugEnabled()) {
                            log.debug("Bean name changed from {} to {}", beanName, serviceName);
                        }
                    }
                    applicationContext.getDefaultORM().addService(serviceName, null, beanClass); // 保留原始注册名称
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to process AbstractService subclass: " + beanName, e);
            }
        }

        // 处理继承关系
        for (InheritInfo inheritInfo : inheritList) {
            String serviceName = inheritInfo.getServiceName();
            String inheritName = inheritInfo.getInheritName();
            Class<?> className = inheritInfo.getClassName();

            applicationContext.getDefaultORM().addService(serviceName, inheritName, className); // 注册继承关系
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不需要额外处理
    }

    private boolean isAbstractService(Class<?> beanClass) {
        return AbstractService.class.isAssignableFrom(beanClass) &&
                !beanClass.isInterface() &&
                !Modifier.isAbstract(beanClass.getModifiers());
    }
}