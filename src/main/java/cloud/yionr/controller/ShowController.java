package cloud.yionr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;

@RestController
public class ShowController {
    @RequestMapping("/show")
    public String show(HttpServletRequest req){
        //不断读指定文件夹里面的文件,每读一个提取文件名放入数组
        File file = new File(req.getServletContext().getRealPath("WEB-INF/homeWork"));
        File[] files = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File fileitem :
                files) {
            System.out.println(fileitem.getName());
            arrayList.add(fileitem.getName());
        }
        System.out.println(arrayList.toString());
        return arrayList.toString();
    }
}
