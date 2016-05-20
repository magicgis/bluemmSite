<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推荐码管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/localization/messages_zh.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var type = $("#office").val();
			if(type == '0'){
				
			}else if(type == '1'){
				document.getElementById("check").style.display = 'none';
			}else if(type == '2'){
				document.getElementById("add").style.display = 'none';
				document.getElementById("tadd").style.display = 'none';
				document.getElementById("edit").style.display = 'none';
			}else{
				document.getElementById("add").style.display = 'none';
				document.getElementById("tadd").style.display = 'none';
				document.getElementById("edit").style.display = 'none';
				document.getElementById("check").style.display = 'none';
			}
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
		<li class="active"><a href="${ctx}/recommendinfo/mallRecommendInfo/">推荐码列表</a></li>
		<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit"><li><a id="tadd" href="${ctx}/recommendinfo/mallRecommendInfo/form">推荐码添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallRecommendInfo" action="${ctx}/recommendinfo/mallRecommendInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="office" name="office" type="hidden" value="${office}"/>
		<input id="choose" name="choose" type="hidden" value=""/>
		<input id="aduitStatuss" name="aduitStatuss" type="hidden" value=""/>
		<ul class="ul-form">
			<li><label>订单编号：</label>
				<form:input path="orderCode" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;订单付款时间：
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallRecommendInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			    至
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallRecommendInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注册人手机号：
				<form:input path="registrantMobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否有原推荐码：
				<form:select path="haveRecommend" style="width:177px;" id="haveRecommend">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('recommend_is_have')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>原推荐码：</label>
				<form:input path="oldRecommend" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>审核状态：</label>
				<form:select path="aduitStatus" style="width:177px;" id="aduitStatus">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('recommend_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input class="btn" type="button" value="重置" onclick="resetBtn()" /></li>
			<li class="clearfix"></li>
		</ul>
		<div class="btn-group">
			<a class="btn btn-primary" href="#" onclick="javascript:add();" id="add"><i
				class="icon-file"></i>新建</a> <a class="btn btn-info" href="#"
				onclick="javascript:edit();" id="edit"><i class="icon-edit"></i>编辑</a> <a href="#"
				class="btn btn-warning" onclick="javascript:check();" id="check"><i
				class="icon-off"></i>审核</a>
		</div>
		<!-- <ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul> -->
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th align="center">&nbsp;</th>
				<th align="center">序号</th>
				<th align="center">订单编号</th>
				<th align="center">订单付款时间</th>
				<th align="center">订单金额</th>
				<th align="center">注册人</th>
				<th align="center">注册人手机号</th>
				<th align="center">原推荐码</th>
				<th align="center">修改后推荐码</th>
				<th align="center">修改人</th>
				<th align="center">修改时间</th>
				<th align="center">审核状态</th>
				<th align="center">审核原因说明</th>
				<th align="center">审核人</th>
				<th align="center">审核时间</th>
				<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallRecommendInfo" varStatus="status">
			<tr>
				<td>
					<input type="radio" name="recommend" value="${mallRecommendInfo.id}" onclick="choose('${mallRecommendInfo.id}','${mallRecommendInfo.aduitStatus }')">
				</td>
				<td align="center">
					${status.index+1 }
				</td>
				<td align="center">${mallRecommendInfo.orderCode}</td>
				<td align="center"><fmt:formatDate value="${mallRecommendInfo.orderPayTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center">${mallRecommendInfo.orderMoney}</td>
				<td align="center">${mallRecommendInfo.registrant}</td>
				<td align="center">${mallRecommendInfo.registrantMobile}</td>
				<c:choose>
					<c:when test="${mallRecommendInfo.oldRecommend eq '(未填推荐码)'}">
						<td align="center"><span style="color:red">(未填推荐码)</span></td>
					</c:when>
					<c:otherwise>
						<td align="center">${mallRecommendInfo.oldRecommend}</td>
					</c:otherwise>
				</c:choose>
				<td align="center">${mallRecommendInfo.newRecommend}</td>
				<td align="center">${mallRecommendInfo.updatBy}</td>
				<td align="center"><fmt:formatDate value="${mallRecommendInfo.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<c:choose>
					<c:when test="${mallRecommendInfo.aduitStatus eq '2'}">
					<td align="center"><span style="color:red">${fns:getDictLabel(mallRecommendInfo.aduitStatus, 'recommend_status', '')}</span></td>
					</c:when>
					<c:otherwise>
						<td align="center">${fns:getDictLabel(mallRecommendInfo.aduitStatus, 'recommend_status', '')}</td>
					</c:otherwise>
				</c:choose>
				<td align="center">${mallRecommendInfo.aduitReason}</td>
				<td align="center">${mallRecommendInfo.aduitBy}</td>
				<td align="center"><fmt:formatDate value="${mallRecommendInfo.aduitTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit"><td>
					<a href="#" onclick="getOperItem('${mallRecommendInfo.id}')">查看明细</a>
    				<%-- <a href="${ctx}/recommendinfo/mallRecommendInfo/form?id=${mallRecommendInfo.id}">修改</a> --%>
					<%-- <a href="${ctx}/recommendinfo/mallRecommendInfo/delete?id=${mallRecommendInfo.id}" onclick="return confirmx('确认要删除该推荐码吗？', this.href)">删除</a> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
<script type="text/javascript">
function getOperItem(id){
	var url = '${ctx}/recommendinfo/mallRecommendInfo/getOperItem?id='+id;
	pageii = layer.open({
		title : "操作明细",
		type : 2,
		area : [ "70%", "50%" ],
		content : url,
		end : function() {
		}
	});
}

function add(){
	var url = '${ctx}/recommendinfo/mallRecommendInfo/form';
	window.location.href = url;
}

function choose(id,aduitStatuss){
	$("#choose").val(id);
	$("#aduitStatuss").val(aduitStatuss);
}

function edit(){
	var ids = $("#choose").val();
	var status = $("#aduitStatuss").val();
	if(ids == null || ids == ''){
		layer.alert("请选择订单");
		return false;
	}
	if('2' != status){
		layer.alert("当前状态不能编辑");
		return false;
	}
	var url = '${ctx}/recommendinfo/mallRecommendInfo/modify?id='+ids;
	window.location.href = url;
}

function check(){
	var ids = $("#choose").val();
	var status = $("#aduitStatuss").val();
	if(ids == null || ids == ''){
		layer.alert("请选择订单");
		return false;
	}
 	if('0' != status){
		layer.alert("当前状态不能审核");
		return false;
	}
 		var url = '${ctx}/recommendinfo/mallRecommendInfo/check?id='+ids;
 		window.location.href = url;
	
}

function resetBtn(){
	$("#orderCode").val("");
	$("#st").val("");
	$("#et").val("");
	$("#registrantMobile").val("");
	$("#oldRecommend").val("");
    $("#aduitStatus").find("option[value='']").attr("selected",true);
    $("#haveRecommend").find("option[value='']").attr("selected",true); 
	$(".select2-chosen").html("--请选择--");	
}
</script>
</body>
</html>