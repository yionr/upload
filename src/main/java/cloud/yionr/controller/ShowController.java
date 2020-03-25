package cloud.yionr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class ShowController {
    @RequestMapping("/show")
    public String show(HttpServletRequest req){
        //获取当前周（相对于开学）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDate = LocalDateTime.of(2020,3,4,0,0);
//        +1 参考upload
        int weekNum = (now.getDayOfYear() + 1 - baseDate.getDayOfYear())/7 + 1;

        //根据得到的周次去服务器里面查询指定文件夹，如果不存在（指定周还没有人上传，但是有人查询）则创建文件夹
//        File file = new File(req.getServletContext().getRealPath("WEB-INF/homeWork/第" + weekNum + "周作业"));
//        File file = new File("/root/homeWork/" + weekNum);
//        测试专用地址
        File file = new File("/Users/Yionr/homeWork/" + weekNum);
        if (!file.exists())
            file.mkdirs();
        //不断读文件夹里面的文件,每读一个提取文件名放入数组
        //FIXME 讲道理，之前没有前面这个创建文件夹的步骤，这里应该会报错啊
        File[] files = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File fileItem :
                files) {
            arrayList.add(fileItem.getName());
        }
        return arrayList.toString();
    }
}
