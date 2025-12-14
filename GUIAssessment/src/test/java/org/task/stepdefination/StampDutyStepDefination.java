package org.task.stepdefination;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.task.pages.StampDutyPages;
import org.testng.Assert;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.Set;

public class StampDutyStepDefination {
    //    @Steps
    StampDutyPages steps;
//
//    @Given("^User is on service NSW check motor vehicle stamp duty page$")
//    public void userIsOnCheckMotorVehicleStampDutyServiceNSWPage() {
//
//        steps.userIsOnCheckMotorVehicleStampDutyServiceNSWPage();
//    }

    private WebDriver driver;
    private StampDutyPages stampDutyPages;
    private Capabilities options;

//    @Before
//    public void setup(){
//
////        System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\\\JyotiGill\\\\chrome\\\\win64-114.0.5735.133\\\\chrome-win64\\\\chrome.exe");
////        driver = new ChromeDriver();
//        ChromeOptions options = new ChromeOptions();
//        this.options = options;
//        driver = new ChromeDriver(options);
//    }


    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup(); // auto-resolves a compatible driver
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
    }



    @After
    public void teardown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Warning: teardown failed: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }


    @Given("^User is on service NSW check motor vehicle stamp duty page$")
    public void userIsOnCheckMotorVehicleStampDutyServiceNSWPage() {
        driver.get("https://www.service.nsw.gov.au/transaction/check-motor-vehicle-stamp-duty");
        stampDutyPages = new StampDutyPages(driver);
    }



//    @When("User click on check online button")
//    public void userClickOnCheckOnlineButton() {
////        steps.checkOnlineButton();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
//        getDriver().findElement(By.xpath("//a[normalize-space(.)='Check online']")).click();
//
//    }
//
//    @Then("User redirected to another site of Revenue NSW calculator")
//    public void userRedirectedToAnotherSiteOfRevenueNSWCalculator() {
//
//        String expectedUrl = "https://www.apps09.revenue.nsw.gov.au/erevenue/calculators/motorsimple.php";
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.urlToBe(expectedUrl));
//        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "User is not on the Revenue NSW calculator page.");
//    }


    @When("User click on check online button")
    public void userClickOnCheckOnlineButton() {
        By checkOnline = By.xpath("//a[@role='button' and contains(@href,'/erevenue/calculators/motorsimple.php')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(checkOnline));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn));
            btn.click();
        } catch (ElementClickInterceptedException e) {
            // fallback
            new Actions(driver).moveToElement(btn).pause(Duration.ofMillis(200)).click().perform();
        }
    }

    @Then("User redirected to another site of Revenue NSW calculator")
    public void userRedirectedToAnotherSiteOfRevenueNSWCalculator() {
        String expectedUrl = "https://www.apps09.revenue.nsw.gov.au/erevenue/calculators/motorsimple.php";

        // switch to new tab if opened
        Set<String> all = driver.getWindowHandles();
        if (all.size() > 1) {
            driver.switchTo().window(all.stream().reduce((first, second) -> second).get());
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/erevenue/calculators/motorsimple.php"));
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "User is not on the Revenue NSW calculator page.");
    }

    @Then("User click on {string} for registration for a passenger vehicle")
    public void userClickOnYesForRegistrationForAPassengerVehicle(String option) {

        String forValue;
        switch (option.trim().toLowerCase()) {
            case "yes":
                forValue = "passenger_Y";
                break;
            case "no":
                forValue = "passenger_N";
                break;
            default:
                throw new IllegalArgumentException("Unsupported option: " + option + ". Use 'Yes' or 'No'.");
        }

        // Robust locator: click the LABEL rather than the hidden INPUT
        By labelLocator = By.cssSelector("label.nsw-form-radio__label[for='" + forValue + "']");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(labelLocator));

        // Ensure the element is in view (fixed/sticky headers can intercept clicks)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", label);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(label));
            label.click();
        } catch (ElementClickInterceptedException e) {
            // Fallback to Actions, then JS click if still intercepted
            try {
                new Actions(driver).moveToElement(label).pause(Duration.ofMillis(150)).click().perform();
            } catch (Exception ignored) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label);
            }
        }


    }


    @And("User provide value {string} in purchase price")
    public void userProvideValueInPurchasePrice(String amount) {
        By purchasePriceField = By.id("purchasePrice");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(purchasePriceField));

        // Scroll into view if needed
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input);

        // Clear and enter value
        input.clear();
        input.sendKeys(amount);
    }


    @Then("User click on Calculate button")
    public void userClickOnCalculateButton() {

        By calculateButton = By.xpath("//button[@type='submit' and normalize-space()='Calculate']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(calculateButton));

        // Scroll into view if needed
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            // Fallback to Actions or JS click
            new Actions(driver).moveToElement(button).pause(Duration.ofMillis(150)).click().perform();
        }


    }

    @And("User verify popup shows passenger {string}, purchase price {string}, and duty payable {string}")
    public void userVerifyPopupWithDynamicValues(String passengerOptionRaw, String purchaseRaw, String dutyRaw) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1) Wait for modal
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-dialog")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", modal);

        // 2) Verify header and section (static text)
        String header = normalize(modal.findElement(By.cssSelector(".modal-header .modal-title")).getText());
        if (!header.equalsIgnoreCase("Calculation")) {
            throw new AssertionError("Expected modal header 'Calculation' but found: " + header);
        }
        WebElement sectionHeader = modal.findElement(By.xpath(".//div[contains(@class,'modal-body')]//h3 | .//div[contains(@class,'modal-body')]//h4"));
        String section = normalize(sectionHeader.getText());
        if (!section.equalsIgnoreCase("Motor vehicle registration")) {
            throw new AssertionError("Expected section 'Motor vehicle registration' but found: " + section);
        }

        // 3) Container holding details/result
        WebElement detailsContainer = modal.findElement(By.xpath(
                ".//div[contains(@class,'modal-body')]//div[contains(@class,'card') or contains(@class,'alert') or contains(@class,'panel')]"
        ));

        // 4) Normalize inputs
        String passenger = normalizeOption(passengerOptionRaw);          // "yes" / "no" (case-insensitive)
        String expectedPassenger = passenger.equals("yes") ? "Yes" : "No";

        // Purchase & duty: accept "45000" or "45,000" → format to "$45,000.00"
        String expectedPurchase = formatAud(parseWholeDollars(purchaseRaw));
        String expectedDuty     = formatAud(parseWholeDollars(dutyRaw));

        // 5) Assert rows in the popup
        assertDetailRow(detailsContainer,
                "Is this registration for a passenger vehicle?",
                expectedPassenger);

        assertDetailRow(detailsContainer,
                "Purchase price or value",
                expectedPurchase);

        assertDetailRow(detailsContainer,
                "Duty payable",
                expectedDuty);

        // 6) Optional: check Close button exists
        WebElement closeBtn = modal.findElement(By.xpath(".//button[normalize-space()='Close']"));
        if (!closeBtn.isDisplayed()) {
            throw new AssertionError("Close button not visible on modal.");
        }
    }

    // ---------- Helpers ----------
    private String normalize(String s) {
        return s == null ? "" : s.replace("\u00A0", " ").trim().replaceAll("\\s{2,}", " ");
    }

    private String normalizeOption(String raw) {
        if (raw == null) return "";
        return raw.replace("\u201C", "\"")
                .replace("\u201D", "\"")
                .trim()
                .toLowerCase();
    }

    private long parseWholeDollars(String raw) {
        if (raw == null) throw new IllegalArgumentException("Amount cannot be null");
        // Strip everything except digits; “45,000”, “$45,000.00” → 45000
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) throw new IllegalArgumentException("Amount has no digits: " + raw);
        return Long.parseLong(digits);
    }

    private String formatAud(long amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "AU"));
        // Ensure trailing .00 for whole dollars (AU site shows cents in your screenshot)
        return nf.format(amount);
    }

    private void assertDetailRow(WebElement container, String labelText, String expectedValue) {
        String xpath = ".//tr[.//td[normalize-space()='" + labelText + "']]//td[last()]"
                + " | .//div[contains(@class,'row')][.//div[normalize-space()='" + labelText + "']]//div[last()]"
                + " | .//*[self::dt or self::span][normalize-space()='" + labelText + "']/following-sibling::*[1]";
        WebElement valueEl = container.findElement(By.xpath(xpath));
        String actual = normalize(valueEl.getText());

        if (!actual.equalsIgnoreCase(expectedValue)) {
            throw new AssertionError("For '" + labelText + "' expected '" + expectedValue + "' but found '" + actual + "'.");
        }
    }


}

