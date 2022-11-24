package com.duheng.hsbot.test;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class GetImageTest {
    public static void main(String[] args) throws Exception {
        String url="http://43.138.43.168:8080/?code=AAECAcLcBQyU/APbkQThnwTaowTHsgTAuQTnuQTp0AS+4wTE7QSX7wSk7wQO6ukD9PYDxfsDh/0Dw4AE4aQE5aQEwKwEg8gEv9MEweMEzOQE0OQE9usFAA==";

        //这里设置下载的驱动路径，Windows对应chromedriver.exe Linux对应chromedriver，具体路径看你把驱动放在哪
        System.setProperty("webdriver.chrome.driver", "D:\\zhuomian\\images\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //截屏支持
        options.setCapability("takesScreenshot", true);
        //css搜索支持
        options.setCapability("cssSelectorsEnabled", true);
        options.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(options);
        try {
            //设置需要访问的地址
            driver.get(url);
//                //获取高度和宽度一定要在设置URL之后，不然会导致获取不到页面真实的宽高；
//                Long width = (Long)driver.executeScript("return document.documentElement.scrollWidth");
//                Long height =(Long) driver.executeScript("return document.documentElement.scrollHeight");
            Long width = 1000L;
            Long height = 600L;
            System.out.println("高度："+height);
            //设置窗口宽高，设置后才能截全
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
            //设置截图文件保存的路径
            String screenshotPath = "D:\\zhuomian\\images\\imgGG.png";
            File srcFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        }catch (Exception e){
            throw new RuntimeException("截图失败",e);
        }finally {
            driver.quit();
        }


    }
}
