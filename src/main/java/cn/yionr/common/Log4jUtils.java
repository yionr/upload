package cn.yionr.common;

import cn.yionr.controller.UploadController;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class Log4jUtils {
    private File logFile;
    {
        Properties properties = new Properties();
        try {
            properties.load(UploadController.class.getResourceAsStream("/common.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(properties.getProperty("homeWorkRoot"));
        logFile = new File(file,"visitCount");
        if (!logFile.exists()){
            try {
                logFile.createNewFile();
                IOUtils.write("0", new FileOutputStream(logFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void addVisitCount(){
        int count;
        try {
            count = Integer.parseInt(IOUtils.toString(new FileReader(logFile)));
            IOUtils.write(++count+"",new FileOutputStream(logFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getVisitCount(){
        try {
            return Integer.parseInt(IOUtils.toString(new FileReader(logFile)));
        } catch (IOException e) {
//            目前知道，存在一定几率（基本上都是部署刚完成的时候）访问logFile，存在读取内容为空字符串""的情况
            return 0;
        }
    }
}
