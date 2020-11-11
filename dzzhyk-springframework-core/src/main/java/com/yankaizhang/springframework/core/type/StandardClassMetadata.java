package com.yankaizhang.springframework.core.type;


import java.lang.reflect.Modifier;

/**
 * 类的元数据接口的一个标准实现
 *
 * 创建这个对象需要制定某个类Class，然后使用标准反射创建StandardClassMetadata
 * @author dzzhyk
 */

public class StandardClassMetadata implements ClassMetadata {

    private final Class<?> introspectedClass;

    public StandardClassMetadata(Class<?> introspectedClass) {
        this.introspectedClass = introspectedClass;
    }

    public Class<?> getIntrospectedClass() {
        return introspectedClass;
    }

    @Override
    public String getClassName() {
        return this.introspectedClass.getName();
    }

    @Override
    public boolean isInterface() {
        return this.introspectedClass.isInterface();
    }

    @Override
    public boolean isAnnotation() {
        return this.introspectedClass.isAnnotation();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(this.introspectedClass.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.introspectedClass.getModifiers());
    }

    @Override
    public boolean isIndependent() {
        // 这个目标类不是匿名内部类
        return (!hasEnclosingClass() ||
                // 声明这个目标类的对象不为null，而且这个类是静态，则可以说明这个类是静态内部类
                (this.introspectedClass.getDeclaringClass() != null &&
                        Modifier.isStatic(this.introspectedClass.getModifiers())));
    }

    @Override
    public String getEnclosingClassName() {
        Class<?> enclosingClass = this.introspectedClass.getEnclosingClass();
        return (enclosingClass != null ? enclosingClass.getName() : null);
    }

    @Override
    public String getSuperClass() {
        Class<?> superclass = this.introspectedClass.getSuperclass();
        return (superclass != null? superclass.getName() : null);
    }

    @Override
    public String[] getInterfaceNames() {
        Class<?>[] interfaces = this.introspectedClass.getInterfaces();
        String[] strings = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            strings[i] = interfaces[i].getName();
        }
        return strings;
    }

    @Override
    public String[] getMemberClassNames() {
        Class<?>[] declaredClasses = this.introspectedClass.getDeclaredClasses();
        String[] strings = new String[declaredClasses.length];
        for (int i = 0; i < declaredClasses.length; i++) {
            strings[i] = declaredClasses[i].getName();
        }
        return strings;
    }
}
