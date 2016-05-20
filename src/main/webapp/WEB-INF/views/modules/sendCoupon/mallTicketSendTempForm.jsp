<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>派发门票管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/">派发门票列表</a></li>
		<li class="active"><a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/form?id=${mallTicketSendTemp.id}">派发门票<shiro:hasPermission name="mallticketsendtemp:mallTicketSendTemp:edit">${not empty mallTicketSendTemp.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="mallticketsendtemp:mallTicketSendTemp:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallTicketSendTemp" action="${ctx}/mallticketsendtemp/mallTicketSendTemp/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">收票人手机号：</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发票数：</label>
			<div class="controls">
				<form:input path="sendNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动编号：</label>
			<div class="controls">
				<form:input path="actId" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="mallticketsendtemp:mallTicketSendTemp:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>