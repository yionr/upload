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
        LocalDateTime baseDate = LocalDateTime.of(2020,3,1,0,0);
        int weekNum = (now.getDayOfYear() - baseDate.getDayOfYear())/7 + 1;
        //不断读指定文件夹里面的文件,每读一个提取文件名放入数组
        File file = new File(req.getServletContext().getRealPath("WEB-INF/homeWork/第" + weekNum + "周作业"));
        File[] files = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File fileItem :
                files) {
            System.out.println(fileItem.getName());
            arrayList.add(fileItem.getName());
        }
//        System.out.println(arrayList.toString());
        return arrayList.toString();
    }
}
