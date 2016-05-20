<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品管理</title>
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
			//$("#name").focus();
			var url ;
			var li;
			var urls ;
			url = '${mallProductInfo.picUrl}';
			if(url!=null&&url!=""){
				$("#picUrlPreview").children().remove();
				li = "<li><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
				li += "<a href=\"javascript:\" onclick=\"picUrlDel(this);\">×</a></li>";
				$("#picUrlPreview").append(li);
			}
			$("#inputForm").validate({
				rules : {
					productSku : {
						required : true,
						minlength: 8,
						maxlength: 8,
						startWith: true,
						remote: {
							url: "${ctx}/single/mallProductInfo/checkProductSku",   	//后台处理程序
							type: "post",               //数据发送方式
							dataType: "json",           //接受数据格式   
							data: {                     //要传递的数据
								productSku: function() {
									return $("#productSku").val();
								},
								productId: function() {
									return $("#productId").val();
								}
							}
						}
					},
					productName : {
						required : true
					},
					marketPrice : {
						required : true,
						number : true,
						min : 0
					},
					carton : {
						digits : true,
						min : 0
					},
					picUrl : {
						required : true
					},
					type : {
						required : true
					}
				},
				messages : {
					productSku : {
						required : "请输入产品SKU",
						minlength: "产品SKU必须为8位字符",
						maxlength: "产品SKU必须为8位字符",
						startWith : "产品SKU第一位数字必须是1",
						remote : "重复SKU"
					},
					productName : {
						required : "请输入产品名称"
					},
					marketPrice : {
						required : "请输入市场价",
						number : "请输入合法数字",
						min : "请输入大于0的数字"
					},
					picUrl : {
						required : "请添加图片"
					},
					carton : {
						digits : "箱规必须是整数",
						min : "箱规必须是大于0的整数"
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
		jQuery.validator.addMethod("startWith",
				function(value, element){
					var returnVal = false;  
					var firstStr = value.substr(0,1);
					if(firstStr=='1'){
						returnVal = true;
					}
	                return returnVal; 
			},""); 
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/single/mallProductInfo/">产品列表</a></li>
		<li class="active"><a href="${ctx}/single/mallProductInfo/form?id=${mallProductInfo.id}">产品<shiro:hasPermission name="single:mallProductInfo:edit">${not empty mallProductInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="single:mallProductInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallProductInfo" action="${ctx}/single/mallProductInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="productId"/>
		<sys:message content="${message}"/>	
		<table style="width: 90%;">
				<tr>
					<td align="right" width="100px"><strong>产品SKU：</strong></td>
					<td><form:input path="productSku" htmlEscape="false" maxlength="32" class="input-xlarge "/>
					<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right" width="100px"><strong>产品名称：</strong></td>
					<td><form:input path="productName" htmlEscape="false" maxlength="500" class="input-xlarge "/>
					 <span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right"><strong>产品类型：</strong></td>
					<td>
						<form:select path="type" class="input-xlarge ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('product_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
				</tr>
				<tr>
					<td align="right" width="100px"><strong>市场价：</strong></td>
					<td><form:input path="marketPrice" htmlEscape="false" maxlength="11" class="input-xlarge " />
						<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right"><strong>条码：</strong></td>
					<td><form:input path="barCode" htmlEscape="false" maxlength="32" class="input-xlarge "/>
					</td>
				</tr>
				<tr>
					<td align="right"><strong>箱规：</strong></td>
					<td><form:input path="carton" htmlEscape="false" maxlength="11" class="input-xlarge "/>
						</td>
				</tr>
				<tr>
					<td><label class="control-label"><strong>产品图片：</strong></label>
					</td>
					<td colspan="3">		
						<sys:ckfinder input="picUrl" type="images" uploadPath="/photo" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden path="picUrl" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" />
					</td>
				</tr>
			</table>	
		<div class="form-actions">
			<shiro:hasPermission name="single:mallProductInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>