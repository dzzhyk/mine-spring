package com.yankaizhang.springframework.aop;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yankaizhang.springframework.aop.support.ClazzUtils.dividePackageClassMethodParamsString;

/**
 * pointCut信息封装类
 * @author dzzhyk
 */
@SuppressWarnings("all")
public class PointCutConfig {

    private int modifyType;
    private String returnType;
    /**
     * 8 static
     * 16 final
     * 24 static final
     */
    private int declareType = 0;
    private String packageName;
    private String className;
    private String methodName;
    private Class<?>[] paramTypes;
    private Class<?>[] throwTypes;

    public PointCutConfig(Method method) {
        modifyType = method.getModifiers();
        returnType = method.getReturnType().getTypeName();
        throwTypes = method.getExceptionTypes();
        paramTypes = method.getParameterTypes();

        if (modifyType > 4){
            if (modifyType > 24){
                declareType = 24;
            }else if (modifyType > 16){
                declareType = 16;
            }else if (modifyType > 8){
                declareType = 8;
            }
        }

        // "public static final java.lang.String com.yankaizhang.springframework.test.service.impl.UserServiceImpl.helloUser(java.lang.String)"
        String methodString = method.toString();
        String[] methodParts = methodString.split(" ");
        int index = 2;
        if (declareType != 0){
           switch (declareType){
               case 8:
               case 16:
                   index = 3;
                   break;
               case 24:
                   index = 4;
                   break;
               default:break;
           }
        }
        // "java.lang.String com.yankaizhang.springframework.test.service.impl.UserServiceImpl.helloUser(java.lang.String)"
        String backPart = methodParts[index];
        String withoutParams = backPart.substring(0, backPart.lastIndexOf("("));
        String[] withoutParamsSplit = withoutParams.split("\\.");
        methodName = withoutParamsSplit[withoutParamsSplit.length - 1];
        className = withoutParamsSplit[withoutParamsSplit.length - 2];
        if (withoutParamsSplit.length > 2){
            StringBuilder builder = new StringBuilder();
            for (int i=0; i<withoutParamsSplit.length-2; i++){
                builder.append(withoutParamsSplit[i]);
                if (i != withoutParamsSplit.length-3){
                    builder.append(".");
                }
            }
            packageName = builder.toString();
        }else {
            packageName = "";
        }
    }

    /**
     * 判断切点表达式和该config包装是否匹配
     * public * com.yankaizhang.springframework.test.service.impl.*.*(*)
     * 注意：切点表达式暂时不支持 声明异常类型匹配
     */
    public boolean matches(String pointCut) throws Exception {

        // 排除异常类型声明
        String temp = pointCut;
        if (pointCut.contains("throws")){
            temp = pointCut.substring(0, pointCut.lastIndexOf("throws") - 1);
        }

        String[] parts = temp.split(" ");
        if (parts.length==1){
            // 包名
            String[] strings = dividePackageClassMethodParamsString(parts[0]);
            return checkSimpleExecutionParts(strings);
        }else if (parts.length==2){
            // 返回值 包名
            String[] strings = dividePackageClassMethodParamsString(parts[1]);
            return checkReturnValueMatches(parts[0]) &&
                    checkSimpleExecutionParts(strings);
        }else if (parts.length==3){
            // 修饰符 返回值 包名
            int count = getModifyNumber(parts[0]);
            if (count != modifyType){
                return false;
            }
            String[] strings = dividePackageClassMethodParamsString(parts[2]);
            return  checkReturnValueMatches(parts[1]) &&
                    checkSimpleExecutionParts(strings);
        }else if (parts.length==4){
            // 修饰符1 修饰符2 返回值 包名
            int count = getModifyNumber(parts[0]) + getModifyNumber(parts[1]);
            if (count != modifyType){
                return false;
            }
            String[] strings = dividePackageClassMethodParamsString(parts[3]);
            return  checkReturnValueMatches(parts[2]) &&
                    checkSimpleExecutionParts(strings);
        }else if (parts.length==5){
            // 修饰符1 修饰符2 修饰符3 返回值 包名
            int count = getModifyNumber(parts[0]) + getModifyNumber(parts[1]) + getModifyNumber(parts[2]);
            if (count != modifyType){
                return false;
            }
            String[] strings = dividePackageClassMethodParamsString(parts[4]);
            return  checkReturnValueMatches(parts[3]) &&
                    checkSimpleExecutionParts(strings);
        }
        return false;
    }

    /**
     * 获取修饰符数字
     */
    private int getModifyNumber(String modifier){
        // 什么都不加 是0 ， public  是1 ，private 是 2 ，protected 是 4，static 是 8 ，final 是 16。
        int num = 0;
        switch (modifier){
            case "public":num = 1;break;
            case "private": num = 2;break;
            case "protected": num = 4;break;
            case "static": num = 8;break;
            case "final": num = 16;break;
            default:
                break;
        }
        return num;
    }


    /**
     * 检查返回值是否匹配
     */
    private boolean checkReturnValueMatches(String returnTypeString){
        return "*".equals(returnTypeString) || returnType.equals(returnTypeString);
    }

    /**
     * 检查包名是否匹配
     */
    private boolean checkPackageNameMatches(String packageNameString){
        // 可能存在子包
        return "*".equals(packageNameString) || packageName.equals(packageNameString) || packageName.contains(packageNameString);
    }

    private boolean checkClassNameMatches(String classNameString){
        return "*".equals(classNameString) || className.equals(classNameString);
    }

    /**
     * 检查方法名是否匹配
     */
    private boolean checkMethodNameMatches(String methodNameString){
        return "*".equals(methodNameString) || methodName.equals(methodNameString);
    }

    /**
     * 检查参数类型是否匹配
     */
    private boolean checkParamsTypeMatches(String paramsString){
        if ("*".equals(paramsString)) {
            return true;
        }
        String[] split = paramsString.split(",");
        List<String> tempStrings = new ArrayList<>();
        for (Class<?> paramType : paramTypes) {
            tempStrings.add(paramType.getTypeName());
        }
        for (String s : split) {
            tempStrings.remove(s);
        }
        return tempStrings.size()==0;
    }

    /**
     * 简易形式切点表达式检查
     */
    private boolean checkSimpleExecutionParts(String[] strings){
        return checkPackageNameMatches(strings[0]) &&
                checkClassNameMatches(strings[1]) &&
                checkMethodNameMatches(strings[2]) &&
                checkParamsTypeMatches(strings[3]);
    }

    public int getModifyType() {
        return modifyType;
    }

    public void setModifyType(int modifyType) {
        this.modifyType = modifyType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public int getDeclareType() {
        return declareType;
    }

    public void setDeclareType(int declareType) {
        this.declareType = declareType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Class<?>[] getThrowTypes() {
        return throwTypes;
    }

    public void setThrowTypes(Class<?>[] throwTypes) {
        this.throwTypes = throwTypes;
    }

    @Override
    public String toString() {
        return "PointCutConfig{" +
                "modifyType=" + modifyType +
                ", returnType='" + returnType + '\'' +
                ", declareType=" + declareType +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", throwTypes=" + Arrays.toString(throwTypes) +
                '}';
    }
}
