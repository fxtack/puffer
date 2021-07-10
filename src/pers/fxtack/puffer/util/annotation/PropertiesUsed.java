package pers.fxtack.puffer.util.annotation;

import java.lang.annotation.*;

/**
 * 这是一个非常简单的注解, 作用仅仅是为了提醒开发者该类 (使用了该注解的类), 使用了配置类 (SingletonProperties), 来进行相关配置。
 *
 * @author Fxtack
 * @version 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface PropertiesUsed {

}
