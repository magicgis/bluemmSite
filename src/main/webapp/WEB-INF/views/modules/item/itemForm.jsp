<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/common/json2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/lib/jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.min.css"
	type="text/css">
	
	<script>
//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
function banBackSpace(e){   
    var ev = e || window.event;//获取event对象   
    var obj = ev.target || ev.srcElement;//获取事件源   
    var t = obj.type || obj.getAttribute('type');//获取事件源类型  
    //获取作为判断条件的事件类型
    var vReadOnly = obj.getAttribute('readonly');
    //处理null值情况
    if(vReadOnly == null){
    	vReadOnly = false;
    } else {
        vReadOnly = (vReadOnly=='readonly' || vReadOnly=='true' ) ? true : false;
    }
    if(vReadOnly==true){   
        return false;   
    }   
    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
    var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
                ?true:false;   
    //判断
    if(flag2){
        return false;
    }
}
//禁止后退键 作用于Firefox、Opera
document.onkeypress=banBackSpace;
//禁止后退键  作用于IE、Chrome
document.onkeydown=banBackSpace;
</script>
<script type="text/javascript">
	var readFlag = '${readFlag}';
	$(document).ready(function() {
		debugger;
		var url ;
		var li;
		var urls ;
		url = '${mallItemInfo.mainPicUrl}';
		if(url!=null&&url!=""){
			$("#mainPicUrlPreview").children().remove();
			li = "<li><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
			li += "<a href=\"javascript:\" onclick=\"mainPicUrlDel(this);\">×</a></li>";
			$("#mainPicUrlPreview").append(li);
		}
		url =  '${mallItemInfo.introPicUrl }';
		if(url!=null&&url!=""){
			urls = url.split("|");
			$("#introPicUrlPreview").children().remove();
			for (var i=0; i<urls.length; i++){
				if (urls[i]!=""){
					li = "<li><img src=\""+urls[i]+"\" url=\""+urls[i]+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
					li += "<a href=\"javascript:\" onclick=\"introPicUrlDel(this);\">×</a></li>";
					$("#introPicUrlPreview").append(li);
				}
			}
		}
		url =  '${mallItemInfo.artMainPicUrl }';
		if(url!=null&&url!=""){
			urls = url.split("|");
			$("#artMainPicUrlPreview").children().remove();
			for (var i=0; i<urls.length; i++){
				if (urls[i]!=""){
					li = "<li><img src=\""+urls[i]+"\" url=\""+urls[i]+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
					li += "<a href=\"javascript:\" onclick=\"artMainPicUrlDel(this);\">×</a></li>";
					$("#artMainPicUrlPreview").append(li);
				}
			}
		}
		var itemId = '${mallItemInfo.itemId}';
		if (itemId != null && itemId != "") {
			var mallItemDetails = '${mallItemDetailVos}';
			setGoodData(mallItemDetails);
		}
		$("#inputForm").validate({
			rules : {
				itemSku : {
					required : true,
					minlength: 8,
					maxlength: 8,
					remote: {
						url: "${ctx}/item/checkItemSku",   	//后台处理程序
						type: "post",               //数据发送方式
						dataType: "json",           //接受数据格式   
						data: {                     //要传递的数据
							itemSku: function() {
								return $("#itemSku").val();
							},
							onlineTime: function() {
								return $("#onlineTime").val();
							},
							offlineTime: function() {
								return $("#offlineTime").val();
							},
							itemId: function() {
								return $("#itemId").val();
							}
						}
					}
				},
				categoryId : {
					required : true
				},
				itemName : {
					required : true
				},
				memberPriceFloat : {
					required : true,
					number:true
				},
				marketPriceFloat : {
					required : true,
					number:true
				},
				status : {
					required : true
				},
				hasPresale : {
					required : true
				},
				onlineTime : {
					required : true
				},
				offlineTime : {
					required : true
				},
				position : {
					required : true,
					digits:true
				},
				mainPicUrl :{
					required : true
				},
				stock : {
					number:true
				},
				categoryPosition : {
					required : true,
					digits:true
				}
			},
			messages : {
				itemSku : {
					required : "请填写商品SKU",
					minlength: "商品SKU必须是8位字符",
					maxlength: "商品SKU必须是8位字符",
					remote: "包含重复SKU"
				},
				categoryId : {
					required : "请填写选择商品分类"
				},
				itemName : {
					required : "请填写商品名称"
				},
				memberPriceFloat : {
					required : "请填写商品体验价",
					number : "商品体验价必须为数字"
				},
				marketPriceFloat : {
					required : "请选择子产品，系统自动统计市场价",
					number : "商品市场价必须为数字"
				},
				status : {
					required : "请选择商品状态"
				},
				hasPresale : {
					required : "请选择商品是否预售"
				},
				onlineTime : {
					required : "请选择商品上架时间"
				},
				offlineTime : {
					required : "请选择商品下架时间"
				},
				position : {
					required : "请填写商品排序号",
					digits : "排序号必须是整数"
				},
				mainPicUrl :{
					required : "请添加主图"
				},
				stock : {
					number : "请填写数字"
				},
				categoryPosition : {
					required : "请填写分类排序号",
					digits : "排序号必须是整数"
				}
			}
		});
	});
	function addSingle() {
		$("#goodDetail").val("");
		var url = '${ctx}/item/single';
		pageii = layer.open({
			title : "添加单品",
			type : 2,
			area : [ "90%", "80%" ],
			content : url,
			end : function() {
				var mallItemDetails = $("#goodDetail").val();
				setGoodData(mallItemDetails);
			}
		});
	}
	function addSuite() {
		$("#goodDetail").val("");
		var url = '${ctx}/item/suite';
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
	function addVirtual() {
		$("#goodDetail").val("");
		var url = '${ctx}/item/virtual';
		pageii = layer.open({
			title : "添加虚拟产品",
			type : 2,
			area : [ "90%", "80%" ],
			content : url,
			end : function() {
				var mallItemDetails = $("#goodDetail").val();
				setGoodData(mallItemDetails);
			}
		});
	}
	function setGoodData(data) {
		if(data==""){
			return;
		}
		var goodData = JSON.parse(data);
		for (var i = 0; i < goodData.length; i++) {
			var good = goodData[i];
			var newStr = "<tr>";
			newStr += "<td><input type='checkbox' name='goodCheck' value='"+good.goodId+"'></td>";
			newStr += "<td>" + good.goodSku + "</td>";
			newStr += "<td>" + good.goodName + "</td>";
			newStr += "<td>" + (good.goodType == 'single' ? '单品' : (good.goodType == 'suite' ? '套装' : '虚拟'))  + "</td>";
			newStr += "<td>" + good.price + "  </td>";
			newStr += "<td>" + good.marketPrice + "</td>";
			if(readFlag=='true'){
				newStr += "<td>"+ good.num + "</td>";
			}else{
				newStr += "<td><input type='text' class='input-mini' name='goodNum' value='"
					+ (good.num == 0 ? 1 : good.num) + "'></td>";
			}
			newStr += "</tr>";
			$("#goodTbody").append(newStr);
		}
	}
	function CheckAllGood() {
		var ischeck = $("#GoodCheckAll").attr("checked");
		if (ischeck == undefined) {
			$(":checkbox").removeAttr("checked");
		} else {
			$(":checkbox").attr("checked", "checked");
		}
	}
	function deleteGood() {
		var checkboxs = $("input[name='goodCheck']:checkbox:checked");
		if (checkboxs.length <= 0) {
			layer.alert("请至少选择一个产品!");
			return;
		}
		del();
	}
	function del() {
		$("input[name='goodCheck']:checkbox:checked").each(function() { // 遍历选中的checkbox		
			n = $(this).parents("tr").index(); // 获取checkbox所在行的顺序			
			$("#goodTbody").find("tr:eq(" + n + ")").remove();
		});
	}
	function getGoodData() {
		var goods = new Array();
		var trEle = $("#goodTbody").find("tr");
		for (var i = 0; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var good = new Object();
			good.goodId = tdEle[0].firstChild.defaultValue;
			good.goodSku = tdEle[1].innerHTML;
			good.goodName = tdEle[2].innerHTML;
			good.goodType = tdEle[3].innerHTML;
			good.priceFloat = tdEle[4].innerHTML;
			good.marketPrice = tdEle[5].innerHTML;
			if(readFlag=='true'){
				good.num = tdEle[6].innerHTML;
			}else{
				good.num = tdEle[6].firstChild.value;
			}
			goods[i] = good;
		}
		var json = JSON.stringify(goods);
		$("#goodDetail").val(json);
	}
	function JqValidate(){  
	   	return $("#inputForm").validate({
			rules : {
				itemSku : {
					required : true,
					minlength: 8,
					maxlength: 8,
					remote: {
						url: "${ctx}/item/checkItemSku",   	//后台处理程序
						async : true,
						type: "post",               //数据发送方式
						dataType: "json",           //接受数据格式   
						data: {                     //要传递的数据
							itemSku: function() {
								return $("#itemSku").val();
							},
							onlineTime: function() {
								return $("#onlineTime").val();
							},
							offlineTime: function() {
								return $("#offlineTime").val();
							},
							itemId: function() {
								return $("#itemId").val();
							}
						}
					}
				}
			},
			messages : {
				itemSku : {
					required : "请填写商品SKU"
				}
			}
	   	}).form(); 
	}  
	  
	function saveBtn(){
		debugger;
     	if(JqValidate()){    
			if(getMarketPrice()){
				getGoodData();
				var msg = checkUrl();
				if(msg==""){
		    		save();  
				}else{
					layer.alert(msg);
				}
			}
     	}else{
     		layer.alert("填写数据有误！");
     	}
	}  
	
	function checkUrl(){
		var main = $("#mainPicUrl").val();
		if(main==null||main==""){
			return "请添加主图";
		}
		var introPicUrl = $("#introPicUrl").val();
		if(introPicUrl==null||introPicUrl==""){
			return "请至少添加一张细节图";
		}
		return "";
	}
	
	function save() {
		$.ajax({
			cache : true,
			type : "POST",
			async : false,
			url : '${ctx}/item/save',
			data : $('#inputForm').serialize(),// 你的formid
			success : function(msg) {
				if (msg == "saveSuccess") {
					layer.alert("保存成功", function() {
						closePage();
					});
				} else if (msg == "updateSuccess") {
					layer.alert("更新成功", function() {
						closePage();
					});
				} else {
					layer.confirm("操作失败,是否返回列表?", function() {
						closePage();
					})
				}
			}
		});
	}
	function closePage() {
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引 
		parent.layer.close(index);
	}
	function countPriceBtn(){
		var flag = getMarketPrice();
	}
	
	//获取总市场价
	function getMarketPrice() {
		var marketPriceFloat = 0;
		var trEle = $("#goodTbody").find("tr");
		if (trEle.length == 0) {
			layer.alert("请先选择产品");
			return false;
		}
		for (var i = 0; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			if(readFlag=='true'){
				marketPriceFloat += tdEle[5].innerHTML * tdEle[6].innerHTML;
			}else{
				marketPriceFloat += tdEle[5].innerHTML * tdEle[6].firstChild.value;
			}
		}
		$("#marketPriceFloat").val(marketPriceFloat.toFixed(2));
		return countPrice();
	}
	//比重法计算每个商品体验价
	function countPrice() {
		var memberPriceFloat = $("#memberPriceFloat").val();
		if (memberPriceFloat == 0) {
			layer.alert("请填写商品体验价");
			return false;
		}
		var marketPriceFloat = $("#marketPriceFloat").val();
		if (parseInt(memberPriceFloat) > parseInt(marketPriceFloat)) {
			//TODO 需要添加校验
		}
		var trEle = $("#goodTbody").find("tr");
		for (var i = 0; i < trEle.length; i++) {
			var tdEle = trEle[i].cells;
			var onememberPrice = memberPriceFloat * parseFloat(tdEle[5].innerHTML) / marketPriceFloat;
			tdEle[4].innerHTML = onememberPrice.toFixed(2);
		}
		return true;
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
</style>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallItemInfo" action="#"
		method="post" class="breadcrumb form-search">
		<form:hidden path="itemId" />
		<form:hidden path="isValid" />
		<input type="hidden" id="goodDetail" name="goodDetail">
		<sys:message content="${message}" />
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 90%;">
				<tbody>
					<tr>
						<td align="right" width="100px"><strong>商品sku：</strong></td>
						<td><form:input path="itemSku" class="input-medium itemSkuFirstChar" />
							<span class="help-inline"><font color="red">*</font></span></td>
						<td align="right" width="100px"><strong>商品类别：</strong></td>
						<td><form:select path="categoryId"
								class="input-medium categoryIdCheck">
								<form:option value="" label="--请选择--" />
								<form:options items="${fns:getDictList('item_category')}"
									temLabel="label" itemValue="value" />
							</form:select> <span class="help-inline"><font color="red">*</font></span></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>商品名称：</strong></td>
						<td colspan="3"><form:input path="itemName"
								class="input-xxlarge" /> <span class="help-inline"><font
								color="red">*</font></span></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>体验价：</strong></td>
						<td>
							<c:choose>
								<c:when test="${readFlag }">
									<form:input path="memberPriceFloat" class="input-medium" readonly="true"/>
								</c:when>
								<c:otherwise>
									<form:input path="memberPriceFloat" class="input-medium" />
								</c:otherwise>
							</c:choose>
							<span
							class="help-inline"><font color="red">*</font></span></td>
						<td align="right"><strong>市场价：</strong></td>
						<td><form:input path="marketPriceFloat"
								class="input-medium" readonly="true" /> <span
							class="help-inline"><font color="red">*</font></span></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>是否上架：</strong></td>
						<td><form:select path="status" class="input-medium">
								<form:option value="" label="--请选择--" />
								<form:options items="${fns:getDictList('yes_no')}"
									temLabel="label" itemValue="value" />
							</form:select> <span class="help-inline"><font color="red">*</font></span></td>
						<td align="right"><strong>库存：</strong></td>
						<td><form:input path="stock" class="input-medium" /></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>上架时间：</strong></td>
						<td><input id="onlineTime" name="onlineTime" readonly="readonly"
							class="input-medium Wdate" type="text"
							value="<fmt:formatDate value="${mallItemInfo.onlineTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" /> <span
							class="help-inline"><font color="red">*</font></span></td>
						<td align="right"><strong>下架时间：</strong></td>
						<td><input id="offlineTime" name="offlineTime" readonly="readonly"
							class="input-medium Wdate" type="text"
							value="<fmt:formatDate value="${mallItemInfo.offlineTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" /> <span
							class="help-inline"><font color="red">*</font></span></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>全部排序：</strong></td>
						<td><form:input path="position" class="input-medium" /><span
							class="help-inline"><font color="red">*</font></span></td>
						<td align="right"><strong>分类排序：</strong></td>
						<td><form:input path="categoryPosition" class="input-medium" /><span
							class="help-inline"><font color="red">*</font></span></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>卖点描述:</strong></td>
						<td colspan="3"><form:textarea path="sellPoint" cols="100"
								rows="3" class="input-xxlarge" /></td>
					</tr>
					<tr>
						<td colspan="4">
							<div class="line"></div>
						</td>
					</tr>
					<tr>
						<td align="right"><strong>商品描述:</strong></td>
						<td colspan="3"><form:textarea path="itemDesc" cols="100"
								rows="3" class="input-xxlarge" /></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		<fieldset title="图片信息">
			<legend>图片信息</legend>
			<table>
				<tbody>
					<tr>
						<td valign="top" align="right"><strong>商品主图:</strong></td>
						<td>
						<sys:ckfinder 
								input="mainPicUrl" type="images" uploadPath="/photo"
								selectMultiple="false" maxWidth="100" maxHeight="100" />
								<form:hidden id="mainPicUrl" path="mainPicUrl" readonly="true" 
								htmlEscape="false" maxlength="255" class="input-xlarge" />
<%-- 								<img alt="商品主图" src="${mallItemInfo.mainPicUrl }" height="150px;"> --%>
						</td>
					</tr>
					<tr>
						<td valign="top" align="right"><strong>商品细节图:</strong></td>
						<td><form:hidden id="introPicUrl" path="introPicUrl"
								htmlEscape="false" maxlength="255" class="input-xlarge" /> <sys:ckfinder
								input="introPicUrl" type="images" uploadPath="/photo"
								selectMultiple="true" maxWidth="100" maxHeight="100" /></td>
					</tr>
					<tr>
						<td valign="top" align="right"><strong>商品详情图:</strong></td>
						<td><form:hidden id="artMainPicUrl" path="artMainPicUrl"
								htmlEscape="false" maxlength="255" class="input-xlarge" /> <sys:ckfinder
								input="artMainPicUrl" type="images" uploadPath="/photo"
								selectMultiple="true" maxWidth="100" maxHeight="100" /></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		<fieldset title="产品信息">
			<legend>产品信息</legend>
			<div>
				<table id="btnTable">
					<tr>
						<c:choose>
							<c:when test="${readFlag }">
								
							</c:when>
							<c:otherwise>
								<td>&nbsp;&nbsp;</td>
								<td><a href="javascript:addSingle()"
									class="btn btn-info marR10" id="newPlan">增加单品</a></td>
								<td>&nbsp;&nbsp;</td>
								<td><a href="javascript:addSuite()"
									class="btn btn-info marR10" type="submit">增加套餐</a></td>
								<td>&nbsp;&nbsp;</td>
								<td><a href="javascript:addVirtual()"
									class="btn btn-info marR10" type="submit">增加虚拟</a></td>
								<td>&nbsp;&nbsp;</td>
								<td><a href="javascript:deleteGood()"
									class="btn btn-info marR10" id="copyPlan">删除</a></td>
								<td>&nbsp;&nbsp;</td>
								<td><a href="javascript:countPriceBtn()"
									class="btn btn-info marR10" id="copyPlan" title="比重法计算产品体验价">计算价格</a></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</table>
			</div>
			<div class="table-responsive">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th align="center"><input type="checkbox" id="GoodCheckAll"
								onclick="CheckAllGood()"></th>
							<th align="center">产品SKU</th>
							<th align="center">产品名称</th>
							<th align="center">产品类型</th>
							<th align="center">体验价</th>
							<th align="center">市场价</th>
							<th align="center">数量</th>
						</tr>
					</thead>
					<tbody id="goodTbody">
					</tbody>
				</table>
			</div>
			<div class="form-actions">
				<!-- 				<input id="btnSubmit" class="btn btn-primary" type="submit" -->
				<!-- 					value="保 存"/>&nbsp; -->
				<input class="btn btn-primary" type="button" onclick="saveBtn()"
					value="保 存" />&nbsp;
			</div>
		</fieldset>
	</form:form>
</body>
</html>