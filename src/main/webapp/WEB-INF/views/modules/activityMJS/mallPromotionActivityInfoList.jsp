<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优惠券活动管理</title>
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
			$("#activityName").val("");
			$("#st").val("");
			$("#et").val("");
			$("#activityTheme").find("option[value='']").attr("selected",true);
		    $("#status").find("option[value='']").attr("selected",true);
			$(".select2-chosen").html("--请选择--");
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/activityMJS/mallPromotionActivityInfo/">满就送活动列表</a></li>
		<shiro:hasPermission name="activityMJS:mallPromotionActivityInfo:edit"><li><a href="${ctx}/activityMJS/mallPromotionActivityInfo/form">满就送活动添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPromotionActivityInfo" action="${ctx}/activityMJS/mallPromotionActivityInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>活动编码：</label>
				<form:input path="activityCode" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>活动名称：</label>
				<form:input path="activityName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>活动时间：</label>
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionActivityInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					~
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionActivityInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>活动大类：</label>
				<form:select path="activityTheme" style="width:177px;">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('activity_category')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status" style="width:177px;">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('coupon_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th align="center">活动大类</th>
				<th align="center">活动名称</th>
				<th align="center">状态</th>
				<th align="center">开始时间</th>
				<th align="center">结束时间</th>
				<th align="center">操作人</th>
				<th align="center">操作时间</th>
				<shiro:hasPermission name="activityMJS:mallPromotionActivityInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallPromotionActivityInfo" varStatus="status">
			<tr>
				<td align="center">${status.index+1 }</td>
				<td align="center">${mallPromotionActivityInfo.activityCode}</td>
				<td align="center">${fns:getDictLabel(mallPromotionActivityInfo.activityTheme, 'activity_category', '')}</td>
				<td align="center">${mallPromotionActivityInfo.activityName}</td>
				<td align="center">${fns:getDictLabel(mallPromotionActivityInfo.status, 'coupon_status', '')}</td>
				<td align="center"><fmt:formatDate value="${mallPromotionActivityInfo.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center"><fmt:formatDate value="${mallPromotionActivityInfo.endTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center">${mallPromotionActivityInfo.updateBy.id}</td>
				<td align="center"><fmt:formatDate value="${mallPromotionActivityInfo.updateDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<shiro:hasPermission name="activityMJS:mallPromotionActivityInfo:edit"><td>
					<c:if test="${mallPromotionActivityInfo.status == 1}">
					<a href="${ctx}/activityMJS/mallPromotionActivityInfo/modify?id=${mallPromotionActivityInfo.activityId}">编辑</a>
					</c:if>
    				<a href="${ctx}/activityMJS/mallPromotionActivityInfo/form?id=${mallPromotionActivityInfo.activityId}">查看</a>
    				<c:if test="${mallPromotionActivityInfo.status == 1 ||mallPromotionActivityInfo.status == 2 }">
					<a href="${ctx}/activityMJS/mallPromotionActivityInfo/delete?id=${mallPromotionActivityInfo.activityId}" onclick="return confirmx('确认要取消该满就送活动吗？', this.href)">取消</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>