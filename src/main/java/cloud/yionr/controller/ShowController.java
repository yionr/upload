package cloud.yionr.controller;

import cloud.yionr.common.ServerFileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowController {
    @Autowired
    ServerFileTool serverFileTool;

    @RequestMapping("/show")
    public String show(){
        System.out.println(serverFileTool.getFileListWithSuf().toString());
        return serverFileTool.getFileListWithSuf().toString();
    }
}
