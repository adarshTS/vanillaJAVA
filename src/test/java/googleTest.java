import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import com.browserstack.Sdk.TestClient;

public class googleTest {

    public static void main(String[] args) {

        TestClient testClient = new TestClient()
            .setTestName("googleTest")                          
            .setTestHierarchy(Arrays.asList("test", "java", "googleTest")) 
            .setFilePath("src/test/java/googleTest.java");    

        WebDriver driver = null;

        try {
            System.out.println("Setting up ChromeDriver");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

            testClient.start();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            System.out.println("Opening https://www.google.com/");
            driver.get("https://www.google.com/");

            // Handle consent dialog if it appears
            try {
                System.out.println("Checking for consent dialog");
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement consentButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("button[id='L2AGLb'], button[aria-label*='Accept'], div[role='dialog'] button")
                ));
                if (consentButton.isDisplayed()) {
                    System.out.println("Accepting Google consent");
                    consentButton.click();
                }
            } catch (Exception ignored) {
                System.out.println("No consent dialog detected");
            }

            System.out.println("Searching for 'Browserstack'");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
            searchBox.sendKeys("Browserstack");
            searchBox.sendKeys(Keys.ENTER);

            System.out.println("Waiting for search results");
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.titleContains("Browserstack"),
                    ExpectedConditions.titleContains("BrowserStack")
            ));

            System.out.println("Clicking the first search result");
            WebElement firstResult = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("h3")));
            firstResult.click();

            System.out.println("Verifying navigation to a BrowserStack page");
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("browserstack"),
                    ExpectedConditions.titleContains("BrowserStack")
            ));

            testClient.Pass();
            System.out.println("Test passed");
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