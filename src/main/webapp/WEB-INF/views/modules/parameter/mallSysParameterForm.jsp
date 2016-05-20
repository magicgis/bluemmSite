<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数管理</title>
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
		<li><a href="${ctx}/parameter/mallSysParameter/">系统参数列表</a></li>
		<li class="active"><a href="${ctx}/parameter/mallSysParameter/form?id=${mallSysParameter.paraId}">系统参数<shiro:hasPermission name="parameter:mallSysParameter:edit">${not empty mallSysParameter.paraId?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="parameter:mallSysParameter:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallSysParameter" action="${ctx}/parameter/mallSysParameter/save" method="post" class="form-horizontal">
		<form:hidden path="paraId"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">参数类型：</label>
			<div class="controls">
				<form:input path="paraType" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参数编码：</label>
			<div class="controls">
				<form:input path="paraCode" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述内容：</label>
			<div class="controls">
				<form:input path="paraDesc" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参数值：</label>
			<div class="controls">
				<form:input path="paraValue" htmlEscape="false" maxlength="1024" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="parameter:mallSysParameter:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>