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
	function addItem(){
		debugger;
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
	function getProductInfo(obj){
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
					debugger;
					var itemName="";
					if(data!=null&&data!=""){
						var item = JSON.parse(data);
						var itemType = item.goodType;
						if(itemType=='single'){
							itemType = "单品";
						}else if(itemType=='suite'){
							itemType = '套装'
						}else if(itemType=='virtual'){
							itemType = '虚拟'
						}
						$("#productName").val(item.goodName);
						$("#giftType").val(item.goodType);
						$("#giftTypeLabel").val(itemType);
						$("#giftPrice").val(item.marketPrice/100);
						$("#giftPicture").val(item.picUrl);
					}else{
						layer.alert("找不到商品/产品！");
					}
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
		debugger;
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
	function addLevel(tableId){
// 		alert(tableId);
		$("#goodDetail").val("");
		var url = '${ctx}/activityMJS/mallPromotionActivityInfo/addLevel';
		pageii = layer.open({
			title : "添加套餐",
			type : 2,
			area : [ "90%", "80%" ],
			content : url,
			end : function() {
				var mallItemDetails = $("#goodDetail").val();
				setGoodData(mallItemDetails);
			}
		});
	}
	function changePreferentialType(e){
		var type = e.value;
		if(type=='less_money'){
			$(".nogift").show();
			$(".less_money").show();
			$(".discount").hide();
			$(".gift").hide();
		}else if(type=='discount'){
			$(".nogift").show();
			$(".less_money").hide();
			$(".discount").show();
			$(".gift").hide();
		}else if(type=='gift'){
			$(".less_money").hide();
			$(".discount").hide();
			$(".gift").show();
			$(".nogift").hide();
		}
	}
	function closePage(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引 
		parent.layer.close(index);
	}
	function getLevel(){
		debugger;
		var condition = '${condition}';
		var level = new Object();
		var conditionValueDouble = $("#conditionValueDouble").val();		//优惠条件
		var preferentialType = $("#preferentialType").val();	//优惠方式
		var preferentialTypeLabel = $("#preferentialType option:selected")[0].label;
		var preferentialValueDouble = $("#preferentialValueDouble").val();	//优惠方式值
		if("count_over"==condition){
			conditionValueDouble = parseInt(conditionValueDouble);
		}else{
			if("less_money"==preferentialType){
				if(parseFloat(conditionValueDouble)<parseFloat(preferentialValueDouble)){
					layer.alert("优惠减钱金额不得超过优惠条件！");
					return;
				}
			}
		}
		if("discount"==preferentialType){
			preferentialValueDouble = parseFloat(preferentialValueDouble);
			if(preferentialValueDouble>=10||preferentialValueDouble<5){
				layer.alert("折扣区间为5~10。<br><font color='red'>如98折请输入9.8</font>");
				return;
			}
		}
		level.conditionValueDouble = conditionValueDouble;
		level.preferentialType = preferentialType;
		level.preferentialTypeLabel = preferentialTypeLabel;
		level.preferentialValueDouble = preferentialValueDouble;
		
		if(preferentialType=="gift"){
			var giftSku = $("#giftSku").val();
			var giftName = $("#giftName").val();
			var giftNum = $("#giftNum").val();
			var giftType = $("#giftType").val();
			var giftPriceFloat = $("#giftPrice").val();
			var giftPicture = $("#giftPicture").val();
			level.giftSku = giftSku;
			level.giftName = giftName;
			level.giftNum = giftNum;
			level.giftType = giftType;
			level.giftPriceFloat = giftPriceFloat;
			level.giftPicture = giftPicture;
		}
		var jsonStr = JSON.stringify(level);
		parent.$("#levelJson").val(jsonStr);
		closePage();
	}
	function clearInput(obj){
		if(obj.value=="请输入商品SKU"||obj.value=="请输入产品SKU"){
			obj.value = "";
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
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<fieldset title="基本信息">
			<table style="width: 95%;">
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td width="100px;"><strong>优惠条件</strong></td>
					<td><form:input path="conditionValueDouble" class="input-small"/>
					${fns:getDictLabel(condition, 'activity_mjs_condition', '')}
					<span class="help-inline"><font color="red">*</font></span></td>
					<td width="100px;"><strong>优惠方式</strong></td>
					<td><form:select path="preferentialType" class="input-small" onchange="changePreferentialType(this);">
							<form:options items="${fns:getDictList('activity_preferential_type')}"
									temLabel="label" itemValue="value" />
						</form:select><span class="help-inline"><font color="red">*</font></td>
				</tr>
				<tr>
					<td colspan="8">
						<div class="line"></div>
					</td>
				</tr>
				<tr>
					<td width="100px;" class="less_money"><strong>减钱（元）</strong></td>
					<td width="100px;" class="discount" style="display: none;"><strong>打折</strong></td>
					<td class="nogift"><form:input path="preferentialValueDouble" class="input-small"/></td>
				</tr>
				<tr class="gift" style="display: none;">
					<td colspan="4">
						<table title="赠品信息" style="width: 100%">
							<tr>
								<td width="100px;"><strong>赠品显示名称</strong></td>
								<td colspan="3"><form:input path="giftName" class="input-xxlarge"/></td></td>
							</tr>
							<tr>
								<td><strong>赠品SKU</strong></td>
								<td><form:input path="giftSku" class="input-small" onblur="getProductInfo(this);" onfocus="clearInput(this);"/></td>
								<td><strong>赠品数量</strong></td>
								<td><form:input path="giftNum" class="input-small" value="1"/></td>
							</tr>
							<tr>
								<td colspan="4">
									<div class="line"></div>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<input id="giftPicture" type="hidden">
									<table style="width: 100%" border="1">
										<tr>
											<td align="center" width="60%">产品名称</td>
											<td align="center" width="20%">产品类型</td>
											<td align="center" width="20%">市场价</td>
										</tr>
										<tr>
											<td><input id="productName" class="noborder" type="text" readonly="readonly"></td>
											<td><input id="giftType" class="noborder" type="hidden" readonly="readonly"><input id="giftTypeLabel" class="noborder" type="text" readonly="readonly"></td>
											<td><input id="giftPrice" class="noborder" type="text" readonly="readonly"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="activityMJS:mallPromotionActivityInfo:edit"><input class="btn btn-primary" type="button" onclick="getLevel();" value="确 定"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="closePage();"/>
		</div>
	</form:form>
</body>
</html>