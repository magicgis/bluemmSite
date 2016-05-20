<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>虚拟产品管理</title>
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
		<li class="active"><a href="${ctx}/mallvirtualproduct/mallVirtualProduct/">虚拟产品列表</a></li>
		<shiro:hasPermission name="mallvirtualproduct:mallVirtualProduct:edit"><li><a href="${ctx}/mallvirtualproduct/mallVirtualProduct/form">虚拟产品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallVirtualProduct" action="${ctx}/mallvirtualproduct/mallVirtualProduct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>产品SKU：</label>
				<form:input path="vpSku" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>产品名称：</label>
				<form:input path="vpName" htmlEscape="false" maxlength="500" class="input-medium"/>
			</li>
			<li><label>产品类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label="--请选择--" />
					<form:options items="${fns:getDictList('virtual_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>序号</th>
				<th class="sort-column vp_sku">产品SKU</th>
				<th class="sort-column vp_name">产品名称</th>
				<th class="sort-column type">产品类型</th>
				<th class="sort-column market_price">市场价</th>
				<shiro:hasPermission name="mallvirtualproduct:mallVirtualProduct:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallVirtualProduct" varStatus="status">
			<tr>
				<th>${status.index+1 }</th>
				<th>${mallVirtualProduct.vpSku }</th>
				<th>${mallVirtualProduct.vpName }</th>
				<th>${fns:getDictLabel(mallVirtualProduct.type, 'virtual_type', '')}</th>
				<th>${mallVirtualProduct.marketPrice/100 }</th>
				<shiro:hasPermission name="mallvirtualproduct:mallVirtualProduct:edit"><td>
    				<a href="${ctx}/mallvirtualproduct/mallVirtualProduct/form?vpId=${mallVirtualProduct.vpId}">修改</a>
					<a href="${ctx}/mallvirtualproduct/mallVirtualProduct/delete?vpId=${mallVirtualProduct.vpId}" onclick="return confirmx('确认要删除该虚拟产品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>