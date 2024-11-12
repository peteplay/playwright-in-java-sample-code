package com.serenitydojo.playwright.toolshop.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key="cucumber.plugin",
        value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, " +
                "pretty, " +
                "html:target/cucumber-reports/cucumber.html, " +
                "json:target/cucumber-reports/cucumber.json")
@SelectClasspathResource("/features")
public class AcceptanceTestSuite {
}
