package com.duheng.hsbot.listener;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.annotation.GroupMessageHandler;
import com.zhuangxv.bot.annotation.GroupUserAddHandler;
import com.zhuangxv.bot.core.Group;
import com.zhuangxv.bot.core.Member;
import com.zhuangxv.bot.injector.object.UserAddMessage;
import com.zhuangxv.bot.message.MessageChain;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class CardListener {

    @Value("${driverPath}")
    private String driverPath;
    @Value("${imgPath}")
    private String imgPath;
    @Value("${cardUrl}")
    private String cardUrl;
    @Value("${groupImagePaht}")
    private String groupImagePaht;


    @GroupMessageHandler(groupIds = {}, regex = "^泽.*")
    public void talk(Group group, Member member) throws Exception {
        MessageChain mc = new MessageChain();
/*        mc.at(member.getUserId()).text("\n")
                .text("1. @我 + 聊天内容").text("\n")
                .text("2. 点歌大风吹").text("\n")
                .text("3. 网红+视频（网红/明星/热舞/风景/游戏/动物）").text("\n")
                .text("4. 定时任务：每日早安，晚安【不可操作】").text("\n")
                .text("5. 定时任务：每日八杯水【不可操作】").text("\n")
                .text("6. 帮助");*/
        mc.at(member.getUserId()).text("泽老师牛逼!");
        group.sendMessage(mc);
    }

    @GroupMessageHandler(groupIds = {}, regex = "[\\r\\n|\\d|\\w|\\s|\\D|\\W|\\S]*(AAE)[\\r\\n|\\d|\\w|\\s|\\D|\\W|\\S]*")
    public void card(Group group, Member member, MessageChain mc) throws Exception {

        String s = mc.toMessageString();
        List<Map<String,Object>> list = JSONObject.parseObject(s, List.class);
        Map<String,String> data = (Map<String, String>) list.get(0).get("data");
        String text = data.get("text");
        String two = "AAE"+ text.split("AAE")[1];
        String code = null;
        String regFileName = "AAE.*[QAA|AA=|AQA|AA==|QQA]";
        // 匹配当前正则表达式
        Matcher matcher = Pattern.compile(regFileName).matcher(two);
        // 定义当前文件的文件名称
        // 判断是否可以找到匹配正则表达式的字符
        if (matcher.find()) {
            // 将匹配当前正则表达式的字符串即文件名称进行赋值
            code = matcher.group();
        }
        if (code!=null){
            System.out.println(code.length());
            MessageChain mcCall = new MessageChain();
            String img = getImg(code);
            mcCall.at(member.getUserId()).image(img).text(code);
            group.sendMessage(mcCall);
        }

    }

    /**
     * 入群欢迎
     * @param group
     * @param um
     * @throws Exception
     */
    @GroupUserAddHandler(groupIds={})
    public void userAdd(Group group, UserAddMessage um) throws Exception {
        MessageChain mc = new MessageChain();
        Long userId = um.getUserId();
        Member member = group.getMember(userId);
        mc.text("热烈欢迎【" + member.getNickname() + "】")
                .text("加入【" + group.getGroupName()+"】")
                .text("进群请喊泽老师牛逼!!!!")
                .image(groupImagePaht);
        group.sendMessage(mc);

    }

    /**
     * 生成随机数
     * @return
     */
    private String randomId(){
        return (int)(Math.random()*220+1) + "";
    }




    public String getImg(String code){
        String url= cardUrl +code;
        String screenshotPath = null;
        //这里设置下载的驱动路径，Windows对应chromedriver.exe Linux对应chromedriver，具体路径看你把驱动放在哪
        System.setProperty("webdriver.chrome.driver", driverPath);
      ChromeOptions options = new ChromeOptions();
   /*       //ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //截屏支持
        options.setCapability("takesScreenshot", true);
        //css搜索支持
        options.setCapability("cssSelectorsEnabled", true);
        options.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(options);*/
        //ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //截屏支持
        options.setCapability("takesScreenshot", true);
        //css搜索支持
        options.setCapability("cssSelectorsEnabled", true);
        //设置浏览器参数
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(options);
        //设置超时，避免有些内容加载过慢导致截不到图
        driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.MINUTES);
        driver.manage().timeouts().setScriptTimeout(3, TimeUnit.MINUTES);
        try {
            //设置需要访问的地址
            driver.get(url);
//                //获取高度和宽度一定要在设置URL之后，不然会导致获取不到页面真实的宽高；
//                Long width = (Long)driver.executeScript("return document.documentElement.scrollWidth");
//                Long height =(Long) driver.executeScript("return document.documentElement.scrollHeight");
            Long width = 900L;
            Long height = 1000L;
            System.out.println("高度："+height);
            //设置窗口宽高，设置后才能截全
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
            //设置截图文件保存的路径
            screenshotPath = imgPath;
            File srcFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        }catch (Exception e){
            throw new RuntimeException("截图失败",e);
        }finally {
            driver.quit();
        }
        return "file:///"+screenshotPath;
    }
}
