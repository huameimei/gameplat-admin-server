package com.gameplat.admin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** 扩展工具类 */
public class DateUtil2 extends com.gameplat.base.common.util.DateUtil {

  /**
   * 获取当前时间星期
   *
   * @param dt
   * @return
   */
  public static int getWeekNumOfDate(Date dt) {
    int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
    Calendar cal = Calendar.getInstance();
    cal.setTime(dt);
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0) {
      w = 0;
    }
    return weekDays[w];
  }

  /**
   * 获取某段时间内的周一（二等等）的日期
   *
   * @param dataBegin 开始日期
   * @param dataEnd 结束日期
   * @param weekDays 获取周几，1－6代表周一到周六。0代表周日
   * @return 返回日期List
   */
  public static List<String> getDayOfWeekWithinDateInterval(
      String dataBegin, String dataEnd, int weekDays) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<String> dateResult = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    String[] dateInterval = {dataBegin, dataEnd};
    Date[] dates = new Date[dateInterval.length];
    for (int i = 0; i < dateInterval.length; i++) {
      String[] ymd = dateInterval[i].split("[^\\d]+");
      cal.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]) - 1, Integer.parseInt(ymd[2]));
      dates[i] = cal.getTime();
    }
    for (Date date = dates[0]; date.compareTo(dates[1]) <= 0; ) {
      cal.setTime(date);
      if (cal.get(Calendar.DAY_OF_WEEK) - 1 == weekDays) {
        String format = sdf.format(date);
        dateResult.add(format);
      }
      cal.add(Calendar.DATE, 1);
      date = cal.getTime();
    }
    return dateResult;
  }

  /**
   * @param @param date1
   * @param @param date2
   * @param @return 参数
   * @return boolean 返回类型
   * @throws @Description: (是否是同一天)
   * @author huangjian
   * @date 2017年12月12日 下午12:04:14
   */
  public static boolean isSameDate(Date date1, Date date2) {
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);

    boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    boolean isSameDate =
        isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

    return isSameDate;
  }

  /**
   * 获取时间段内每月的最后一天
   *
   * @param dBegin
   * @param dEnd
   * @return
   */
  public static List<Date> findEndDates(Date dBegin, Date dEnd) {
    //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    List<Date> lDate = new ArrayList<>();
    //        lDate.add(simpleDateFormat.format(dBegin));
    Calendar calBegin = Calendar.getInstance();
    // 使用给定的 Date 设置此 Calendar 的时间
    calBegin.setTime(dBegin);
    Calendar calEnd = Calendar.getInstance();
    // 使用给定的 Date 设置此 Calendar 的时间
    calEnd.setTime(dEnd);
    // 测试此日期是否在指定日期之后

    Calendar calBegin_1 = (Calendar) calBegin.clone();
    // 把月底提出来
    calBegin_1.add(Calendar.MONDAY, 1);
    calBegin_1.set(Calendar.DAY_OF_MONTH, 0);

    boolean endMouth = false;
    while (dEnd.after(calBegin.getTime())) {
      if (calBegin_1.equals(calBegin) || endMouth) {
        // 保存最后一天
        lDate.add(calBegin.getTime());

        // 保存第一天
        calBegin.add(Calendar.MONTH, 1);
        //                calBegin.set(Calendar.DAY_OF_MONTH,1);
        //                lDate.add(simpleDateFormat.format(calBegin.getTime()));
        //
        calBegin.add(Calendar.MONDAY, 1);
        calBegin.set(Calendar.DAY_OF_MONTH, 0);
        //
        endMouth = true;

      } else {
        calBegin.add(Calendar.DAY_OF_MONTH, 1);
      }
    }
    //        lDate.add(dEnd);
    //        lDate.remove(0);
    return lDate;
  }

  /**
   * 被比较的时间是否大于当前时间
   *
   * @param compareDate 被比较的时间
   * @return
   * @throws ParseException
   */
  public static boolean compareCurrentDate(Date compareDate) {
    Date now = new Date();
    boolean greaterThan = now.getTime() < compareDate.getTime();
    return greaterThan;
  }

  /**
   * 被比较的时间是否大于当前时间
   *
   * @param compareDate 被比较的时间
   * @return
   * @throws ParseException
   */
  public static boolean compareCurrentDateLess(Date compareDate) {
    Date now = new Date();
    boolean greaterThan = now.getTime() > compareDate.getTime();
    return greaterThan;
  }
}
