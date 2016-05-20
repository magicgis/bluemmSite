<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手动推券管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出推券信息吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/couponmen/mallPromotionActivityCouponMensend/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/couponmen/mallPromotionActivityCouponMensend/");
			$("#searchForm").submit();
        	return false;
        }
		function resetBtn(){
			$("#userMobile").val("");
			$("#activityCode").val("");
			$("#creatCode").val("");
			$("#st").val("");
			$("#et").val("");
			$("#isSend").find("option[value='']").attr("selected",true);
			$(".select2-chosen").html("--请选择--");
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/couponmen/mallPromotionActivityCouponMensend/">手动推券列表</a></li>
		<shiro:hasPermission name="couponmen:mallPromotionActivityCouponMensend:edit"><li><a href="${ctx}/couponmen/mallPromotionActivityCouponMensend/form">手动推券添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallPromotionActivityCouponMensend" action="${ctx}/couponmen/mallPromotionActivityCouponMensend/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>消费者手机：</label>
				<form:input path="userMobile" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>活动编号：</label>
				<form:input path="activityCode" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>操作人编号：</label>
				<form:input path="creatCode" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>操作时间：</label>
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionCouponInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			     ~
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallPromotionCouponInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label>推券状态：</label>
				<form:select path="isSend" style="width:177px;">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('send_coupon_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>&nbsp;</label>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input class="btn" type="button" value="重置" onclick="resetBtn()" /></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>消费者手机号</th>
				<th>活动编码</th>
				<th>活动名称</th>
				<th>推送券数</th>
				<th>推送券</th>
				<th>推券状态</th>
				<th>操作人编号</th>
				<th>操作人</th>
				<th>操作时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="send" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${send.userMobile }</td>
				<td>${send.activityCode }</td>
				<td>${send.activitySName }</td>
				<td>${send.couponNum }</td>
				<td>${send.couponStr }</td>
				<td>${fns:getDictLabel(send.isSend, 'send_coupon_status', '')}</td>
				<td>${send.creatCode }</td>
				<td>${send.creatBy }</td>
				<td><fmt:formatDate value="${send.creatTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td> 
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>