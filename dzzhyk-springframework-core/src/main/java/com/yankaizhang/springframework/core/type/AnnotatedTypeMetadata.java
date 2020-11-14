/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yankaizhang.springframework.core.type;

import com.yankaizhang.springframework.util.AnnotationUtils;

import java.lang.annotation.Annotation;

/**
 * 原来我们在获取注解信息的时候，需要使用反射获取
 * 这个接口实现了一些常用的获取注解类型信息的方法default实现，只要实现这个接口，每次就可以省掉自己写下面的反射语句了。
 *
 * {@code
 * 		Annotation annotation = getClass().getAnnotation(Component.class);
 * }
 *
 * 什么东西实现这个接口有用呢？在Spring里面显然Class对象和Method对象的元数据类型实现这个接口最合适
 * 这样在元数据中就直接加入了获取注解信息的功能
 *
 * 当然，为了方便，这个类我简化过了，去掉了复杂的包装类，所以使用起来还不是"太方便"
 * @author dzzhyk
 */
public interface AnnotatedTypeMetadata {

	/**
	 * 获取所有注解
	 * @return 注解列表
	 */
	Annotation[] getAnnotations();

	/**
	 * 根据指定的注解类获取某个注解对象
	 * @param annotationClass 注解类
	 * @return 注解对象
	 */
	default Annotation getAnnotation(Class<? extends Annotation> annotationClass){
		for (Annotation annotation : getAnnotations()) {
			// 这里拿到的注解annotation是代理对象，所以需要获取代理对象的目标对象类
			Class<?> annotationClazz = AnnotationUtils.getAnnotationJdkProxyTarget(annotation);
			if (annotationClazz == annotationClass){
				return annotation;
			}
		}
		return null;
	}

	/**
	 * 判断当前的数据上有没有某个注解，经常用到！
	 * @param annotationClass 注解类class
	 */
	default boolean isAnnotated(Class<? extends Annotation> annotationClass) {
		return (null != getAnnotation(annotationClass));
	}

}
