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
		$("#sendStatus").find("option[value='']").attr("selected",true);
		$(".select2-chosen").html("--请选择--");
	}
	function send(){
		debugger;
		var applyId = $("input[name='sendCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要发放的申请单！");
			return;
		}
		var status = $("input[name='sendCheck']:checked")[0].parentNode.parentNode.cells[7].innerText;
		status = status.trim();
		if(status=="锁定"){
			layer.alert("申请单已锁定！");
			return;
		}else if(status=="已发放"){
			layer.alert("申请单已发放！");
			return;
		}
		var url="${ctx}/hb/mallWxRedpacketApply/form?id="+applyId+"&type=send";
		location.href=url;
	}
	function lockConfirmx(){
		debugger;
		var applyId = $("input[name='sendCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要锁定的申请单！");
			return;
		}
		var status = $("input[name='sendCheck']:checked")[0].parentNode.parentNode.cells[7].innerText;
		status = status.trim();
		if(status=="锁定"){
			layer.alert("申请单已锁定！");
			return;
		}
		var msg = confirmx('确认要锁定该申请单吗？',
				function(){
					lock();
				});
	}
	function lock(){
		var applyId = $("input[name='sendCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要锁定的申请单！");
			return;
		}
		var status = $("input[name='sendCheck']:checked")[0].parentNode.parentNode.cells[7].innerText;
		status = status.trim();
		if(status=="锁定"){
			layer.alert("申请单已锁定！");
			return;
		}
		var url = "${ctx}/hb/mallWxRedpacketApply/lock";
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				applyid : applyId
			}, 
			success : function(data){
				if(data!=null&&data!=""){
					var flag = data.flag;
					if(flag == "success"){
						var url="${ctx}/hb/mallWxRedpacketApply/list?type=send";
						layer.alert("锁定成功！",function(){location.href=url;});
					}else{
						layer.alert("锁定失败！");
					}
				}else{
					layer.alert("数据出错，请系统联系管理员!");
				}
			}
		});
	}
	function query(){
		var applyId = $("input[name='sendCheck']:checked").val();
		if(applyId==null||applyId==""){
			layer.alert("请选择需要查看的申请单！");
			return;
		}
		var url="${ctx}/hb/mallWxRedpacketApply/form?id="+applyId+"&type=send&sendType=query";
		location.href=url;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/hb/mallWxRedpacketApply/list?type=send">红包发放列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="mallWxRedpacketApply" action="${ctx}/hb/mallWxRedpacketApply/list?type=send" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>申请单号：</label>
				<form:input path="applyid" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>发放状态</label>
				<form:select path="sendStatus" class="input-medium">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('hb_send_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>审核日期：</label>
				<input id="st" name="startAduitTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallWxRedpacketApply.startAduitTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					~
				<input id="et" name="endAduitTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallWxRedpacketApply.endAduitTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input class="btn" type="button" value="重置" onclick="resetBtn()" /></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<a class="btn btn-danger" href="javascript:send();" ><i
				class="icon-share-alt"></i>发放</a>&nbsp;
			<a class="btn btn-primary" href="javascript:query();" ><i
				class="icon-zoom-in"></i>查看</a>&nbsp;
			<a class="btn btn-primary" href="javascript:lockConfirmx();"><i
				class="icon-off"></i>锁定</a> 
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>序号</th>
				<th>申请单号</th>
				<th>商户名称</th>
				<th>审核日期</th>
				<th>红包总个数（个）</th>
				<th>红包总金额（元）</th>
				<th>发放状态</th>
				<th>操作人</th>
				<th>操作时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallWxRedpacketApply" varStatus="status">
			<tr>
				<td><input type="radio" name="sendCheck" value="${mallWxRedpacketApply.applyid}"></td>
				<td><input type="hidden" value="${mallWxRedpacketApply.applystatus }"/>${status.index+1 }</td>
				<td>${mallWxRedpacketApply.applyid}</td>
				<td>${mallWxRedpacketApply.merchantName}</td>
				<td><fmt:formatDate value="${mallWxRedpacketApply.checkdate}" pattern="yyyy-MM-dd"/></td>
				<td>${mallWxRedpacketApply.redpacketTotalCount}</td>
				<td>${mallWxRedpacketApply.redpacketTotalAmount}</td>
				<td>
					${fns:getDictLabel(mallWxRedpacketApply.sendStatus, 'hb_send_status', '')}
				</td>
				<td>${mallWxRedpacketApply.operator}</td>
				<td><fmt:formatDate value="${mallWxRedpacketApply.operatortime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>