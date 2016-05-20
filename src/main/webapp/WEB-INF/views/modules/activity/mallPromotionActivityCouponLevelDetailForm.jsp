<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>mall_promotion_管理</title>
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
		<li><a href="${ctx}/activity/mallPromotionActivityCouponLevelDetail/">mall_promotion_列表</a></li>
		<li class="active"><a href="${ctx}/activity/mallPromotionActivityCouponLevelDetail/form?id=${mallPromotionActivityCouponLevelDetail.id}">mall_promotion_<shiro:hasPermission name="activity:mallPromotionActivityCouponLevelDetail:edit">${not empty mallPromotionActivityCouponLevelDetail.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="activity:mallPromotionActivityCouponLevelDetail:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityCouponLevelDetail" action="${ctx}/activity/mallPromotionActivityCouponLevelDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">id：</label>
			<div class="controls">
				<form:input path="couponLevelDetailId" htmlEscape="false" maxlength="20" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">coupon_level_id：</label>
			<div class="controls">
				<form:input path="couponLevelId" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">优惠券：</label>
			<div class="controls">
				<form:input path="couponId" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单次发放数量：</label>
			<div class="controls">
				<form:input path="sendNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="activity:mallPromotionActivityCouponLevelDetail:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>