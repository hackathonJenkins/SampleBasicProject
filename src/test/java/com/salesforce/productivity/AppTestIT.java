package com.salesforce.productivity;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

public class AppTestIT extends TestCase{

    public void testHttpCode() {
        JenkinReportGenerationUtil obj = new JenkinReportGenerationUtil();
        Assert.assertEquals(200, obj.canHitGoogle());
    }

    /**
     * Create the test case
     *
     * @param testName
     *            name of the test case
     */
    public AppTestIT(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTestIT.class);
    }
}
