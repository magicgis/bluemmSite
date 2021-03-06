<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>推荐码申请审核</title>
<meta name="decorator" content="default"/>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
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
	function showWarning(obj){
		$("#warningImg").toggle();
	}
	function hideWarning(){
		$("#warningImg").toggle();
	}
	function changeRangeType(e){
		var scope = e.value;
		if(scope=='part'){
			$("#partItem").show();
		}else if(scope=='wholeshop'){
			$("#partItem").hide();
		}
	}
	function changeTheme(e){
		var theme = e.value;
		if(theme=='amount_over'){
			$("#amount_over").show();
			$("#count_over").hide();
		}else if(theme=='count_over'){
			$("#amount_over").hide();
			$("#count_over").show();
		}
	}
	function closePage(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引 
		parent.layer.close(index);
	}
	
	function clearInput(obj){
		if(obj.value=="请输入商品SKU"||obj.value=="请输入产品SKU"){
			obj.value = "";
		}
	}
	function closePage(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引 
		parent.layer.close(index);
	}
	
	function getActivity(){
		var tableId = 'activitysPart';
		var activity = new Object();
		var isChecked = false;
		var trEle = $("#"+tableId+"").find("tr");
		if(trEle.length <= 1){
			layer.alert("不能提交，请返回。");
			return false;
		}
		for(var i=1;i<trEle.length;i++){
			var tdEle = trEle[i].cells;
			var acti = tdEle[0].firstChild;
			if(acti.checked){
				activity.activityId = tdEle[2].innerHTML;
				activity.activityCode = tdEle[3].innerHTML;
				activity.activityName = tdEle[4].innerHTML;
				activity.activitySName = tdEle[5].innerHTML;
				activity.startTime = tdEle[6].innerHTML;
				activity.endTime = tdEle[7].innerHTML;
				isChecked = true;
			}
		}
		if(!isChecked){
			layer.alert("请选择活动。");
			return false;
		}
		parent.$("#activityData").val("");
		var json = JSON.stringify(activity);
		parent.$("#activityData").val(json)
		closePage();
	}
</script>
<style type="text/css">
.line {
	width: 100%;
	height: 2px;
	margin: 10px 0;
	font-size: 0;
	overflow: hidden;
	background-color: transparent;
	border-width: 0;
	border-top: 1px solid #e8e8e8;
}
input.error { border: 1px solid red; }
label.error {
	position : absolute;
	background-color: white;
    padding-left: 16px;
    padding-bottom: 2px;
    font-weight: bold;
    color: #EA5200;
}
label.checked {
    position : absolute;
	background-color: white;
    padding-left: 16px;
    padding-bottom: 2px;
    font-weight: bold;
    color: #EA5200;
}
input.noborder{
	color:gray;
	height:18px;
	border-left:0px;
	border-top:0px;
	border-right:0px;
	border-bottom:0px;
}
</style>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityMjsLevel" action="#" method="post" class="form-horizontal">
		<sys:message content="${message}"/>		
		<fieldset title="推荐码申请审核">
			<table style="width: 100%;">
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<!-- <tr>
					<td width="100px;"><strong>推券活动选择</strong></td>
					<td><input id="btnCancel" class="btn" type="button" value="返 回" onclick="closePage();"/></td>
					</tr>
				<tr> -->
					<td colspan="4">
						<div class="line"></div>
					</td>
				</tr>
				<tr id="couponsItem">
					<td colspan="8">
						<table id="activitysPart" border="1" style="width: 100%;" class='table table-striped table-bordered table-condensed'>
							<tr>
								<td width="200px;" align="center"><strong>序号</strong></td>
								<td width="200px;" align="center"><strong>修改人</strong></td>
								<td width="300px;" align="center"><strong>修改时间</strong></td>
								<td width="300px;" align="center"><strong>修改后推荐码</strong></td>
								<td width="300px;" align="center"><strong>凭证</strong></td>
								<td width="300px;" align="center"><strong>审核结果</strong></td>
								<td width="300px;" align="center"><strong>原因说明</strong></td>
								<td width="300px;" align="center"><strong>审核人</strong></td>
								<td width="300px;" align="center"><strong>审核时间</strong></td>
							</tr>
							<c:forEach items="${listOper}" var="oper" varStatus="status">
							<tr>
									<td align="center">${oper.index}</td>
									<td align="center">${oper.updatBy}</td>
									<td align="center"><fmt:formatDate value="${oper.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td align="center">${oper.newRecommend }</td>
									<td align="center"><a href="${oper.pictureFirst }"target="view_window">凭证1</a>&nbsp;<a href="${oper.pictureSecond }"target="view_window">凭证2</a></td>
									<td align="center">${fns:getDictLabel(oper.aduitStatus, 'recommend_status', '')}</td>
									<td align="center">${oper.aduitReason }</td>
									<td align="center">${oper.aduitBy}</td>
									<td align="center"><fmt:formatDate value="${oper.aduitTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
	</form:form>
</body>
</html>