package cloud.yionr.controller;

import cloud.yionr.Exception.SqlQueryException;
import cloud.yionr.common.DateTool;
import cloud.yionr.entity.Student;
import cloud.yionr.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class ReviseController {
    @Autowired
    StudentService studentService;

    @Autowired
    DateTool dateTool;

    private Logger logger = Logger.getLogger(ReviseController.class);

    @PostMapping("revise")
    public String revise(String id, String name, String oFName, HttpServletRequest request) throws UnsupportedEncodingException, SqlQueryException {

        logger.info("---------------START---------------");

        if (!dateTool.isWorkingDay()) {
            logger.warn("用户ip: " + request.getRemoteAddr() + " 文件名:  " + oFName + "  试图在禁止上传的时间点上传文件，已拦截");
            logger.info("-----------------FAIL----------------");
            logger.info("###########################################################");
                return "over";
        }

        logger.info("用户ip: " + request.getRemoteAddr() + " 提交文件,完整文件名称为: " + oFName + " 开始检测！");
        logger.info("此控制器提取的id为: " + id + " ,name为: " + name);
//        try {
//            System.out.println(0 / 0);
//        } catch (Exception e) {
//            throw new IdNotMatchException("know exception!");
//        }
        Student student;
        try {
            student = studentService.FindByName(name);  //这里，如果name是null的话，是不会报错的
        } catch (Exception e) {
            logger.warn("查询数据库过程中出现异常!");
            logger.info("---------------FAIL---------------");
            logger.info("###########################################################");
            throw new SqlQueryException("查询数据库过程中出现异常!");
        }
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
            if (id.length() == 2 || id.length() == 11)
                student = studentService.FindById(id);
            else{
//如果学号不是2 or 11 位，则认为这个文件命名无法接受，接下来查cookie
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
//            如果没有根据学号找到，是不会到达这里的（已经被上一个else return了）
            if (!student.getName().equals(name)){
//        到这里，表示根据学号找到了学生,但是name错了
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