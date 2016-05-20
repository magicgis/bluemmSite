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
	var loaddata = true;
		$(document).ready(function() {
			var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
	        var nScrollTop = 0;   //滚动到的当前位置
	     	var nDivHight = $("#scroll_head").height();
	       	$("#scroll_head").scroll(function(){
				nScrollHight = $(this)[0].scrollHeight;
				nScrollTop = $(this)[0].scrollTop;
				if(nScrollTop + nDivHight >= nScrollHight && loaddata){
					loaddata = false;
					getMoreData();
				}
	        });
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					getItemData();
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
		<li class="active"><a href="${ctx}/activityauth/mallPromotionActivityAuth/form?id=${mallPromotionActivityAuth.id}">推券活动授权<shiro:hasPermission name="activityauth:mallPromotionActivityAuth:edit">查看</shiro:hasPermission><shiro:lacksPermission name="activityauth:mallPromotionActivityAuth:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityAuth" action="${ctx}/activityauth/mallPromotionActivityAuth/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="activityData"  name="activityData"/>
		<input type="hidden" id="angelItemData"  name="angelItemData"/>
		<input type="hidden" id="authId"  name="authId" value='${mallPromotionActivityAuth.authId}'/>
		<input type="hidden" id="pageNo" name="pageNo"
			value='${pageNo }'>
		<sys:message content="${message}"/>	
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td><strong>操作人编号</strong></td>
					<td><form:input path="updateCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人</strong></td>
					<td><form:input path="updatBy" class="input-medium" readonly="true"/></td>
					<td><strong>操作时间</strong></td>
					<td><input name="updateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityAuth.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
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
					<form:input path="activityCode"  readonly="true"/></td>
					<td width='300px;' align='center'><strong>活动名称</strong>&nbsp;<input id="activityName" value="${mallPromotionActivityAuth.activityName}" type="text" readonly></td>
					<td width='300px;' align='center'><strong>活动简称</strong>&nbsp;<input id="activitySName" value="${mallPromotionActivityAuth.activitySName}" type="text" readonly></td>
				</tr>
				<tr><td width='300px;' align='left'><span class="help-inline">&nbsp;</span></tr>
				<tr>
					<td width='300px;' align='left'><strong>开始时间</strong>&nbsp;<span class="help-inline"><font color="white">*</font></span>
					<input id="startTime" value="<fmt:formatDate value='${mallPromotionActivityAuth.startTime}' pattern='yyyy-MM-dd HH:mm:ss' />" type="text" readonly></td>
					<td width='300px;' align='center'><strong>结束时间</strong>&nbsp;<input id="endTime" value="<fmt:formatDate value='${mallPromotionActivityAuth.endTime}' pattern='yyyy-MM-dd HH:mm:ss' />" type="text" readonly></td>
				</tr>
			</table>
		</fieldset>
		<br><br>
		<fieldset title="授权天使账号">
			<legend>授权天使账号</legend>
			<table style="width: 95%;">
				<tr id="partItem">
					<td width="50%">
					<div id="scroll_head" class="tableContainer" style="width: 100%; height: 350px; overflow: scroll;">
						<table id="angelPart" border="1" style="width: 600px;border-color: #0099FF;" >
						<thead class="fixedHeader">
						<tr>
						<th><strong>序号</strong></th>
						<th><strong>月亮天使账号</strong><span class="help-inline"><font color="red">*</font></span></th>
						<th><strong>天使姓名</strong></th>
						<th><strong>联系方式</strong></th>
						<th><strong>操作</strong></th>
						</tr>
						</thead>
						<tbody class="scrollContent">
						<c:forEach items="${mallPromotionActivityAuth.angelUserList}" var="mallPromotionActivityAuthAngel" varStatus="status">
							<tr>
								<td align="center">
									${status.index+1 }
								</td>
								<td align="left">${mallPromotionActivityAuthAngel.angelId}</td>
								<td align="center">${mallPromotionActivityAuthAngel.angelName}</td>
								<td align="center">${mallPromotionActivityAuthAngel.angelMobile}</td>
								<td width='100px;' align='center'><a href='javascript:'>删除</a></td>
							</tr>
						</c:forEach>
						</tbody>
						</table>
						</div>
						</td>
						</tr>
						</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
<script type="text/javascript">
	function getActivityItem(){
		var url = '${ctx}/activityauth/mallPromotionActivityAuth/getActivityItem';
		pageii = layer.open({
			title : "推荐活动选择",
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
		newStr += "<td width='100px;' align='center'><a href='javascript:'>删除</a></td>";
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
		var j = 0;
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			item.angelId = tdEle[1].firstChild.value;
			item.angelName = tdEle[2].innerHTML;
			item.angelMobile = tdEle[3].innerHTML;
			if(item.angelId==""||item.angelId==null){
				j++;
			}else{
				items[i-j-1] = item;
			}
		}
		var json = JSON.stringify(items);
		$("#angelItemData").val(json);
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
	
function getMoreData(){
		var pageNo = $("#pageNo").val();
		var authId = $("#authId").val();
		var url = "${ctx}/activityauth/mallPromotionActivityAuth/getMoreData";
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				pageNo : pageNo,
				authId : authId
			}, 
			success : function(data){
				if(data!=null&&data!=""){
					var flag = data.flag;
					if(flag=="false"){
						layer.alert("数据已经全部展示");
						return;
					}else if(flag=="success"){
						var pageNo = data.pageNo;
						$("#pageNo").val(pageNo);
						var details = data.json;
						if(details.length>0){
							var index = $("#angelPart tbody tr").length;
							var str = "";
							
							for(var i=0;i<details.length;i++){
								str += "<tr>";
								str += "<td align='center'>"+(index+1)+"</td>";
								str += "<td align='left'>"+details[i].angelId+"</td>";
								str += "<td align='center'>"+details[i].angelName+"</td>";
								str += "<td align='center'>"+ details[i].angelMobile+"</td>";
								str += "<td align='center'><a href='javascript:''>删除</a></td>";
								str += "</tr>";
								index++;
							}
							addTr("angelPart",str);
							loaddata = true;
						}else{
							layer.alert("数据已经全部展示！");
						}
					}
				}else{
					layer.alert("数据出错，请系统联系管理员!");
				}
			}
		});
	}
	
	function getItemInfo(obj){
		var angelCode = obj.value;
		if(angelCode==null||angelCode==""){
			layer.alert("请输入月亮天使账号");
		}else{
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
							$td.nextSibling.innerHTML = "";
							$td.nextSibling.nextSibling.innerHTML = "";
							return;
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