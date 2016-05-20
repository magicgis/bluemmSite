<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推荐商品管理</title>
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
		function setPage(){
			$("#pageNo").val(1);
			return true;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/recommend/mallItemRecommend/">推荐商品列表</a></li>
		<shiro:hasPermission name="recommend:mallItemRecommend:edit"><li><a href="${ctx}/recommend/mallItemRecommend/form">推荐商品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallItemRecommend" action="${ctx}/recommend/mallItemRecommend/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>商品SKU：</label>
				<form:input path="itemSku" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>推荐类型：</label>
				<form:select path="recommendType" class="input-medium">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('recommend_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>开始时间：</label>
				<input name="onRecomDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallItemRecommend.onRecomDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="offRecomDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallItemRecommend.offRecomDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			</li>
			<li><label>商品状态：</label>
				<form:select path="itemStatus" class="input-medium">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('item_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>是否推荐：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
				<th>商品SKU</th>
				<th>商品名称</th>
				<th>推荐类型</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th class="sort-column position">排序序号</th>
				<th>商品状态</th>
				<shiro:hasPermission name="recommend:mallItemRecommend:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallItemRecommend" varStatus="status">  
			<tr>
				<td align="center"><a href="${ctx}/recommend/mallItemRecommend/form?id=${mallItemRecommend.id}">
					${status.index+1}
				</a></td>
				<td>
					${mallItemRecommend.itemSku}
				</td>
				<td align="center">
					${mallItemRecommend.itemName}
				</td>
				<td>
					${fns:getDictLabel(mallItemRecommend.recommendType, 'recommend_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${mallItemRecommend.onRecomDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mallItemRecommend.offRecomDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mallItemRecommend.position}
				</td>
				<td>
					${fns:getDictLabel(mallItemRecommend.itemStatus, 'item_status', '')}
				</td>
				<shiro:hasPermission name="recommend:mallItemRecommend:edit"><td>
    				<a href="${ctx}/recommend/mallItemRecommend/form?id=${mallItemRecommend.id}">修改</a>
<%-- 					<a href="${ctx}/recommend/mallItemRecommend/delete?id=${mallItemRecommend.id}" onclick="return confirmx('确认要删除该推荐商品吗？', this.href)">删除</a> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>