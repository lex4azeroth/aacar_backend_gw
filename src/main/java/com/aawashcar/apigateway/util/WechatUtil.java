package com.aawashcar.apigateway.util;

import java.io.DataInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.aawashcar.apigateway.model.WechatNotify;

public class WechatUtil {

	/**
	 * 微信回调成功后 将 xml 转换为 WechatNotify 对象
	 * 
	 * @param request
	 * @return WechatNotify 对象
	 */
	public static WechatNotify getNotifyBean(HttpServletRequest request) {
		try {
			DataInputStream in = new DataInputStream(request.getInputStream());
			byte[] dataOrigin = new byte[request.getContentLength()];
			// 根据长度，将消息实体的内容读入字节数组dataOrigin中
			in.readFully(dataOrigin);
			// 关闭数据流
			in.close();
			// 从字节数组中得到表示实体的字符串
			String xml = new String(dataOrigin);
			// 将 xml 转换为 WechatNotify 对象
			Object object = XMLUtil.xmlToBean(WechatNotify.class, xml);
			if (object != null && object instanceof WechatNotify) {
				WechatNotify notify = (WechatNotify) object;
				return notify;
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
