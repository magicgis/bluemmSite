<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
	<title>支付记录管理</title>
	<meta name="decorator" content="default"/>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/common/json2.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		

		// var json = JSON.stringify(goods);
		function refund_alipay_fnt(outTradeNo){
			var str = "";
			if(null!=outTradeNo) str = outTradeNo;
		    if(str==""){
		    	layer.alert("请至少选择一个订单!");
		    	return;
		    }
			var url = '${ctx}/pay/mallPayRefund/refund_alipay?outTradeNos='+str;
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:url,
		        success: function(msg) {
		        	var isSuccess = msg.isSuccess;
		        	var responseCode = msg.responseCode;
		        	var responseMsg = msg.responseMsg;
		        	var refund_url = msg.refund_url;
		        	if(isSuccess && responseCode==100){
		        		responseMsg = null!=responseMsg?responseMsg.trim():"";
		        		if(""==responseMsg){
			            	window.open(refund_url);
		        		} else {
		        			alert(responseMsg);
		        			window.open(refund_url);
		        		}
		        	} else {
		        		layer.alert(responseMsg);
		        	}
		        }
		    });
		}

		function refund_wxpay_fnt(outTradeNo){
			var str = "";
			if(null!=outTradeNo) str = outTradeNo;
			else {
			}
		    if(str==""){
		    	layer.alert("请至少选择一个订单!");
		    	return;
		    }
		      //loading层
	        var index = layer.load(1, {
	          shade: [0.5,'#fff'] //0.1透明度的白色背景
	        });
			var url = '${ctx}/pay/mallPayRefund/refund_wxpay?outTradeNos='+str;
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:url,
		        success: function(msg) {
		        	var isSuccess = msg.isSuccess;
		        	var responseCode = msg.responseCode;
		        	var responseMsg = msg.responseMsg;
		        	var refund_url = msg.refund_url;
		        	if(isSuccess && responseCode==100){
	        			alert(responseMsg);
		        		layer.alert(responseMsg);
	        			page(1,15,'');
		        	} else {
	        			alert(responseMsg);
	        			page(1,15,'');
		        	}
		        }
		    });
		}
		

		function validate_fnt(outTradeNo){
			var str = "";
			if(null!=outTradeNo) str = outTradeNo;
		    if(str==""){
		    	layer.alert("请至少选择一个订单!");
		    	return;
		    }
			var url = '${ctx}/modules/pay/validate?out_trade_no='+str;
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:url,
		        success: function(msg) {
		        	var isSuccess = msg.isSuccess;
		        	var responseCode = msg.responseCode;
		        	var responseMsg = msg.responseMsg;
		        	var refund_url = msg.refund_url;
		        	if(isSuccess && responseCode==100){
	        			alert(responseMsg);
	        			page(1,15,'');
		        	} else {
	        			alert(responseMsg);
	        			page(1,15,'');
		        	}
		        }
		    });
		}
		
		function checkAllItem() {
			var ischeck = $("#itemCheckAll").attr("checked");
			if (ischeck == undefined) {
				$(":checkbox").removeAttr("checked");
			} else {
				$(":checkbox").attr("checked", "checked");
			}
		}
		function refund_batch_fnt(){
			var str="";
			var payPlatform = "";
			var isSamePayPlatform = true;
	        $("input[name='itemCheckBox']:checkbox").each(function(){ 
	            if($(this).attr("checked")){
	            	var value = $(this).val().trim();
	            	var array = value.split(',');
	                str += array[0]+",";
	                if(""==payPlatform){
		                payPlatform += array[1];
	                } else {
	                	if(payPlatform!=array[1]){
	                		isSamePayPlatform = false;
	                	}
	                }
	            }
	        });
	       
	        if(!isSamePayPlatform){
	        	layer.alert("请选择同一个支付平台的订单!");
	        	return;
	        } else {
		        if(str==""){
		        	layer.alert("请至少选择一个订单!");
		        	return;
		        }
		      
		        if(payPlatform=="alipay"){
		        	refund_alipay_fnt(str);
		        } else if(payPlatform=="wxpay"){
		        	refund_wxpay_fnt(str);
		        }
	        }
		}
		
		function check(e) { 
		    var re = /^\d+(?=\.{0,1}\d+$|$)/ 
		        if (e.value != "") { 
		            if (!re.test(e.value)) { 
		                alert("请输入正确的数字"); 
		                e.value = ""; 
		                e.focus(); 
		            } 
		        } 
		    } 
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pay/mallPayPaymentInfo/">支付记录列表</a></li>
		<%--
		<shiro:hasPermission name="pay:mallPayPaymentInfo:edit"><li><a href="${ctx}/pay/mallPayPaymentInfo/form">支付记录添加</a></li></shiro:hasPermission>
		 --%>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPayPaymentInfo" action="${ctx}/pay/mallPayPaymentInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value=" pay_time desc"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="outTradeNo" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>交易流水号：</label>
				<form:input path="transactionId" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>支付状态：</label>
				<form:select path="payStatus" class="input-medium">
					<form:option value="" label="---请选择---"/>
					<form:options items="${fns:getDictList('payStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>支付金额：</label>
				<form:input path="totalFee1" htmlEscape="false" maxlength="11" class="input-medium" style='width:80px'
				onblur="check(this)"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" />~
				<form:input path="totalFee2" htmlEscape="false" maxlength="11" class="input-medium" style='width:80px'
				onblur="check(this)"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" />
			</li>
			<li><label>支付平台：</label>
				<form:select path="payPlatform" class="input-medium">
					<form:option value="" label="---请选择---"/>
					<form:options items="${fns:getDictList('payPlatform')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>支付时间：</label>
				<input name="payTime1" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPayPaymentInfo.payTime1}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					~
				<input name="payTime2" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPayPaymentInfo.payTime2}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>是否验证：</label>
				<form:select path="validate" class="input-medium">
					<form:option value="" label="---请选择---"/>
					<form:options items="${fns:getDictList('validate')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
		<div class="btn-group">
			<a class="btn btn-primary" href="javascript:refund_batch_fnt();">
				<i class="icon-file"></i>退款
			</a>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="itemCheckAll" onclick="checkAllItem()"></th>
				<!-- 
				<th>主键ID</th> -->
				<th>订单号</th>
				<th>交易流水号</th>
				<th>支付状态</th>
				<th>支付金额</th>
				<th>支付平台</th>
				<th>订单类型</th>
				<!-- <th>商家编号</th> <th>预付款编号</th> -->
				<th>创建时间</th>
				<th>支付时间</th>
				<!-- <th>接口返回信息</th> -->
				<th>是否认证</th>
				<th>操作</th>
				<!-- <shiro:hasPermission name="pay:mallPayPaymentInfo:edit"></shiro:hasPermission> -->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallPayPaymentInfo">
			<tr>
				<td><input type="checkbox" name="itemCheckBox" value="${mallPayPaymentInfo.outTradeNo},${mallPayPaymentInfo.payPlatform}" payPlatform="${mallPayPaymentInfo.payPlatform}"></td>
				<!-- 
				<td><a href="${ctx}/pay/mallPayPaymentInfo/form?id=${mallPayPaymentInfo.id}">
					${mallPayPaymentInfo.paymentId}
				</a></td>
				 -->
				<td>
					${mallPayPaymentInfo.outTradeNo}
				</td>
				<td>
					${mallPayPaymentInfo.transactionId}
				</td>
				<td>
					${fns:getDictLabel(mallPayPaymentInfo.payStatus, 'payStatus', '')}
				</td>
				<td align="right" style="text-align:right"> 
					${mallPayPaymentInfo.totalFee/100}&nbsp;
				</td>
				<td>
					${fns:getDictLabel(mallPayPaymentInfo.payPlatform, 'payPlatform', '')}
				</td>
				<td>
					${mallPayPaymentInfo.tradeType}
				</td>
				<%-- 
				<td>
					${mallPayPaymentInfo.mchId}
				</td>
				<td>
					${mallPayPaymentInfo.prepayId}
				</td> --%>
				<td>
					<fmt:formatDate value="${mallPayPaymentInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mallPayPaymentInfo.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%-- 
				<td>
					${mallPayPaymentInfo.responseMsg}
				</td>
				 --%>
				<td>
					${fns:getDictLabel(mallPayPaymentInfo.validate, 'validate', '')}
				</td>
				<td>
				<shiro:hasPermission name="pay:mallPayPaymentInfo:edit">
    				<a href="javascript:validate_fnt('${mallPayPaymentInfo.outTradeNo}');" onclick="return confirmx('确认要同步该支付记录吗？', this.href)">同步</a>
    			</shiro:hasPermission>
				<shiro:hasPermission name="pay:mallPayRefund:refund">
					<c:if test="${mallPayPaymentInfo.payPlatform eq 'alipay' && mallPayPaymentInfo.payStatus eq 'SUCCESS'}">
						 <a href="javascript:refund_alipay_fnt('${mallPayPaymentInfo.outTradeNo}');" onclick="return confirmx('确认要退款该支付宝支付记录吗？', this.href)">退款</a>
					</c:if>
					<c:if test="${mallPayPaymentInfo.payPlatform eq 'wxpay' && mallPayPaymentInfo.payStatus eq 'SUCCESS'}">
						 <a href="javascript:refund_wxpay_fnt('${mallPayPaymentInfo.outTradeNo}');" onclick="return confirmx('确认要退款该微信支付记录吗？', this.href)">退款</a>
					</c:if>
    				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
