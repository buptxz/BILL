package edu.sc.csce740;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;

/**
 * This test is to test the BILLIntf.
 * Instead of testing different implementations, testing the interface is more convenient and accurate
 * as customers will be using our interface, not caring about the implementations.
 * We can add more parameters if there are more implementations of BILLIntf.
 */
@RunWith(Parameterized.class)
public class BILLIntfTest {
    @Test
    public void test() {}


    @Parameterized.Parameters
    public static Collection<Object[]> createImplementations() throws FileNotFoundException {
        return Arrays.asList(new Object[][] {
                { new BILL() }
        });
    }
}
