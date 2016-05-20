<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优惠券活动管理</title>
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
				//券整数校验
				var flag = getCouponMapData();
				if(!flag){
					return;
				}
				//时间校验
				var startTime = $("#startTime")[0].value;
				var endTime = $("#endTime")[0].value;
				var date1 = new Date(Date.parse(startTime));
	    		var date2 = new Date(Date.parse(endTime));
	    		if (date1.getTime() > date2.getTime()) {
	    			layer.alert("结束时间不得小于开始时间。");
	    			return;
	    		}
	    		//活动范围检验
				getItemData();
	    		var rangeType = $("input[name='rangeType']:checked").val();
				if(rangeType=="part"){
					var itemJson = $("#itemData").val();
					if(itemJson=="[]"){
						layer.alert("请添加活动范围！");
		    			return;
					}
				}
	    		if(!checkNull()){
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
		<li class="active"><a href="${ctx}/activity/mallPromotionActivityInfo/form?id=${mallPromotionActivityInfo.id}">优惠券活动<shiro:hasPermission name="activity:mallPromotionActivityInfo:edit">${not empty mallPromotionActivityInfo.id?'修改':'添加'}</shiro:hasPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallPromotionActivityInfo" action="${ctx}/activity/mallPromotionActivityInfo/save" method="post" class="form-horizontal">
		<input type="hidden" id="itemData"  name="itemData"/>
		<input type="hidden" id="couponData"  name="couponData"/>
		<input type="hidden" id="couponName"  name="couponName"/>
		<input type="hidden" id="couponLevelData"  name="couponLevelData"/>
		<input type="hidden" id="couponMapData"  name="couponMapData"/>
		<input type="hidden" id="levelDetailData"  name="levelDetailData"/>
		<input type="hidden" id="levelDetailData2"  name="levelDetailData2"/>
		<input type="hidden" id="levelJson"  name="levelJson"/>
		<input type="hidden" id="isNum" name="isNum"/>
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
					<td><form:select path="activityTheme" style="width:177px;" onchange="changecouponType(this);">
							<form:option value="" label="--请选择--"></form:option>
							<form:options items="${fns:getDictList('activity_category')}"
									temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
					<td><strong>活动简称</strong></td>
					<td><form:input path="activitySName" class="input-medium" placeholder='最多8个字'/>
					<span class="help-inline"><font color="red">*</font></span></td>
					<td name="fixedTime" class="fixedTime"><strong>开始时间</strong></td>
					<td name="fixedTime" class="fixedTime"><input id="startTime" name="startTime" type="text" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallPromotionActivityInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
					<td><strong>活动名称</strong></td>
					<td colspan="3"><form:input path="activityName" class="input-xxlarge" placeholder='最多20个字'/>
					<span class="help-inline"><font color="red">*</font></span></td>
					<td colspan="4"></td>
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
			<table>
				<tr id="partItem">
					<td colspan="7" style="width:52%">
						<a class="btn btn-primary" href="javascript:addItem()"><i
								class="icon-file"></i>新增</a>
						<table id="rangeTypePart" border="1" style="width: 800px;border-color: #0099FF;" >
							<tr>
								<td width="100px;" align="center"><strong>序号</strong></td>
								<td width="200px;" align="center"><strong>商品SKU</strong></td>
								<td width="400px;" align="center"><strong>商品名称</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
						</table>
					</td>
					<td>
					<div style="color: red;padding-top: 30px">可选商品限制：状态有效商品。</div>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset title="活动逻辑">
			<legend>活动逻辑</legend>
			<table style="width: 95%;">
				<tr>
					<td colspan="8"><strong>发券方式</strong><span class="help-inline"><font color="red">*</font></span>
					<form:select path="sendCouponType" style="width:177px;" onchange="showType(this);">
								<form:options items="${fns:getDictList('activity_send_coupon_type')}"
									temLabel="label" itemValue="value" />
					</form:select>
						<img src="${pageContext.request.contextPath}/static/images/warning.png" onmouseover="showWarning1(this);" onmouseout="hideWarning1();">
						<div id="warningImg1" style="position: absolute;display: none;left: 250px;background-color: white;">
							<ul style="color: red;list-style-type: none;">
								<li>买单赠券：</li>
								<li>&nbsp;&nbsp;订单付款后，给符合条件的有效订用户发送优惠券，需要使用活动范围。</li>
								<li>自助领取</li>
								<li>&nbsp;&nbsp;用户自助通过领取入口领取优惠券。</li>
								<li>人工发放</li>
								<li>&nbsp;&nbsp;根据人工维护发放数据，给对应的用户按照活动逻辑发放优惠券。</li>
								<li>注册赠券</li>
								<li>&nbsp;&nbsp;根据注册数据，给对应的用户按照活动逻辑发放优惠券。</li>
							</ul>
						</div>
						<span align="left" id="theme2" style="display:none">
						<strong>活动排序</strong><span class="help-inline"><font color="red">*</font></span>
						<input id="position" name="position" class="input-medium required digits" />
<%-- 						<form:input path="position" class="input-medium required digits" /> --%>
						</span>
						
						<span align="left" id="theme">
						<strong>满足条件</strong>
						<form:select path="condition" style="width:177px;" onchange="changeCondition(this);">
							<form:options items="${fns:getDictList('activity_mjs_condition')}"
									temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
				</tr>
				<tr id="couponsItem">
					<td colspan="7" style="width:52%">
					<strong>可发券</strong><span class="help-inline"><font color="red">*</font></span>
						<a class="btn btn-primary" href="javascript:addCouponsItem()"><i
								class="icon-file"></i>新增</a>
						<img src="${pageContext.request.contextPath}/static/images/warning.png" onmouseover="showWarningType('warningImgCoupon');" onmouseout="hideWarningType('warningImgCoupon');">
						<div id="warningImgCoupon" style="position: absolute;display: none;left: 150px;background-color: white;">
							<ul style="color: red;list-style-type: none;">
								<li>总法当数量：本次优惠券活动中，该券总共可以法当的数量；</li>
								<li>个人领取上限（未使用+已使用）：一个消费者，最多可以领取券的数量；</li>
								<li>个人领取上限（未使用）：一个消费者，当前可以使用的该券的数量。</li>
							</ul>
						</div>
						<table id="couponsPart" border="1" style="width: 800px;border-color: #0099FF;" >
							<tr>
								<td width="200px;" align="center"><strong>券编码</strong></td>
								<td width="200px;" align="center"><strong>券简称</strong></td>
								<td width="100px;" align="center"><strong>总发放数量</strong></td>
								<td width="100px;" align="center"><strong>个人领取上限（未使用+已使用）</strong></td>
								<td width="100px;" align="center"><strong>个人领取上限（未使用）</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
						</table>
						
					</td>
					<td style="text-align: left;">
					</td>
				</tr>
				<tr id="count_over">
					<td colspan="2">
						<a class="btn btn-primary" href="javascript:addLevel('count_overTable')"><i
								class="icon-file"></i>增加一级优惠</a>
						<table id="count_overTable" border="1" style="width: 800px;border-color: #0099FF;" >
							<tr>
								<td width="200px;" align="center"><strong>优惠级别</strong></td>
								<td width="200px;" align="center"><strong>优惠条件</strong></td>
								<td width="400px;" align="center">
									<strong>优惠方式</strong>
									<img src="${pageContext.request.contextPath}/static/images/warning.png" onmouseover="showWarningType('warningImgLevel');" onmouseout="hideWarningType('warningImgLevel');">
									<div id="warningImgLevel" style="position: absolute;display: none;left: 250px;background-color: white;">
										<ul style="color: red;list-style-type: none;">
											<li>选择券后请输入选中券的单次发放数量</li>
											<li>单次发放数量：</li>
											<li>每次触发发券后，改券发放给消费者的数量</li>
										</ul>
									</div>
								</td>
								<td width="100px;" align="center"><strong>优惠区间</strong></td>
								<td width="100px;" align="center"><strong>操作</strong></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div id="sysDiv" style="color: red;">
				分级优惠：<br>
				可设置多级优惠条件及内容，按能触发的最大级别优惠进行计算。<br>
				如：设置活动为多级优惠，一级优惠条件为100元，优惠方式为A券1张；二级优惠条件为200元，优惠方式为A券1张、B券1张，<br>
				当结算商品中在活动范围内的商品应付总金额为200时，触发二级优惠得出优惠：A券1张、B券1张，针对当前订单会给购买者自动推送A券1张、B券1张。
			</div>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="activity:mallPromotionActivityInfo:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-info" type="button" value="返 回" onclick="reback()"/>
		</div>
	</form:form>
	
<script type="text/javascript">
function showWarning(obj){
	$("#warningImg").toggle();
}
function hideWarning(){
	$("#warningImg").toggle();
}
function showWarningType(tableId){
	$("#"+tableId).toggle();
}
function hideWarningType(tableId){
	$("#"+tableId).toggle();
}
function showWarning1(obj){
	$("#warningImg1").toggle();
}
function hideWarning1(){
	$("#warningImg1").toggle();
}
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
		$("#itemData").val("");
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
		var ac = document.getElementById("theme2").style.display;
		if(ac != 'none' && partItemIndex > 1){
			layer.alert("只能添加一个");
			return;
		}
		var newStr = "<tr name='couponsItems'>";
		newStr += "<td  align='left'><input type='text' name='couponCode' class='input-small' placeholder='请输入券编码' ";
		newStr += "style='color:gray;height:18px;border-left:0px;border-top:0px;border-right:0px;border-bottom:0px;width:150px' ";
		newStr += "onfocus='clearInput(this)' onblur='getCouponsItemInfo(this)'/></td>";
		newStr += "<td  align='center'></td>";
		newStr += "<td style='display:none' name='couponids' width='100px;' align='center'></td>";
		newStr += "<td  align='center'><input id='input1"+partItemIndex+"' type='text' class='input-small'></td>";
		newStr += "<td  align='center'><input id='input2"+partItemIndex+"' type='text' class='input-small'></td>";
		newStr += "<td  align='center'><input id='input3"+partItemIndex+"' type='text' class='input-small'></td>";
		newStr += "<td  align='center'><a href='javascript:' onclick='deleteItem(this)'>删除</a></td>";
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
	function deleteLevel(obj){
		debugger;
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
				prevTR.childNodes[4].innerHTML = atr + "~" + (btr);
			}else{
				prevTR.childNodes[4].innerHTML = prevTR.childNodes[1].innerHTML + "~";
			}
		}
		while(nextTR.firstChild!=null){
			nextTR.firstChild.innerHTML = objIndex;
			objIndex ++;
			nextTR = nextTR.nextSibling;
		}
		obj.parentNode.parentNode.remove();
	}
	function deleteCouponItem(obj){
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
					var $td = obj.parentNode;
					if(data!=null&&data!=""){
						var flag = data.flag;
						var json = data.json;
						if(flag=="error"){
							layer.alert(json);
							$td.nextSibling.innerHTML = "";
							return false;
						}
						var itemName="";
						
						var item = JSON.parse(json);
						itemName = item.itemName;
					}else{
						layer.alert("查找不到商品/产品,请确认SKU是否正确!");
						$td.nextSibling.innerHTML = "";
						return false;
					}
					$td.nextSibling.innerHTML = itemName;
					//$td.nextSibling.nextSibling.innerHTML = itemId;
				}
			});
		}
	}
	String.prototype.trim=function() { return this.replace(/(^\s*)|(\s*$)/g, ""); } 
	function getCouponsItemInfo(obj){
		
		var codes = document.getElementsByName("couponCode");
		if(codes.length > 1){
			for(var i=0;i<codes.length-1;i++){
				for(var j=i+1;j<codes.length;j++){
					if(codes[i].value!=""){
						if(codes[j].value== codes[i].value){
							layer.alert("券主数据重复,请确认券编码是否正确!");
							return;
						}
					}
				}
				
			}
		}
		var couponCode = obj.value;
		if(couponCode==null||couponCode==""){
			obj.value = "请输入券编码";
		}else{
			couponCode = couponCode.trim();
			obj.value = couponCode;
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
						couponName = item.couponSName;
						couponId = item.couponId;
					}else{
						layer.alert("券已取消或已失效!");
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
		debugger;
		var tableId = "couponsPart";
		var items = new Array();
		var trEle = $("#"+tableId+"").find("tr");
		var name = "";
		$('#isNum').val("");
		if(trEle.length < 2){
			layer.alert("请输入可发券");
			return false;
		}
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
			var tNum =  tdEle[3].firstChild.value;
			var uNum = tdEle[4].firstChild.value;
			var uuNum = tdEle[5].firstChild.value;
			if(tNum==""||uNum==""||uuNum==""){
				layer.alert("总发放数量、个人领取上限（未使用+已使用）、个人领取上限（未使用）都不能为空！");
				return false;
			}
			if (!(/(^[1-9]\d*$)/.test(tNum))){
				$('#isNum').val(-1);
				layer.alert("总发放数量必须为整数");
				return false;
			}
			if (!(/(^[1-9]\d*$)/.test(uNum))){
				$('#isNum').val(-1);
				layer.alert("个人领取上限（未使用+已使用）必须为整数");
				return false;
			}
			if (!(/(^[1-9]\d*$)/.test(uuNum))){
				$('#isNum').val(-1);
				layer.alert("个人领取上限（未使用）必须为整数");
				return false;
			}
			tNum = parseInt(tNum);
			uNum = parseInt(uNum);
			uuNum = parseInt(uuNum);
			if(uNum<uuNum){
				layer.alert("未使用券应不大于未使用+已使用券");
				return false;
			}
			if(tNum < uNum ){
				layer.alert("个人领取上限应小于总发放数量");
				return false;
			}
			items[i-1] = item;
		}
		var json = JSON.stringify(items);
		$("#couponMapData").val(json);
		return true;
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
		var cid = document.getElementsByName('couponids');
		if(cid.length > 0){
			for(var i=0;i<cid.length;i++){
				if(cid[i].innerHTML == null || cid[i].innerHTML == ''){
					layer.alert("请先输入正确券编码");
					return;
				}
			}
		}
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
		var condition = $("#condition option:selected")[0];
		var conditionLabel = condition.label;
		var data = JSON.parse(levelJson);
		var detail = JSON.parse(levelDetail);
		var str = "";
		for(var i=0;i<detail.length;i++){
			str += detail[i].couponId + ":" + detail[i].sendNum + ";";
		}
		var partItemIndex = $("#"+tableId+" tr").length;
		var newStr = "<tr>";
		newStr += "<td width='100px;' align='center'>" + partItemIndex + "</td>";
		newStr += "<td width='100px;' align='center'>"+data[0]+conditionLabel+"</td>";
		newStr += "<td style='display:none' width='100px;' align='center'>"+str+"</td>";
		newStr += "<td width='100px;' align='center'>"+data[1]+"</td>";
		newStr += "<td width='100px;' align='center'>"+data[0]+"~</td>";
		newStr += "<td width='100px;' align='center'><input type='hidden' value='"+levelJson+"'><a href='javascript:' onclick='deleteLevel(this)'>删除</a></td>";
		newStr += "</tr>";
		
		if(partItemIndex!=1){
			var lastTr = $("#"+tableId+" tr:last")[0];
			var tdEle = lastTr.cells;
	 		str = tdEle[4].innerHTML;
	 		var prevCondition = tdEle[1].innerHTML;
	 		prevCondition = prevCondition.substr(0,prevCondition.length-1);
	 		prevCondition = Number(prevCondition);
	 		var nowCondition = Number(data[0]);
	 		if(prevCondition>=nowCondition){
	 			layer.alert("优惠条件不能比上一优惠级别低!");
	 			return false;
	 		}
			if(str.charAt(str.length-1)=="~"){
				tdEle[4].innerHTML = str+(data[0]-0.01).toFixed(2);
			}
		}  
		addTr(tableId,newStr);
	}
	
	function showType(obj){
		var coupons = document.getElementsByName("couponsItems");
		if(coupons.length > 0){
			for(var i=coupons.length;i>0;i--){
				coupons[i-1].remove();
			}
		}
		$("#levelDetailData").val("");
		$("#levelDetailData2").val("");
		$("#couponLevelData").val("");
		var type = obj.value;
		if(type !='sys'){
			$("#count_over").hide();
			//$("#couponsItem").hide();
			$("#theme").hide();
			$("#sysDiv").hide();
		}else{
			$("#count_over").show();
			$("#couponsItem").show();
			$("#theme").show();
			$("#sysDiv").show();
		}
		if(type == 'self'){
			$("#theme2").show();
			$("#position").show();
		}else{
			$("#theme2").hide();
			$("#position").hide();
		}
		
	}
	
	function showWarning(obj){
		$("#warningImg").toggle();
	}
	function hideWarning(){
		$("#warningImg").toggle();
	}
	function testType(){
		var sendCouponType = $("#sendCouponType").val();
		alert(sendCouponType);
	}
	function checkNull(){
		debugger;
		//非空校验
		if(checkCoupon()){
			var sendCouponType = $("#sendCouponType").val();
			if(sendCouponType=="sys"){
				if(checkLevel()){
					return true;
				}
			}else{
				return true;
			}
		}
		return false;
	}
	function checkCoupon(){
		debugger;
		//发券非空校验
		//getCouponMapData();
		var couponMapJson = $("#couponMapData").val();
		if(couponMapJson=="[]"){
			layer.alert("请添加可发券！");
			return false;
		}
		return true;
	}
	function checkLevel(){
		//优惠非空校验
		debugger;
		getCouponLevelData();
		var couponLevelJson = $("#couponLevelData").val();
		if(couponLevelJson=="[]"){
			layer.alert("请添加级别优惠！");
			return false;
		}
		return true;
	}
	function reback(){
		location.href='${ctx}/activity/mallPromotionActivityInfo/';
	}
	function changeCondition(e){
		var trEle = $("#count_overTable").find("tr");
		for(var i=1;i<trEle.length;i++){
			var navigatorName = "Microsoft Internet Explorer"; 
			if(navigator.appName == navigatorName){    
				trEle[i].removeNode(true);
			}else{    
				trEle[i].remove();
			}
		}
	}
</script>
</body>
</html>
