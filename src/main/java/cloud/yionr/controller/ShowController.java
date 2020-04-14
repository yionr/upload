package cloud.yionr.controller;

import cloud.yionr.common.Log4jUtils;
import cloud.yionr.common.ServerFileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowController {
    @Autowired
    ServerFileTool serverFileTool;

    @Autowired
    Log4jUtils log4jUtils;

    @RequestMapping("/show")
    public String show(){
        log4jUtils.addVisitCount();
        return serverFileTool.getFileList().toString();
    }

    @RequestMapping("/showVisitCount")
    public int showVisitCount(){
        return log4jUtils.getVisitCount();
    }
}
