package com.news.common.utils;

public class MathUtil {
	public static void main(String[] args) {
		int result = gcd(16, 12);
		System.out.println(result);

		int[] a = { 1, 2, 3, 4, 6, 7, 8, 9 };
		int key = 6;
		result = rank(key, a, 0, a.length);
		System.out.println(result);

	}

	// p,q最小公倍数
	public static int gcd(int p, int q) {
		if (q == 0) {
			return p;
		}
		int r = p % q;
		return gcd(q, r);
	}

	// 求key值在列表a中的位置【a为顺序列表】
	public static int rank(int key, int[] a, int low, int high) {
		if (low > high) {
			return -1;
		}
		int wid = low + (high - low) / 2;
		int value = a[wid];
		if (key > value) {
			return rank(key, a, wid + 1, high);
		} else if (key < value) {
			return rank(key, a, low, wid - 1);
		} else {
			return wid;
		}
	}

}
