package com.dbolshak.testtask.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on bean's methods which must be called after setting a base_dir.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PostSetDir {
    /**
     * Almost methods marked by PostSetDir is used only to serve classes which are need for cache implementation of TopicDao.
     *
     * @return true (as a default) when annotated method is used only by cache.
     */
    boolean usedByCacheDao() default true;
}
