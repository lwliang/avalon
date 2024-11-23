/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.external.service;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyFactory implements MethodInterceptor {
    public CglibProxyFactory() {
    }

    /**
     * 1、代理对象；2、委托类方法；3、方法参数；4、代理方法的MethodProxy对象。
     *
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("事务开始......" + method.getName());
        Object o1 = methodProxy.invokeSuper(o, objects);
        System.out.println("事务结束......." + method.getName());
        return o1;
    }
}
