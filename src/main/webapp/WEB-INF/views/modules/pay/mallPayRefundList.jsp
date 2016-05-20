<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退款记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }

		function delete_fnt(ids){
		    if(null==ids || ids==""){
		    	layer.alert("请至少选择一个订单!");
		    	return;
		    }
			var url = '${ctx}/pay/mallPayRefund/delete?ids='+ids;
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:url,
		        success: function(msg) {
		        	var isSuccess = msg.isSuccess;
		        	var responseCode = msg.responseCode;
		        	var responseMsg = msg.responseMsg;
		        	if(isSuccess && responseCode==100){
	        			alert(responseMsg);
	        			page(1,15,'');
	        			//location.reload();
	        			//window.location.href=window.location.href;
		        	} else {
	        			alert(responseMsg);
	        			page(1,15,'');
	        			//location.reload();
	        			//window.location.href=window.location.href;
		        	}
		        }
		    });
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
		<li class="active"><a href="${ctx}/pay/mallPayRefund/">退款记录列表</a></li>
		<%--
		<shiro:hasPermission name="pay:mallPayRefund:edit"><li><a href="${ctx}/pay/mallPayRefund/form">退款记录添加</a></li></shiro:hasPermission>
		 --%>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPayRefund" action="${ctx}/pay/mallPayRefund/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value=" pay_time desc"/>
		<ul class="ul-form">
			<%--
			<li><label>主键ID：</label>
				<form:input path="id" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			 --%>
			<li><label>订单号：</label>
				<form:input path="outTradeNo" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>交易流水号：</label>
				<form:input path="transactionId" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>退款金额：</label>
				<form:input path="totalFee1" htmlEscape="false" maxlength="11" class="input-medium" style='width:80px'
				onblur="check(this)"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" />~
				<form:input path="totalFee2" htmlEscape="false" maxlength="11" class="input-medium" style='width:80px'
				onblur="check(this)"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" />
			</li>
			<li><label>退款时间：</label>
				<input name="payTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPayRefund.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>支付平台：</label>
				<form:select path="payPlatform" class="input-medium">
					<form:option value="" label="---请选择---"/>
					<form:options items="${fns:getDictList('payPlatform')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>支付时间：</label>
				<input name="payTime1" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPayRefund.payTime1}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					~
				<input name="payTime2" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPayRefund.payTime2}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<!-- 
				<th>主键ID</th>
				 -->
				<th>订单号</th>
				<th>交易流水号</th>
				<th>支付平台</th>
				<th>创建时间</th>
				<th>退款时间</th>
				<th>支付状态</th>
				<th>支付金额</th>
				<th>是否认证</th>
				<th>退款号</th>
				<th>订单类型</th>
				
				<shiro:hasPermission name="pay:mallPayRefund:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallPayRefund">
			<tr>
			<!-- 
				<td><a href="${ctx}/pay/mallPayRefund/form?id=${mallPayRefund.id}">
					${mallPayRefund.id}
				</a></td>
				 -->
				<td>
					${mallPayRefund.outTradeNo}
				</td>
				<td>
					${mallPayRefund.transactionId}
				</td>
				<td>
					${fns:getDictLabel(mallPayRefund.payPlatform, 'payPlatform', '')}
				</td>
				<td>
					<fmt:formatDate value="${mallPayRefund.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mallPayRefund.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(mallPayRefund.payStatus, 'payStatus', '')}
				</td>
				<td align="right" style="text-align:right">
					${mallPayRefund.totalFee/100}&nbsp;
				</td>
				<td>
					${fns:getDictLabel(mallPayRefund.validate, 'validate', '')}
				</td>
				<td>
					${mallPayRefund.transCodeMsg}
				</td>
				<td>
					${mallPayRefund.tradeType}
				</td>
				<shiro:hasPermission name="pay:mallPayRefund:edit"><td>
				
					<c:if test="${mallPayRefund.payStatus ne 'SUCCESS' }">
						 <a href="javascript:delete_fnt('${mallPayRefund.id}');" onclick="return confirmx('确认要删除该退款记录吗？', this.href)">删除</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>