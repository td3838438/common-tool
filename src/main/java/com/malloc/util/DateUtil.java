package com.malloc.util;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tuyh3(tuyh3 @ asiainfo.com)
 * @desc	时间处理类
 * @date 2021/1/29 17:31
 * @Version
 */
public final class DateUtil {

	private static Logger logger = Logger.getLogger(DateUtil.class);
	private static String CLASS = DateUtil.class.getName();

	private DateUtil(){}

	/**
	 * 获取当前时间，以中文形式表现
	 *
	 * @return
	 * @desc yyyy年MM月dd日 hh时mm分ss秒 例如： 2018年12月29日 22时53分30秒
	 */
	public static String getNowDatetimeHan() {
		return new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒").format(new Date());
	}

	/**
	 * 获取当前时间，年月日以-分开，时分秒以:分开
	 *
	 * @return
	 * @desc yyyy-MM-dd hh:mm:ss 例如：2018-12-29 22:53:30
	 */
	public static String getNowDatetimeHeng() {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	}

	/**
	 * 获取当前时间，年月日以/分开，时分秒以:分开
	 *
	 * @return
	 * @desc yyyy/MM/dd hh:mm:ss 例如：2018/12/29 22:53:30
	 */
	public static String getNowDatetimeXie() {
		return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());
	}

	/**
	 * 获取当前时间，年月日以_分开，时分秒以:分开
	 *
	 * @return
	 * @desc yyyy_MM_dd hh:mm:ss 例如：2018_12_29 22:53:30
	 */
	public static String getNowDatetime() {
		return new SimpleDateFormat("yyyy_MM_dd hh:mm:ss").format(new Date());
	}

	/**
	 * 将日期转化为字符串，年月日以_分开，时分秒以:分开
	 *
	 * @param date
	 * @return yyyy_MM_dd hh:mm:ss
	 */
	public static String getDatetime(Date date) {
		return new SimpleDateFormat("yyyy_MM_dd hh:mm:ss").format(date);
	}

	/**
	 * 将年月日转化为字符串，以_分开
	 *
	 * @param date
	 * @return yyyy_MM_dd
	 */
	public static String getDate(Date date) {
		return new SimpleDateFormat("yyyy_MM_dd").format(date);
	}

	/**
	 * 将时分秒转化为字符串，以:分开
	 *
	 * @param date
	 * @return hh:mm:ss
	 */
	public static String gettime(Date date) {
		return new SimpleDateFormat("hh:mm:ss").format(date);
	}

	/**
	 * 通过一串字符串来获取年月日
	 *
	 * @param strDate
	 * @return
	 */
	public static Date getDate(String strDate) {
		logger.debug(">>>>>" + CLASS + ".getDate().....");
		logger.debug("1===strDate:" + strDate);
		Date date = null;
		try {
			strDate = strDate.replace('/', '_').replace('-', '_')
					.replace('.', '_').replace(',', '_').replace(':', '_');
			logger.debug("2===strDate:" + strDate);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy_MM_dd");
			try {
				date = simpleDateFormat.parse(strDate);

				logger.debug(getDate(date));
			} catch (Exception e) {
				if (date == null) {
					logger.debug("格式有误！");
					throw new Exception("格式有误！");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getDate().");
			return date;
		}
	}

	/**
	 * 通过一串字符串来获取时分秒
	 *
	 * @param strTime
	 * @return
	 */
	public static Date getTime(String strTime) {
		logger.debug(">>>>>" + CLASS + ".getTime().....");
		logger.debug("strTime:" + strTime);
		Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
			try {
				date = simpleDateFormat.parse(strTime);

				logger.debug(gettime(date));
			} catch (ParseException e) {
				if (date == null) {
					logger.debug("格式有误！");
					throw new Exception("格式有误！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getTime().");
			return date;
		}
	}

	/**
	 * 通过一串字符串来获取时间
	 *
	 * @param strDatetime
	 * @return
	 */
	public static Date getDatetime(String strDatetime) {
		logger.debug(">>>>>" + CLASS + ".getDatetime().....");
		logger.debug("strDatetime:" + strDatetime);
		Date datetime = null;
		try {
			String[] str = strDatetime.trim().split(" ");

			if (str.length == 2) {
				Date date = getDate(str[0]);
				Date time = getTime(str[1]);
				datetime = new Date(date.getTime() + time.getTime());
			} else if (str.length == 1) {
				if (strDatetime.trim().length() == 10) {
					datetime = getDate(str[0]);
				} else if (strDatetime.trim().length() == 8) {
					datetime = getTime(str[0]);
				} else {
					logger.debug("格式有误！");
					throw new Exception("格式有误！");
				}
			} else {
				logger.debug("格式有误！");
				throw new Exception("格式有误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getDatetime().");
			return datetime;
		}
	}

	/**
	 * 通过两个字符串时间来获取时间差
	 *
	 * @param strBegin
	 * @param strEnd
	 * @return
	 */
	public static Date getBeginEndDatetime(String strBegin, String strEnd) {
		logger.debug(">>>>>" + CLASS + ".getBeginEndDatetime().....");
		logger.debug("strBegin:" + strBegin);
		logger.debug("strEnd:" + strEnd);
		Date date = null;
		try {
			Date beginDate = getDatetime(strBegin);
			Date endDate = getDatetime(strEnd);
			long time = endDate.getTime() - beginDate.getTime();
			date = new Date(time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginEndDatetime().");
			return date;
		}
	}

	/**
	 * 通过两个时间Date对象来获取时间差
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static Date getBeginEndDatetime(Date beginDate, Date endDate) {
		logger.debug(">>>>>" + CLASS + ".getBeginEndDatetime().....");
		logger.debug("beginDate:" + beginDate);
		logger.debug("endDate:" + endDate);
		Date date = null;
		try {
			long time = endDate.getTime() - beginDate.getTime();
			date = new Date(time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginEndDatetime().");
			return date;
		}
	}

	/**
	 * 通过两个时间Object对象来获取时间差
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Date getBeginEndDatetime(Object begin, Object end) {
		logger.debug(">>>>>" + CLASS + ".getBeginEndDatetime().....");
		logger.debug("begin:" + begin);
		logger.debug("end:" + end);
		Date date = null;
		try {
			if (begin instanceof Date) {
				if(end instanceof Date) {
					date = getBeginEndDatetime((Date) begin, (Date) end);
				} else if(end instanceof String) {
					date = getBeginEndDatetime((Date) begin, getDatetime((String) end));
				}
			} else if (begin instanceof String) {
				if(end instanceof String) {
					date = getBeginEndDatetime((String) begin, (String) end);
				} else if(end instanceof Date) {
					date = getBeginEndDatetime((String) begin, getDatetime((Date) end));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginEndDatetime().");
			return date;
		}
	}

	/**
	 * 通过两个字符串时间来获取时间差
	 *
	 * @param strBegin
	 * @param strEnd
	 * @return
	 */
	public static String getBeginEndString(String strBegin, String strEnd) {
		logger.debug(">>>>>" + CLASS + ".getBeginEndString().....");
		logger.debug("strBegin:" + strBegin);
		logger.debug("strEnd:" + strEnd);
		String strTime = null;
		try {
			long time = getBeginEndDatetime(strBegin, strEnd).getTime();
			long secend = time / 1000;
			if (secend < 60) {
				strTime = secend + "秒";
			} else if (secend < 60 * 60) {
				strTime = (secend / 60) + "分" + (secend % 60) + "秒";
			} else if (secend < 60 * 60 * 24) {
				strTime = (secend / 60 / 60) + "时" + (secend / 60 % 60) + "分"
						+ (secend % 60) + "秒";
			} else if (secend >= 60 * 60 * 24) {
				strTime = (secend / 60 / 60 / 24) + "天"
						+ (secend / 60 / 60 % 24) + "时" + (secend / 60 % 60)
						+ "分" + (secend % 60) + "秒";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginEndString().");
			return strTime;
		}
	}

	/**
	 * 通过两个时间Date对象来获取时间差
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static String getBeginEndString(Date beginDate, Date endDate) {
		logger.debug(">>>>>" + CLASS + ".getBeginEndString().....");
		logger.debug("beginDate:" + beginDate);
		logger.debug("endDate:" + endDate);
		String strTime = null;
		try {
			long time = getBeginEndDatetime(beginDate, endDate).getTime();
			long secend = time / 1000;
			if (secend < 60) {
				strTime = secend + "秒";
			} else if (secend < 60 * 60) {
				strTime = (secend / 60) + "分" + (secend % 60) + "秒";
			} else if (secend < 60 * 60 * 24) {
				strTime = (secend / 60 / 60) + "时" + (secend / 60 % 60) + "分"
						+ (secend % 60) + "秒";
			} else if (secend >= 60 * 60 * 24) {
				strTime = (secend / 60 / 60 / 24) + "天"
						+ (secend / 60 / 60 % 24) + "时" + (secend / 60 % 60)
						+ "分" + (secend % 60) + "秒";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginEndString().");
			return strTime;
		}
	}

	/**
	 * 通过两个时间Object对象来获取时间差
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String getBeginEndString(Object begin, Object end) {
		logger.debug(">>>>>" + CLASS + ".getBeginEndString().....");
		logger.debug("begin:" + begin);
		logger.debug("end:" + end);
		String strDate = null;
		try {
			if (begin instanceof Date) {
				if(end instanceof Date) {
					strDate = getBeginEndString((Date) begin, (Date) end);
				} else if(end instanceof String) {
					strDate = getBeginEndString((Date) begin, getDatetime((String) end));
				}
			} else if (begin instanceof String) {
				if(end instanceof String) {
					strDate = getBeginEndString((String) begin, (String) end);
				} else if(end instanceof Date) {
					strDate = getBeginEndString((String) begin, getDatetime((Date) end));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginEndString().");
			return strDate;
		}
	}

	/**
	 * 通过字符串时间来获取与当前时间的时间差
	 *
	 * @param strBegin
	 * @return
	 */
	public static Date getBeginNowDatetime(String strBegin) {
		logger.debug(">>>>>" + CLASS + ".getBeginNowDatetime().....");
		logger.debug("strBegin:" + strBegin);
		Date date = null;
		try {
			date = getBeginEndDatetime(strBegin, getNowDatetime());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginNowDatetime().");
			return date;
		}
	}

	/**
	 * 通过时间Date对象来获取与当前时间的时间差
	 *
	 * @param beginDate
	 * @return
	 */
	public static Date getBeginNowDatetime(Date beginDate) {
		logger.debug(">>>>>" + CLASS + ".getBeginNowDatetime().....");
		logger.debug("beginDate:" + beginDate);
		Date date = null;
		try {
			date = getBeginEndDatetime(beginDate, new Date());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginNowDatetime().");
			return date;
		}
	}

	/**
	 * 通过时间Object对象来获取与当前时间的时间差
	 *
	 * @param begin
	 * @return
	 */
	public static Date getBeginNowDatetime(Object begin) {
		logger.debug(">>>>>" + CLASS + ".getBeginNowDatetime().....");
		logger.debug("begin:" + begin);
		Date date = null;
		try {
			if (begin instanceof Date) {
				date = getBeginNowDatetime((Date) begin);
			} else if (begin instanceof String) {
				date = getBeginNowDatetime((String) begin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginNowDatetime().");
			return date;
		}
	}

	/**
	 * 通过字符串时间来获取与当前时间的时间差
	 *
	 * @param strBegin
	 * @return
	 */
	public static String getBeginNowString(String strBegin) {
		logger.debug(">>>>>" + CLASS + ".getBeginNowString().....");
		logger.debug("strBegin:" + strBegin);
		String strDate = null;
		try {
			strDate = getBeginEndString(strBegin, getNowDatetime());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginNowString().");
			return strDate;
		}
	}

	/**
	 * 通过时间Date对象来获取与当前时间的时间差
	 *
	 * @param beginDate
	 * @return
	 */
	public static String getBeginNowString(Date beginDate) {
		logger.debug(">>>>>" + CLASS + ".getBeginNowString().....");
		logger.debug("beginDate:" + beginDate);
		String strDate = null;
		try {
			strDate = getBeginEndString(beginDate, new Date());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginNowString().");
			return strDate;
		}
	}

	/**
	 * 通过时间Object对象来获取与当前时间的时间差
	 *
	 * @param begin
	 * @return
	 */
	public static String getBeginNowString(Object begin) {
		logger.debug(">>>>>" + CLASS + ".getBeginNowString().....");
		logger.debug("begin:" + begin);
		String strDate = null;
		try {
			if (begin instanceof Date) {
				strDate = getBeginNowString((Date) begin);
			} else if (begin instanceof String) {
				strDate = getBeginNowString((String) begin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASS + ".getBeginNowString().");
			return strDate;
		}
	}

	public static void main(String[] args) {
		// System.out.println(getNowDatetime_());
		// getDate("2018/3/2");
		// getTime("22:27:30");
		// System.out.println(getDatetime(getDatetime("   2018-12-29 22:23:30    ")));

		/*
		 * // 记录debug级别的信息 logger.debug("This is debug message."); //
		 * 记录info级别的信息 logger.info("This is info message."); // 记录error级别的信息
		 * logger.error("This is error message.");
		 */

		System.out.println(getBeginEndString("2018-12-30 13:00:00",
				"2018-12-31 14:03:00"));
		//System.gc();

	}

}
