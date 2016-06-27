import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;

public class Yandex {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver");
        driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void yandexSearch() throws Exception {
        String[] electronics_item = {"Наушники","Телевизоры"};
        String[] earphone_brand_array = {"Beats"};
        String[] television_brand_array = {"Samsung","LG"};
        String[][] brands = {earphone_brand_array, television_brand_array};
        String[] money_range = {"5000", "20000"};

        for (int x = 0; x < electronics_item.length; x++) {
            //Set up search
            driver.get("http://yandex.ru");
            driver.findElement(By.linkText("Маркет")).click();
            driver.findElement(By.linkText("Электроника")).click();
            //Item
            driver.findElement(By.linkText(electronics_item[x])).click();
            driver.findElement(By.linkText("Расширенный поиск →")).click();
            driver.findElement(By.id("gf-pricefrom-var")).click();
            driver.findElement(By.id("gf-pricefrom-var")).clear();
            //Money
            driver.findElement(By.id("gf-pricefrom-var")).sendKeys(money_range[x]);
            //Checkboxes
            for (int j = 0; j < brands[x].length; j++) {
                String click_brand = "//label[text()='" + brands[x][j] + "']";
                driver.findElement(By.xpath(click_brand)).click();
            }
            Thread.sleep(2000);
            //Click OK
            driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
            Thread.sleep(5000);
            //Get first Item text
            String first_television = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div[1]/div[1]/div[3]/div/div[1]/div/h3/a/span")).getText();
            System.out.println("Первый Вещь: " + first_television);
            //Get first Item HTML
            String first_television_html = driver.findElement(By.xpath("/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div[1]/div[1]/div[3]/div/div[1]/div/h3/a")).getAttribute("href");
            //Prints out list of 7 item
            System.out.println("####### List of Some Stuff ######");
            for (int i = 1; i < 8; i++) {
                try {
                    //Тут примерное так идет хpath
                    //div[1]/div[1],2,3
                    //потом реклама, потом по хорошему еше 7
                    //div[3]/div[1],2,3,4,5,6,7
                    //может не самый лудче способ проверить, но вроде работает, для демонстрации пойдет
                    //Лудчый способ будет через сss вроде:
                    //elements = findElements(By.cssSelector("snippet-card__header-text"))
                    //elements[0],elements[1] итд
                    //Но я уже сделал через xpath
                    String firstHalf = "/html/body/div[1]/div[4]/div[2]/div[1]/div[2]/div[3]/div[";
                    String secondHalf = "]/div[3]/div/div[1]/div/h3/a/span";
                    System.out.println(driver.findElement(By.xpath(firstHalf + Integer.toString(i) +secondHalf)).getText());
                } catch (Exception e) {
                    System.out.println("Less than 10 listings, number of listings(+3): " + i);
                    break;
                }
            }
            Thread.sleep(5000);
            //Open first Items page
            driver.get(first_television_html);
            //Check to see it equal to first item in list
            String searched_television = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div[1]/div/div/h1")).getText();

            if (searched_television.equals(first_television)) {
                System.out.println("Одинаковй");
            } else {
                System.out.println("Другой");
            }

            //Finished
            Thread.sleep(5000);
        }
    }
    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();

    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}