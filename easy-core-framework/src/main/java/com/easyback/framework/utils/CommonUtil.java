package com.easyback.framework.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.ServiceException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtil {

	public static Double converCents2Yuan(Long price){
		if(price!=null && price>0){
			return price/100.0;
		}
		return 0.0;
	}

	/**
	 * 带 ，号字符串 配对
	 * @param str "11,12,13"
	 * @param pair "1" 返回false; "11" 返回true
	 * @return
	 */
	public static Boolean isContains(String str,String pair){

		String [] testStr = pair.split(",");
		return StrUtil.containsAnyIgnoreCase(str,testStr);

	}

	public static LocalDateTime orderLeaseEndTime(LocalDateTime leaseStartTime, Integer leaseTerm) {
		if(leaseStartTime == null) {
			return null;
		}

		if (leaseTerm.compareTo(90) >= 0) {
			return LocalDateTimeUtil.endOfDay(leaseStartTime.plusMonths(leaseTerm / 30).plusDays(-1));
		}

		return LocalDateTimeUtil.endOfDay(leaseStartTime.plusDays(leaseTerm-1));
	}

	/**
	 * 根据租赁天数和订单开始时间计算订单结束时间
	 * @param leaseStartTime 订单开始时间
	 * @param leaseTerm 租赁天数
	 * @return
	 */
	public static DateTime paymentEndTimeDay2End(Date leaseStartTime, Integer leaseTerm) {

		if(leaseStartTime == null) {
			return null;
		}

		if (leaseTerm.compareTo(90) >= 0) {
			return end2Time(DateUtil.offsetDay(DateUtil.offsetMonth(leaseStartTime, 1), -1));
		} else {
			return end2Time(DateUtil.offsetDay(leaseStartTime,leaseTerm-1));
		}
	}

	public static DateTime end2Time(DateTime time){
		String s = DateUtil.format(time, DatePattern.NORM_DATE_PATTERN) + " 23:59:59";
		return DateUtil.parse(s,DatePattern.NORM_DATETIME_PATTERN);
	}

	/**
	 * 将指定时间改为 23时59纷59秒
	 * @param date
	 * @return
	 */
	public static Date getDateWithMaxTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		//增加毫秒设置为0
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取指定日期下个月的还款日
	 * @param date
	 * @param dueDate 还款日
	 * @return
	 */
	public static Date getNextMonthDueDate(Date date,Integer dueDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,dueDate);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

	public static String freeMarkFullData(String templateName, String content, Map<String,Object> data) throws Exception {

		if (StrUtil.isBlank(templateName)) {
			return null;
		}

		if (StrUtil.isBlank(content)) {
			return null;
		}

		if (data == null) {
			return null;
		}

		freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);

		StringTemplateLoader stringLoader = new StringTemplateLoader();

		stringLoader.putTemplate(templateName, content);

		cfg.setTemplateLoader(stringLoader);

		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

		StringWriter writer = new StringWriter();
		cfg.getTemplate(templateName).process(data, writer);

		return writer.toString();
	}

	public static String getIpAddress(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 如果是多级代理，那么取第一个ip为客户端ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(0, ip.indexOf(",")).trim();
		}

		return ip;
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 解析支付宝回调参数
	 * @param parameterMap
	 * @return
	 */
	public static Map<String, String> parseAlipayCallbackParams(Map<String, String[]> parameterMap) {

		if (Objects.isNull(parameterMap) || parameterMap.size() == 0) {
			return null;
		}

		Map<String, String> params = new HashMap<>(30);

		for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {

			String name = (String) iter.next();
			String[] values = parameterMap.get(name);
			String value = "";
			for (int i = 0; i < values.length; i++) {
				value = (i == values.length - 1) ? value + values[i] : value + values[i] + ",";
			}

			params.put(name, value);
		}

		return params;
	}

	/**
	 * 获取打星手机号
	 * @param phone
	 * @return
	 */
	public static String getStarPhone(String phone) {

		if (StrUtil.isBlank(phone)) {
			return null;
		}

		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
	}

	/**
	 * 获取打星手机号
	 * @param idCard
	 * @return
	 */
	public static String getStarIdCard(String idCard) {

		if (StrUtil.isBlank(idCard)) {
			return null;
		}

		return idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2");
	}

	/**
	 * 格式化规格
	 * @param conjunction
	 * @param specificationJson
	 * @return
	 */
	public static String formatSpecification(CharSequence conjunction, String specificationJson) {

		if (StrUtil.isBlank(specificationJson)) {
			return null;
		}

		JSONArray specificationArrays = JSONUtil.parseArray(specificationJson);

		StringBuilder specification = new StringBuilder();

		for (Object obj : specificationArrays) {
			if (specification.length() > 0) {
				specification.append(conjunction);
			}

			specification.append(((JSONObject) obj).getStr("value"));
		}

		return specification.toString();
	}

	/**
	 * 获取首个图片
	 * @param imageJson
	 * @return
	 */
	public static String getFirstImage(String imageJson) {

		if (StrUtil.isBlank(imageJson)) {
			return null;
		}

		JSONArray imageArray = JSONUtil.parseArray(imageJson);
		if (Objects.isNull(imageArray) || imageArray.size() == 0) {
			return null;
		}

		JSONObject image = (JSONObject)imageArray.getObj(0, null);
		return image.getStr("url");
	}

	/**
	 * 获取下单用户角色
	 * @param legalPersonStatus
	 * @return
	 */
	public static String getOrderUserRole(boolean legalPersonStatus) {

		return legalPersonStatus ? "法人" : "经办人";
	}

	/**
	 * 处理oss私有图片
	 * @param privateImage
	 * @return
	 */
	public static String handleOssPrivateImage(String privateImage) {
		return StrUtil.subBefore(privateImage, '?', false);
	}

	public static boolean parseCondition(String condition, List<Boolean> resultList) {

		if (resultList == null || resultList.size() == 0) {
			return false;
		}

		int count = resultList.stream().filter(result->result).collect(Collectors.toList()).size();

		if (condition.equals("or") && count > 0) {
			return true;
		}

		if (condition.equals("and") && resultList.size() == count) {
			return true;
		}

		return false;
	}

	public static boolean formulaCalculate(String condition, String v1, String v2) {
		return formulaCalculate(condition, v1, v2, false);
	}

	public static boolean formulaCalculate(String condition, String v1, String v2, boolean isCompareNumber) {

		if ("equal".equals(condition) && v1.equals(v2)) {
			return true;
		}

		if ("greater_than".equals(condition) && Integer.parseInt(v1) > Integer.parseInt(v2)) {
			return true;
		}

		if ("less_than".equals(condition) && Integer.parseInt(v1) < Integer.parseInt(v2)) {
			return true;
		}

		if ("greater_than_or_equal".equals(condition) && Integer.parseInt(v1) >= Integer.parseInt(v2)) {
			return true;
		}

		if ("less_than_or_equal".equals(condition) && Integer.parseInt(v1) <= Integer.parseInt(v2)) {
			return true;
		}

		if ("in".equals(condition)) {

			List<String> array = Arrays.asList(v2.split(StrUtil.COMMA));
			if (isCompareNumber) {
				return new BigDecimal(v1).compareTo(new BigDecimal(array.get(0))) > -1 && new BigDecimal(v1).compareTo(new BigDecimal(array.get(1))) == -1;
			} else {
				return array.contains(v1);
			}
		}

		if ("not_equal".equals(condition) && Integer.parseInt(v1) != Integer.parseInt(v2)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取cron下次执行时间
	 * @param date
	 * @param cronStr
	 * @return
	 */
	public static Date getCronNextExecuteTime(Date date, String cronStr) {
		if (StrUtil.isBlank(cronStr)) {
			throw new ServiceException("cronStr is Illegal!");
		}

		CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronStr);
		return cronSequenceGenerator.next(date);
	}
}