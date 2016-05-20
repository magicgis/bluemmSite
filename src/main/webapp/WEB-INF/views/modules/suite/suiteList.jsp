<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>套餐管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js">
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/common/json2.js">
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(function(){
		     $('.table tr').click(function(){
		    	 debugger;
		    	 var param=$(this).find("td:first input");
				 var suiteId = param[0].defaultValue;
		    	 see(suiteId);
		     });
		}) 
		function see(suiteId) {
			$("#productInfo").empty();
			if(suiteId!=undefined){
				$.ajax({
					  type: 'POST',
					  url: '${ctx}/suite/mallItemSuite/suiteDetail',
					  data: "suiteId="+suiteId ,
					  success: function(data){
						  debugger;
						  if(data.length>0){
							  for(var i=0;i<data.length;i++){
								 var obj= data[i];
								 if(obj.sgoodType=="single"){
									 obj.sgoodType="单品";
								 }else if(obj.sgoodType=="suite"){
									 obj.sgoodType="套装";
								 }
								 var newStr = "<tr>";
								 newStr += "<td>" + obj.sgoodSku + "</td>";
								 newStr += "<td>" + obj.itemName + "</td>";
								 newStr += "<td>" + obj.sgoodType + "</td>";
								 newStr += "<td>" + obj.marketPrice/100 + "</td>";
								 newStr += "<td>" + obj.num + "</td>";
								 newStr += "</tr>";
								 $("#productInfo").append(newStr);
							  }
						  }
					  }
					});
			}
			
		}
		function setPage(){
			$("#pageNo").val(1);
			return true;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/suite/mallItemSuite/suiteList/">套餐列表</a></li>
		<shiro:hasPermission name="suite:mallItemSuite:edit"><li><a href="${ctx}/suite/mallItemSuite/form">套餐添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallItemSuite" action="${ctx}/suite/mallItemSuite/suiteList/" method="post" class="breadcrumb form-search">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
	<ul class="ul-form">
		<li><label>套餐SKU：</label>
			<form:input path="suiteSku" htmlEscape="false" maxlength="32" class="input-medium"/>
		</li>
		<li><label>套餐名称：</label>
			<form:input path="suiteName" htmlEscape="false" maxlength="500" class="input-medium"/>
		</li>
		<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="setPage();"/></li>
		<li class="clearfix"></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<th>序号</th>
			<th>套餐SKU</th>
			<th>套餐名称</th>
			<th>产品类型</th>	
			<th>市场价</th>
			<th>创建人</th>
			<th>创建时间</th>
			<th>修改人</th>
			<th>修改时间</th>
			<shiro:hasPermission name="suite:mallItemSuite:edit"><th width="75px">操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="suite" varStatus="status">
			<tr>
				<td>${status.index+1 }<input type="hidden" value="${suite.suiteId }"></td>
				<td>${suite.suiteSku}</td>
				<td>${suite.suiteName}</td>
				<td>${fns:getDictLabel(suite.type, 'product_type', '')}</td>
				<td>${suite.marketPrice/100}</td>
 				<td>${suite.createByWho}</td>
				<td><fmt:formatDate value="${suite.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${suite.modifyBy}</td>
				<td><fmt:formatDate value="${suite.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="suite:mallItemSuite:edit"><td>
    				<a href="${ctx}/suite/mallItemSuite/form?suiteId=${suite.suiteId}">修改</a>
					<a href="${ctx}/suite/mallItemSuite/delete?suiteId=${suite.suiteId}" onclick="return confirmx('确认要删除该产品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	套装明细
		<table id="list" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>产品ID</th><th>产品名称</th><th >货物类型</th><th>市场价</th><th>数量</th></tr></thead>
			<tbody id="productInfo">
			</tbody>
		</table>
	</form:form>
</body>
</html>