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
			var url ;
			var li;
			url = '${mallRecommendInfo.pictureFirst}';
			if(url!=null&&url!=""){
				$("#pictureFirstPreview").children().remove();
				li = "<li><a href=\""+url+"\" target=\"view_window\"><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
				li += "</a></li>";
				$("#pictureFirstPreview").append(li);
			}
			
			url = '${mallRecommendInfo.pictureSecond}';
			if(url!=null&&url!=""){
				$("#pictureSecondPreview").children().remove();
				li = "<li><a href=\""+url+"\" target=\"view_window\"><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
				li += "</a></li>";
				$("#pictureSecondPreview").append(li);
			}
			geta();
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var status = $("#aduitStatus").val();
					if(status == null || status == '' || status == '0'){
						layer.alert("请选择审核结果");
						return false;
					}
					var reson = $("#aduitReason").val();
					if('2' == status){
						if(reson == null || reson == ''){
							layer.alert("审核不通过请注明原因");
							return false;
						}
					}
					if(reson.length>100){
						layer.alert("注明原因字数太长");
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
		<li class="active"><a href="${ctx}/recommendinfo/mallRecommendInfo/form?id=${mallRecommendInfo.id}">推荐码<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit">审核</shiro:hasPermission><shiro:lacksPermission name="recommendinfo:mallRecommendInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallRecommendInfo" action="${ctx}/recommendinfo/mallRecommendInfo/checkUpdate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="aduitStatus"/>
		<form:hidden path="updatBy" class="input-medium" readonly="true"/>
		<input name="updateTime" type="hidden" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallRecommendInfo.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
		<sys:message content="${message}"/>		
		<fieldset title="审核推荐码">
			<legend>审核推荐码</legend>
			<table style="width: 95%;">
				<tr>
					<td  width='100px;' align='center'><strong>审核人</strong>&nbsp;&nbsp;&nbsp;<form:input path="aduitBy" class="input-medium" readonly="true"/></td>
					<td width='100px;' align='center'><strong>操作时间</strong>&nbsp;&nbsp;&nbsp;<input name="aduitTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallRecommendInfo.aduitTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
				 <td width='200px;'  align='center'>&nbsp;&nbsp;&nbsp;订单编号&nbsp;
					<form:input path="orderCode" htmlEscape="false" maxlength="32" onblur="getOrder(this)" readonly="true"/></td>
				 <td width='300px;' align='center'>订单付款时间&nbsp;
				  <input name="orderPayTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallRecommendInfo.orderPayTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
				 </td>
				 <td width='150px;' align='center' colspan="2">订单金额&nbsp;<form:input path="orderMoney" htmlEscape="false" readonly="true"/></td>
				</tr>
				<tr><td width='300px;' 	><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='300px;' align='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注册人&nbsp;
					<form:input path="registrant" htmlEscape="false" maxlength="32"  readonly="true"/></td>
					<td width='400px;'align='center' >注册人手机号&nbsp;
						<form:input path="registrantMobile" htmlEscape="false" maxlength="11"  readonly="true"/></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='300px;' align='center' >当前推荐码&nbsp;
					<form:input path="oldRecommend" htmlEscape="false" maxlength="32" readonly="true" value="${mallRecommendInfo.oldRecommend}"/></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='400px' align="center">&nbsp;<span style="color:blue">修改后推荐码</span>&nbsp;&nbsp;<form:input path="newRecommend" htmlEscape="false" maxlength="11" readonly="true"/><span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td  width='100px;' align='left' valign="top" height="200px">
						<span style="color:blue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上传凭证1</span>&nbsp;&nbsp;&nbsp;&nbsp;请上传订单详情截屏<span class="help-inline"><font color="red">*</font></span>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sys:ckfinder input="pictureFirst" type="images" uploadPath="/recommendPhoto" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden path="pictureFirst" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" /></td>
				
					<td  width='100px;' align='left' valign="top" height="200px">
						<span style="color:blue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上传凭证2</span>&nbsp;&nbsp;&nbsp;&nbsp;请上传注册人个人资料页截屏<span class="help-inline"><font color="red">*</font></span>
					<sys:ckfinder input="pictureSecond" type="images" uploadPath="/recommendPhoto" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden path="pictureSecond" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" /></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td  width='100px;' align='left'><strong>审核结果</strong>&nbsp;&nbsp;&nbsp;<input type="radio" value="1" name="status" onclick="pass('1')">通过</input>&nbsp;&nbsp;&nbsp;<input type="radio" value="2"  name="status" onclick="pass('2')">不通过</input></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td  width='100px;' align='left'><strong>原因说明</strong>&nbsp;&nbsp;&nbsp;<form:input path="aduitReason" htmlEscape="false" maxlength="100" placeholder="若不通过请注明原因"/></td>
				</tr>
			</table>
		</fieldset>	
		<div class="form-actions">
			<shiro:hasPermission name="recommendinfo:mallRecommendInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="确认"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
<script type="text/javascript">
function getOrder(obj){
	var orderCode = obj.value;
	$("#orderMoney").val("");
	if(orderCode == null || orderCode == ''){
		layer.alert("订单编号不能为空");
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
				layer.alert("找不到相关订单信息");
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

function pass(status){
	 $("#aduitStatus").val(status);
}

function geta(){
	var a = document.getElementsByTagName("a");
	for(var i=0;i<a.length;i++){
		var c = a[i].innerHTML;
		if(c == '选择' ||  c== '清除'){
			a[i].style.display='none';
		}
	}
}

</script>
</body>
</html>