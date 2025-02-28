package com.brighthorizons;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "com.brighthorizons.stepdefinitions" }, plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json"
}, monochrome = true)
public class TestRunner {
    /**
     * Main method to run the Cucumber tests directly
     * You can run this class as a Java application
     */
    public static void main(String[] args) {
        System.out.println("Starting Cucumber tests...");
        Result result = JUnitCore.runClasses(TestRunner.class);

        if (result.wasSuccessful()) {
            System.out.println("All tests passed successfully!");
        } else {
            System.out.println("Tests failed! Details:");
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }

        System.out.println("Tests completed. Run count: " + result.getRunCount());
        System.out.println("Failure count: " + result.getFailureCount());
        System.out.println("Run time: " + result.getRunTime() + "ms");

        // Report location
        System.out.println("\nHTML report generated at: " + System.getProperty("user.dir")
                + "/target/cucumber-reports/cucumber-pretty.html");
    }
}