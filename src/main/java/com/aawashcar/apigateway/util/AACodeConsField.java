package com.aawashcar.apigateway.util;

import java.io.Serializable;

/**
 * @author fei.tang
 */
public final class AACodeConsField implements Serializable {

	private static final long serialVersionUID = 8789423033223487174L;

	private AACodeConsField() {
	}

	// 20000 - 29999 all good
	// OK
	public static final String SUCCESS_MSG_20000 = "20000";
	// Worker apply succeed
	public static final String SUCCESS_APPLY_WORKER_20200 = "20200";
	
	
	
	// 30000 - 39999 worker error
	// already applied
	public static final String ERROR_ALREADY_APPLIED_30000 = "30000";
	public static final String ERROR_INVALID_INPUT_30100 = "30100";

	// 系统异常
	public static final String ERROR_MSG_90000 = "90000";

	// 参数异常
	public static final String ERROR_MSG_90001 = "90001";

	/**
	 * 10001 - 39999 登录注册等基础模块
	 */
	/* 验证码模块 */
	// 您今天获取验证码过多，请联系客服~
	public static final String ERROR_MSG_10100 = "10100";
	// 验证码错误
	public static final String ERROR_MSG_10101 = "10101";
	// 您输入的验证已过期，请重新获取
	public static final String ERROR_MSG_10102 = "10102";
	// 请您在60秒之后重新获取验证码
	public static final String ERROR_MSG_10103 = "10103";

	/* 登录模块 */
	// 登录名不能为空
	public static final String ERROR_MSG_10200 = "10200";
	// 登录密码不能为空
	public static final String ERROR_MSG_10201 = "10201";
	// 账号不存在
	public static final String ERROR_MSG_10202 = "10202";
	// 密码错误
	public static final String ERROR_MSG_10203 = "10203";
	// 账号已被冻结
	public static final String ERROR_MSG_10204 = "10204";
	// 用户名或密码错误,请重新输入
	public static final String ERROR_MSG_10205 = "10205";
	// 旧密码错误
	public static final String ERROR_MSG_10206 = "10206";
	// 用户不存在
	public static final String ERROR_MSG_10207 = "10207";
	// 该账号已禁用
	public static final String ERROR_MSG_10208 = "10208";

	public static final String ERROR_MSG_10209 = "10209";

	// 请求头信息为空
	public static final String ERROR_MSG_10212 = "10212";
	// 请求头信息(ID为空)
	public static final String ERROR_MSG_10213 = "10213";
	// 请求头信息(登录key为空)
	public static final String ERROR_MSG_10214 = "10214";
	// 请求头信息(ID不存在)
	public static final String ERROR_MSG_10215 = "10215";
	// 请求头信息(登录key错误)
	public static final String ERROR_MSG_10216 = "10216";
	// 请求头信息(RequestHeader 转换失败 MobileHead 对象失败)
	public static final String ERROR_MSG_10217 = "10217";
	// 请求头信息(token无效)
	public static final String ERROR_MSG_10218 = "10218";

	/* 注册模块 */
	// 该账号已存在
	public static final String ERROR_MSG_10300 = "10300";
	// 手机号不能为空
	public static final String ERROR_MSG_10301 = "10301";
	// 旧密码不能为空
	public static final String ERROR_MSG_10302 = "10302";
	// 新密码不能为空
	public static final String ERROR_MSG_10303 = "10303";
	// 该账户不存在
	public static final String ERROR_MSG_10304 = "10304";
	// 发送短信失败原因
	public static final String ERROR_MSG_10305 = "10305";
	// 图片验证码错误
	public static final String ERROR_MSG_10306 = "10306";
	// 验证两次输入密码是否一致
	public static final String ERROR_MSG_10307 = "10307";
	// 该用户不存在
	public static final String ERROR_MSG_10308 = "10308";
	// 更新用户失败
	public static final String ERROR_MSG_10309 = "10309";

	/* 上传图片模块 */
	// 上传的文件格式不支持
	public static final String ERROR_MSG_10400 = "10400";
	// 上传的文件太大，文件大小不能超过5M
	public static final String ERROR_MSG_10401 = "10401";
	// 上传图片种类不能为空
	public static final String ERROR_MSG_10402 = "10402";
	// 上传文件失败
	public static final String ERROR_MSG_10403 = "10403";

	public static final String ERROR_MSG_10404 = "10404";

	// 起始地或目的地不能为空

	// 需求添加异常
	public static final String ERROR_MSG_20104 = "20104";

	// 该发车需求已确认下单
	public static final String ERROR_MSG_20105 = "20105";

	// 发车需求ID不存在
	public static final String ERROR_MSG_20106 = "20106";

	// 该司机已匹配此发车需求
	public static final String ERROR_MSG_20107 = "20107";

	// 该会员已匹配此承运需求
	public static final String ERROR_MSG_20108 = "20108";

	// 该承运需求已确认下单
	public static final String ERROR_MSG_20109 = "20109";

	// 承运需求ID不存在
	public static final String ERROR_MSG_20110 = "20110";

	// 起始地CODE不存在
	public static final String ERROR_MSG_20111 = "20111";

	// 目的CODE不存在
	public static final String ERROR_MSG_20112 = "20112";

	// 车辆已经存在
	public static final String ERROR_MSG_20113 = "20113";

	/**
	 * 40001 - 49999 订单模块
	 */
	// 未查询到订单数据
	public static final String ERROR_MSG_40001 = "40001";
	// 该订单状态已改变，请刷新~
	public static final String ERROR_MSG_40002 = "40002";

	// 订单编号不能为空
	public static final String ERROR_MSG_40003 = "40003";

	// 订单状态不符
	public static final String ERROR_MSG_40004 = "40004";

	// 未指定支付渠道
	public static final String ERROR_MSG_40005 = "40005";

	// 该订单已成功支付，请不要重复操作！
	public static final String ERROR_MSG_40006 = "40006";

	// 未指定订单支付类型 预付款或者尾款
	public static final String ERROR_MSG_40007 = "40007";

	// 微信支付参数不符合要求
	public static final String ERROR_MSG_40008 = "40008";

	// 积分不够
	public static final String ERROR_MSG_40009 = "40009";

	// 用户订单编号不存在
	public static final String ERROR_MSG_41001 = "41001";

	// 支付金额不符
	public static final String ERROR_MSG_41002 = "41002";

	// 订单不能取消
	public static final String ERROR_MSG_41003 = "41003";

	// 微信统一下单失败
	public static final String ERROR_MSG_41004 = "41004";

	// 优惠券号不存在
	public static final String ERROR_MSG_42001 = "42001";

	// 优惠券金额不符
	public static final String ERROR_MSG_42002 = "42002";

	// 优惠券已过期
	public static final String ERROR_MSG_42003 = "42003";

	// 优惠券已使用
	public static final String ERROR_MSG_42004 = "42004";

	// 产品价格或会员价格不能为0
	public static final String ERROR_MSG_42005 = "42005";

	// 请重新选择您的车型
	public static final String ERROR_MSG_42006 = "42006";

	// 请重新选择您的所有在地区
	public static final String ERROR_MSG_42007 = "42007";

	// 请重新选择你的时间
	public static final String ERROR_MSG_42008 = "42008";

}
