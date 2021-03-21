package com.github.sdorra.jaxrstie;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JaxRsTie {

  String value() default ".*";

}
