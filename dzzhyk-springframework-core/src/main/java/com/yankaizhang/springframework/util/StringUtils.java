package com.yankaizhang.springframework.util;

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
}
