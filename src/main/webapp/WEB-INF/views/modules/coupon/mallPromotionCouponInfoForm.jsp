<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>券主数据管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript"
src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.metadata.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
		$("#inputForm").validate({
			rules : {
				couponSName : {
					required : true,
					maxlength : 8
				},
				couponName : {
					required : true,
					maxlength : 20
				},
				direction : {
					required : true,
					maxlength : 500
				}
			},
			messages : {
				couponSName : {
					required : "请输入券简称",
					maxlength : "券简称最多8个文字"
				},
				couponName : {
					required : "请输入券名称",
					maxlength : "券名称最多20个文字"
				},
				direction : {
					required : "请输入权益说明",
					maxlength : "最多不超过500个字符"
				}
			},
			submitHandler: function(form){
				debugger;
				var couponType = $("#couponType option:selected")[0].value;
				var giftNum = document.getElementById('rule.giftNum');
				if(couponType=="online_gift"){
					var $priority = $("input[name='yxj']");
					var str = "";
					var prioritys = new Array();
					for(var i=0;i<$priority.length;i++){
						var temp = $priority[i].value;
						if(prioritys.in_array(temp)){
							layer.alert("优先级不能重复");
							return;
						}else{
							prioritys[i] = temp;
						}
					}
				}
				if(couponType=="online_gift"&&giftNum.value<=0){
					layer.alert("赠品数量必须是正整数");
					return;
				}
				var timeType = $("#timeType option:selected")[0].value;
				if(timeType=="fixedTime"){
					var startTime = $("#startTime")[0].value;
					if(startTime==null||startTime==""){
						layer.alert("请填写开始时间");
		    			return;
					}
					var endTime = $("#endTime")[0].value;
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
				}else if(timeType=="validTime"){
					var validDays = $("#validDays").val();
					if(validDays<=0){
						layer.alert("有效天数必须大于0");
						return ;
					}
				}
				
				getItemData(couponType);
				var json = $("#itemData").val();
				var rangeType = $("input[name='rule.rangeType']:checked").val();
				if(json=="[]"&&rangeType=="part"){
					layer.alert("请完善活动券使用逻辑!");
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
		debugger;
		var couponId = '${mallPromotionCouponInfo.couponId}';
		if(couponId!=null&&couponId!=""){
			$("#addDiv").hide();
			$("#queryDiv").show();
		}
		var couponType = '${mallPromotionCouponInfo.couponType}';
		var list = '${mallPromotionCouponInfo.listRangeJson}';
		var timeType = '${mallPromotionCouponInfo.timeType}';
		if(list!=null&&list!=""){
			setItemData(list,couponType); 
		}
		showcouponType(couponType);
		showTimeType(timeType);
	});
	Array.prototype.in_array = function(e){
		for(i=0;i<this.length;i++){
			if(this[i] == e)
			return true;
		}
		return false;
	}
	function setItemData(data,couponType){
		debugger;
		var itemData = JSON.parse(data);
		var newStr = "";
		if(couponType=="online_discount"){
			for(var i = 0; i < itemData.length; i++){
				var item = itemData[i];
				newStr += "<tr>";
				newStr += "<td width='100px;' align='center'>" + (i+1) + "</td>";
				newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='"+item.sku+"' "
				newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' /></td>";
				newStr += "<td width='300px;' align='center'>"+item.name+"</td>";
				newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='return false'>删除</a></td>";
				newStr += "</tr>";
			}
		}else if(couponType=="online_gift"){
			for(var i = 0; i < itemData.length; i++){
				var item = itemData[i];
				newStr += "<tr>";
				newStr += "<td width='100px;' align='center'>" + (i+1) + "</td>";
				newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='"+item.sku+"' "
				newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' /></td>";
				newStr += "<td width='300px;' align='center'>"+item.name+"</td>";
				newStr += "<td width='100px;' align='left'><input  type='text' class='input-small' value='"+item.priority+"'";
				newStr += " style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px'/></td>";
				newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='return false'>删除</a></td>";
				newStr += "</tr>";
			}
		}else if(couponType=="offline_gift"){
			for(var i = 0; i < itemData.length; i++){
				var item = itemData[i];
				newStr += "<tr>";
				newStr += "<td width='100px;' align='center'>" + (i+1) + "</td>";
				newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='"+item.sku+"' "
				newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' /></td>";
				newStr += "<td width='300px;' align='center'>"+item.name+"</td>";
				newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='"+item.num+"'";
				newStr += " style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px'/></td>";
				newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='return false'>删除</a></td>";
				newStr += "</tr>";
			}
		}
		addTr(couponType+"Table",newStr);
	}
	function changeTimeType(e){
		var timeType = e.value;
		if(timeType!='fixedTime'){
			$("#startTime")[0].value = "";
			$("#endTime")[0].value = "";
		}
		showTimeType(timeType);
	}
	
	function showTimeType(timeType){
		debugger;
		if(timeType=='fixedTime'){
			//固定使用时间
			$(".fixedTime").show();
			$(".validTime").hide();
			$(".activityTime").hide();
		}else if(timeType=='validTime'){
			//领取后有效时间
			$(".fixedTime").hide();
			$(".validTime").show();
			$(".activityTime").hide();
		}else if(timeType=='activityTime'){
			//关联活动时间
			$(".fixedTime").hide();
			$(".validTime").hide();
			$(".activityTime").show();
		}else{
			//无
			$(".fixedTime").hide();
			$(".validTime").hide();
			$(".activityTime").hide();
		}
	}
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
	function changecouponType(e){
		var couponType = e.value;
		showcouponType(couponType);
	}
	
	function showcouponType(couponType){
		if(couponType=='online_discount'){
			//固定使用时间
			$("#online_discount").show();
			$("#online_gift").hide();
			$("#offline_gift").hide();
		}else if(couponType=='online_gift'){
			//领取后有效时间
			$("#online_discount").hide();
			$("#online_gift").show();
			$("#offline_gift").hide();
		}else if(couponType=='offline_gift'){
			//关联活动时间
			$("#online_discount").hide();
			$("#online_gift").hide();
			$("#offline_gift").show();
		}else{
			//无
			$("#online_discount").hide();
			$("#online_gift").hide();
			$("#offline_gift").hide();
		}
	}
	
	function addItem(){
		var couponType = $("#couponType option:selected")[0].value;
		var tableId = couponType+"Table";
		var partItemIndex = $("#"+tableId+" tr").length;
		
		var newStr = "<tr>";
		newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
		newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='请输入商品SKU' ";
		newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' ";
		newStr += "onfocus='clearInput(this)' onblur='getItemInfo(this)'/></td>";
		newStr += "<td width='300px;' align='center'></td>";
		
		if(couponType=="online_discount"){
			newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
			newStr += "</tr>";
			addTr("online_discountTable",newStr);
		}else if(couponType=="online_gift"){
			newStr += "<td width='100px;' align='left'><input name='yxj' type='text' class='input-small digits required'";
			newStr += " style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px'/></td>";
			newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
			newStr += "</tr>";
			addTr("online_giftTable",newStr);
		}else if(couponType=="offline_gift"){
			newStr = "<tr>";
			newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
			newStr += "<td width='100px;' align='left'><input type='text' class='input-small' value='请输入产品SKU' ";
			newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' ";
			newStr += "onfocus='clearInput(this)' onblur='getProductInfo(this)'/></td>";
			newStr += "<td width='300px;' align='center'></td>";
			newStr += "<td width='100px;' align='left'><input type='text' class='input-small digits required' ";
			newStr += " style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px'/></td>";
			newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
			newStr += "</tr>";
			addTr("offline_giftTable",newStr);
		}
	}
	
	function deleteItem(obj){
		debugger;
		var objIndex = obj.parentNode.parentNode.firstChild.innerHTML;
		var nowTR = obj.parentNode.parentNode;
		var trs = nowTR.nextSibling;
		while(trs!=null&&trs.firstChild!=null){
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
	
	function clearInput(obj){
		if(obj.value=="请输入商品SKU"||obj.value=="请输入产品SKU"){
			obj.value = "";
		}
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
		var itemSku = obj.value;
		if(itemSku==null||itemSku==""){
			obj.value = "请输入商品SKU";
		}else{
			var url = '${ctx}/coupon/mallPromotionCouponInfo/getItemBySku';
	        $.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					itemSku : itemSku
				}, 
				success : function(data){
					var itemName="";
					var $td = obj.parentNode;
					if(data!=null&&data!=""){
						var item = JSON.parse(data);
						itemName = item.itemName;
					}else{
						layer.alert("查找不到商品/产品,请确认SKU是否正确!");
					}
					$td.nextSibling.innerHTML = itemName;
				}
			});
		}
	}
	
	function getProductInfo(obj){
		if(checkTable()){
			layer.alert("已含有重复商品!");	
			return;
		}
		var itemSku = obj.value;
		if(itemSku==null||itemSku==""){
			obj.value = "请输入产品SKU";
		}else{
			var url = '${ctx}/coupon/mallPromotionCouponInfo/getProductBySku';
	        $.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					itemSku : itemSku
				}, 
				success : function(data){
					var itemName="";
					var $td = obj.parentNode;
					if(data!=null&&data!=""){
						var item = JSON.parse(data);
						itemName = item.goodName;
					}else{
						layer.alert("找不到商品/产品！");
					}
					$td.nextSibling.innerHTML = itemName;
					$td.nextSibling.nextSibling.firstChild.value = 1;
				}
			});
		}
	}
	
	function checkTable(){
		debugger;
		var couponType = $("#couponType option:selected")[0].value;
		var tableId = couponType+"Table";
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

	function getItemData(couponType) {
		debugger;
		var tableId = couponType+"Table";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		var j = 0;
		if(couponType=="online_discount"){
			for (var i = 1; i < trEle.length; i++) {
				var tdEle = trEle[i].cells;
				var item = new Object();
				item.sku = tdEle[1].firstChild.value;
				item.name = tdEle[2].innerHTML;
				if(item.name==null||item.name==""){
					j++;
				}else{
					items[i-j-1] = item;
				}
			}
		}else if(couponType=="online_gift"){
			for (var i = 1; i < trEle.length; i++) {
				var tdEle = trEle[i].cells;
				var item = new Object();
				item.sku = tdEle[1].firstChild.value;
				item.name = tdEle[2].innerHTML;
				item.priority = parseInt(tdEle[3].firstChild.value);
				if(item.name==null||item.name==""){
					j++;
				}else{
					items[i-j-1] = item;
				}
			}
		}else if(couponType=="offline_gift"){
			for (var i = 1; i < trEle.length; i++) {
				debugger;
				var tdEle = trEle[i].cells;
				var item = new Object();
				item.sku = tdEle[1].firstChild.value;
				item.name = tdEle[2].innerHTML;
				if(tdEle[3].firstChild.value==""){
					item.num = 1;
				}else{
					item.num = parseInt(tdEle[3].firstChild.value);
				}
				if(item.name==null||item.name==""){
					j++;
				}else{
					items[i-j-1] = item;
				}
			}
		}
		var json = JSON.stringify(items);
		$("#itemData").val(json);
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
		<li><a href="${ctx}/coupon/mallPromotionCouponInfo/">活动券列表</a></li>
		<li class="active">
		<a href="${ctx}/coupon/mallPromotionCouponInfo/form?id=${mallPromotionCouponInfo.couponId}">
		<shiro:hasPermission name="coupon:mallPromotionCouponInfo:edit">${not empty mallPromotionCouponInfo.couponId?'活动券查看':'活动券添加'}</shiro:hasPermission>
		</a></li>
	</ul>
	<br/>
	<form:form id="inputForm" modelAttribute="mallPromotionCouponInfo" action="${ctx}/coupon/mallPromotionCouponInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="itemData" name="itemData" >
		<sys:message content="${message}"/>		
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td><strong>券类编码</strong></td>
					<td><form:input path="couponCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人编号</strong></td>
					<td><form:input path="opCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人</strong></td>
					<td><form:input path="op" class="input-medium" readonly="true"/></td>
					<td><strong>操作时间</strong></td>
					<td><input name="opTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${mallPromotionCouponInfo.opTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>券类型</strong></td>
					<td><form:select path="couponType" style="width:177px;" onchange="changecouponType(this);">
							<form:options items="${fns:getDictList('coupon_type')}"
									temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td><strong>时间类型</strong></td>
					<td><form:select path="timeType" style="width:177px;" onchange="changeTimeType(this);">
							<form:options items="${fns:getDictList('coupon_time_type')}"
									temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td name="fixedTime" class="fixedTime"><strong>开始时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="startTime" name="startTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionCouponInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/></td>
					<td name="fixedTime" class="fixedTime"><strong>结束时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="endTime" name="endTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionCouponInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/></td>
					<td name="validTime" class="validTime" hidden="true"><strong>有效天数</strong></td>
					<td name="validTime" class="validTime" hidden="true">
						<form:input path="validDays" htmlEscape="false" maxlength="32" class="input-medium digits" /></td>
					<td name="couponTimeType" class="activityTime" hidden="true"></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>券名称</strong></td>
					<td colspan="3"><form:input path="couponName" class="input-xxlarge"/>
					<span class="help-inline"><font color="red">*</font></span></td>
					<td><strong>券简称</strong></td>
					<td><form:input path="couponSName" class="input-medium"/>
					<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td><strong>权益说明</strong></td>
					<td colspan="7">
						<form:textarea path="direction" cols="1000" rows="3" class="input-xxlarge"/>
						<span class="help-inline"><font color="red">*</font></span>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset title="使用逻辑">
			<legend>使用逻辑</legend>
			<div id="online_discount" name="网店抵扣券">
				<table>
					<tr>
						<td width="100px;"><strong>使用条件</strong></td>
						<td>订单满（元）</td>
						<td><form:input path="rule.conditionValueDouble" htmlEscape="false" maxlength="32" class="input-medium number required"/>
						<span class="help-inline"><font color="red">*</font></span></td>
						<td>抵扣（元）</td>
						<td><form:input path="rule.denominationDouble" htmlEscape="false" maxlength="32" class="input-medium number required"/>
						<span class="help-inline"><font color="red">*</font></span></td>
					</tr>
					<tr>
						<td width="100px;"><strong>适用范围</strong></td>
						<td colspan="2">
							<form:radiobuttons path="rule.rangeType" items="${fns:getDictList('coupon_range_type')}" itemLabel="label" itemValue="value" htmlEscape="false"
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
					<tr>
						<td colspan="5">
							<div id="partItem">
								<a class="btn btn-primary" href="javascript:addItem()"><i
								class="icon-file"></i>新增</a>
								<font color="red">可选商品SKU限制逻辑:当前有效的产商品SKU</font>
								<table id="online_discountTable" border="1" style="width: 600px;border-color: #0099FF;" >
									<tr>
										<td width="100px;" align="center"><strong>序号</strong></td>
										<td width="200px;" align="center"><strong>商品SKU</strong></td>
										<td width="300px;" align="center"><strong>商品名称</strong></td>
										<td width="100px;" align="center"><strong>操作</strong></td>
									</tr>
								</table>
								<div style="color: red;">
								订单内包含适用范围内商品，可以使用优惠券。如：<br>
								当使用条件为满100元抵扣5元时，如果订单包含商品A（50元）x1、B（55元）x2,<br>
								其中B属于适用范围内商品，订单中B总价为110元＞100元，此时可以使用该优惠券抵扣5元。
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="online_gift" name="网店赠品券" style="display: none;">
				<table style="width: 50%;">
					<tr>
						<td><strong>赠品名称</strong></td>
						<td colspan="2"><form:input path="rule.giftName" htmlEscape="false" maxlength="50" class="input-large required"/>
						<span class="help-inline"><font color="red">*</font></span></td>
						<td><strong>赠品数量</strong></td>
						<td><form:input path="rule.giftNum" htmlEscape="false" maxlength="32" class="input-medium digits required" />
						<span class="help-inline"><font color="red">*</font></span></td>
					</tr>
					<tr>
						<td><strong>使用范围</strong></td>
						<td><a class="btn btn-primary" href="javascript:addItem()"><i
							class="icon-file"></i>新建</a></td>
						<td>
						<td colspan="2" align="left"><font color="red">可选商品SKU限制逻辑:当前有效的商品SKU</font>
						</td>
					</tr>
				</table>
				<table id="online_giftTable" border="1" style="width: 700px;border-color: #0099FF;" >
					<tr>
						<td width="100px;" align="center"><strong>序号</strong></td>
						<td width="150px;" align="center"><strong>商品SKU</strong></td>
						<td width="300px;" align="center"><strong>商品名称</strong></td>
						<td width="75px;" align="center"><strong>优先级</strong></td>
						<td width="75px;" align="center"><strong>操作</strong></td>
					</tr>
				</table>
				<table>
					<tr>
						<td colspan="2">
							<font color="red">
								订单内产商品按照优先级顺序分摊优惠，优先级越小越优先分摊。如：</br>
								订单内包含商品Ax2、Bx1均属于适用范围内产品，A优先级为1，B优先级为2。</br>
								当赠品数量=1时，优惠首先分摊在A上，消费者还需支付Ax1+Bx1的金额。</br>
								当赠品数量=2时，优惠首先分摊在A上，消费者还需支付Bx1的金额。
							</font>
						</td>
					</tr>
				</table>
			</div>
			<div id="offline_gift" name="线下兑换券" style="display: none;">
				<table>
					<tr>
						<td width="100px;"><strong>兑换产品</strong></td>
						<td><a class="btn btn-primary" href="javascript:addItem()"><i
								class="icon-file"></i>新建</a></td>
						<td width="100px;"></td>
						<td><font color="red">可选产品SKU限制逻辑:当前有效的产品SKU</font></td>
					</tr>
				</table>
				<table id="offline_giftTable" border="1" style="width: 700px;border-color: #0099FF;" >
					<tr>
						<td width="100px;" align="center"><strong>序号</strong></td>
						<td width="150px;" align="center"><strong>产品SKU</strong></td>
						<td width="300px;" align="center"><strong>产品名称</strong></td>
						<td width="75px;" align="center"><strong>产品数量</strong></td>
						<td width="75px;" align="center"><strong>操作</strong></td>
					</tr>
				</table>
				<table>
					<tr>
						<td colspan="2">
							<font color="red">
								兑换产品为该优惠券可线下兑换的产品及可兑换每个产品的数量。如：</br>
								兑换产品包含Ax2，Bx1，消费者可使用该兑换券，在指定地点兑换产品Ax2+Bx1。
							</font>
						</td>
					</tr>
				</table>
			</div>
		</fieldset>
		<div id="addDiv" class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnCancel" class="btn" type="button" value="取 消" 
					onclick="return confirmx('是否确认取消？', this.href='${ctx}/coupon/mallPromotionCouponInfo/')" />
		</div>
		<div id="queryDiv" class="form-actions" style="display: none;">
			<input class="btn btn-info" type="button" value="返 回" onclick="reback()"/>
			<script type="text/javascript">
				function reback(){
					location.href='${ctx}/coupon/mallPromotionCouponInfo/';
				}
			</script>
		</div>
	</form:form>
</body>
</html>