<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推券活动授权管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/localization/messages_zh.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			document.onkeydown=function(evt){
				if(evt.keyCode ==13){
				return false;
				}
			}
			//$("#name").focus();
			$("#inputForm").validate({
				rules : {
					activityCode : {
						required : true
					}
				},messages : {
					activityCode : {
						required : "请选择活动"
					}
				},
				submitHandler: function(form){
					var isOk = getItemData();
					if(!isOk){
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
		<li><a href="${ctx}/activityauth/mallPromotionActivityAuth/">推券活动授权列表</a></li>
		<li class="active"><a href="${ctx}/activityauth/mallPromotionActivityAuth/form?id=${mallPromotionActivityAuth.id}">推券活动授权<shiro:hasPermission name="activityauth:mallPromotionActivityAuth:edit">${not empty mallPromotionActivityAuth.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="activityauth:mallPromotionActivityAuth:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityAuth" action="${ctx}/activityauth/mallPromotionActivityAuth/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="activityData"  name="activityData"/>
		<input type="hidden" id="angelItemData"  name="angelItemData"/>
		<sys:message content="${message}"/>	
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td><strong>操作人编号</strong></td>
					<td><form:input path="createCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人</strong></td>
					<td><form:input path="createName" class="input-medium" readonly="true"/></td>
					<td><strong>操作时间</strong></td>
					<td><input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityAuth.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
				</tr>
			</table>
		</fieldset>	
		<br><br>
		<fieldset title="推券活动选择">
			<legend>推券活动选择</legend>
			<br>
			<table>
				<tr>
					<td width='300px;' align='center' style="display:none"> <form:input path="activityId"  readonly="true"/></td>
					<td width='100px;' align='left'><strong>活动编码</strong><span class="help-inline"><font color="red">*</font></span>
					<form:input path="activityCode"  onclick='getActivityItem(this)' readonly="true" style="background-Color:white ;"/></td>
					<td width='300px;' align='center'><strong>活动名称</strong>&nbsp;<input id="activityName" type="text" readonly></td>
					<td width='300px;' align='center'><strong>活动简称</strong>&nbsp;<input id="activitySName" type="text" readonly></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='300px;' align='left'><strong>开始时间</strong><span class="help-inline"><font color="white">*</font></span>
					<input id="startTime" name="startTime" type="text" readonly></td>
					<td width='300px;' align='center'><strong>结束时间</strong>&nbsp;<input id="endTime" name="endTime" type="text" readonly></td>
				</tr>
			</table>
		</fieldset>
		<br><br>
		<fieldset title="授权天使账号">
			<legend>授权天使账号 &nbsp;<a class="btn btn-primary" href="javascript:addItem()"><i
								class="icon-file"></i>新增</a></legend>
			<table style="width: 95%;">
				<tr id="partItem">
					<td width="50%">
						<table id="angelPart" border="1" style="width: 600px;border-color: #0099FF;" >
							<tr>
								<td width="100px;" align="center"><strong>序号</strong></td>
								<td width="200px;" align="center"><strong>月亮天使账号</strong><span class="help-inline"><font color="red">*</font></span></td>
								<td width="300px;" align="center"><strong>天使姓名</strong></td>
								<td width="300px;" align="center"><strong>联系方式</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
						</table>
						</td>
						</tr>
						</table>
		</fieldset>
		<%-- <div class="control-group">
			<label class="control-label">主键：</label>
			<div class="controls">
				<form:input path="authId" htmlEscape="false" maxlength="32" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动ID：</label>
			<div class="controls">
				<form:input path="activityId" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否有效：</label>
			<div class="controls">
				<form:input path="status" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">创建人编号：</label>
			<div class="controls">
				<form:input path="creatCode" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">创建人：</label>
			<div class="controls">
				<form:input path="creatBy" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">创建时间：</label>
			<div class="controls">
				<input name="creatTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityAuth.creatTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">修改人编号：</label>
			<div class="controls">
				<form:input path="updateCode" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">修改时间：</label>
			<div class="controls">
				<input name="updateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityAuth.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div> --%>
		<div class="form-actions">
			<shiro:hasPermission name="activityauth:mallPromotionActivityAuth:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
<script type="text/javascript">
	function getActivityItem(){
		var url = '${ctx}/activityauth/mallPromotionActivityAuth/getActivityItem';
		pageii = layer.open({
			title : "推券活动选择",
			type : 2,
			area : [ "70%", "50%" ],
			content : url,
			end : function() {
				var activityData = $("#activityData").val();
				if(activityData == ""){
					return false;
				}
				addActivity(activityData);
			}
		});
	}
	
	function addActivity(json){
		var data = JSON.parse(json);
		$("#activityId").val(data.activityId);
		$("#activityCode").val(data.activityCode);
		$("#activityName").val(data.activityName);
		$("#activitySName").val(data.activitySName);
		$("#startTime").val(data.startTime);
		$("#endTime").val(data.endTime);
	}
	
	function addItem(){
		var tableId = "angelPart";
		var partItemIndex = $("#"+tableId+" tr").length;
		var newStr = "<tr>";
		newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
		newStr += "<td width='100px;' align='left'><input type='text' class='input-small' ";
		newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' ";
		newStr += "onfocus='clearInput(this)' onblur='getItemInfo(this)'/></td>";
		newStr += "<td width='300px;' align='center'></td>";
		newStr += "<td width='300px;' align='center'></td>";
		newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
		newStr += "</tr>";
		addTr("angelPart",newStr);
	}
	function addTr(tab, trHtml){
	     //获取table最后一行 $("#tab tr:last")
	     //获取table第一行 $("#tab tr").eq(0)
	     //获取table倒数第二行 $("#tab tr").eq(-2)
	     var $tr=$("#"+tab+" tr:last");
	     if($tr.size()==0){
	        alert("指定的table id不存在！");
	        return;
	     }
	     $tr.after(trHtml);
	}
	
	function getItemData() {
		var tableId = "angelPart";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		if(trEle.length < 2){
			layer.alert("请输入授权天使账号");
			return false;
		}
		var j = 0;
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			item.angelId = tdEle[1].firstChild.value;
			item.angelName = tdEle[2].innerHTML;
			item.angelMobile = tdEle[3].innerHTML;
			if(item.angelId.length > 16){
				layer.alert("月亮天使账号超长，请重新输入");
				return false;
			}
			if(item.angelName == '' || item.angelMobile == ''){
				layer.alert("月亮天使账号出错，请重新输入");
				return false;
			}
			if(item.angelId==""||item.angelId==null){
				j++;
			}else{
				items[i-j-1] = item;
			}
		}
		if(items.length > 1){
			for(var i=0;i<items.length;i++){
				for(var j=i+1;j<items.length;j++){
					if(items[i].angelId == items[j].angelId){
						layer.alert("天使账号不能重复添加");
						return false;
					}
				}
			}
		}
		var json = JSON.stringify(items);
		$("#angelItemData").val(json);
		return true;
	}
	
	function deleteItem(obj){
		var objIndex = obj.parentNode.parentNode.firstChild.innerHTML;
		var nowTR = obj.parentNode.parentNode;
		var trs = nowTR.nextSibling;
		while(trs.firstChild!=null){
			trs.firstChild.innerHTML = objIndex;
			objIndex ++;
			trs = trs.nextSibling;
		}
		var navigatorName = "Microsoft Internet Explorer"; 
		if(navigator.appName == navigatorName){    
			obj.parentNode.parentNode.removeNode(true);
		}else{    
			obj.parentNode.parentNode.remove();
		}
	}
	
	function getItemInfo(obj){
		var angelCode = obj.value;
		var tx = obj.parentNode;
		tx.nextSibling.innerHTML = "";
		tx.nextSibling.nextSibling.innerHTML = "";
		if(angelCode==null||angelCode==""){
			layer.alert("请输入月亮天使账号");
			return false;
		}else{
			if (!(/(^[1-9]\d*$)/.test(angelCode))){
				layer.alert("月亮天使账号只能输入数字");
				return false;
			}
			var url = '${ctx}/activityauth/mallPromotionActivityAuth/getAngelByCode';
	        $.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					angelCode : angelCode
				}, 
				error: function(XMLHttpRequest,textStatus,errorThrown) {
	                   alert(XMLHttpRequest.status);
	                   alert(XMLHttpRequest.readyState);  
	                   alert(textStatus);  
	                   $("#showLoading").show();
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
						var realName="";
						var mobileNo="";
						var item = JSON.parse(json);
						realName = item.realName;
						mobileNo = item.mobileNo;
					}else{
						layer.alert("找不到相关账号信息");
						return false;
					} 
					 $td.nextSibling.innerHTML = realName;
					 $td.nextSibling.nextSibling.innerHTML = mobileNo;
				}
			});
		}
	}
</script>
</body>
</html>