<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>券主数据管理</title>
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
			$("#couponCode").val("");
			$("#couponSName").val("");
			$("#st").val("");
			$("#et").val("");
			$("#couponType").find("option[value='']").attr("selected",true);
		    $("#status").find("option[value='']").attr("selected",true);
			$(".select2-chosen").html("--请选择--");
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/coupon/mallPromotionCouponInfo/">活动券列表</a></li>
		<shiro:hasPermission name="coupon:mallPromotionCouponInfo:edit"><li><a href="${ctx}/coupon/mallPromotionCouponInfo/form">活动券添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPromotionCouponInfo" action="${ctx}/coupon/mallPromotionCouponInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>券编码：</label>
				<form:input path="couponCode" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>券简称：</label>
				<form:input path="couponSName" htmlEscape="false" maxlength="256" class="input-medium"/>
			</li>
			<li><label>有效时间：</label>
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionCouponInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			     ~
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionCouponInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label>券类型：</label>
				<form:select path="couponType" style="width:177px;">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('coupon_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>序号</th>
				<th>券编码</th>
				<th>券类型</th>
				<th>券简称</th>
				<th>券名称</th>
				<th>状态</th>
				<th>时间类型</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>领取后有效天数</th>
				<th>操作人编号</th>
				<th>操作人</th>
				<th>操作时间</th>
				<shiro:hasPermission name="coupon:mallPromotionCouponInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallCouponInfo" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${mallCouponInfo.couponCode }</td>
				<td>${fns:getDictLabel(mallCouponInfo.couponType, 'coupon_type', '')}</td>
				<td>${mallCouponInfo.couponSName }</td>
				<td>${mallCouponInfo.couponName }</td>
				<td>${fns:getDictLabel(mallCouponInfo.status, 'coupon_status', '')}</td>
				<td>${fns:getDictLabel(mallCouponInfo.timeType, 'coupon_time_type', '')}</td>
				<td><fmt:formatDate value="${mallCouponInfo.startTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${mallCouponInfo.endTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${mallCouponInfo.validDaysStr }</td>
 				<td>${mallCouponInfo.opCode }</td> 
				<td>${mallCouponInfo.op }</td>
				<td><fmt:formatDate value="${mallCouponInfo.opTime }"
 							pattern="yyyy-MM-dd HH:mm:ss" /></td> 
				<shiro:hasPermission name="coupon:mallPromotionCouponInfo:edit"><td>
    				<a href="${ctx}/coupon/mallPromotionCouponInfo/form?id=${mallCouponInfo.couponId}">查看</a>
    				<c:if test="${mallCouponInfo.status == 1 ||mallCouponInfo.status == 2 }">
						<a href="${ctx}/coupon/mallPromotionCouponInfo/delete?id=${mallCouponInfo.couponId}" onclick="return confirmx('是否确认取消？', this.href)">取消</a>
    				</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>