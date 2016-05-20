<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>红包申请管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript"
src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
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
		$("#applyid").val("");
		$("#applystatus").val("");
		$("#st").val("");
		$("#et").val("");
		$("#applystatus").find("option[value='']").attr("selected",true);
		$(".select2-chosen").html("--请选择--");
	}
	function aduit(){
		var applyId = $("input[name='aduitCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要审核的申请单！");
			return;
		}
		var url="${ctx}/hb/mallWxRedpacketApply/form?id="+applyId+"&type=aduit";
		location.href=url;
	}
// 	<shiro:hasPermission name="hb:mallWxRedpacketApply:edit"><td>
// 	<c:if test="${mallWxRedpacketApply.applystatus eq 1 or mallWxRedpacketApply.applystatus eq 4 }">
// 	<a href="${ctx}/hb/mallWxRedpacketApply/form?id=${mallWxRedpacketApply.applyid}">修改</a>
// 	</c:if>
// 	<a href="${ctx}/hb/mallWxRedpacketApply/form?id=${mallWxRedpacketApply.applyid}&type=query">查看</a>
// </td></shiro:hasPermission>
	function edit(){
		debugger;
		var applyId = $("input[name='editCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要编辑的申请单！");
			return;
		}
		var status = $("input[name='editCheck']:checked")[0].parentNode.parentNode.cells[1].firstChild.value;
		status = status.trim();
		if(status=="1"||status=="4"){
			var url="${ctx}/hb/mallWxRedpacketApply/form?id="+applyId;
			location.href=url;
		}else{
			layer.alert("当前申请单不可以编辑！");
		}
	}
	function query(){
		var applyId = $("input[name='editCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要查看的申请单！");
			return;
		}
		var url="${ctx}/hb/mallWxRedpacketApply/form?id="+applyId+"&type=query";
		location.href=url;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/hb/mallWxRedpacketApply/">红包申请列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="mallWxRedpacketApply" action="${ctx}/hb/mallWxRedpacketApply/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>申请单号：</label>
				<form:input path="applyid" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>申请状态</label>
				<form:select path="applystatus" class="input-medium">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('hb_apply_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>申请日期：</label>
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallWxRedpacketApply.startTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					~
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallWxRedpacketApply.endTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input class="btn" type="button" value="重置" onclick="resetBtn()" /></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<shiro:hasPermission name="hb:mallWxRedpacketApply:edit">
			<li>
				<a class="btn btn-primary" href="${ctx}/hb/mallWxRedpacketApply/form" ><i
				class="icon-plus"></i>新增</a>&nbsp;
				<a class="btn btn-primary" href="javascript:edit();" ><i
				class="icon-pencil"></i>编辑</a>&nbsp;
				<a class="btn btn-primary" href="javascript:query();" ><i
				class="icon-zoom-in"></i>查看</a>&nbsp;
			</li>
			</shiro:hasPermission>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>序号</th>
				<th>申请单号</th>
				<th>申请日期</th>
				<th>红包总个数（个）</th>
				<th>红包总金额（元）</th>
				<th>申请状态</th>
				<th>审核意见</th>
				<th>申请人</th>
				<th>审核人</th>
				<th>审核日期</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallWxRedpacketApply" varStatus="status">
			<tr>
				<td><input type="radio" name="editCheck" value="${mallWxRedpacketApply.applyid}"></td>
				<td><input type="hidden" value="${mallWxRedpacketApply.applystatus }"/>${status.index+1 }</td>
				<td>
					${mallWxRedpacketApply.applyid}
				</td>
				<td>
					<fmt:formatDate value="${mallWxRedpacketApply.applydate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${mallWxRedpacketApply.redpacketTotalCount}
				</td>
				<td>
					${mallWxRedpacketApply.redpacketTotalAmount}
				</td>
				<td>
					${fns:getDictLabel(mallWxRedpacketApply.applystatus, 'hb_apply_status', '')}
				</td>
				<td>${mallWxRedpacketApply.backReason }</td>
				<td>${mallWxRedpacketApply.applyercode }</td>
				<td>${mallWxRedpacketApply.checkername }</td>
				<td><fmt:formatDate value="${mallWxRedpacketApply.checkdate }" pattern="yyyy-MM-dd"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>