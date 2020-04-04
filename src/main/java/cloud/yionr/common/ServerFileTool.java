package cloud.yionr.common;

import cloud.yionr.controller.UploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ServerFileTool {
    @Autowired
    DateTool dateTool;


    public Properties properties;
    {
        properties = new Properties();
        try {
            properties.load(UploadController.class.getResourceAsStream("/common.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getFileListWithSuf(){
        File file = new File(properties.getProperty("homeWorkRoot") , dateTool.getWeek()+"");
        if (!file.exists())
            file.mkdirs();
        //不断读文件夹里面的文件,每读一个提取文件名放入数组
        File[] files = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File fileItem :
                files) {
            arrayList.add(fileItem.getName());
        }
        return arrayList;
    }
    public List<String> getFileListWithoutSuf(){
        List<String> fileListWithSuf = getFileListWithSuf();
        List<String> fileListWithoutSuf = new ArrayList<>();
        for (String fileItem:
        fileListWithSuf){
            String fileItemWithoutSuf = fileItem.split("\\.")[0];
            fileListWithoutSuf.add(fileItemWithoutSuf);
        }
        return fileListWithoutSuf;
    }
}
