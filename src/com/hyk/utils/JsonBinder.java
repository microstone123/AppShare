package com.hyk.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;

public class JsonBinder {
	private ObjectMapper mapper;

	@SuppressWarnings("deprecation")
	public JsonBinder(Inclusion inclusion) {
		mapper = new ObjectMapper();
		// 设置输出包含的属性
		mapper.getSerializationConfig().setSerializationInclusion(inclusion);
		// 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
		mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 创建输出全部属性到Json字符串的Binder.
	 */
	public static JsonBinder buildNormalBinder() {
		return new JsonBinder(Inclusion.ALWAYS);
	}

	/**
	 * 创建只输出非空属性到Json字符串的Binder.
	 */
	public static JsonBinder buildNonNullBinder() {
		return new JsonBinder(Inclusion.NON_NULL);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Binder.
	 */
	public static JsonBinder buildNonDefaultBinder() {
		return new JsonBinder(Inclusion.NON_DEFAULT);
	}

	/**
	 * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
	 * 
	 * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: List<MyBean> beanList =
	 * binder.getMapper().readValue(listString, new
	 * TypeReference<List<MyBean>>() {});
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * fromJsonToObject<br>
	 * jackjson把json字符串转换为Java对象的实现方法
	 * 
	 * <pre>
	 * return Jackson.jsonToObj(this.answersJson, new TypeReference&lt;List&lt;StanzaAnswer&gt;&gt;() {
	 * });
	 * </pre>
	 * 
	 * @param <T>
	 *            转换为的java对象
	 * @param json
	 *            json字符串
	 * @param typeReference
	 *            jackjson自定义的类型
	 * @return 返回Java对象
	 */
	public <T> T jsonToObj(String json, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(json, typeReference);
		} catch (JsonParseException e) {
			Log.w("JsonParseException: ", e);
		} catch (JsonMappingException e) {
			Log.w("JsonMappingException: ", e);
		} catch (IOException e) {
			Log.w("IOException: ", e);
		}
		return null;
	}

	/**
	 * fromJsonToObject<br>
	 * json转换为java对象
	 * 
	 * <pre>
	 * return Jackson.jsonToObj(this.answersJson, Jackson.class);
	 * </pre>
	 * 
	 * @param <T>
	 *            要转换的对象
	 * @param json
	 *            字符串
	 * @param valueType
	 *            对象的class
	 * @return 返回对象
	 */
	public <T> T jsonToObj(String json, Class<T> valueType) {
		try {
			return mapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			Log.w("JsonParseException: ", e);
		} catch (JsonMappingException e) {
			Log.w("JsonMappingException: ", e);
		} catch (IOException e) {
			Log.w("IOException: ", e);
		}
		return null;
	}

	/**
	 * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
	 */
	public String toJson(Object object) {

		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			Log.w("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数.
	 */
	public void setDateFormat(String pattern) {
		if (StringUtils.isNotBlank(pattern)) {
			DateFormat df = new SimpleDateFormat(pattern);
			mapper.getSerializationConfig().setDateFormat(df);
			mapper.getDeserializationConfig().setDateFormat(df);
		}
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}

	public static void main(String[] args) {

	}
}
