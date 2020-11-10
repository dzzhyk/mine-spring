package com.yankaizhang.springframework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自制String工具类
 */
public class StringUtils {


    /**
     * 把一个元素添加到一个String数组中
     * @param target 目标数组
     * @param value  准备添加的元素
     * @return 新的String类型的数组
     */
    public static String[] addStringToArray(String[] target, String value) {
        if (null == target || target.length==0){
            return new String[]{value};
        }
        String[] res = new String[target.length + 1];
        System.arraycopy(target, 0, res, 0, target.length);
        res[target.length] = value;
        return res;
    }


    /**
     * 将集合中的元素转换为数组形式
     */
    public static String[] toStringArray(Collection<String> collections) {
        if (collections.isEmpty()){
            return new String[]{};
        }else{
            return collections.toArray(new String[0]);
        }
    }

    /**
     * 将首字母小写
     */
    public static String toLowerCase(String string){
        char[] chars = string.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
