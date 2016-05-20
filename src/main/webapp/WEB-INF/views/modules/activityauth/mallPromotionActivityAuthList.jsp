<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推券活动授权管理</title>
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
		
		function resetBtn(){
			$("#activityCode").val("");
			$("#activitySName").val("");
			$("#st").val("");
			$("#et").val("");
		    $("#status").find("option[value='']").attr("selected",true);
			$(".select2-chosen").html("--请选择--");
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/activityauth/mallPromotionActivityAuth/">推券活动授权列表</a></li>
		<shiro:hasPermission name="activityauth:mallPromotionActivityAuth:edit"><li><a href="${ctx}/activityauth/mallPromotionActivityAuth/form">推券活动授权添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPromotionActivityAuth" action="${ctx}/activityauth/mallPromotionActivityAuth/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>活动编码：</label>
				<form:input path="activityCode" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>活动名称：</label>
				<form:input path="activitySName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>活动时间：</label>
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionActivityAuth.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			    ~
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionActivityAuth.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>授权状态：</label>
				<form:select path="status" style="width:177px;" id="status">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('activity_auth_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input class="btn" type="button" value="重置" onclick="resetBtn()" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th align="center">序号</th>
				<th align="center">活动编码</th>
				<th align="center">活动名称</th>
				<th align="center">活动开始时间</th>
				<th align="center">活动结束时间</th>
				<th align="center">授权状态</th>
				<th align="center">操作人编号</th>
				<th align="center">操作人</th>
				<th align="center">操作时间</th>
				<shiro:hasPermission name="activityauth:mallPromotionActivityAuth:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallPromotionActivityAuth" varStatus="status">
			<tr>
				<td align="center">
					${status.index+1 }
				</td>
				<td align="center">${mallPromotionActivityAuth.activityCode}</td>
				<td align="center">${mallPromotionActivityAuth.activitySName}</td>
				<td align="center"><fmt:formatDate value="${mallPromotionActivityAuth.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center"><fmt:formatDate value="${mallPromotionActivityAuth.endTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center">${fns:getDictLabel(mallPromotionActivityAuth.status, 'activity_auth_status', '')}</td>
				<td align="center">${mallPromotionActivityAuth.updateCode}</td>
				<td align="center">${mallPromotionActivityAuth.updatBy}</td>
				<td align="center"><fmt:formatDate value="${mallPromotionActivityAuth.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<shiro:hasPermission name="activityauth:mallPromotionActivityAuth:edit"><td>
					 <a href="${ctx}/activityauth/mallPromotionActivityAuth/info?authId=${mallPromotionActivityAuth.authId}" >查看</a>
					 <c:if test="${mallPromotionActivityAuth.status == 1}">
					<a href="${ctx}/activityauth/mallPromotionActivityAuth/modify?id=${mallPromotionActivityAuth.authId}" >编辑</a>
					<a href="${ctx}/activityauth/mallPromotionActivityAuth/cancel?authId=${mallPromotionActivityAuth.authId}&status=2" onclick="return confirmx('确认要取消该活动授权信息？', this.href)">取消</a>
					</c:if> 
    				<%-- <a href="${ctx}/activityauth/mallPromotionActivityAuth/form?id=${mallPromotionActivityAuth.id}">修改</a>
					<a href="${ctx}/activityauth/mallPromotionActivityAuth/delete?id=${mallPromotionActivityAuth.id}" onclick="return confirmx('确认要删除该推券活动授权吗？', this.href)">删除</a> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>