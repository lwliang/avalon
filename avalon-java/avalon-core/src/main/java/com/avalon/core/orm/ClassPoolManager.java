package com.avalon.core.orm;

import javassist.*;

import java.util.List;


/**
 * 动态生成模型工具类
 *
 * @author lwlianghehe@gmail.com
 * @date 2024/12/06 9:27
 */
public class ClassPoolManager {
    /**
     * 模型动态继承
     *
     * @param classPool    模型修改器
     * @param parentClass  父类
     * @param childClasses 子类
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static Class<?> createEnhancedClass(ClassPool classPool,
                                               Class<?> parentClass,
                                               List<Class<?>> childClasses)
            throws NotFoundException, CannotCompileException {
        CtClass parentCtClass = classPool.get(parentClass.getName());
        CtClass childCtClass = null;

        for (Class<?> childClass : childClasses) {
            childCtClass = classPool.get(childClass.getName()); // 子类
            // 设置继承关系
            childCtClass.setSuperclass(parentCtClass);
            parentCtClass.detach();
            parentCtClass = childCtClass;
        }

        String newClassName = childClasses.get(childClasses.size() - 1).getName() + "$Enhanced_" + System.currentTimeMillis();
        childCtClass.setName(newClassName);
        //  加载类到当前类加载器
        Class<?> targetClass = childCtClass.toClass(ClassLoader.getSystemClassLoader());

        childCtClass.detach();

        return targetClass;
    }

    /**
     * 创建类修改器
     *
     * @return
     */
    public static ClassPool createClassPool() {
        ClassPool classPool = new ClassPool(true);
        try {
            // 1. 添加系统类路径
            classPool.appendSystemPath();

            // 2. 获取当前上下文的类加载器
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            classPool.appendClassPath(new LoaderClassPath(contextClassLoader));

            return classPool;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create ClassPool", e);
        }
    }
}
