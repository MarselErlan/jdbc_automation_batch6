
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"html:target/cucumberReport.html", "json:target/testReport.json"},
        glue = {""},
        features = "/Users/macbookpro/Desktop/HelenProject/jdbc_automation_batch6/src/test/resources/features",
        tags = "@Test",
        dryRun = false

)






public class CucumberRunner {

}
