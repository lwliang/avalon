package Javassist;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.avalon.erp.ErpApplication;

import java.lang.reflect.Method;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApplication.class)
public class JavassistTest {
    private static final Logger log = LoggerFactory.getLogger(JavassistTest.class);

    @Test
    public void test() throws Exception {
        // 获取ExtendUserModel类的字节码
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(getClass()));

        // 获取ExtendUserModel的CtClass
        CtClass ctClass = pool.get(ExtendUserModel.class.getName());

        // 备份原始父类
        CtClass originalSuperclass = ctClass.getSuperclass();

        try {
            // 修改父类为UserModel
            ctClass.setSuperclass(pool.get(UserModel.class.getName()));

            // 获取修改后的字节码
            byte[] modifiedBytecode = ctClass.toBytecode();

            // 使用自定义类加载器加载修改后的类
            CustomClassLoader loader = new CustomClassLoader();
            Class<?> modifiedClass = loader.defineClass(ExtendUserModel.class.getName(), modifiedBytecode);

            // 创建实例并测试
            Object instance = modifiedClass.newInstance();
            testModifiedClass(modifiedClass, instance);

        } finally {
            // 恢复原始父类
            ctClass.setSuperclass(originalSuperclass);
            // 释放资源
            ctClass.detach();
        }
    }

    // 自定义类加载器
    private static class CustomClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] bytecode) {
            return defineClass(name, bytecode, 0, bytecode.length);
        }
    }

    private void testModifiedClass(Class<?> modifiedClass, Object instance) throws Exception {
        log.info("Testing modified class: " + modifiedClass.getName());

        // 测试原有的方法
        Method setNameMethod = modifiedClass.getMethod("setName", String.class);
        setNameMethod.invoke(instance, "Test Name");

        Method getNameMethod = modifiedClass.getMethod("getName");
        String name = (String) getNameMethod.invoke(instance);
        log.info("Name: " + name);

        Method customMethod = modifiedClass.getMethod("customMethod");
        customMethod.invoke(instance);

        // 测试从UserModel继承的方法
        Method setUserFieldMethod = modifiedClass.getMethod("setUserField", String.class);
        setUserFieldMethod.invoke(instance, "Test UserField");

        Method getUserFieldMethod = modifiedClass.getMethod("getUserField");
        String userField = (String) getUserFieldMethod.invoke(instance);
        log.info("UserField: " + userField);

        Method userMethod = modifiedClass.getMethod("userMethod");
        userMethod.invoke(instance);

        Method sayHelloMethod = modifiedClass.getMethod("sayHello");
        sayHelloMethod.invoke(instance);
    }
}