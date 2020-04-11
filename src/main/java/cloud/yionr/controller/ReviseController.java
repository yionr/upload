package cloud.yionr.controller;

import cloud.yionr.common.DateTool;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.DayOfWeek;

@RestController
public class ReviseController {
    @Autowired
    StudentService studentService;

    @Autowired
    DateTool dateTool;

    private Logger logger = Logger.getLogger(ReviseController.class);

    @RequestMapping("revise")
    public String revise(String id, String name, String oFName, HttpServletRequest request) throws UnsupportedEncodingException {

        if (!dateTool.isWorkingDay()) {
                logger.warn("用户ip: " + request.getRemoteAddr() + " 文件名:  " + oFName + "  试图在禁止上传的时间点上传文件，已拦截");
                return "over";
        }

        logger.info("用户ip: " + request.getRemoteAddr() + " 提交文件,完整文件名称为: " + oFName + " 开始检测！");
        logger.info("此控制器提取的id为: " + id + " ,name为: " + name);

        Student student = studentService.FindByName(name);
//        如果根据姓名找到了这个学生的话，进一步判断正确学号
        if (student != null){
            //如果学生输入的学号和正确的学号不一致，则返回正确学号，提醒用户
            if (id.length() == 11)
                if (!student.getId().equals(id)){
                    logger.warn(name + "学号填写错误，提示纠正");
                    return "correctId:" + student.getId();
                }
//            FIXME 文件名是：19吴伟 这种情况 会直接放行，不提示学号错误，但实际上这个属于例外情况，应该提示学号错误的，不过问题不严重
            if (id.length() == 2)
                if (!student.getId().substring(9,11).equals(id)){
                    logger.warn(name + "学号填写错误，提示纠正");
                    return "correctId:" + student.getId().substring(9,11);
                }
            if (id.equals("null"))
                logger.warn("无法识别此命名格式下的id，但是已经根据姓名找到了id(这条日志会伴随下面的'格式完全正确'一同打印出)");
        }
//        如果姓名无法识别，则根据学号来查找
        else{
            if (id.length() == 11)
                student = studentService.FindById(id);
            else if (id.length() == 2)
                student = studentService.FindByLastId("2017%" + id);
        }
//        如果到这还没有找到的话，就说明，这个文件名，不是以学号开头，也不是以姓名结尾的，暂时无法处理
        if (student == null){
//            如果用户提交的文件是无规律的话，查询是否有cookie，用cookie来处理
            String cookieUserInfo;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie :
                    cookies) {
                if (cookie.getName().equals("userInfo")){
//                发现了之前为这个浏览器缓存的cookie数据
//                    userInfo为`学号.姓名`格式
                    cookieUserInfo = URLDecoder.decode(cookie.getValue(),"utf-8");
                    logger.info("无法识别文件名，但是发现cookie，userInfo: " + cookieUserInfo);
                    return "cookie:" + cookieUserInfo;
                }
            }
//            如果没有发现cookie，则让用户手动填写表单
            logger.warn("无法识别！请求用户手动填写表单");
            return "false";
        }
        else{
//        到这里，表示根据学号找到了学生,但是name错了
            if (!student.getName().equals(name)){
                logger.warn(id + "姓名填写错误，提示纠正");
                return "correctName:" + student.getName();
            }
        }
//        到这里，代表格式完全正确
        logger.info("格式完全正确，不需要纠正！");
//        如果是16级的学生
        if (student.getId().startsWith("2016"))
            return student.getId() + student.getName();
//        不是16级的话
        return student.getId().substring(9,11) + student.getName();
    }
}