<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>优惠券活动管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
		//活动范围
		debugger;
		var scope = '${mallPromotionActivityInfo.rangeType}';
		if(scope=='part'){
			$("#partItem").show();
		}else if(scope=='wholeshop'){
			$("#partItem").hide();
		}
		//发券方式
		var type = '${mallPromotionActivityInfo.coupon.sendCouponType}';
		if(type =='sys'){
			$("#theme").show();
		}else if(type =='self'){
			$("#theme2").show();
			$("#count_over").hide();
		}else if(type =='man_send'  || type =='sys_regist'){
			$("#count_over").hide();
		}
		
		$("#inputForm").validate({
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
				var startTime = $("#startTime")[0].value;
				var endTime = $("#endTime")[0].value;
				var date1 = new Date(Date.parse(startTime));
				var date2 = new Date(Date.parse(endTime));
				if (date1.getTime() > date2.getTime()) {
					layer.alert("结束时间不得小于开始时间。");
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
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/activity/mallPromotionActivityInfo/">优惠券活动列表</a></li>
		<li class="active"><a href="${ctx}/activity/mallPromotionActivityInfo/form?id=${mallPromotionActivityInfo.id}">优惠券活动编辑</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityInfo" action="${ctx}/activity/mallPromotionActivityInfo/update" method="post" class="form-horizontal">
		<input type="hidden" id="itemData"  name="itemData"/>
		<input type="hidden" id="couponData"  name="couponData"/>
		<input type="hidden" id="couponName"  name="couponName"/>
		<input type="hidden" id="couponLevelData"  name="couponLevelData"/>
		<input type="hidden" id="couponMapData"  name="couponMapData"/>
		<input type="hidden" id="levelDetailData"  name="levelDetailData"/>
		<input type="hidden" id="levelDetailData2"  name="levelDetailData2"/>
		<input type="hidden" id="levelJson"  name="levelJson"/>
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<fieldset title="基本信息">
			<table style="width: 95%;">
				<tr>
					<td><strong>活动编码</strong></td>
					<td><form:input path="activityCode" class="input-medium" readonly="true"/></td>
					<td><strong>操作人编号</strong></td>
					<td><form:input path="updatBy" class="input-medium" readonly="true"/></td>
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
					<td><form:select path="activityTheme" style="width:177px;" onchange="changecouponType(this);" disabled="true">
							<form:option value="" label="--请选择--"></form:option>
							<form:options items="${fns:getDictList('activity_category')}"
									temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td><strong>活动名称</strong></td>
					<td colspan="3"><form:input path="activityName" class="input-xxlarge"/>
					<span class="help-inline"><font color="red">*</font></span></td>
					<td><strong>活动简称</strong></td>
					<td><form:input path="activitySName" class="input-medium"/>
					<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td name="fixedTime" class="fixedTime"><strong>开始时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="startTime" name="startTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionActivityInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="" readonly="true"/>
						<span class="help-inline"><font color="red">*</font></span></td>
					<td name="fixedTime" class="fixedTime"><strong>结束时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="endTime" name="endTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionActivityInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
							onchange="changeRangeType(this);" lang="part" disabled="true"/>
					</td>
				</tr>
				<tr id="partItem">
					<td colspan="8">
						<!-- <a class="btn btn-primary" href="javascript:addItem()"><i
								class="icon-file"></i>新增</a> -->	
						<table id="rangeTypePart" border="1" style="width: 600px;border-color: #0099FF;" >
							<tr>
								<td width="100px;" align="center"><strong>序号</strong></td>
								<td width="200px;" align="center"><strong>商品SKU</strong></td>
								<td width="300px;" align="center"><strong>商品名称</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
							<c:forEach items="${mallPromotionActivityInfo.range}" var="range" varStatus="status">
								<tr>
									<td width="100px;" align="center">${status.index+1 }</td>
									<td width="200px;" align="center">${range.itemSku }</td>
									<td width="300px;" align="center">${range.itemName }</td>
									<td width="100px;" align="center"><a href="javascript:">删除</a></td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset title="活动逻辑">
			<legend>活动逻辑</legend>
			<table style="width: 95%">
				<tr>
					<td width="100px;">
						<strong>发券方式</strong><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td width="200px;">
						<div>
							<form:select path="coupon.sendCouponType" style="width:177px;" onchange="showType(this);" disabled="true">
								<form:options items="${fns:getDictList('activity_send_coupon_type')}"
									temLabel="label" itemValue="value" />
							</form:select>
						</div>
					</td>
					<td id="theme" style="display:none;">	
						<strong>满足条件</strong>&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="coupon.conditionType" style="width:177px;" onchange="changeTheme(this);" disabled="true">
							<form:options items="${fns:getDictList('activity_mjs_condition')}"
								temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td id="theme2" style="display:none;">
						<strong>活动排序</strong>&nbsp;&nbsp;&nbsp;&nbsp;<span class="help-inline"><font color="red">*</font></span>
						<form:input path="coupon.position" class="input-medium"/>
					</td>
					<td colspan="5" ></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr id="couponsItem">
					<td>
					<strong>可发券</strong><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td>
						<!-- <a class="btn btn-primary" href="javascript:addCouponsItem()"><i
								class="icon-file"></i>新增</a> -->
					</td>
					<td colspan="6"></td>
				</tr>
				<tr>
					<td colspan="8">
						<table id="couponsPart" border="1" style="width: 600px;border-color: #0099FF;" >
							<tr>
								<td width="100px;" align="center"><strong>券编码</strong></td>
								<td width="300px;" align="center"><strong>券简称</strong></td>
								<td width="100px;" align="center"><strong>总发放数量</strong></td>
								<td width="200px;" align="center"><strong>个人领取上限（未使用+已使用）</strong></td>
								<td width="200px;" align="center"><strong>个人领取上限（未使用）</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
							<c:forEach items="${mallPromotionActivityInfo.couponMap}" var="map" varStatus="status">
								<tr>
									<td align="center">${map.coupon.couponCode }</td>
									<td align="center">${map.coupon.couponSName }</td>
									<td align="center">${map.totalLimit }</td>
									<td align="center">${map.userLimit }</td>
									<td align="center">${map.userUsableLimit }</td>
									<td width="100px;" align="center"><a href="javascript:">删除</a></td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr id="count_over">
					<td colspan="8">
						<!-- <a class="btn btn-primary" href="javascript:addLevel('count_overTable')"><i
								class="icon-file"></i>增加一级优惠</a> -->
						<table id="count_overTable" border="1" style="width: 600px;border-color: #0099FF;" >
							<tr>
								<td width="100px;" align="center"><strong>优惠级别</strong></td>
								<td width="200px;" align="center"><strong>优惠条件</strong></td>
								<td width="300px;" align="center"><strong>优惠方式</strong></td>
								<td width="100px;" align="center"><strong>优惠区间</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
							<c:forEach items="${mallPromotionActivityInfo.couponLevel}" var="level" varStatus="status">
								<tr>
									<td align="center">${level.level }</td>
									<td align="center">${level.conditionValue }${fns:getDictLabel(mallPromotionActivityInfo.coupon.conditionType, 'activity_mjs_condition', '')}</td>
									<td align="center">${level.levelDetailStr }</td>
									<c:choose>
										<c:when test="${mallPromotionActivityInfo.coupon.conditionType == 'amount_type'}">
											<c:choose>
												<c:when test="${!status.last}">
													<td align="center">${level.conditionValue }~${mallPromotionActivityInfo.couponLevel[status.index+1].conditionValue-0.01}</td>
												</c:when>
												<c:otherwise>
													<td align="center">${level.conditionValue }~</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${!status.last}">
													<td align="center">${level.conditionValue }~${mallPromotionActivityInfo.couponLevel[status.index+1].conditionValue}</td>
												</c:when>
												<c:otherwise>
													<td align="center">${level.conditionValue }~</td>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
									<td width="100px;" align="center"><a href="javascript:">删除</a></td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="activity:mallPromotionActivityInfo:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-info" type="button" value="返 回" onclick="reback()"/>
		</div>
	</form:form>
	
<script type="text/javascript">
function test1(){
	getItemData2();
	var d = $("#couponName").val();
	return d;
}

function changeRangeType(e){
	var scope = e.value;
	if(scope=='part'){
		$("#partItem").show();
	}else if(scope=='wholeshop'){
		$("#partItem").hide();
	}
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
	function addCouponsItem(){
		var tableId = "couponsPart";
		var partItemIndex = $("#"+tableId+" tr").length;
		var newStr = "<tr>";
		newStr += "<td width='500px;' align='left'><input type='text' class='input-small' placeholder='请输入券编码' ";
		newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px' ";
		newStr += "onfocus='clearInput(this)' onblur='getCouponsItemInfo(this)'/></td>";
		newStr += "<td width='100px;' align='center'></td>";
		newStr += "<td style='display:none' width='100px;' align='center'></td>";
		newStr += "<td width='100px;' align='center'><input type='text' class='input-small'></td>";
		newStr += "<td width='100px;' align='center'><input type='text' class='input-small'></td>";
		newStr += "<td width='100px;' align='center'><input type='text' class='input-small'></td>";
		newStr += "<td width='100px;' align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
		newStr += "</tr>";
		addTr("couponsPart",newStr);
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
		obj.parentNode.parentNode.remove();
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
			var url = '${ctx}/activity/mallPromotionActivityInfo/getItemBySku';
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
					debugger;
					//var itemName="";
					//var itemId="";
					//var $td = obj.parentNode;
					if(data!=null&&data!=""){
						var flag = data.flag;
						var json = data.json;
						if(flag=="error"){
							layer.alert(json);
							return;
						}
						var itemName="";
						var $td = obj.parentNode;
						var item = JSON.parse(json);
						itemName = item.itemName;
					}else{
						layer.alert("查找不到商品/产品,请确认SKU是否正确!");
					}
					$td.nextSibling.innerHTML = itemName;
					//$td.nextSibling.nextSibling.innerHTML = itemId;
				}
			});
		}
	}
	
	function getCouponsItemInfo(obj){
// 		if(checkTable()){
// 			layer.alert("已含有重复商品!");	
// 			return;
// 		}
		var couponCode = obj.value;
		if(couponCode==null||couponCode==""){
			obj.value = "请输入券编码";
		}else{
			var url = '${ctx}/coupon/mallPromotionCouponInfo/getCoupon';
	        $.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					couponCode : couponCode
				}, 
				success : function(data){
					var couponName="";
					var couponId="";
					var $td = obj.parentNode;
					if(data!=null&&data!=""){
						var item = JSON.parse(data);
						couponName = item.couponName;
						couponId = item.couponId;
					}else{
						layer.alert("查找不到券主数据,请确认券编码是否正确!");
					}
					$td.nextSibling.innerHTML = couponName;
					$td.nextSibling.nextSibling.innerHTML = couponId;
				}
			});
		}
	}
	
	function getItemData() {
		var tableId = "rangeTypePart";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			item.itemSku = tdEle[1].firstChild.value;
			item.itemName = tdEle[2].innerHTML;
			items[i-1] = item;
		}
		var json = JSON.stringify(items);
		$("#itemData").val(json);
	}
	
	function getItemData2() {
		var tableId = "couponsPart";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		var name = "";
		var z = $("#condition").val();
		if(z == 'count_over'){
			z = '件'
		}else if('amount_over'){
			z = '元';
		}
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			if(tdEle[0].firstChild.value == null || tdEle[0].firstChild.value == '' ||  tdEle[0].firstChild.value == '请输入券编码'){
				continue;
			}
			item.itemSku = tdEle[0].firstChild.value;
			item.itemName = tdEle[1].innerHTML;
			item.itemId = tdEle[2].innerHTML;
			item.itemType = z;
			name += item.itemName+":";
			items[i-1] = item;
		}
		if(name != ""){
			name += z;
		}
		var json = JSON.stringify(items);
		$("#couponName").val(name);
		$("#couponData").val(json);
	}
	
	function getCouponMapData() {
		var tableId = "couponsPart";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		var name = "";
	
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			if(tdEle[0].firstChild.value == null || tdEle[0].firstChild.value == '' ||  tdEle[0].firstChild.value == '请输入券编码'){
				continue;
			}
			item.couponId = tdEle[2].innerHTML;
			//item.itemName = tdEle[1].innerHTML;
			item.totalLimit = tdEle[3].firstChild.value;
			item.userLimit = tdEle[4].firstChild.value;
			item.userUsableLimit = tdEle[5].firstChild.value;
			items[i-1] = item;
		}
		var json = JSON.stringify(items);
		$("#couponMapData").val(json);
	}
	
	function getCouponLevelData() {
		var tableId = "count_overTable";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		for (var i = 1; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var item = new Object();
			item.level = tdEle[0].innerHTML;
			item.conditionValue = tdEle[1].innerHTML;
			item.levelDetailStr = tdEle[2].innerHTML;
			items[i-1] = item;
		}
		var json = JSON.stringify(items);
		$("#couponLevelData").val(json);
	}
	
	function addLevel(tableId){
		$("#levelJson").val("");
		$("#levelDetailData").val("");
		$("#couponLevelData").val("");
		var condition = $("#condition option:selected")[0];
		var conditionValue = condition.value;
		var conditionLabel = condition.label;
		$("#goodDetail").val("");
		var url = '${ctx}/activity/mallPromotionActivityInfo/addLevel?condition='+conditionValue;
		pageii = layer.open({
			title : "新增优惠级别",
			type : 2,
			area : [ "50%", "50%" ],
			content : url,
			end : function() {
				var levelJson = $("#levelDetailData").val();
				var levelDetail = $("#levelDetailData2").val();
				if(levelJson==""){
					return;
				}
				setLevel(levelJson,levelDetail,tableId);
			}
		});
	}
	
	function setLevel(levelJson,levelDetail,tableId){
		var data = JSON.parse(levelJson);
		var detail = JSON.parse(levelDetail);
		var str = "";
		for(var i=0;i<detail.length;i++){
			str += detail[i].couponId + ":" + detail[i].sendNum + ";";
		}
		var partItemIndex = $("#"+tableId+" tr").length;
		var newStr = "<tr>";
		newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
		newStr += "<td width='100px;' align='center'>"+data[0]+"</td>";
		newStr += "<td style='display:none' width='100px;' align='center'>"+str+"</td>";
		newStr += "<td width='100px;' align='center'>"+data[1]+"</td>";
		newStr += "<td width='100px;' align='center'>"+data[0]+"~</td>";
		newStr += "<td width='100px;' align='center'><input type='hidden' value='"+levelJson+"'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
		newStr += "</tr>";
		addTr(tableId,newStr);
	}
	
	function showType(obj){
		var type = obj.value;
		if(type !='sys'){
			$("#count_over").hide();
			//$("#couponsItem").hide();
			$("#theme").hide();
		}else{
			$("#count_over").show();
			$("#couponsItem").show();
			$("#theme").show();
		}
		if(type == 'self'){
			$("#theme2").show();
			$("#position").show();
		}else{
			$("#theme2").hide();
			$("#position").hide();
		}
		
	}
	function reback(){
		location.href='${ctx}/activity/mallPromotionActivityInfo/';
	}
</script>
</body>
</html>