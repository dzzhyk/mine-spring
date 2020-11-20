package com.yankaizhang.spring.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包名工具类
 * @author dzzhyk
 */
@SuppressWarnings("unused")
public class ClazzUtils {
    private static final String CLASS_SUFFIX = ".class";
    private static final String CLASS_FILE_PREFIX = File.separator + "classes"  + File.separator;
    private static final String PACKAGE_SEPARATOR = ".";


    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClazzUtils.class);

    /**
     * 查找包下的所有类的名字
     * @param packageName
     * @param showChildPackageFlag 是否需要显示子包内容
     * @return List集合，内容为类的全名
     */
    public static List<String> getClazzName(String packageName, boolean showChildPackageFlag ) {

        List<String> result = new ArrayList<>();

        try {
            // 如果是类名不是包名
            Class<?> clazz = Class.forName(packageName);
            result.add(clazz.getName());
            return result;
        } catch (ClassNotFoundException e) {

        }

        String suffixPath = packageName.replaceAll("\\.", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {

            Enumeration<URL> urls = loader.getResources(suffixPath);

            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if("file".equals(protocol)) {
                        String path = url.getPath();
                        log.debug("定位至 ===> " + path);
                        result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));
                    } else if("jar".equals(protocol)) {
                        JarFile jarFile = null;
                        try{
                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        if(jarFile != null) {
                            result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 递归获取所有class文件的名字
     * @param file
     * @param flag	是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByFile(File file, boolean flag) {
        List<String> result =  new ArrayList<>();
        if(!file.exists()) {
            return result;
        }
        if(file.isFile()) {
            String path = file.getPath();
            // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
            if(path.endsWith(CLASS_SUFFIX)) {
                path = path.replace(CLASS_SUFFIX, "");
                // 从"/classes/"后面开始截取
                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                        .replace(File.separator, PACKAGE_SEPARATOR);
                if(!clazzName.contains("$")) {
                    result.add(clazzName);
                }
            }
            return result;

        } else {
            File[] listFiles = file.listFiles();
            if(listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    if(flag) {
                        result.addAll(getAllClassNameByFile(f, flag));
                    } else {
                        if(f.isFile()){
                            String path = f.getPath();
                            if(path.endsWith(CLASS_SUFFIX)) {
                                path = path.replace(CLASS_SUFFIX, "");
                                // 从"/classes/"后面开始截取
                                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                                        .replace(File.separator, PACKAGE_SEPARATOR);
                                if(!clazzName.contains("$")) {
                                    result.add(clazzName);
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }
    }


    /**
     * 递归获取jar所有class文件的名字
     * @param jarFile
     * @param packageName 包名
     * @param flag	是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {
        List<String> result =  new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if(name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if(flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if(name.startsWith(packageName) && !name.contains("$")) {
                        result.add(name);
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if(packageName.equals(name.substring(0, name.lastIndexOf("."))) && !name.contains("$")) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 分离字符串
     * com.yankaizhang.springframework.test.service.impl.*.*(*)
     */
    public static String[] dividePackageClassMethodParamsString(String ss) throws Exception{
        String[] strings = new String[4];
        String temp = ss.substring(0, ss.lastIndexOf("("));
        String[] split = temp.split("\\.");

        if (split.length == 2){
            // 根目录下的类名.方法名
            strings[0] = "";                    // packageName
            strings[1] = split[split.length-2]; // className
            strings[2] = split[split.length-1]; // methodName
        }else if (split.length > 2){
            StringBuilder builder = new StringBuilder();
            for (int i=0; i<split.length-2; i++){
                builder.append(split[i]);
                if (i != split.length-3){
                    builder.append(".");
                }
            }
            strings[0] = builder.toString();    // packageName
            strings[1] = split[split.length-2]; // className
            strings[2] = split[split.length-1]; // methodName
        }else{
            throw new Exception("切点表达式部分\""+ ss +"\"有误 ==> 不完整的包名/类名/方法名信息");
        }
        strings[3] = ss.substring(ss.lastIndexOf("(")+1, ss.lastIndexOf(")"));
        return strings;
    }
}
