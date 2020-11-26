package com.yankaizhang.spring.aop.support;

import com.yankaizhang.spring.util.ClassUtils;

import java.util.Comparator;

/**
 * AdviceSupport比较器
 * 按照目标层级深度从深到浅排列
 * @author dzzhyk
 */
public class AdviceSupportComparator implements Comparator<AdvisedSupport> {
    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdviceSupportComparator.class);
    @Override
    public int compare(AdvisedSupport o1, AdvisedSupport o2) {
        String pointCut1 = o1.getAopConfig().getPointCut();
        String pointCut2 = o2.getAopConfig().getPointCut();
        return doCompare(pointCut1, pointCut2);
    }

    private int doCompare(String pointCut1, String pointCut2){

        // com.yankaizhang.springframework.test.service.impl.TestServiceImpl.*(*)
        String[] split1 = pointCut1.split(" ");
        String[] split2 = pointCut2.split(" ");
        String temp1 = "";
        String temp2 = "";
        try {
            String[] part1 = getPackageClassMethodParamsPart(split1);
            String[] part2 = getPackageClassMethodParamsPart(split2);
            temp1 = generateStringFromPartForCompare(part1);
            temp2 = generateStringFromPartForCompare(part2);
        }catch (Exception e){
            log.warn("AOP切面排序出错 ==> " + e.getMessage());
            e.printStackTrace();
        }
        return temp2.compareTo(temp1);
    }

    private String[] getPackageClassMethodParamsPart(String[] split) throws Exception {
        String ss = split[split.length-1];
        return ClassUtils.dividePackageClassMethodParamsString(ss);
    }

    private String generateStringFromPartForCompare(String[] part){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < part.length; i++) {
            if ("*".equals(part[i])){
                builder.append("2");
            }else if ("".equals(part[i])){
                builder.append("0");
            }else{
                builder.append("1");
            }
        }
        return builder.toString();
    }
}
