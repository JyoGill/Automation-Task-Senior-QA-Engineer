package StepDefinition;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "test.java",
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json"
        },
        monochrome = true
)
public class APITestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
