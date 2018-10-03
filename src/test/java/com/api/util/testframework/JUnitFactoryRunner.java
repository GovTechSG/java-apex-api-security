package com.api.util.testframework;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

/**
 * @author GDS-PDD
 *
 * Custom JUnit Runner
 * This is the base class to create the runtime JUnit TC.
 */
public class JUnitFactoryRunner extends BlockJUnit4ClassRunner {
		
    protected LinkedList<FrameworkMethod> tests = new LinkedList<FrameworkMethod>();
    /**
    * Creates a customized BlockJUnit4ClassRunner to run cls.
    * It throws the InitializationError if the test class is malformed.
    **/
    public JUnitFactoryRunner(Class<?> cls) throws InitializationError {
        super(cls);
        try {
            computeTests();
        } catch (Exception e) {
        }
    } 
    /**
    * To ensure the test class constructor is called at least
    * once during testing. This is adding all the tests
    * which will be entertained as new fixture for running a test.
    *
    * @throws Exception
    */
    protected void computeTests() throws Exception {
        tests.addAll(super.computeTestMethods());
        tests.addAll(computeFactoryTests());
    }
    /**
    * Find all methods in our test class marked with @JUnitTestFactory,
    * and for each @JUnitTestFactory, find any methods marked with @JUnitFactoryTest,
    * and add them to the List.
    *
    * @return
    * @throws Exception
    */
    protected Collection<? extends FrameworkMethod> computeFactoryTests() throws Exception {
        List<JUnitFrameworkFactory> tests = new LinkedList<JUnitFrameworkFactory>();
        for (FrameworkMethod method: getTestClass().getAnnotatedMethods(JUnitTestFactory.class)) {
            if (! Modifier.isStatic(method.getMethod().getModifiers())) {
                throw new InitializationError("Exception during initialization as method must be static.");
            }
            Object instances = method.getMethod().invoke(getTestClass().getJavaClass());
            if (instances.getClass().isArray()) {
                instances = Arrays.asList((Object[]) instances);
            }
            if (! (instances instanceof Iterable<?>)) {
                instances = Collections.singletonList(instances);
            }
            for (Object instance: (Iterable<?>) instances) {
            	RuntimeTestCase tc = (RuntimeTestCase) instance;
                for (FrameworkMethod m: new TestClass(instance.getClass()).getAnnotatedMethods(JUnitFactoryTest.class)){
                	if(tc.getTestName().contains(m.getMethod().getName())){
                		tests.add(new JUnitFrameworkFactory(m.getMethod(), instance, method.getName()));
                	}	
                }
                	
            }
        }
        return tests;
    }
    /**Returns the methods that run tests.**/
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return tests;
    }
    @Override
    protected void validateInstanceMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        validateTestMethods(errors);
    }
}