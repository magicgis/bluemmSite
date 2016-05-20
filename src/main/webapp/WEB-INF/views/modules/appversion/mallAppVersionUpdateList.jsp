<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>版本信息管理</title>
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
		<li class="active"><a href="${ctx}/appversion/mallAppVersionUpdate/">版本信息列表</a></li>
		<shiro:hasPermission name="appversion:mallAppVersionUpdate:edit"><li><a href="${ctx}/appversion/mallAppVersionUpdate/form">版本信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallAppVersionUpdate" action="${ctx}/appversion/mallAppVersionUpdate/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>强制更新：</label>
				<form:select path="mustUpdate" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>版本号：</label>
				<form:input path="version" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>平台：</label>
				<form:select path="platform" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mall_app_platform')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>app类型：</label>
				<form:input path="appType" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('app_update_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>外部版本号</th>
				<th>平台</th>
				<th>是否强制更新</th>
				<th>内部编号</th>
				<th>app类型</th>
				<th>状态</th>
				<shiro:hasPermission name="appversion:mallAppVersionUpdate:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallAppVersionUpdate">
			<tr>
				<td>
					<a href="${ctx}/appversion/mallAppVersionUpdate/form?id=${mallAppVersionUpdate.id}">
						${mallAppVersionUpdate.version}
					</a>
				</td>
				<td>
					${fns:getDictLabel(mallAppVersionUpdate.platform, 'mall_app_platform', '')}
					
				</td>
				<td>
					${fns:getDictLabel(mallAppVersionUpdate.mustUpdate, 'yes_no', '')}
				</td>
				<td>
					${mallAppVersionUpdate.buildVersion}
				</td>
				<td>
					${mallAppVersionUpdate.appType}
				</td>
				<td>
					${fns:getDictLabel(mallAppVersionUpdate.status, 'app_update_status', '')}
				</td>
				<shiro:hasPermission name="appversion:mallAppVersionUpdate:edit"><td>
    				<a href="${ctx}/appversion/mallAppVersionUpdate/form?id=${mallAppVersionUpdate.id}">修改</a>
<%-- 					<a href="${ctx}/appversion/mallAppVersionUpdate/delete?id=${mallAppVersionUpdate.id}" onclick="return confirmx('确认要删除该版本信息吗？', this.href)">删除</a> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>