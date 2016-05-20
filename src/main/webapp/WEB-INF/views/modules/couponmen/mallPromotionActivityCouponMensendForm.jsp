<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>手动推券管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
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
</style>
<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				debugger;
				var activityCode = $("#activityCode").val();
				if(activityCode==""||activityCode==null){
					layer.alert("请选择发券活动！");
					return;
				}
				getCouponData();
				var coupondata = $("#couponData").val();
				if(coupondata==""||coupondata==null){
					return;
				}
				getConsumerData();
				var consumerdata = $("#consumerData").val();
				if(consumerdata==""||consumerdata==null){
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
	function chooseActivity(){
		var url = '${ctx}/couponmen/mallPromotionActivityCouponMensend/getSelfActivity';
		pageii = layer.open({
			title : "推券活动选择",
			type : 2,
			area : [ "900px", "400px" ],
			btn: ['确定', '取消'],
			content : url,
			yes: function(index){
                layer.close(index);
				var activityId = $("#activityId").val();
				if(activityId!=null&&activityId!=""){
					var url = '${ctx}/couponmen/mallPromotionActivityCouponMensend/getCouponList';
			        $.ajax({
						type : "POST",
						async : true,
						url : url,
						data : {
							activityId : activityId
						}, 
						success : function(data){
							debugger;
							if(data!=null&&data!=""){
								var activityJson = data.activity;
								var activity = JSON.parse(activityJson);
								$('#activityCode').val(activity.activityCode);
								$('#activityName').val(activity.activityName);
								$('#activitySName').val(activity.activitySName);
								$('#st').val(activity.startTime);
								$('#et').val(activity.endTime);
								var tbody = $("#couponBody")[0];
								var trs = $("#couponBody tr");
								if(trs.length>0){
									for(var i=0;i<trs.length;i++){
										tbody.removeChild(trs[i]);
									}
								}
								var coupons = data.json;
								var str = "";
								for(var i=0;i<coupons.length;i++){
									str += "<tr align='center'>";
									str += "<td><input type='checkbox' name='coupon' value='"+coupons[i].couponCode+"' onchange='changeCouponNum()'></td>";
									str += "<td>"+(i+1)+"</td>";
									str += "<td>"+coupons[i].couponName+"</td>";
									str += "<td>"+coupons[i].couponSName+"</td>";
									str += "</tr>";
								}
								$("#couponBody").append(str);
							}else{
								layer.alert("数据出错，请确认活动数据是否正确!");
							}
						}
					});
				}
			},
			no: function(index){
				
			}
		});
	}
	function changeCouponNum(){
		var checkNode = $("input[name='coupon']:checked");
		$("#couponNum")[0].innerHTML = checkNode.length;
	}
	function addConsumer(){
		var trs = $("#consumerBody tr");
		var str = "";
		str += "<tr align='center'>";
		str += "<td>"+(trs.length+1)+"</td>";
		str += "<td><input type='text' class='input-small' ";
		str += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' ";
		str += "onblur='checkMobile(this)'/></td>";
		str += "<td><a href='javascript:' onclick='javascript:delConsumer(this)'>删除</a></td>";
		str += "</tr>";
		$("#consumerBody").append(str);
	}
	function delConsumer(obj){
		debugger;
		var navigatorName = "Microsoft Internet Explorer"; 
		if(navigator.appName == navigatorName){    
			obj.parentNode.parentNode.removeNode(true);
		}else{    
			obj.parentNode.parentNode.remove();
		}
		var trs = $("#consumerBody tr");
		for(var i=0;i<trs.length;i++){
			trs[i].cells[0].innerHTML = i+1;
		}
	}
	function checkMobile(obj){
		var mobileStr = obj.value;
		var reg = /^((13[\d])|(14[5,7,9])|(15[^4,\D])|(17[^2,4,9,\D])|(18[\d]))\d{8}$/;
		if(!reg.test(mobileStr)){
			obj.value = "";
			layer.alert("手机号码格式有误");
		}
	}
	function getCouponData(){
		var checkNode = $("input[name='coupon']:checked");
		if(checkNode.length>0){
			var coupons = new Array();
			for(var i=0;i<checkNode.length;i++){
				var coupon = new Object();
				var $tr = checkNode[i].parentNode.parentNode;
				var $tds = $tr.cells;
				coupon.couponCode = checkNode[i].value;
				coupon.couponSName = $tds[3].innerHTML;
				coupons[i] = coupon;
			}
			var json = JSON.stringify(coupons);
			$("#couponData").val(json);
		}else{
			layer.alert("请添加需要推送的券！");
		}
	}
	function getConsumerData(){
		var $trs = $("#consumerBody tr");
		if($trs.length>0){
			var mobileStr = "";
			for(var i=0;i<$trs.length;i++){
				var $tds = $trs[i].cells;
				var mobile = $tds[1].firstChild.value;
				if(mobile!=null&&mobile!=""){
					mobileStr += mobile+";";
				}
			}
			if(mobileStr.length==0){
				layer.alert("请至少添加一个需要推送的人员手机号！");
				return;
			}
			$("#consumerData").val(mobileStr);
		}else{
			layer.alert("请添加需要推送的人员手机号！");
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/couponmen/mallPromotionActivityCouponMensend/">手动推券列表</a></li>
		<li class="active"><a href="${ctx}/couponmen/mallPromotionActivityCouponMensend/form?id=${mallPromotionActivityCouponMensend.id}">手动推券<shiro:hasPermission name="couponmen:mallPromotionActivityCouponMensend:edit">${not empty mallPromotionActivityCouponMensend.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="couponmen:mallPromotionActivityCouponMensend:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityCouponMensend" action="${ctx}/couponmen/mallPromotionActivityCouponMensend/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="activityId" name="activityId">
		<input type="hidden" id="couponData" name="couponData">
		<input type="hidden" id="consumerData" name="consumerData">
		<sys:message content="${message}"/>		
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="100px;"><strong>操作人编号</strong></td>
					<td width="25%"><form:input path="creatCode" class="input-medium" readonly="true" value=""/></td>
					<td width="100px;"><strong>操作人</strong></td>
					<td><form:input path="creatBy" class="input-medium" readonly="true"/></td>
					<td><strong>操作时间</strong></td>
					<td><input name="creatTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityCouponMensend.creatTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
				</tr>
			</table>
		</fieldset>
		<p></p>
		<fieldset title="发券活动选择">
			<legend>发券活动选择</legend>
			<table style="width: 95%;">
				<tr>
					<td width="100px;"><strong>活动编码</strong></td>
					<td  width="25%">
						<div class="input-group">
					      <input type="text" id="activityCode" name="activityCode" class="input-medium" readonly="readonly">
					      <span class="input-group-btn">
					        <button class="btn btn-default" type="button" onclick="javascript:chooseActivity();">选择</button>
					      </span>
					    </div>
					</td>
					<td colspan="4"></td>
				</tr>
				<tr>
					<td colspan="6">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td width="100px;"><strong>开始时间</strong></td>
					<td><input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityCouponMensend.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
					<td width="100px;"><strong>结束时间</strong></td>
					<td><input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityCouponMensend.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td colspan="6">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>活动简称</strong></td>
					<td><form:input path="activityName" class="input-medium" readonly="true" value=""/></td>
					<td><strong>活动名称</strong></td>
					<td colspan="3"><form:input path="activitySName" class="input-xxlarge" readonly="true" value=""/></td>
				</tr>
			</table>
		</fieldset>
		<p></p>
		<fieldset title="选择券">
			<legend>选择券</legend>
			<strong>推券数量：<label id="couponNum"></label></strong>
			<table style="width: 60%;border-color: grey;" border="1" >
				<thead>
					<tr>
						<th width="10%">选择</th>
						<th width="10%">序号</th>
						<th width="50%">券名称</th>
						<th width="30%">券简称</th>
					</tr>
				</thead>
				<tbody id="couponBody">
					
				</tbody>
			</table>
		</fieldset>
		<fieldset title="消费者手机">
			<legend>消费者手机</legend>
			<a class="btn btn-primary" href="javascript:addConsumer()"><i class="icon-file"></i>新增</a>
			<table style="width: 30%;" border="1">
				<thead>
					<tr align="center">
						<th width="20%"><strong>序号</strong></th>
						<th width="60%"><strong>消费者手机<font color="red">*</font></strong></th>
						<th width="20%"><strong>操作</strong></th>
					</tr>
				</thead>
				<tbody id="consumerBody">
					
				</tbody>
			</table>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="couponmen:mallPromotionActivityCouponMensend:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="javascript:reback()"/>
		</div>
		<script type="text/javascript">
			function reback(){
				location.href='${ctx}/couponmen/mallPromotionActivityCouponMensend/';
			}
		</script>
	</form:form>
</body>
</html>