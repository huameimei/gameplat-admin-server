package com.gameplat.admin.utils;

import javax.xml.bind.DatatypeConverter;

public class Xxtea {

	public static String encryptToXxteaByBase64(String text, String key) throws Exception {
		byte[] data = text.getBytes("UTF-8");
		if (data.length == 0) {
			return "";
		}
		byte[] k = key.getBytes("UTF-8");
		return encryptBASE64(toByteArray(encrypt(toIntArray(data, true), toIntArray(k, false)), false));
	}

	public static String decryptToXxteaByBase64(String text, String key) throws Exception {
		// byte[] b = (new BASE64Decoder()).decodeBuffer(text);
		byte[] b = DatatypeConverter.parseBase64Binary(text);
		byte[] k = key.getBytes("UTF-8");
		if (b.length == 0) {
			return "";
		}

		byte[] d = toByteArray(decrypt(toIntArray(b, false), toIntArray(k, false)), true);

		// byte[] d = decrypt(b, k);
		String v = new String(d, "UTF-8");
		return v;
	}

	private static String encryptBASE64(byte[] key) throws Exception {
		return DatatypeConverter.printBase64Binary(key).replaceAll("\r\n", "");
	}

	private static int[] encrypt(int[] v, int[] k) {
		int n = v.length - 1;
		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];
			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], delta = 0x9E3779B9, sum = 0, e;
		int p, q = 6 + 52 / (n + 1);
		while (q-- > 0) {
			sum = sum + delta;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			y = v[0];
			z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		}
		return v;
	}

	private static int[] decrypt(int[] v, int[] k) {
		int n = v.length - 1;
		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];
			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], delta = 0x9E3779B9, sum, e;
		int p, q = 6 + 52 / (n + 1);
		sum = q * delta;
		while (sum != 0) {
			e = sum >>> 2 & 3;
			for (p = n; p > 0; p--) {
				z = v[p - 1];
				y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			z = v[n];
			y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			sum = sum - delta;
		}
		return v;
	}

	private static int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;
		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	private static byte[] toByteArray(int[] data, boolean includeLength) throws Exception {
		int n;
		if (includeLength) {
			n = data[data.length - 1];
		} else {
			n = data.length << 2;
		}
		if (n > 61858764) {
			throw new Exception();
		}
		byte[] result = new byte[n];
		for (int i = 0; i < n; i++) {
			result[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3));
		}
		return result;
	}
}
