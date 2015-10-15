package com.hyk.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @ClassName: RandomUtil
 * @Description: TODO(随机生成无重复数组)
 * @author linhaishi
 * @date 2013-10-12 下午5:02:18
 *
 */
public class RandomUtil {
	/**
	 * 
	 * @param randomInt 最大值  从 0到randomInt
	 * @param count 生成的个数
	 * @return
	 */
	public static int[] getExchangeCode(int randomInt, int count) {
		StringBuffer randomValidateCode = new StringBuffer();
		if (count > randomInt) {
			count = randomInt;
		}
		for (int j = 0; j < count; j++) {
			int one = RandomUtils.nextInt(randomInt);// 获得一个随机数
			if (j == 0) {
				randomValidateCode.append(one).append("#");// 添加随机数和分隔符
			} else if (j > 0) {
				String[] all = randomValidateCode.toString().split("#");// 分割成字符串数组
				// 调用是否重复方法teseEquals
				if (teseEquals(all, one, randomValidateCode) == 1) {
					randomValidateCode.append(one).append("#");// 如果不重复就添加随机数和分隔符
				} else {
					j--;// 如果重复的话将j-1
				}
			}
		}
		// 对得到的8位随机数用分隔符进行分割
		String[] result = randomValidateCode.toString().split("#");
		int[] res = new int[result.length];
		for (int r = 0; r < result.length; r++) {
			if(StringUtils.isNotEmpty(result[r])){
				res[r] = Integer.valueOf(result[r]);
			}
		}
		return res;
	}

	// 测试是否重复
	public static int teseEquals(String[] all, int one, StringBuffer randomValidateCode) {
		for (int i = 0; i < all.length; i++) {
			if (one == Integer.parseInt(all[i])) {
				return 0;// 重复就返回0
			}
		}
		return 1;// 不重复返回1
	}
}
