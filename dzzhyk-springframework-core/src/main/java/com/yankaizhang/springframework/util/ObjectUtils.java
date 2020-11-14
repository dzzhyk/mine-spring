package com.yankaizhang.springframework.util;

import java.util.Arrays;

/**
 * 对象操作的工具类
 *
 * @author dzzhyk
 */
public class ObjectUtils {

    /**
     * 判断两个对象是否相同
     */
    public static boolean nullSafeEquals(Object o1, Object o2){
        if (o1 == o2){
            return true;
        }
        if (o1==null || o2==null){
            return false;
        }
        if (o1.equals(o2)){
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()){
            return arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * 判断两个数组是否相同
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

    /**
     * 把一个元素添加到一个Object数组中
     */
    public static Object[] addObjectToArray(Object[] target, Object value) {
        if (null == target || target.length==0){
            return new Object[]{value};
        }
        Object[] res = new Object[target.length + 1];
        System.arraycopy(target, 0, res, 0, target.length);
        res[target.length] = value;
        return res;
    }

}
