<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品活动管理</title>
<meta name="decorator" content="default"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/spinner/jquery.spinner.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/spinner/jquery.spinner.js"></script>

<script type="text/javascript">
var isOk = true;
	$(document).ready(function() {
		$("#activityItemValues").hide();
		$(".decrease").attr("disabled", true); 
		$(".increase").attr("disabled", true); 
		//$("#name").focus();
		$("#inputForms").validate({
			rules : {
				activityTheme : {
					required : true
				},
				activityName : {
					required : true,
					maxlength : 20
				},
				activitySName : {
					required : true,
					maxlength : 8
				},
				startTime : {
					required : true
				},
				endTime : {
					required : true
				},
				activityDesc : {
					required : true
				}
			},
			messages : {
				activityTheme : {
					required : "请选择活动大类"
				},
				activityName : {
					required : "请填写活动名称",
					maxlength : "活动名称最多20个字"
				},
				activitySName : {
					required : "请填写活动简称",
					maxlength : "活动简称最多8个字"
				},
				startTime : {
					required : "请填写活动开始时间"
				},
				endTime : {
					required : "请填写活动结束时间"
				},
				activityDesc : {
					required : "请填写活动说明"
				}
			},
			submitHandler: function(form){
				//时间校验
				var startTime = $("#startTime")[0].value;
				var endTime = $("#endTime")[0].value;
				var abvalue = $("#abvalue").val();
				var activityItemType = $("#activityItemType").val();
				var restrictionType = $("#restrictionType").val();
				var activityItemValue = $("#activityItemValues").val();
				var activityDesc = $("#activityDesc").val();
				var restrictionValue = $("#spv1").val();
				var date1 = new Date(Date.parse(startTime));
	    		var date2 = new Date(Date.parse(endTime));
	    		if (date1.getTime() > date2.getTime()) {
	    			layer.alert("结束时间不得小于开始时间。");
	    			return;
	    		}
	    		//活动范围检验
				getItemData();
				var dot = activityItemValue.indexOf(".");
	            if(dot != -1){
	            	var dotCnt = activityItemValue.substring(dot+1,activityItemValue.length);
	            	 if(activityItemType == "limited_price"){
	            		 if(dotCnt.length > 2){
	 	                    alert("金额小数位已超过2位！");
	 	                    return false;
	 	                }
	 	            }else if(activityItemType == "lmited_discount"){
	 	            	if(dotCnt.length > 1){
	 	                    alert("折扣力度 ，小数位已超过1位！");
	 	                    return false;
	 	                }
	 	            }
	            }
	            if(activityItemType == "lmited_discount"){
 	            	activityItemValue = parseFloat(activityItemValue);
	 	   			if(activityItemValue>=10||activityItemValue<5){
	 	   				layer.alert("折扣区间为5~10。<br><font color='red'>如98折请输入9.8</font>");
	 	   				return false;
	 	   			}
	            }
	            if(activityDesc.length > 256){
	            	layer.alert("活动说明超过长度，请重新输入");
	            	return false;
	            }
	    		var rangeType = $("input[name='rangeType']:checked").val();
				if(rangeType=="part"){
					var itemJson = $("#itemData").val();
					if(itemJson=="[]"){
						layer.alert("请添加活动范围！");
		    			return;
					} 	
				}
				if (activityItemType == '') {
	    			layer.alert("请选择活动方式");
	    			return;
	    		}
				if (!(/^\d+(\.\d+)?$/.test(activityItemValue))){
					layer.alert("活动方式条件只能输入数字而且不能是负数");
					return false;
				}
				if (restrictionType == '') {
	    			layer.alert("请选择限购类型");
	    			return;
	    		}
				if (activityItemValue == '') {
	    			layer.alert("请输入活动方式条件");
	    			return;
	    		}
				if(restrictionType != 'not_restriction'){
					if(!(/(^[1-9]\d*$)/.test(restrictionValue)) ){
						layer.alert("限购条件只能输入正整数而且不能为0");
						return false;
					}
				}
				if(abvalue != '1'){
					return;
				}
				if(!isOk){
					return false;
				}
				loading('正在提交，请稍等...');
				form.submit();
				isOk = false;
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
		
		var activityId = '${mallPromotionActivityInfo.activityId}';
		var listRange = '${mallPromotionActivityInfo.rangeJson}';
		var listLevel = '${mallPromotionActivityInfo.levelJson}';
		var condition = '${mallPromotionActivityInfo.condition}';
		var rangeType = '${mallPromotionActivityInfo.rangeType}';
		
		if(activityId!=null&&activityId!=""){
			$("#addDiv").hide();
			$("#queryDiv").show();
		}
		if(rangeType=='part'){
			$("#partItem").show();
		}else if(rangeType=='wholeshop'){
			$("#partItem").hide();
		}
		if(condition=='amount_over'){
			$("#amount_over").show();
			$("#count_over").hide();
		}else if(condition=='count_over'){
			$("#amount_over").hide();
			$("#count_over").show();
		}
		if(listRange!=null&&listRange!=""){
			setItemData(listRange); 
		}
		if(listLevel!=null&&listLevel!=""){
			setLevelData(listLevel,condition+"Table"); 
		}
	});
// 	function test(){
// 		var condition = $("#condition").val();
// 		getLevelData(condition+"Table");
// 	}
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
			var startTime = $("#startTime")[0].value;
			var endTime = $("#endTime")[0].value;
			if(startTime==null||startTime==""){
				layer.alert("请填写开始时间");
				$("input[name='rangeType']").eq(0).attr("checked","checked");
	            $("input[name='rangeType']").eq(1).removeAttr("checked");
	            $("input[name='rangeType']").eq(0).click();
				return;
			}
			if(endTime==null||endTime==""){
				$("input[name='rangeType']").eq(0).attr("checked","checked");
	            $("input[name='rangeType']").eq(1).removeAttr("checked");
	            $("input[name='rangeType']").eq(0).click();
				layer.alert("请填写结束时间");
				return;
			}
			var date1 = new Date(Date.parse(startTime));
			var date2 = new Date(Date.parse(endTime));
			if (date1.getTime() > date2.getTime()) {
				$("input[name='rangeType']").eq(0).attr("checked","checked");
	            $("input[name='rangeType']").eq(1).removeAttr("checked");
	            $("input[name='rangeType']").eq(0).click();
				layer.alert("结束时间不得小于开始时间。");
				return;
			}
			var url = '${ctx}/activityItem/mallPromotionActivityInfo/checkSholeshop';
	        $.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					startTime : date1,
					endTime : date2
				}, 
				success : function(data){
					if(data!=null&&data!=""){
						var flag = data.flag;
						if(flag=="error"){
							$("input[name='rangeType']").eq(0).attr("checked","checked");
				            $("input[name='rangeType']").eq(1).removeAttr("checked");
				            $("input[name='rangeType']").eq(0).click();
							layer.alert("已有活动适用全部商品,活动范围不可重复！");
							return;
						}else if(flag=="success"){
							$("#partItem").hide();
						}
					}else{
						$("input[name='rangeType']").eq(0).attr("checked","checked");
			            $("input[name='rangeType']").eq(1).removeAttr("checked");
			            $("input[name='rangeType']").eq(0).click();
						layer.alert("数据出错，请系统联系管理员!");
					}
					$td.nextSibling.innerHTML = itemName;
				}
			});
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
	
	function setItemData(data){
		var itemData = JSON.parse(data);
		var newStr = "";
		for(var i = 0; i < itemData.length; i++){
			var item = itemData[i];
			newStr += "<tr>";
			newStr += "<td width='100px;' align='center'>" + (i+1) + "</td>";
			newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='"+item.itemSku+"' "
			newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' /></td>";
			newStr += "<td width='300px;' align='center'>"+item.itemName+"</td>";
			newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='return false'>删除</a></td>";
			newStr += "</tr>";
		}
		addTr("rangeTypePart",newStr);
	}
	function addItem(){
		var tableId = "rangeTypePart";
		var partItemIndex = $("#"+tableId+" tr").length;
		var newStr = "<tr>";
		newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
		newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='请输入商品SKU' ";
		newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' ";
		newStr += "onfocus='clearInput(this)' onblur='getItemInfo(this)'/></td>";
		newStr += "<td width='300px;' align='center'></td>";
		newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
		newStr += "</tr>";
		addTr("rangeTypePart",newStr);
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
	function getItemInfo(obj){
		if(checkTable()){
			layer.alert("已含有重复商品!");	
			return;
		}
		
		var startTime = $("#startTime")[0].value;
		var endTime = $("#endTime")[0].value;
		if(startTime==null||startTime==""){
			layer.alert("请填写开始时间");
			return;
		}
		if(endTime==null||endTime==""){
			layer.alert("请填写结束时间");
			return;
		}
		var date1 = new Date(Date.parse(startTime));
		var date2 = new Date(Date.parse(endTime));
		if (date1.getTime() > date2.getTime()) {
			layer.alert("结束时间不得小于开始时间。");
			return;
		}
		var itemSku = obj.value;
		if(itemSku==null||itemSku==""){
			obj.value = "请输入商品SKU";
		}else{
			var url = '${ctx}/activityItem/mallPromotionActivityInfo/getItemBySku';
	        $.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					itemSku : itemSku,
					startTime : startTime,
					endTime : endTime
				}, 
				success : function(data){
					
					var $td = obj.parentNode;
					if(data!=null&&data!=""){
						var flag = data.flag;
						var json = data.json;
						if(flag=="error"){
							layer.alert(json);
							$td.nextSibling.innerHTML = "";
							return;
						}
						var itemName="";
						var item = JSON.parse(json);
						itemName = item.itemName;
					}else{
						layer.alert("查找不到商品/产品,请确认SKU是否正确!");
						$td.nextSibling.innerHTML = "";
						return;
					}
					$td.nextSibling.innerHTML = itemName;
				}
			});
		}
	}
	function clearInput(obj){
		if(obj.value=="请输入商品SKU"||obj.value=="请输入产品SKU"){
			obj.value = "";
		}
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
	
	function getItemData() {
		var tableId = "rangeTypePart";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		var j = 0;
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			item.itemSku = tdEle[1].firstChild.value;
			item.itemName = tdEle[2].innerHTML;
			if(item.itemName==""||item.itemName==null){
				j++;
			}else{
				items[i-j-1] = item;
			}
		}
		var json = JSON.stringify(items);
		$("#itemData").val(json);
	}
	 
	String.prototype.startWith=function(str){ 
	var reg=new RegExp("^"+str); 
	return reg.test(this); 
	} 
	//测试ok，直接使用str.endWith("abc")方式调用即可 
	String.prototype.endWith=function(str){ 
	var reg=new RegExp(str+"$"); 
	return reg.test(this); 
	} 
	function setLevel(levelJson,tableId){
		var data = JSON.parse(levelJson);
		var conditionLabel = $("#condition option:selected")[0].label;
		var partItemIndex = $("#"+tableId+" tr").length;
		var newStr = "<tr>";
		newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
		newStr += "<td width='100px;' align='center'>"+(data.conditionValueDouble+conditionLabel)+"</td>";
		if(data.preferentialType=="less_money"){
			newStr += "<td width='200px;' align='center'>"+(data.preferentialTypeLabel+","+data.preferentialValueDouble+"元")+"</td>";
		}else if(data.preferentialType=="discount"){
			newStr += "<td width='200px;' align='center'>"+(data.preferentialTypeLabel+","+data.preferentialValueDouble+"折")+"</td>";
		}else if(data.preferentialType=="gift"){
			newStr += "<td width='200px;' align='center'>"+(data.preferentialTypeLabel+","+data.giftName+"*"+data.giftNum)+"</td>";
		}else{
			newStr += "<td width='200px;' align='center'></td>";
		}
		newStr += "<td width='100px;' align='center'>"+data.conditionValueDouble+"~"+"</td>";
		newStr += "<td width='100px;' align='center'><input type='hidden' value='"+levelJson+"'><a href='javascript:' onclick='deleteLevel(this)'>删除</a></td>";
		newStr += "</tr>";
		if(partItemIndex!=1){
			var lastTr = $("#"+tableId+" tr:last")[0];
			var tdEle = lastTr.cells;
			var str = tdEle[3].innerHTML;
			if(str.endWith("~")){
				tdEle[3].innerHTML = str+(data.conditionValueDouble-0.01).toFixed(2);
			}
		}
		addTr(tableId,newStr);
	}
	function deleteLevel(obj){
		
		var objIndex = obj.parentNode.parentNode.firstChild.innerHTML;
		var nowTR = obj.parentNode.parentNode;
		var nextTR = nowTR.nextSibling;
		if(objIndex!=1){
			var prevTR = nowTR.previousSibling;
			if(nextTR.firstChild!=null){
				var atr = prevTR.childNodes[1].innerHTML;
				atr = atr.substr(0,atr.length-1);
				var btr = nextTR.childNodes[1].innerHTML;
				btr = btr.substr(0,btr.length-1) -0.01;
				btr = btr.toFixed(2);
				prevTR.childNodes[3].innerHTML = atr + "~" + (btr);
			}else{
				prevTR.childNodes[3].innerHTML = prevTR.childNodes[1].innerHTML + "~";
			}
		}
		while(nextTR.firstChild!=null){
			nextTR.firstChild.innerHTML = objIndex;
			objIndex ++;
			nextTR = nextTR.nextSibling;
		}
		obj.parentNode.parentNode.remove();
	}
	function getLevelData(tableId){
		
		var levels = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		for(var i=1;i<trEle.length;i++){
			var tdEle = trEle[i].cells;
			var level = tdEle[4].firstChild.value;
			var data = JSON.parse(level);
			levels[i-1] = data;
		}
		var json = JSON.stringify(levels);
		$("#levelData").val(json);
	}
	function setLevelData(data,tableId){
		
		var conditionLabel = $("#condition option:selected")[0].label;
		var levelData = JSON.parse(data);
		for(var i = 0; i < levelData.length; i++){
			var data = levelData[i];
			var newStr = "<tr>";
			newStr += "<td width='100px;' align='center'>" + (i+1) + "</td>";
			newStr += "<td width='100px;' align='center'>"+(data.conditionValueDouble+conditionLabel)+"</td>";
			if(data.preferentialType=="less_money"){
				newStr += "<td width='200px;' align='center'>"+("减钱,"+data.preferentialValueDouble+"元")+"</td>";
			}else if(data.preferentialType=="discount"){
				newStr += "<td width='200px;' align='center'>"+("打折,"+(parseInt(data.preferentialValueDouble)/10)+"折")+"</td>";
			}else if(data.preferentialType=="gift"){
				newStr += "<td width='200px;' align='center'>"+("送赠品,"+data.giftName+"*"+data.giftNum)+"</td>";
			}else{
				newStr += "<td width='200px;' align='center'></td>";
			}
			if(i==levelData.length-1){
				newStr += "<td width='100px;' align='center'>"+data.conditionValueDouble+"~"+"</td>";
			}else{
				newStr += "<td width='100px;' align='center'>"+data.conditionValueDouble+"~"+(levelData[i+1].conditionValueDouble-0.01).toFixed(2)+"</td>";
			}
			newStr += "<td width='100px;' align='center'><input type='hidden' value='"+data+"'><a href='javascript:'>删除</a></td>";
			newStr += "</tr>";
			addTr(tableId,newStr);
		}
		
	}
	function checkTable(){
		var tableId = "rangeTypePart";
		var arr = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var sku = tdEle[1].firstChild.value;
			var num = arr.indexOf(sku);
			if(arr.length>0&&num>=0){
				return true;
			}
			if(sku!="请输入商品SKU"&&sku!="请输入产品SKU"){
				arr[i-1] = sku;
			}
		}
		return false;
	}
	function removeItem(){
		
		getItemData();
		var rangeType = $("input[name='rangeType']:checked").val();
		if(rangeType=="part"){
			var itemJson = $("#itemData").val();
			if(itemJson=="[]"){
    			return;
			}else{
				var tableId = "rangeTypePart";
				var items = new Array();
				var trEle = $("#"+tableId+"").find("tr");
				for(var i=1;i<trEle.length;i++){
					var navigatorName = "Microsoft Internet Explorer"; 
					if(navigator.appName == navigatorName){    
						trEle[i].removeNode(true);
					}else{    
						trEle[i].remove();
					}
				}
			}
		}
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
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/activityItem/mallPromotionActivityInfo/">商品活动列表</a></li>
		<li class="active"><a href="${ctx}/activityItem/mallPromotionActivityInfo/form?id=${mallPromotionActivityInfo.id}">商品活动<shiro:hasPermission name="activityItem:mallPromotionActivityInfo:edit">${not empty mallPromotionActivityInfo.id?'查看':'添加'}</shiro:hasPermission><shiro:lacksPermission name="activityItem:mallPromotionActivityInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForms" modelAttribute="mallPromotionActivityInfo" action="${ctx}/activityItem/mallPromotionActivityInfo/save" method="post" class="form-horizontal">
		<input type="hidden" id="activityItemType"  name="activityItemType"/>
		<input type="hidden" id="restrictionType"  name="restrictionType"/>
		<input type="hidden" id="restrictionValue"  name="restrictionValue"/>
		<input type="hidden" id="abvalue"  name="abvalue"/>
		<input type="hidden" id="itemData"  name="itemData"/>
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td><strong>活动编码</strong></td>
					<td><form:input path="activityCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人编号</strong></td>
					<td><form:input path="createCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人</strong></td>
					<td><form:input path="createName" class="input-medium" readonly="true"/></td>
					<td><strong>操作时间</strong></td>
					<td><input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionActivityInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>活动大类</strong></td>
					<td><form:select path="activityTheme" onchange="changecouponType(this);" style="width:177px;">
							<form:option value="" label="--请选择--"></form:option>
							<form:options items="${fns:getDictList('activity_category')}"
									itemLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td><strong>活动简称</strong></td>
					<td><form:input path="activitySName" class="input-medium" placeholder='最多8个字'/>
					<span class="help-inline"><font color="red">*</font></span></td>
					<td name="fixedTime" class="fixedTime"><strong>开始时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="startTime" name="startTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionActivityInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" onchange="removeItem()"/>
						<span class="help-inline"><font color="red">*</font></span></td>
					<td name="fixedTime" class="fixedTime"><strong>结束时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="endTime" name="endTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionActivityInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" onchange="removeItem()"/>
						<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>活动名称</strong></td>
					<td colspan="3"><form:input path="activityName" class="input-xxlarge" placeholder='最多20个字'/>
					<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>活动说明</strong></td>
					<td colspan="7">
						<form:textarea path="activityDesc" cols="1000" rows="3" class="input-xxlarge"/>
						<span class="help-inline"><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>活动范围</strong><span class="help-inline"><font color="red">*</font></span></td>
					<td colspan="7">
						<form:radiobuttons path="rangeType" items="${fns:getDictList('coupon_range_type')}" itemLabel="label" itemValue="value" htmlEscape="false"
							onchange="changeRangeType(this);" lang="part"/>
							<img src="${pageContext.request.contextPath}/static/images/warning.png" onmouseover="showWarning(this);" onmouseout="hideWarning();">
							<div id="warningImg" style="position: absolute;display: none;left: 250px;background-color: white;">
								<ul style="color: red;list-style-type: none;">
									<li>部分参与：</li>
									<li>&nbsp;&nbsp;需要手动选择参与商品</li>
									<li>全店适用：</li>
									<li>&nbsp;&nbsp;当前有效商品都参与该活动</li>
								</ul>
							</div>
					</td>
				</tr>
			</table>
			<table style="width: 95%;">
				<tr id="partItem">
					<td width="50%">
						<a class="btn btn-primary" href="javascript:addItem()"><i
								class="icon-file"></i>新增</a>
						<font color="red" title="修改活动时间后会清空活动范围">请先填写活动时间后再填写活动范围</font>
						<table id="rangeTypePart" border="1" style="width: 600px;border-color: #0099FF;" >
							<tr>
								<td width="100px;" align="center"><strong>序号</strong></td>
								<td width="200px;" align="center"><strong>商品SKU</strong></td>
								<td width="300px;" align="center"><strong>商品名称</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
						</table>
					</td>
					<td align="left">
						<div style="color: red;">选择商品限制：<br>
							1、当前有效的商品；<br>
							2、当前未参加同级别优惠的商品。
						</div>
					</td>
				</tr>
			</table>
		</fieldset>
		<br>
		<fieldset title="活动逻辑">
			<legend>活动逻辑</legend>
			<table style="width: 95%">
				<tr>
					<td width="10%"><strong>活动方式</strong><span class="help-inline"><font color="red">*</font></span></td>
					<td align="left" width="10%">
						<input type="radio" name="aType"  value="limited_price" onclick="changItemType(0)"/><strong>限时降价</strong>
					</td>
					<td align="left" width="20%">
						<input type="radio" name="aType"  value="lmited_discount" onclick="changItemType(1)"/><strong>限时折扣</strong>
					</td>
					<td align="left" id="iType">
						<span><strong  id="iValue">&nbsp;</strong></span><span class="help-inline"><font color="white" id="fcol">*</font></span>
						<input id="activityItemValues" name="activityItemValues"/>
					</td>
				</tr>
				<tr>
					<td width="10%"><strong>限购</strong><span class="help-inline"><font color="red">*</font></span></td>
					<td align="left" width="10%"><input type="radio" name="rType" value="not_restriction" onclick="changRType(0)"/><strong>不限购</strong></td>
					<td align="left" width="20%"><input type="radio" name="rType" value="promotion_restriction" onclick="changRType(1)"/><strong>优惠限购</strong>&nbsp;<input type="text" class="spinner" readonly="true" onchange="checkValue(1)" id="spv1"/>
					<!-- <td align="left" width="20%"><input type="radio" name="rType" value="number_restriction" onclick="changRType(2)"/><strong>数量限购</strong>&nbsp;<input type="text" class="spinner" readonly="true" onchange="checkValue(2)"/> -->
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions" id="addDiv">
			<shiro:hasPermission name="activityItem:mallPromotionActivityInfo:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="vale()"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="reback()"/>
		</div>
		<div class="form-actions" id="queryDiv" style="display: none;">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="reback()"/>
		</div>
		<script type="text/javascript">
			function reback(){
				location.href='${ctx}/activityItem/mallPromotionActivityInfo/';
			}
		</script>
	</form:form>

<script type="text/javascript">
	function checkValue(type){
		var rval = "";
		if(type == 1){
			rval = $(".spinner").eq(0).val();
		}else if(type == 2){
			rval = $(".spinner").eq(1).val();
		}
		$("#restrictionValue").val(rval);
	}
	function changItemType(type){
		if(type == 0){
			document.getElementById("iValue").innerHTML = "降价金额（元）";
		}else if(type == 1){
			document.getElementById("iValue").innerHTML = "折扣力度（折）";
		}
		var val=$('input:radio[name="aType"]:checked').val();
		$("#activityItemType").val(val);
		$("#activityItemValues").show();
		$("#fcol").css("color","red");
	}
	function changRType(type){
		$(".decrease").attr("disabled", true); 
		$(".increase").attr("disabled", true); 
		$(".spinner").attr("readonly",true);
		var rval = "";
		if(type == 0){
			$("#restrictionValue").val();
		}else if(type == 1){
			$(".increase").eq(0).attr("disabled", false);
			$(".decrease").eq(0).attr("disabled", false); 
			$(".spinner").eq(0).attr("readonly",false);
			rval = $(".spinner").eq(0).val();
		}else if(type == 2){
			$(".increase").eq(1).attr("disabled", false);
			$(".decrease").eq(1).attr("disabled", false); 
			$(".spinner").eq(1).attr("readonly",false);
			rval = $(".spinner").eq(1).val();
		}
		var val=$('input:radio[name="rType"]:checked').val();
		$("#restrictionType").val(val);
		$("#restrictionValue").val(rval);
	}
	function vale(){
		$("#abvalue").val("1");
	}

	$( ".spinner" ).spinner();
	
	
</script>
</body>
</html>
