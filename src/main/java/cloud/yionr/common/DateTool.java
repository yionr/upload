package cloud.yionr.common;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class DateTool {

//    不把now提取出来是，我觉得可能会存在隐患，就DateTool是用注入的方式创建对象的，那么应该是不会多次创建的吧。。算了我还是不太理解底层
    public int getWeek(){
        //获取当前周（相对于开学）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDate = LocalDateTime.of(2020,3,4,0,0);
        //这里加上1:原本是21-1 = 20 /7 = 2 导致21号归档到第三周 而22号归档到第四周，加上一偏移之后，周次正确
        return (now.getDayOfYear() + 1 - baseDate.getDayOfYear())/7 + 1;
    }

    public DayOfWeek getWeekDay(){
        LocalDateTime now = LocalDateTime.now();
        return now.getDayOfWeek();
    }

    public int getHour(){
        LocalDateTime now = LocalDateTime.now();
        return now.getHour();
    }
    public boolean isWorkingDay(){
//        周二8点至周五12点为工作日
        if (getWeekDay() == DayOfWeek.WEDNESDAY || getWeekDay() == DayOfWeek.THURSDAY )
            return true;
        else if (getWeekDay() == DayOfWeek.TUESDAY)
            if (getHour() > 8)
                return true;
        else if (getWeekDay() == DayOfWeek.FRIDAY)
            if (getHour() < 12)
                return true;
            return false;
    }
}
