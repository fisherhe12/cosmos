package org.cosmos.common.base.annatation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * 标注参数、属性、方法可为 Null
 *
 * @author fisher
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface Nullable {

}
