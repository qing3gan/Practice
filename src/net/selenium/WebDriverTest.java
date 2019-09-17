package net.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverTest {


    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--proxy-net.server=http://185.206.21.178:40000");
        options.addArguments("--user-agent=Mozilla/4.0 (compatible; MSIE 5.15; Mac_PowerPC)");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--disable-gpu");
        options.addArguments("--headless");

        System.getProperties().setProperty("webdriver.chrome.driver",
                "C:/Users/Agony/Documents/Workspace/untitled/driver/chromedriver.exe");
        WebDriver mDriver = new ChromeDriver(options);

//        mDriver.get("http://www.haiyingshuju.com");
        mDriver.get("https://www.amazon.com");
//        mDriver.get("http://www.amazon.co.uk/s?node=114881031&page=3&sort=featured-rank");
        WebElement webElement = mDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        System.out.println("....." + content);

        mDriver.close();
        mDriver.quit();
    }
}
