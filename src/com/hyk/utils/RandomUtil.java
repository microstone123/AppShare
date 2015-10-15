package com.hyk.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @ClassName: RandomUtil
 * @Description: TODO(����������ظ�����)
 * @author linhaishi
 * @date 2013-10-12 ����5:02:18
 *
 */
public class RandomUtil {
	/**
	 * 
	 * @param randomInt ���ֵ  �� 0��randomInt
	 * @param count ���ɵĸ���
	 * @return
	 */
	public static int[] getExchangeCode(int randomInt, int count) {
		StringBuffer randomValidateCode = new StringBuffer();
		if (count > randomInt) {
			count = randomInt;
		}
		for (int j = 0; j < count; j++) {
			int one = RandomUtils.nextInt(randomInt);// ���һ�������
			if (j == 0) {
				randomValidateCode.append(one).append("#");// ���������ͷָ���
			} else if (j > 0) {
				String[] all = randomValidateCode.toString().split("#");// �ָ���ַ�������
				// �����Ƿ��ظ�����teseEquals
				if (teseEquals(all, one, randomValidateCode) == 1) {
					randomValidateCode.append(one).append("#");// ������ظ������������ͷָ���
				} else {
					j--;// ����ظ��Ļ���j-1
				}
			}
		}
		// �Եõ���8λ������÷ָ������зָ�
		String[] result = randomValidateCode.toString().split("#");
		int[] res = new int[result.length];
		for (int r = 0; r < result.length; r++) {
			if(StringUtils.isNotEmpty(result[r])){
				res[r] = Integer.valueOf(result[r]);
			}
		}
		return res;
	}

	// �����Ƿ��ظ�
	public static int teseEquals(String[] all, int one, StringBuffer randomValidateCode) {
		for (int i = 0; i < all.length; i++) {
			if (one == Integer.parseInt(all[i])) {
				return 0;// �ظ��ͷ���0
			}
		}
		return 1;// ���ظ�����1
	}
}
