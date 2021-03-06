package com.flume.dome.xutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 2017/06/22
 * 
 * @author rui
 *
 */
public class ConverDataObject {
	private static Logger LOG = LoggerFactory.getLogger(ConverDataObject.class);

	public ConverDataObject() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 把文本返换行分隔，并转json
	 * 
	 * @param context
	 * @return
	 * @throws ParseException
	 */
	public static JSONObject conver(String content, boolean consider) throws ParseException {
		// 拆分一行
		String[] contents = content.split("\n");
		if (contents.length > 1) {
			LOG.error("转换数据出现多行记录，请处理...>>{}", content);
			content = contents[0];
		}
		JSONObject jsons = new JSONObject();
		// 按 ` 拆分，再按=折分
		String[] kvStr = content.split("`");
		boolean is_fliter = false;
		for (int row = 0; row < kvStr.length; row++) {
			String[] kvs = kvStr[row].split("=");
			if (kvs == null || kvs.length != 2) {
				continue;
			}
			if (consider) {
				if ("file".equals(kvs[0].trim())) {
					// 过滤条件
					if ("temp_targetor".equals(kvs[1].trim())) {// buffs
						is_fliter = true;
						continue;
					}
					if ("temp_attacker".equals(kvs[1].trim())) {// buffs
						is_fliter = true;
						continue;
					}
					if ("attack_temp_eff".equals(kvs[1].trim())) {
						is_fliter = true;
						continue;
					}
					if ("target_temp_eff".equals(kvs[1].trim())) {
						is_fliter = true;
						continue;
					}
				}
				if (is_fliter) {// 过滤不需要的日志
					continue;
				}
			}

			jsons.put(kvs[0].trim(), kvs[1].trim());
		}

//		if (jsons.containsKey("time")) {
//			// 存储有毫秒时间戳
//			String t = jsons.get("time").toString();
//			// 2017-07-10 12:03:47:307
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//			long dt = df.parse(t).getTime();
//			jsons.put("time_log", dt);
//			// 时间去掉毫秒
//			if (getStrToCount(t, ":") == 3) {// 如果时间带有毫秒，则去掉
//				String tdata = t.substring(0, t.lastIndexOf(":"));
//				jsons.put("time", tdata);
//			} else {
//				jsons.put("time", t);
//			}
//		}
		// 去掉不要的数据
		if ("{}".equals(jsons.toJSONString())) {
			jsons = null;
		}
		return jsons;
	}

	/**
	 * 源 码为json,只是加工数据
	 * 
	 * @param context
	 * @return
	 * @throws ParseException
	 */
	public static JSONObject converStr(String context) throws ParseException {
		if (context == null || "".equals(context)) {
			return null;
		}
		// 拆分一行
		String[] contents = context.split("\n");
		if (contents.length > 1) {
			LOG.error("转换数据出现多行记录，请处理...>>{}", context);
			context = contents[0];
		}
		JSONObject jsons = null;
		try {
			jsons = (JSONObject) JSON.parse(context);
		} catch (Exception e) {
			LOG.error("解析json 错误:{}", context);
			e.printStackTrace();
		}
//		if (jsons != null && jsons.containsKey("time")) {
//			// 存储有毫秒时间戳
//			String t = jsons.get("time").toString();
//			// 2017-07-10 12:03:47:307
//			long dt = 0;
//			
//			if(t.endsWith("SSS")){
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//				 dt = df.parse(t).getTime();
//			}else{
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				 dt = df.parse(t).getTime();
//			}
//			jsons.put("time_log", dt);
//			// 时间去掉毫秒
//			if (getStrToCount(t, ":") == 3) {// 如果时间带有毫秒，则去掉
//				String tdata = t.substring(0, t.lastIndexOf(":"));
//				jsons.put("time", tdata);
//			} else {
//				jsons.put("time", t);
//			}
//		}
		return jsons;
	}

	public static int getStrToCount(String srcStr, String tagStr) {
		int i = 0;
		Pattern p = Pattern.compile(tagStr);
		Matcher m = p.matcher(srcStr);
		while (m.find()) {
			i++;
		}
		return i;
	}

	public static void main(String[] args) throws ParseException {
		JSONObject jsons = new JSONObject();
		System.out.println(jsons);
		int i = getStrToCount("2017-06-16 15:23:07:383", ":");
		System.out.println(i);
		// gamedb______file=temp_targetor`time=2017-06-16
		// 15:23:07:383`uuid=10000005`name=巴尔杜勒`hp=338`en=1000`status=[]`buffs=[]`A_62=1000`A_11=21`A_63=15`A_68=102`A_15=0`A_64=27`A_20=0`A_17=54`A_65=0`A_13=0`A_0=0`A_8=0`A_67=54`A_7=1000`A_66=0`A_1=12`A_69=100`A_3=0`A_119=0`A_6=0`A_2=8`A_118=0`A_120=10000`A_10=0`A_9=11`A_19=86`A_14=0`A_5=98`A_18=0`A_61=338`A_22=0`A_21=60`A_127=1000000`A_16=0`A_4=0`A_12=0`actor_id=101`actor_type=human`race=1`dungeon_id=1001`
		String context = "file=temp_targetorxx`time=2017-06-16 15:23:07:383`uuid=10000005`name=巴尔杜勒`hp=338`en=1000`status=[]`buffs=[]`A_62=1000`A_11=21`A_63=15`A_68=102`A_15=0`A_64=27`A_20=0`A_17=54`A_65=0`A_13=0`A_0=0`A_8=0`A_67=54`A_7=1000`A_66=0`A_1=12`A_69=100`A_3=0`A_119=0`A_6=0`A_2=8`A_118=0`A_120=10000`A_10=0`A_9=11`A_19=86`A_14=0`A_5=98`A_18=0`A_61=338`A_22=0`A_21=60`A_127=1000000`A_16=0`A_4=0`A_12=0`actor_id=101`actor_type=human`race=1`dungeon_id=1001`";
		context += "\n";
		context += "file=effect`time=2017-06-16 15:24:33:124`uuid=10009`name=测试BOSS石头人`effect_id=15000`effect_type=add_hp_scale`skill_id=91016`target=10009`actor_id=10004`actor_type=mon`race=1001`dungeon_id=1001`";
		context += "\n";
		context += "file=add_hp_scale`time=2017-06-16 15:24:33:124`uuid=10009`name=测试BOSS石头人`add_hp=449`final_hp=1498`skill_id=91016`target=10009`actor_id=10004`actor_type=mon`race=1001`dungeon_id=1001`";
		System.out.println(context);
		JSONObject list = conver(context,false);
		System.out.println(list);
	}

}
