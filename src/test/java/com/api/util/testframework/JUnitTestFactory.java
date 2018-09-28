package com.api.util.testframework;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
* Here RetentionPolicy.RUNTIME annotations are to be recorded
* in the class file by the compiler and retained by the VM at
* run time, so they may be read reflectively.
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JUnitTestFactory {
}
