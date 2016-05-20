<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			/* $("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
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
			}); */
		});

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallProductInfo" action="" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">产品Sku:</label>
			<div class="controls">
				<input id="productSku" name="productSku" type="text" value="${mallProductInfo.productSku}" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<input id="productId" name="productId" type="hidden" value="${mallProductInfo.productId}">
		<div class="control-group">
			<label class="control-label">产品名称:</label>
			<div class="controls">
				<input id="productName" name="productName" type="text" value="${mallProductInfo.productName}" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">条码:</label>
			<div class="controls">
				<input id="barCode" name="barCode" type="text" value="${mallProductInfo.barCode}" maxlength="50" minlength="3" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">箱规:</label>
			<div class="controls">
				<input id="carton" name="carton" type="text" value="${mallProductInfo.carton}" maxlength="50" minlength="3" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">市场价:</label>
			<div class="controls">
				<input id="marketPrice" name="marketPrice" type="text" value="${mallProductInfo.marketPrice}" maxlength="50" minlength="3" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="save()"/>
			<input id="btnCancel" class="btn btn-primary" type="button" value="取消" onclick="cancel()"/>
		</div>
	</form:form>
	<script type="text/javascript">
		function cancel(){
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}
		function save(){
			$.ajax({
                cache: true,
                type: "POST",
                url:'${ctx}/product/saveProduct',
                data:$('#inputForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("保存失败");
                },
                success: function(data) {
                	cancel();
                }
            });
		}
		
	</script>
</body>
</html>