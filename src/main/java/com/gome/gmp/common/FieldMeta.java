package com.gome.gmp.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，处理变更记录中字段的相关信息
 *
 * @author wubin
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ ElementType.FIELD }) // 定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented // 说明该注解将被包含在javadoc中
public @interface FieldMeta {

	/**
	 * 字段名称
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 是否记录日志
	 * 
	 * @return
	 */
	boolean isLog() default true;
}
