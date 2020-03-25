package cloud.yionr.controller;

import cloud.yionr.common.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class ShowController {

    @Autowired
    DateTool dateTool;

    @RequestMapping("/show")
    public String show(){

        //根据得到的周次去服务器里面查询指定文件夹，如果不存在（指定周还没有人上传，但是有人查询）则创建文件夹
//        File file = new File("/root/homeWork/" + dateTool.getWeek());
//        测试专用地址
        File file = new File("/Users/Yionr/homeWork/" + dateTool.getWeek());
        if (!file.exists())
            file.mkdirs();
        //不断读文件夹里面的文件,每读一个提取文件名放入数组
        File[] files = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File fileItem :
                files) {
            arrayList.add(fileItem.getName());
        }
        return arrayList.toString();
    }
}
