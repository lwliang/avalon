/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 *  直连商户平台缴费销账输入对象,需要转换成json串发送给第三方系统
 *  @author DELL
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ChargeBillRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 格式 */
	@JSONField(name = "format")
	private String format;

	/** 消息 */
	@JSONField(name = "message")
	private Message message;

	@Override
	public String toString() {
		return "ChargeBillRequest[format=" + format + ",message=" + message.toString() + "]";
	}

	public String getFormat() {
		return format;
	}

	@JSONField(name = "format")
	public void setFormat(String format) {
		this.format = format;
	}

	public Message getMessage() {
		return message;
	}

	@JSONField(name = "message")
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 *
	 * 账单查询内部消息对象实体message内部类
	 *
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public class Message implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 消息头部 */
		@JSONField(name = "head")
		private Head head;

		/** 消息体  */
		@JSONField(name = "info")
		private Info info;

		@Override
		public String toString() {
			return "ChargeBillRequest.Message[head=" + head.toString() + ",info=" + info.toString() + "]";
		}

		public Head getHead() {
			return head;
		}

		@JSONField(name = "head")
		public void setHead(Head head) {
			this.head = head;
		}

		public Info getInfo() {
			return info;
		}

		@JSONField(name = "info")
		public void setInfo(Info info) {
			this.info = info;
		}

		/**
		 *  message子对象head消息头内部类
		 */
		@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
		public class Head implements Serializable {

			private static final long serialVersionUID = 1L;

			/**  渠道编码 */
			@JSONField(name = "channel")
			private String channel;

			/**  交易码  */
			@JSONField(name = "transCode")
			private String transCode;

			/**  交易上行下送标志位  */
			@JSONField(name = "transFlag")
			private String transFlag;

			/**  缴费中心交易序列号 */
			@JSONField(name = "transSeqNum")
			private String transSeqNum;

			/**   时间戳  */
			@JSONField(name = "timeStamp")
			private String timeStamp;

			/**   4为分行iGoal码  */
			@JSONField(name = "branchCode")
			private String branchCode;

			@Override
			public String toString() {
				return "ChargeBillRequest.Message.Head[channel=" + channel +",transCode=" + transCode +
						",transFlag=" + transFlag + ",transSeqNum=" + transSeqNum + ",timestamp=" + timeStamp + ",branchCode=" + branchCode + "]";
			}

			public String getChannel() {
				return channel;
			}

			@JSONField(name = "channel")
			public void setChannel(String channel) {
				this.channel = channel;
			}

			public String getTransFlag() {
				return transFlag;
			}

			@JSONField(name = "transFlag")
			public void setTransFlag(String transFlag) {
				this.transFlag = transFlag;
			}

			public String getTransCode() {
				return transCode;
			}

			@JSONField(name = "transCode")
			public void setTransCode(String transCode) {
				this.transCode = transCode;
			}

			public String getTransSeqNum() {
				return transSeqNum;
			}

			@JSONField(name = "transSeqNum")
			public void setTransSeqNum(String transSeqNum) {
				this.transSeqNum = transSeqNum;
			}


			public String getTimeStamp() {
				return timeStamp;
			}

			@JSONField(name = "timeStamp")
			public void setTimeStamp(String timeStamp) {
				this.timeStamp = timeStamp;
			}

			public String getBranchCode() {
				return branchCode;
			}

			@JSONField(name = "branchCode")
			public void setBranchCode(String branchCode) {
				this.branchCode = branchCode;
			}
		}

		/**
		 * message子对象info消息实体内部类
		 */
		@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
		public class Info implements Serializable {

			private static final long serialVersionUID = 1L;

			/** 缴费项目编号*/
			@JSONField(name = "epayCode")
			private String epayCode;

			/** 第三方商户编号*/
			@JSONField(name = "merchantId")
			private String merchantId;

			/** 缴费中心流水号*/
			@JSONField(name = "traceNo")
			private String traceNo;

			/** 输入要素1*/
			@JSONField(name = "input1")
			private String input1;

			/** 输入要素2*/
			@JSONField(name = "input2")
			private String input2;

			/** 输入要素3*/
			@JSONField(name = "input3")
			private String input3;

			/** 输入要素4*/
			@JSONField(name = "input4")
			private String input4;

			/** 输入要素5*/
			@JSONField(name = "input5")
			private String input5;

			/** 农行16位客户号*/
			@JSONField(name = "userId")
			private String userId;

			/** 缴费金额计算规则*/
			@JSONField(name = "amtRule")
			private String amtRule;

			/** 合并支付的子账单数*/
			@JSONField(name = "payBillCount")
			private String payBillCount;

			/** 合并支付的子账单累加总金额*/
			@JSONField(name = "payBillAmt")
			private String payBillAmt;

			/** 合并支付的子账单*/
			@JSONField(name = "payBillNo")
			private String payBillNo;

			/** 套餐名称*/
			@JSONField(name = "optionName")
			private String optionName;

			/** 套餐编码*/
			@JSONField(name = "optionCode")
			private String optionCode;

			/** 套餐金额*/
			@JSONField(name = "optionAmt")
			private String optionAmt;

			/** 支付方式交易码*/
			@JSONField(name = "payType")
			private String payType;

			/** 缴费支付账号*/
			@JSONField(name = "payAcc")
			private String payAcc;

			/** 支付系统流水号*/
			@JSONField(name = "transPaySeq")
			private String transPaySeq;

			/** 支付系统日期*/
			@JSONField(name = "transDate")
			private String transDate;

			/** 支付系统时间*/
			@JSONField(name = "transTime")
			private String transTime;

			/** 会计日期*/
			@JSONField(name = "settleDate")
			private String settleDate;

			/** 清算模式*/
			@JSONField(name = "clearType")
			private String clearType;

			/** 缓存域信息*/
			@JSONField(name = "cacheMem")
			private String cacheMem;

			/** 销账报文重发次数，通过此字段识别销账报文是否为重发的，0表示首次、1表示重发一次，2表示重发2次，最多重发3次*/
			@JSONField(name = "resendTimes")
			private String resendTimes;

			/** 第三方支付平台商户订单号 第三方平台例如微信支付宝的支付订单号 add 2020-01-13*/
			@JSONField(name = "numOpenMerchantOrder")
			private String numOpenMerchantOrder;


			@Override
			public String toString() {
				return "ChargeBillRequest.Message.Info[epayCode=" + epayCode + ",merchantId=" + merchantId +
						",input1=" + input1 + ",input2=" + input2 + ",input3=" + input3 +
						",input4=" + input4 + ",input5=" + input5 +
						",userId=" + userId + ",traceNo=" + traceNo +
						",amtRule=" + amtRule + ",payBillCount=" + payBillCount +
						",payBillAmt=" + payBillAmt + ",payBillNo=" + payBillNo +
						",optionName=" + optionName + ",optionAmt=" + optionAmt +
						",payType=" + payType + ",payAcc=" + payAcc +
						",transPaySeq=" + transPaySeq + ",transDate=" + transDate + ",transTime=" + transTime +
						",settleDate=" + settleDate + ",clearType=" + clearType +
						",cacheMem=" + cacheMem + ",resendTimes=" + resendTimes  + ",numOpenMerchantOrder=" + numOpenMerchantOrder +"]";
			}

			public String getEpayCode() {
				return epayCode;
			}

			@JSONField(name = "epayCode")
			public void setEpayCode(String epayCode) {
				this.epayCode = epayCode;
			}

			public String getMerchantId() {
				return merchantId;
			}

			@JSONField(name = "merchantId")
			public void setMerchantId(String merchantId) {
				this.merchantId = merchantId;
			}

			public String getInput1() {
				return input1;
			}

			@JSONField(name = "input1")
			public void setInput1(String input1) {
				this.input1 = input1;
			}

			public String getInput2() {
				return input2;
			}

			@JSONField(name = "input2")
			public void setInput2(String input2) {
				this.input2 = input2;
			}

			public String getInput3() {
				return input3;
			}

			@JSONField(name = "input3")
			public void setInput3(String input3) {
				this.input3 = input3;
			}

			public String getInput4() {
				return input4;
			}

			@JSONField(name = "input4")
			public void setInput4(String input4) {
				this.input4 = input4;
			}

			public String getInput5() {
				return input5;
			}

			@JSONField(name = "input5")
			public void setInput5(String input5) {
				this.input5 = input5;
			}

			public String getUserId() {
				return userId;
			}

			@JSONField(name = "userId")
			public void setUserId(String userId) {
				this.userId = userId;
			}

			public String getTraceNo() {
				return traceNo;
			}

			@JSONField(name = "traceNo")
			public void setTraceNo(String traceNo) {
				this.traceNo = traceNo;
			}

			public String getAmtRule() {
				return amtRule;
			}

			@JSONField(name = "amtRule")
			public void setAmtRule(String amtRule) {
				this.amtRule = amtRule;
			}

			public String getPayBillCount() {
				return payBillCount;
			}

			@JSONField(name = "payBillCount")
			public void setPayBillCount(String payBillCount) {
				this.payBillCount = payBillCount;
			}

			public String getPayBillAmt() {
				return payBillAmt;
			}

			@JSONField(name = "payBillAmt")
			public void setPayBillAmt(String payBillAmt) {
				this.payBillAmt = payBillAmt;
			}

			public String getPayBillNo() {
				return payBillNo;
			}

			@JSONField(name = "payBillNo")
			public void setPayBillNo(String payBillNo) {
				this.payBillNo = payBillNo;
			}

			public String getOptionName() {
				return optionName;
			}

			@JSONField(name = "optionName")
			public void setOptionName(String optionName) {
				this.optionName = optionName;
			}

			public String getOptionCode() {
				return optionCode;
			}

			@JSONField(name = "optionCode")
			public void setOptionCode(String optionCode) {
				this.optionCode = optionCode;
			}

			public String getOptionAmt() {
				return optionAmt;
			}

			@JSONField(name = "optionAmt")
			public void setOptionAmt(String optionAmt) {
				this.optionAmt = optionAmt;
			}

			public String getPayType() {
				return payType;
			}

			@JSONField(name = "payType")
			public void setPayType(String payType) {
				this.payType = payType;
			}

			public String getPayAcc() {
				return payAcc;
			}

			@JSONField(name = "payAcc")
			public void setPayAcc(String payAcc) {
				this.payAcc = payAcc;
			}

			public String getTransPaySeq() {
				return transPaySeq;
			}

			@JSONField(name = "transPaySeq")
			public void setTransPaySeq(String transPaySeq) {
				this.transPaySeq = transPaySeq;
			}

			public String getTransDate() {
				return transDate;
			}

			@JSONField(name = "transDate")
			public void setTransDate(String transDate) {
				this.transDate = transDate;
			}

			public String getTransTime() {
				return transTime;
			}

			@JSONField(name = "transTime")
			public void setTransTime(String transTime) {
				this.transTime = transTime;
			}

			public String getSettleDate() {
				return settleDate;
			}

			@JSONField(name = "settleDate")
			public void setSettleDate(String settleDate) {
				this.settleDate = settleDate;
			}

			public String getClearType() {
				return clearType;
			}

			@JSONField(name = "clearType")
			public void setClearType(String clearType) {
				this.clearType = clearType;
			}

			public String getCacheMem() {
				return cacheMem;
			}

			@JSONField(name = "cacheMem")
			public void setCacheMem(String cacheMem) {
				this.cacheMem = cacheMem;
			}

			@JSONField(name = "resendTimes")
			public String getResendTimes() {
				return resendTimes;
			}

			@JSONField(name = "resendTimes")
			public void setResendTimes(String resendTimes) {
				this.resendTimes = resendTimes;
			}

			@JSONField(name = "numOpenMerchantOrder")
			public String getNumOpenMerchantOrder() {
				return numOpenMerchantOrder;
			}

			@JSONField(name = "numOpenMerchantOrder")
			public void setNumOpenMerchantOrder(String numOpenMerchantOrder) {
				this.numOpenMerchantOrder = numOpenMerchantOrder;
			}
		}
	}
}