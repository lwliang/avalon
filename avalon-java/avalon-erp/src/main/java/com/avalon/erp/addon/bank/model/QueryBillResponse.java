/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  直连商户平台账单查询返回对象
 *  @author DELL
 * 
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class QueryBillResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 格式 */
	@JSONField(name = "format")
	private String format;
	
	/** 消息体 */
	@JSONField(name = "message")
	private Message message;
	
	
	public QueryBillResponse(){
		
	}
	
	/**
	 * 构造函数，通过输入对象，构造返回对象数据信息
	 * @param request
	 */
	public QueryBillResponse(QueryBillRequest request) {
		this.setFormat(request.getFormat());
		this.setMessage(new Message(request.getMessage()));
	}
	
	@Override
	public String toString() {
		return "QueryBillResponse[format=" + format + ",message=" + message==null?"":message.toString() + "]";
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
	 * 账单查询内部消息对象返回实体message内部类
	 * 
	 */
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public class Message implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		/** 消息头部 */
		@JSONField(name = "head")
		private Head head;
		
		/** 消息实体  */
		@JSONField(name = "info")
		private Info info;
		
		public Message() {
			this.head = new Head();
			this.info = new Info();
		}
		
		public Message(QueryBillRequest.Message requestMessage){
			this.setHead(new Head(requestMessage.getHead()));
			this.setInfo(new Info(requestMessage.getInfo()));
		}
		
		@Override
		public String toString() {
			return "QueryBillResponse.Message[head=" + head.toString() + ",info=" + info.toString() + "]";
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
		 *  
		 * 账单查询内部消息对象返回实体Head内部类
		 * 
		 */
		@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
		public class Head implements Serializable {
			
			private static final long serialVersionUID = 1L;
			
			/**  渠道 */
			@JSONField(name = "channel")
			private String channel;
			
			/**  交易码  */
			@JSONField(name = "transCode")
			private String transCode;
			
			/**  交易上行下送标志  */
			@JSONField(name = "transFlag")
			private String transFlag;
			
			/**  缴费中心交易序列号 */
			@JSONField(name = "transSeqNum")
			private String transSeqNum;
			
			/** 时间戳  */
			@JSONField(name = "timeStamp")
			private String timeStamp;
			
			/**  查询返回码 */
			@JSONField(name = "returnCode")
			private String returnCode ;
			
			/**  返回提示信息  */
			@JSONField(name = "returnMessage")
			private String returnMessage;
			

			public Head(QueryBillRequest.Message.Head reqMessHead) {
				this.setChannel(reqMessHead.getChannel());
				this.setTransSeqNum(reqMessHead.getTransSeqNum());
				this.setTransCode(reqMessHead.getTransCode());
				this.setReturnCode("");
				this.setReturnMessage("");
				this.setTimeStamp("");
			}

			
			public Head() {
				
			}
			@Override
			public String toString() {
				return "QueryBillResponse.Message.Head[channel=" + channel  + ",transCode=" + transCode + ",transSeqNum=" + transSeqNum
						+ ",timeStamp=" + timeStamp + ",returnCode=" + returnCode + ",returnMessage=" + returnMessage  + "]";
			}
			public String getChannel() {
				return channel;
			}

			public String getTransCode() {
				return transCode;
			}

			@JSONField(name = "transCode")
			public void setTransCode(String transCode) {
				this.transCode = transCode;
			}
			
			public String getTransFlag() {
				return transFlag;
			}

			@JSONField(name = "transFlag")
			public void setTransFlag(String transFlag) {
				this.transFlag = transFlag;
			}
			
			@JSONField(name = "channel")
			public void setChannel(String channel) {
				this.channel = channel;
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

			@JSONField(name = "timestamp")
			public void setTimeStamp(String timeStamp) {
				this.timeStamp = timeStamp;
			}
			
			public String getReturnCode() {
				return returnCode;
			}

			@JSONField(name = "returnCode")
			public void setReturnCode(String returnCode) {
				this.returnCode = returnCode;
			}

			public String getReturnMessage() {
				return returnMessage;
			}

			@JSONField(name = "returnMessage")
			public void setReturnMessage(String returnMessage) {
				this.returnMessage = returnMessage;
			}


		}

		/**
		 *  
		 * 账单查询内部消息对象返回实体Info内部类
		 * 
		 */
		@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
		public class Info implements Serializable {
			
			private static final long serialVersionUID = 1L;
			
			/** 缴费项目唯一标识号*/
			@JSONField(name = "epayCode")
			private String epayCode;
			
			/** 直连商户第三方商户号*/
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
			
			/** 户主名称*/
			@JSONField(name = "custName")
			private String custName;
			
			/** 户主地址*/
			@JSONField(name = "custAddress")
			private String custAddress;
			
			/** 账单关联客户手机号,部分项目可根据该字段，要求客户做短信验证码校验后才显示账单结果*/
			@JSONField(name = "cellPhone")
			private String cellPhone;
			
			/** 缓存域信息*/
			@JSONField(name = "cacheMem")
			private String cacheMem;
			
			/** 备注字段*/
			@JSONField(name = "remark")
			private String remark;
			
			/** 跳转商户地址*/
			@JSONField(name = "callBackUrl")
			private String callBackUrl;
			
			/** 跳转商户地址超链接提示*/
			@JSONField(name = "callBackText")
			private String callBackText;
			
			/** 缴费金额计算规则*/
			@JSONField(name = "amtRule")
			private String amtRule;
			
			/** 子账单数量*/
			@JSONField(name = "totalBillCount")
			private String totalBillCount;
			/** 定制附言信息(有定制电子回单附言信息的，需添加自定义定制附言信息字段)*/
			@JSONField(name = "merchantRemark")
			private String merchantRemark;
			
			/** 账单信息体*/
			@JSONField(name = "bills")
			private ArrayList<Bill> bills;
			
			
			public Info() {
				
			}
			public Info(QueryBillRequest.Message.Info reqMessInfo) {
			    this.setEpayCode(reqMessInfo.getEpayCode());
			    this.setInput1(reqMessInfo.getInput1());
			    this.setInput2(reqMessInfo.getInput2());
			    this.setInput3(reqMessInfo.getInput3());
			    this.setInput4(reqMessInfo.getInput4());
			    this.setInput5(reqMessInfo.getInput5());
			    this.bills = new ArrayList<Bill>();
			}
			
			/**
			 * @return the merchantRemark
			 */
			public String getMerchantRemark() {
				return merchantRemark;
			}

			/**
			 * @param merchantRemark the merchantRemark to set
			 */
			@JSONField(name = "merchantRemark")
			public void setMerchantRemark(String merchantRemark) {
				this.merchantRemark = merchantRemark;
			}

			@Override
			public String toString() {
				return "QueryBillResponse.Message.Info[epayCode=" + epayCode + ", input1="
						+ input1 + ", input2=" + input2 + ", input3=" + input3
						+ ", input4=" + input4 + ", input5=" + input5
						+ ", totalBillCount=" + totalBillCount + ",merchantRemark=" 
						+ merchantRemark + ", bills=" + bills.toString() + "]";
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
			
			public String getTraceNo() {
				return traceNo;
			}

			@JSONField(name = "traceNo")
			public void setTraceNo(String traceNo) {
				this.traceNo = traceNo;
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

			public String getCustName() {
				return custName;
			}

			@JSONField(name = "custName")
			public void setCustName(String custName) {
				this.custName = custName;
			}
			
			public String getCustAddress() {
				return custAddress;
			}

			@JSONField(name = "custAddress")
			public void setCustAddress(String custAddress) {
				this.custAddress = custAddress;
			}
			
			public String getCellPhone() {
				return cellPhone;
			}
			
			@JSONField(name = "cellPhone")
			public void setCellPhone(String cellPhone) {
				this.cellPhone = cellPhone;
			}
			
			public String getCacheMem() {
				return cacheMem;
			}

			@JSONField(name = "cacheMem")
			public void setCacheMem(String cacheMem) {
				this.cacheMem = cacheMem;
			}
			
			public String getRemark() {
				return remark;
			}

			@JSONField(name = "remark")
			public void setRemark(String remark) {
				this.remark = remark;
			}
			
			public String getCallBackUrl() {
				return callBackUrl;
			}

			@JSONField(name = "callBackUrl")
			public void setCallBackUrl(String callBackUrl) {
				this.callBackUrl = callBackUrl;
			}

			public String getCallBackText() {
				return callBackText;
			}

			@JSONField(name = "callBackText")
			public void setCallBackText(String callBackText) {
				this.callBackText = callBackText;
			}

			public String getAmtRule() {
				return amtRule;
			}

			@JSONField(name = "amtRule")
			public void setAmtRule(String amtRule) {
				this.amtRule = amtRule;
			}
			
			public String getTotalBillCount() {
				return totalBillCount;
			}

			@JSONField(name = "totalBillCount")
			public void setTotalBillCount(String totalBillCount) {
				this.totalBillCount = totalBillCount;
			}

			public ArrayList<Bill> getBills() {
				return bills;
			}

			/**
			 *  @param bills
			 *  设置账单循环域子账单
			 */
			@JSONField(name = "bills")
			public void setBill(ArrayList<Bill> bills) {
				this.bills = bills;
			}


			/**
			 *  
			 * 账单查询内部消息对象返回实体Bill内部类
			 * 
			 */
			@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
			public class Bill implements Serializable {
				
				private static final long serialVersionUID = 1L;

				/** 账单编号*/
				@JSONField(name = "billNo")
				private String billNo;
				
				/** 账单名称*/
				@JSONField(name = "billName")
				private String billName;
				
				/** 欠费金额*/
				@JSONField(name = "oweAmt")
				private String oweAmt;
				
				/** 手续费*/
				@JSONField(name = "feeAmt")
				private String feeAmt;
				
				/** 最小金额*/
				@JSONField(name = "minAmt")
				private String minAmt;
				
				/** 最大金额*/
				@JSONField(name = "maxAmt")
				private String maxAmt;
				
				/** 余额*/
				@JSONField(name = "balance")
				private String balance;
				
				/** 缴费账单到期日*/
				@JSONField(name = "expireDate")
				private String expireDate;
				
				/** 收款商户号*/
				@JSONField(name = "rcvMerchantId")
				private String rcvMerchantId;
				
				/** 收款账号*/
				@JSONField(name = "rcvAcc")
				private String rcvAcc;
				
				/** 分账模板号*/
				@JSONField(name = "tempSplitAcc")
				private String tempSplitAcc;		

				/** 均匀时段缴费 */
				@JSONField(name = "unitDetail")
				private UnitDetail unitDetail;
				
				/** 选择套餐*/
				@JSONField(name = "optionDetails")
				private ArrayList<OptionDetail> optionDetails;
				
				/** 账单详情描述*/
				@JSONField(name = "descDetails")
				private ArrayList<DescDetail> descDetails;
				/** 平台商户分账子商户循环域 */
				@JSONField(name = "splitSubMerInfos")
				private ArrayList<SplitSubMerInfo> splitSubMerInfos;
				
				/** 子账单必缴标志，多账单合并支付时默认选中不可不选  true：必选    false：非必选 */
				@JSONField(name = "mustPayFlag")
				private String mustPayFlag;
				
				
				/**
				 * @return
				 */
				public String getMustPayFlag() {
					return mustPayFlag;
				}
				
				/**
				 * @param mustPayFlag
				 */
				@JSONField(name = "mustPayFlag")
				public void setMustPayFlag(String mustPayFlag) {
					this.mustPayFlag = mustPayFlag;
				}
				
				/**
				 * @return
				 */
				public String getTempSplitAcc() {
					return tempSplitAcc;
				}
				/**
				 * 获取平台商户分账子商户循环域信息
				 * @return
				 */
				public ArrayList<SplitSubMerInfo> getSplitSubMerInfos() {
					return splitSubMerInfos;
				}
				/**
				 * 设置平台商户分账子商户循环域信息
				 * @param splitSubMerInfos
				 */
				@JSONField(name = "splitSubMerInfos")
				public void setSplitSubMerInfos(ArrayList<SplitSubMerInfo> splitSubMerInfos) {
					this.splitSubMerInfos = splitSubMerInfos;
				}
				
				public Bill() {
					
				}

				@Override
				public String toString() {
					return "Bill [billNo=" + billNo + ", billName=" + billName
							+ ", oweAmt=" + oweAmt + ", feeAmt=" + feeAmt
							+ ", minAmt=" + minAmt + ", maxAmt=" + maxAmt
							+ ", balance=" + balance + ", expireDate="
							+ expireDate + ", rcvMerchantId=" + rcvMerchantId
							+ ", rcvAcc=" + rcvAcc + ", tempSplitAcc="
							+ tempSplitAcc + ", unitDetail=" + unitDetail
							+ ", optionDetails=" + optionDetails
							+ ", descDetails=" + descDetails
							+ ", splitSubMerInfos=" + splitSubMerInfos
							+ ", mustPayFlag=" + mustPayFlag + "]";
				}
				
				public String getBillNo() {
					return billNo;
				}

				@JSONField(name = "billNo")
				public void setBillNo(String billNo) {
					this.billNo = billNo;
				}

				public String getBillName() {
					return billName;
				}

				@JSONField(name = "billName")
				public void setBillName(String billName) {
					this.billName = billName;
				}

				public String getOweAmt() {
					return oweAmt;
				}

				@JSONField(name = "oweAmt")
				public void setOweAmt(String oweAmt) {
					this.oweAmt = oweAmt;
				}

				public String getFeeAmt() {
					return feeAmt;
				}

				@JSONField(name = "feeAmt")
				public void setFeeAmt(String feeAmt) {
					this.feeAmt = feeAmt;
				}

				public String getMinAmt() {
					return minAmt;
				}

				@JSONField(name = "minAmt")
				public void setMinAmt(String minAmt) {
					this.minAmt = minAmt;
				}

				public String getMaxAmt() {
					return maxAmt;
				}

				@JSONField(name = "maxAmt")
				public void setMaxAmt(String maxAmt) {
					this.maxAmt = maxAmt;
				}

				public String getBalance() {
					return balance;
				}
				@JSONField(name = "balance")
				public void setBalance(String balance) {
					this.balance = balance;
				}

				public String getExpireDate() {
					return expireDate;
				}
				@JSONField(name = "expireDate")
				public void setExpireDate(String expireDate) {
					this.expireDate = expireDate;
				}
				
				public String getRcvMerchantId() {
					return rcvMerchantId;
				}
				@JSONField(name = "rcvMerchantId")
				public void setRcvMerchantId(String rcvMerchantId) {
					this.rcvMerchantId = rcvMerchantId;
				}
				
				public String getRcvAcc() {
					return rcvAcc;
				}
				@JSONField(name = "rcvAcc")
				public void setRcvAcc(String rcvAcc) {
					this.rcvAcc = rcvAcc;
				}
				
				/**
				 * 分账模板号
				 * @return
				 */
				public String getTmpSplitAcc() {
					return tempSplitAcc;
				}
				@JSONField(name = "tempSplitAcc")
				public void setTempSplitAcc(String tempSplitAcc) {
					this.tempSplitAcc = tempSplitAcc;
				}
				
				public UnitDetail getUnitDetail() {
					return unitDetail;
				}
				@JSONField(name = "unitDetail")
				public void setUnitDetail(UnitDetail unitDetail) {
					this.unitDetail = unitDetail;
				}
				
				public ArrayList<DescDetail> getDescDetails() {
					return descDetails;
				}
				/**
				 *  设置账单描述详情键值对
				 *  @param descDetails
				 */
				@JSONField(name = "descDetails")
				public void setDescDetails(ArrayList<DescDetail> descDetails) {
					this.descDetails = descDetails;
				}

				public ArrayList<OptionDetail> getOptionDetails() {
					return optionDetails;
				}
				@JSONField(name = "optionDetails")
				public void setOptionDetails(ArrayList<OptionDetail> optionDetails) {
					this.optionDetails = optionDetails;
				}

				/**
				 * 均匀时段
				 * 
				 * @date 2017年12月21日
				 */
				@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
				public class UnitDetail implements Serializable {
					
					private static final long serialVersionUID = 1L;
					
					/** 单位名称 */
					@JSONField(name = "unitName")
					private String unitName;
					
					/** 单位金额 */
					@JSONField(name = "unitAmount")
					private String unitAmount;
					
					/** 最小单位数量 */
					@JSONField(name = "minUnitNum")
					private String minUnitNum;
					public UnitDetail(){}
					
					public UnitDetail(String unitName, String unitAmount, String minUnitNum) {
						super();
						this.unitName = unitName;
						this.unitAmount = unitAmount;
						this.minUnitNum = minUnitNum;
					}
					public String getUnitName() {
						return unitName;
					}
					
					@JSONField(name = "unitAmount")
					public void setUnitName(String unitName) {
						this.unitName = unitName;
					}
					
					public String getUnitAmount() {
						return unitAmount;
					}
					
					@JSONField(name = "unitAmount")
					public void setUnitAmount(String unitAmount) {
						this.unitAmount = unitAmount;
					}
					
					public String getMinUnitNum() {
						return minUnitNum;
					}
					
					@JSONField(name = "minUnitNum")
					public void setMinUnitNum(String minUnitNum) {
						this.minUnitNum = minUnitNum;
					}
					
					@Override
					public String toString() {
						return "QueryBillResponse.Message.Info.Bill.unitDetail[unitName=" + unitName
								+ ", unitAmount=" + unitAmount + ", minUnitNum=" + minUnitNum + "]";
					}
				}

				/**
				 * @title 选择套餐循环
				 *  
				 *  @date 2017-12-18
				 *  
				 * 
				 */
				@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
				public class OptionDetail implements Serializable {
					
					private static final long serialVersionUID = 1L;
					
					/**套餐编号 根据编号排序*/
					@JSONField(name = "optionCode")
					private String optionCode;
					
					/**套餐名称*/
					@JSONField(name = "optionName")
					private String optionName;
					
					/**套餐金额*/
					@JSONField(name = "optionAmt")
					private String optionAmt;
					
					public OptionDetail(){
						
					}
					
					public OptionDetail(String optionCode, String optionName, String optionAmt) {
						super();
						this.optionCode = optionCode;
						this.optionName = optionName;
						this.optionAmt = optionAmt;
					}

					public String getOptionCode() {
						return optionCode;
					}
					
					@JSONField(name = "optionCode")
					public void setOptionCode(String optionCode) {
						this.optionCode = optionCode;
					}
					
					public String getOptionName() {
						return optionName;
					}
					
					@JSONField(name = "optionName")
					public void setOptionName(String optionName) {
						this.optionName = optionName;
					}

					public String getOptionAmt() {
						return optionAmt;
					}
					@JSONField(name = "optionAmt")
					public void setOptionAmt(String optionAmt) {
						this.optionAmt = optionAmt;
					}

					@Override
					public String toString() {
						return "QueryBillResponse.Message.Info.Bill.OptionDetail[optionCode=" + optionCode
								+ ", optionName=" + optionName + ", optionAmt=" + optionAmt + "]";
					}

				}
				/**
				 * @title 账单详情
				 *  
				 *  @date 2017-12-18
				 */
				@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
				public class DescDetail implements Serializable {
					
					private static final long serialVersionUID = 1L;
					
					/**账单详情-名称*/
					@JSONField(name = "sCpt")
					private String sCpt;
					
					/**账单详情-取值*/
					@JSONField(name = "sVal")
					private String sVal;
					
					
					public DescDetail(){
						
					}
					
					public DescDetail(String sCpt, String sVal) {
						super();
						this.sCpt = sCpt;
						this.sVal = sVal;
					}

					public String getSCpt() {
						return sCpt;
					}

					@JSONField(name = "sCpt")
					public void setSCpt(String sCpt) {
						this.sCpt = sCpt;
					}

					public String getSVal() {
						return sVal;
					}

					@JSONField(name = "sVal")
					public void setSVal(String sVal) {
						this.sVal = sVal;
					}

					@Override
					public String toString() {
						return "QueryBillResponse.Message.Info.Bill.FeeDetail[sCpt=" + sCpt + ",sVal=" + sVal + "]";
					}
				}
				/**
				 * @title 平台商户分账子商户循环域
				 *  
				 * @date 2020-10-22
				 *  
				 * @author marui
				 */
				@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
				public class SplitSubMerInfo implements Serializable {

					private static final long serialVersionUID = 1L;

					/**二级子商户号*/
					@JSONField(name = "splitMerchantId")
					private String splitMerchantId;

					/**二级子商户分账金额*/
					@JSONField(name = "splitAmount")
					private String splitAmount;


					public SplitSubMerInfo(){

					}

					public SplitSubMerInfo(String splitMerchantId, String splitAmount) {
						super();
						this.splitMerchantId = splitMerchantId;
						this.splitAmount = splitAmount;
					}

					public String getSplitMerchantId() {
						return splitMerchantId;
					}

					@JSONField(name = "splitMerchantId")
					public void setSplitMerchantId(String splitMerchantId) {
						this.splitMerchantId = splitMerchantId;
					}

					public String getSplitAmount() {
						return splitAmount;
					}

					@JSONField(name = "splitAmount")
					public void setSplitAmount(String splitAmount) {
						this.splitAmount = splitAmount;
					}


					@Override
					public String toString() {
						return "QueryBillResponse.Message.Info.Bill.SplitSubMerInfo[splitMerchantId=" + splitMerchantId
								+ ", splitAmount=" + splitAmount + "]";
					}
				}
			}
		}
	}
}