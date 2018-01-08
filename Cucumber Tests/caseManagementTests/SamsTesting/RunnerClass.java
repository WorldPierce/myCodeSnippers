package caseManagement.SamsTesting;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)

@CucumberOptions(
//"src/test/java/login.feature",
      features= {"src/test/java/caseManagementTests"},

      format={"pretty","html:target/Reports", "json:target/Reports/test.json"}

      )

public class RunnerClass {

}