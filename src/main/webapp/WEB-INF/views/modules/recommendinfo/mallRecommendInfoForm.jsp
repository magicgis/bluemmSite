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
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var isOk = checkValidate();
					if(isOk != ''){
						layer.alert(isOk);
						return false;
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
		<li><a href="${ctx}/recommendinfo/mallRecommendInfo/">推荐码列表</a></li>
		<li class="active"><a href="${ctx}/recommendinfo/mallRecommendInfo/form?id=${mallRecommendInfo.id}">推荐码<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit">${not empty mallRecommendInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="recommendinfo:mallRecommendInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallRecommendInfo" action="${ctx}/recommendinfo/mallRecommendInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<fieldset title="新增推荐码">
			<legend>新增推荐码</legend>
			<table style="width: 95%;">
				<tr>
					<td  width='100px;' align='center'><strong>操作人</strong>&nbsp;&nbsp;&nbsp;<form:input path="updatBy" class="input-medium" readonly="true"/></td>
					<td width='100px;' align='center'><strong>操作时间</strong>&nbsp;&nbsp;&nbsp;<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallRecommendInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
				 <td width='200px;'  align='center'>&nbsp;&nbsp;&nbsp;订单编号&nbsp;
					<form:input path="orderCode" htmlEscape="false" maxlength="32" onblur="getOrder(this)"/></td>
				 <td width='300px;' align='center'>订单付款时间&nbsp;
				 <form:input path="orderPayTime" htmlEscape="false"  readonly="true"/>
				 </td>
				 <td width='150px;' align='center' colspan="2">订单金额&nbsp;<form:input path="orderMoney" htmlEscape="false" readonly="true"/></td>
				</tr>
				<tr><td width='300px;' align='center'><span class="help-inline" style="color:red;display:none" id="already">该订单推荐码不可修改</span>&nbsp;</tr>
				<tr>
					<td width='300px;' align='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注册人&nbsp;
					<form:input path="registrant" htmlEscape="false" maxlength="32"  readonly="true"/></td>
					<td width='400px;'align='center' >注册人手机号&nbsp;
						<form:input path="registrantMobile" htmlEscape="false" maxlength="11"  readonly="true"/></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='300px;' align='center' >当前推荐码&nbsp;
					<form:input path="oldRecommend" htmlEscape="false" maxlength="32" readonly="true" value=""/></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='400px' align="center">&nbsp;<span style="color:blue">修改推荐码</span>&nbsp;&nbsp;<form:input path="newRecommend" htmlEscape="false" maxlength="11" /><span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td  width='100px;' align='left'>
						<span style="color:blue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上传凭证1</span>&nbsp;&nbsp;&nbsp;&nbsp;请上传订单详情截屏<span class="help-inline"><font color="red">*</font></span>
					</td>
				</tr>
				<tr>
					<td align='center'><sys:ckfinder input="pictureFirst" type="images" uploadPath="/recommendPhoto" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden path="pictureFirst" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" /></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td  width='100px;' align='left'>
						<span style="color:blue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上传凭证2</span>&nbsp;&nbsp;&nbsp;&nbsp;请上传注册人个人资料页截屏<span class="help-inline"><font color="red">*</font></span>
					</td>
				</tr>
				<tr>
					<td align='center'><sys:ckfinder input="pictureSecond" type="images" uploadPath="/recommendPhoto" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden path="pictureSecond" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" /></td>
				</tr>
			</table>
		</fieldset>	
		<div class="form-actions">
			<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="提交审核"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
<script type="text/javascript">
function getOrder(obj){
	var orderCode = obj.value;
	$("#orderMoney").val("");
	$("#registrant").val('');
	$("#orderPayTime").val('');
	$("#registrantMobile").val('');
	$("#oldRecommend").val('');
	document.getElementById("already").style.display="none";
	if(orderCode == null || orderCode == ''){
		layer.alert("订单编号不能为空");
		return false;
	}
	if(orderCode.length<15){
		layer.alert("请输入完整订单编号");
		return false;
	}
	if(orderCode.indexOf('S') != -1){
		layer.alert("请输入父单编号");
		return false;
	}
	var url = '${ctx}/recommendinfo/mallRecommendInfo/getOrder';
	$.ajax({
		type : "POST",
		async : true,
		url : url,
		data : {
			orderCode : orderCode
		},
		success : function(data){
			var $td = obj.parentNode;
			 if(data!=null&&data!=""){
				 
				var flag = data.flag;
				var json = data.json;
				if(flag=="already"){
					document.getElementById("already").style.display="block";
					return false;
				}
				if(flag=="error"){
					layer.alert(json);
					return false;
				}
				var payTime="";
				var mobileNo="";
				var nickName="";
				var recommendCode = "";
				var payTotal = 0;
				var item = JSON.parse(json);
				payTime = item.payTime;
				mobileNo = item.userMobile;
				nickName = item.nickName;
				recommendCode = item.recommendCode;
				payTotal = item.payTotal;
			}else{
				layer.alert("找不到相关订单信息或者订单未付款");
				return false;
			}
			 $("#orderPayTime").val(payTime);
			 $("#orderMoney").val(payTotal/100);
			 $("#registrant").val(nickName);
			 $("#registrantMobile").val(mobileNo);
			 $("#oldRecommend").val(recommendCode);
		}
	});
}

function checkValidate(){
	var registrant = $("#registrant").val();
	var orderPayTime = $("#orderPayTime").val();
	var orderMoney = $("#orderMoney").val();
	var registrantMobile= $("#registrantMobile").val();
	var oldRecommend = $("#oldRecommend").val();
	var newRecommend = $("#newRecommend").val();
	var p1 = $("#pictureFirst").val();
	var p2 = $("#pictureSecond").val();
	
	if(registrant == '' || orderPayTime == ''){
		return "请输入正确订单编号";
	}
	if(newRecommend == ''){
		return "请输入修改推荐码";
	}
	var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
	if (newRecommend.length != 11 || !(/(^[1-9]\d*$)/.test(newRecommend)) || !reg.test(newRecommend)){
		return "推荐码只能输入11位手机号码";
	}
	if(p1 == ''){
		return "请上传凭证1";
	}
	if(p2 == ''){
		return "请上传凭证2";
	}
	if(p1.indexOf('.bmp') != -1 || p1.indexOf('.BMP') != -1){
		return "上传凭证1，禁止上传bmp格式图片";
	}
	if(p2.indexOf('.bmp') != -1 || p2.indexOf('.BMP') != -1){
		return "上传凭证2，禁止上传bmp格式图片";
	}
	return '';
}

</script>
</body>
</html>