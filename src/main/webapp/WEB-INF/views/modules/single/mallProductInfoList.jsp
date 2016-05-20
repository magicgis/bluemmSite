<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function priceChange(){
			debugger;
			var marketPrice = $("#marketPrice").val();
			if(marketPrice==null||marketPrice==""){
				$("#marketPrice").val(0);
			}else if(isNaN(marketPrice)){
				layer.alert("市场价必须是数字");
				$("#marketPrice").val(0);
			}else{}
		}
		function setPage(){
			$("#pageNo").val(1);
			return true;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/single/mallProductInfo/">产品列表</a></li>
		<shiro:hasPermission name="single:mallProductInfo:edit"><li><a href="${ctx}/single/mallProductInfo/form">产品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallProductInfo" action="${ctx}/single/mallProductInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>产品sku：</label>
				<form:input path="productSku" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>产品名称：</label>
				<form:input path="productName" htmlEscape="false" maxlength="500" class="input-medium"/>
			</li>
			<li><label>市场价：</label>
				<form:input path="marketPrice" htmlEscape="false" maxlength="11" class="input-medium" onblur="priceChange()"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="setPage();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>单品SKU</th>
				<th>单品名称</th>
				<th>市场价</th>
<!-- 				<th>条码</th> -->
<!-- 				<th>箱规</th> -->
				<th>产品类型</th>	
				<th>创建人</th>
				<th>创建时间</th>
				<th>修改人</th>
				<th>修改时间</th>
				<shiro:hasPermission name="single:mallProductInfo:edit"><th width="75px">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallProductInfo" varStatus="status">
			<tr>
				<td align="center"><a href="${ctx}/single/mallProductInfo/form?productId=${mallProductInfo.productId}">
					${status.index+1}
				</a></td>
				<td>
					${mallProductInfo.productSku}
				</td>
				<td align="center">
					${mallProductInfo.productName}
				</td>
				<td>
					${mallProductInfo.marketPrice/100}
				</td>
				<td>${fns:getDictLabel(mallProductInfo.type, 'product_type', '')}</td>
<!-- 				<td> -->
<%-- 					${mallProductInfo.barCode} --%>
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					${mallProductInfo.carton} --%>
<!-- 				</td> -->
				<td>
					${mallProductInfo.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${mallProductInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mallProductInfo.modifyBy}
				</td>
				<td>
					<fmt:formatDate value="${mallProductInfo.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="single:mallProductInfo:edit"><td>
    				<a href="${ctx}/single/mallProductInfo/form?productId=${mallProductInfo.productId}">修改</a>
					<a href="${ctx}/single/mallProductInfo/delete?productId=${mallProductInfo.productId}" onclick="return confirmx('确认要删除该产品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>