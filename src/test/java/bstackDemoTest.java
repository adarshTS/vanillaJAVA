import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import com.browserstack.Sdk.TestClient;

public class bstackDemoTest {
    
    public static void main(String[] args) {

    /* 
        - TestClient objects helps to manually feed “what test is this?” metadata into the BrowserStack SDK so it can:
            * create a proper test entity in Test Observability (TRA)
            * correlate that test with the right file, package, and session
            * mark session name and status in Automate 
    */

         TestClient testClient = new TestClient()
                    .setTestName("bstackDemoTest")                           // MANDATORY, logical test case name.
                    .setTestHierarchy(Arrays.asList("test", "java", "bstackDemoTest")) // MANDATORY, This is the "scopes" / hierarchical locator of the test.
                    .setFilePath("src/test/java/bstackDemoTest.java");       // MANDATORY, shows the file path of the test case TRA. 

        WebDriver driver = null;

        try {

            System.out.println("Setting up ChromeDriver");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

            testClient.start();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


            System.out.println("Opening https://bstackdemo.com/");
            driver.get("https://bstackdemo.com/");


            System.out.println("Waiting for page to load");
            wait.until(ExpectedConditions.titleContains("StackDemo"));


            System.out.println("Locating Sign In element with ID 'signin'");
            WebElement signInElement = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("signin"))
            );

            System.out.println("Verifying Sign In element properties");
            System.out.println("Element text: " + signInElement.getText());
            System.out.println("Element is displayed: " + signInElement.isDisplayed());
            System.out.println("Element is enabled: " + signInElement.isEnabled());

            System.out.println("Clicking on Sign In element");
            signInElement.click();

            System.out.println("Waiting to observe result");
            Thread.sleep(3000);
            testClient.Pass();



        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
            testClient.Fail(e);

        } finally {

            if (driver != null) {
                System.out.println("Closing browser");
                driver.quit();
            }
        }
    }
}
