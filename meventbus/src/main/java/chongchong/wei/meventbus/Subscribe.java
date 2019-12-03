package chongchong.wei.meventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 包名：chongchong.wei.meventbus
 * 创建人：apple
 * 创建时间：2019-12-02 16:12
 * 描述：定义注解，在方法上使用
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.POSTING;
}
