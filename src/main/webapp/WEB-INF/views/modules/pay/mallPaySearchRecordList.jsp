<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>第三方同步记录管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pay/mallPaySearchRecord/">第三方同步记录列表</a></li>
		<shiro:hasPermission name="pay:mallPaySearchRecord:edit"><li><a href="${ctx}/pay/mallPaySearchRecord/form">第三方同步记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPaySearchRecord" action="${ctx}/pay/mallPaySearchRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="outTradeNo" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>支付平台：</label>
				<form:input path="payPlatform" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>支付时间：</label>
				<input name="payTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPaySearchRecord.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>交易流水号：</label>
				<form:input path="transactionId" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>支付状态：</label>
				<form:input path="payStatus" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>支付金额：</label>
				<form:input path="totalFee" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>是否认证：</label>
				<form:input path="validate" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键ID</th>
				<th>订单号</th>
				<th>支付平台</th>
				<th>创建时间</th>
				<th>支付时间</th>
				<th>交易流水号</th>
				<th>支付状态</th>
				<th>支付金额</th>
				<th>接口返回信息</th>
				<th>是否认证</th>
				<th>类型</th>
				<th>买方</th>
				<th>卖方</th>
				<th>数据日期</th>
				<th>订单类型</th>
				<shiro:hasPermission name="pay:mallPaySearchRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallPaySearchRecord">
			<tr>
				<td><a href="${ctx}/pay/mallPaySearchRecord/form?id=${mallPaySearchRecord.id}">
					${mallPaySearchRecord.payrecordId}
				</a></td>
				<td>
					${mallPaySearchRecord.outTradeNo}
				</td>
				<td>
					${mallPaySearchRecord.payPlatform}
				</td>
				<td>
					<fmt:formatDate value="${mallPaySearchRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mallPaySearchRecord.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mallPaySearchRecord.transactionId}
				</td>
				<td>
					${mallPaySearchRecord.payStatus}
				</td>
				<td>
					${mallPaySearchRecord.totalFee}
				</td>
				<td>
					${mallPaySearchRecord.responseMsg}
				</td>
				<td>
					${mallPaySearchRecord.validate}
				</td>
				<td>
					${mallPaySearchRecord.transCodeMsg}
				</td>
				<td>
					${mallPaySearchRecord.buyerAccount}
				</td>
				<td>
					${mallPaySearchRecord.sellerAccount}
				</td>
				<td>
					${mallPaySearchRecord.dataDate}
				</td>
				<td>
					${mallPaySearchRecord.tradeType}
				</td>
				<shiro:hasPermission name="pay:mallPaySearchRecord:edit"><td>
    				<a href="${ctx}/pay/mallPaySearchRecord/form?id=${mallPaySearchRecord.id}">修改</a>
					<a href="${ctx}/pay/mallPaySearchRecord/delete?id=${mallPaySearchRecord.id}" onclick="return confirmx('确认要删除该第三方同步记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>