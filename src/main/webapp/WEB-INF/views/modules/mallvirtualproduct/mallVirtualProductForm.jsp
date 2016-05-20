<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>虚拟产品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/common/json2.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/lib/jquery.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.min.js"></script>
	<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.min.css">
	<script type="text/javascript">
		$(document).ready(function() {
			var url ;
			var li;
			url = '${mallVirtualProduct.picUrl}';
			if(url!=null&&url!=""){
				$("#picUrlPreview").children().remove();
				li = "<li><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
				li += "<a href=\"javascript:\" onclick=\"picUrlDel(this);\">×</a></li>";
				$("#picUrlPreview").append(li);
			}
			$("#inputForm").validate({
				rules : {
					vpSku : {
						required : true,
						minlength: 8,
						maxlength: 8,
						remote: {
							url: "${ctx}/mallvirtualproduct/mallVirtualProduct/checkProductSku",   	//后台处理程序
							type: "post",               //数据发送方式
							dataType: "json",           //接受数据格式   
							data: {                     //要传递的数据
								vpSku: function() {
									return $("#vpSku").val();
								},
								vpId: function() {
									return $("#vpId").val();
								}
							}
						}
					},
					vpName : {
						required : true
					},
					marketPriceFloat : {
						required : true,
						number : true,
						min : 0.01
					},
					type : {
						required : true
					}
				},
				messages : {
					vpSku : {
						required : "请输入产品SKU",
						minlength: "产品SKU必须为8位字符",
						maxlength: "产品SKU必须为8位字符",
						remote : "重复SKU"
					},
					vpName : {
						required : "请输入产品名称"
					},
					marketPriceFloat : {
						required : "请输入市场价",
						number : "请输入合法数字",
						min : "请输入大于0的数字"
					},
					type : {
						required : "请选择产品类型"
					}
				},
				submitHandler: function(form){
					var picUrl = $("#picUrl").val();
					if(picUrl==null||picUrl==""){
						layer.alert("请添加图片 ");
						return;
					}
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
		<li><a href="${ctx}/mallvirtualproduct/mallVirtualProduct/">虚拟产品列表</a></li>
		<li class="active"><a href="${ctx}/mallvirtualproduct/mallVirtualProduct/form?id=${mallVirtualProduct.id}">虚拟产品<shiro:hasPermission name="mallvirtualproduct:mallVirtualProduct:edit">${not empty mallVirtualProduct.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="mallvirtualproduct:mallVirtualProduct:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallVirtualProduct" action="${ctx}/mallvirtualproduct/mallVirtualProduct/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="vpId"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">产品SKU：</label>
			<div class="controls">
				<form:input path="vpSku" htmlEscape="false" maxlength="32" class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称：</label>
			<div class="controls">
				<form:input path="vpName" htmlEscape="false" maxlength="500" class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">市场价：</label>
			<div class="controls">
				<form:input path="marketPriceFloat" htmlEscape="false" maxlength="11" class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品类型：</label>
			<div class="controls">
				<form:select path="type" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('virtual_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片：</label>
			<div class="controls">
				<sys:ckfinder input="picUrl" type="images" uploadPath="/photo" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
				<form:hidden id="picUrl" path="picUrl" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" />
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="mallvirtualproduct:mallVirtualProduct:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>