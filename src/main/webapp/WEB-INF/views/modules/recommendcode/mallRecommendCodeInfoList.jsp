<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推荐码管理</title>
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
		<li class="active"><a href="${ctx}/recommendcode/mallRecommendCodeInfo/">推荐码列表</a></li>
		<shiro:hasPermission name="recommendcode:mallRecommendCodeInfo:edit"><li><a href="${ctx}/recommendcode/mallRecommendCodeInfo/form">推荐码添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallRecommendCodeInfo" action="${ctx}/recommendcode/mallRecommendCodeInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>order_code</th>
				<th>old_recommend</th>
				<th>new_recommend</th>
				<th>update_by_code</th>
				<th>update_time</th>
				<th>aduit_status</th>
				<th>aduit_reason</th>
				<th>aduit_by</th>
				<th>aduit_time</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="recommendcode:mallRecommendCodeInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallRecommendCodeInfo">
			<tr>
				<td><a href="${ctx}/recommendcode/mallRecommendCodeInfo/form?id=${mallRecommendCodeInfo.id}">
					${mallRecommendCodeInfo.orderCode}
				</a></td>
				<td>
					${mallRecommendCodeInfo.oldRecommend}
				</td>
				<td>
					${mallRecommendCodeInfo.newRecommend}
				</td>
				<td>
					${mallRecommendCodeInfo.updateByCode.id}
				</td>
				<td>
					<fmt:formatDate value="${mallRecommendCodeInfo.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mallRecommendCodeInfo.aduitStatus}
				</td>
				<td>
					${mallRecommendCodeInfo.aduitReason}
				</td>
				<td>
					${mallRecommendCodeInfo.aduitBy}
				</td>
				<td>
					<fmt:formatDate value="${mallRecommendCodeInfo.aduitTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mallRecommendCodeInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mallRecommendCodeInfo.remarks}
				</td>
				<shiro:hasPermission name="recommendcode:mallRecommendCodeInfo:edit"><td>
    				<a href="${ctx}/recommendcode/mallRecommendCodeInfo/form?id=${mallRecommendCodeInfo.id}">修改</a>
					<a href="${ctx}/recommendcode/mallRecommendCodeInfo/delete?id=${mallRecommendCodeInfo.id}" onclick="return confirmx('确认要删除该推荐码吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>