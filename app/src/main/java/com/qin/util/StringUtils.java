package com.qin.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StringUtils {

	public static String stream2String(InputStream in) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buff = new byte[1024];
			while ((len = in.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			in.close();
			String string = new String(bos.toByteArray());
			return string;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
