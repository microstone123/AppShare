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
		// �����������������
		mapper.getSerializationConfig().setSerializationInclusion(inclusion);
		// ��������ʱ����JSON�ַ����д��ڶ�Java����ʵ��û�е�����
		mapper.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * �������ȫ�����Ե�Json�ַ�����Binder.
	 */
	public static JsonBinder buildNormalBinder() {
		return new JsonBinder(Inclusion.ALWAYS);
	}

	/**
	 * ����ֻ����ǿ����Ե�Json�ַ�����Binder.
	 */
	public static JsonBinder buildNonNullBinder() {
		return new JsonBinder(Inclusion.NON_NULL);
	}

	/**
	 * ����ֻ�����ʼֵ���ı�����Ե�Json�ַ�����Binder.
	 */
	public static JsonBinder buildNonDefaultBinder() {
		return new JsonBinder(Inclusion.NON_DEFAULT);
	}

	/**
	 * ���JSON�ַ���ΪNull��"null"�ַ���,����Null. ���JSON�ַ���Ϊ"[]",���ؿռ���.
	 * 
	 * �����ȡ������List/Map,�Ҳ���List<String>���ּ�����ʱʹ���������: List<MyBean> beanList =
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
	 * jackjson��json�ַ���ת��ΪJava�����ʵ�ַ���
	 * 
	 * <pre>
	 * return Jackson.jsonToObj(this.answersJson, new TypeReference&lt;List&lt;StanzaAnswer&gt;&gt;() {
	 * });
	 * </pre>
	 * 
	 * @param <T>
	 *            ת��Ϊ��java����
	 * @param json
	 *            json�ַ���
	 * @param typeReference
	 *            jackjson�Զ��������
	 * @return ����Java����
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
	 * jsonת��Ϊjava����
	 * 
	 * <pre>
	 * return Jackson.jsonToObj(this.answersJson, Jackson.class);
	 * </pre>
	 * 
	 * @param <T>
	 *            Ҫת���Ķ���
	 * @param json
	 *            �ַ���
	 * @param valueType
	 *            �����class
	 * @return ���ض���
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
	 * �������ΪNull,����"null". �������Ϊ�ռ���,����"[]".
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
	 * ����ת���������͵�format pattern,���������Ĭ�ϴ�ӡTimestamp������.
	 */
	public void setDateFormat(String pattern) {
		if (StringUtils.isNotBlank(pattern)) {
			DateFormat df = new SimpleDateFormat(pattern);
			mapper.getSerializationConfig().setDateFormat(df);
			mapper.getDeserializationConfig().setDateFormat(df);
		}
	}

	/**
	 * ȡ��Mapper����һ�������û�ʹ���������л�API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}

	public static void main(String[] args) {

	}
}
