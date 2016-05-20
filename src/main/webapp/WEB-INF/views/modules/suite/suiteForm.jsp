<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>套装管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/common/json2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/lib/jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/jquery-validation/1.11.1/jquery.validate.min.css"
	type="text/css">
	<script type="text/javascript">
		$(document).ready(function() {
		 	debugger;
			var url ;
			var li;
			url = '${mallItemSuite.picUrl}';
			if(url!=null&&url!=""){
				$("#picUrlPreview").children().remove();
				li = "<li><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
				li += "<a href=\"javascript:\" onclick=\"picUrlDel(this);\">×</a></li>";
				$("#picUrlPreview").append(li);
			}
			var list = '${mallItemSuiteDetails}';
			if(list!=null){
				setGoodData(list); 
			}
			$("#inputForm").validate({
				rules : {
					suiteSku : {
						required : true,
						minlength: 8,
						maxlength: 8,
						startWith: true,
						remote: {
							url: "${ctx}/suite/mallItemSuite/checkSuiteSku",   	//后台处理程序
							type: "post",               //数据发送方式
							dataType: "json",           //接受数据格式   
							data: {                     //要传递的数据
								suiteSku: function() {
									return $("#suiteSku").val();
								},
								suiteId: function() {
									return $("#suiteId").val();
								}
							}
						}
					},
					suiteName : {
						required : true
					},
					marketPriceFloat : {
						required : true,
						number : true,
						min : 0.001
					},
					picUrl : {
						required : true
					},
					type : {
						required : true
					}
				},
				messages : {
					suiteSku : {
						required : "请输入产品SKU",
						minlength: "产品SKU必须为8位字符",
						maxlength: "产品SKU必须为8位字符",
						startWith : "产品SKU第一位数字必须是8",
						remote : "重复SKU"
					},
					suiteName : {
						required : "请输入产品名称"
					},
					marketPriceFloat : {
						required : "请输入市场价",
						number : "请输入合法数字",
						min : "请输入大于0的数字"
					},
					picUrl : {
						required : "请添加图片"
					},
					type : {
						required : "请选择产品类型"
					}
				},
				submitHandler: function(form){
					debugger;
					var picUrl = $("#picUrl").val();
					if(picUrl==null||picUrl==""){
						layer.msg("请添加图片 ");
						return;
					}
					var detailFlag = getGoodData();
					if(!detailFlag){
						layer.msg("请添加单品信息 ");
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
		jQuery.validator.addMethod("startWith",
			function(value, element){
				var returnVal = false;  
				var firstStr = value.substr(0,1);
				if(firstStr=='8'){
					returnVal = true;
				}
                return returnVal; 
		},""); 

		function addSingle() {
			$("#goodDetail").val("");
			var url = '${ctx}/suite/mallItemSuite/single';
			pageii = layer.open({
				title : "添加单品",
				type : 2,
				area : [ "90%", "80%" ],
				content : url,
				end : function() {
					var mallSuiteDetails = $("#goodDetail").val();
					addGoodData(mallSuiteDetails);
				}
			});
		}
		function addGoodData(data) {
			debugger;
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
				newStr += "<td>" + (good.goodType == 'single' ? '单品' : '套餐')+ "</td>";
				newStr += "<td>" + good.marketPrice + "</td>";
				newStr += "<td><input type='text' class='input-mini' name='goodNum' value='"
						+ (good.num == 0 ? 1 : good.num) + "'></td>";
				newStr += "</tr>";
				$("#goodTbody").append(newStr);
			}
		}
		function setGoodData(data) {
			if(data==""){
				return;
			}
			var goodData = JSON.parse(data);
			for (var i = 0; i < goodData.length; i++) {
				var good = goodData[i];
				var newStr = "<tr>";
				newStr += "<td><input type='checkbox' name='goodCheck' value='"+good.sgoodId+"'></td>";
				newStr += "<td>" + good.sgoodSku + "</td>";
				newStr += "<td>" + good.itemName + "</td>";
				newStr += "<td>" + (good.sgoodType == 'single' ? '单品' : '套餐')+ "</td>";
				newStr += "<td>" + (good.marketPrice/100).toFixed(2) + "</td>";
				newStr += "<td><input type='text' class='input-mini' name='goodNum' value='"
						+ (good.num == 0 ? 1 : good.num) + "'></td>";
				newStr += "</tr>";
				$("#goodTbody").append(newStr);
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
				good.marketPrice = tdEle[4].innerHTML;
				good.num = tdEle[5].firstChild.value;
				goods[i] = good;
			}
			var json = JSON.stringify(goods);
			$("#goodDetail").val(json);
			if(trEle.length>0){
				return true;
			}else{
				return false;
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/suite/mallItemSuite/suiteList/">套餐列表</a></li>
		<shiro:hasPermission name="suite:MallItemSuite:edit">
		<li class="active"><a href="${ctx}/suite/mallItemSuite/form">套餐添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallItemSuite" action="${ctx}/suite/mallItemSuite/save" method="post" class="form-horizontal">
	<input type="hidden" id="goodDetail" name="goodDetail">
	<input type="hidden" id="suiteId" name="suiteId" value="${mallItemSuite.suiteId}">
		<table style="width: 90%;">
				<tr>
					<td align="right" width="100px"><strong>产品SKU：</strong></td>
					<td><form:input path="suiteSku" htmlEscape="false" maxlength="32" class="input-xlarge "/>
					<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right" width="100px"><strong>产品名称：</strong></td>
					<td><form:input path="suiteName" htmlEscape="false" maxlength="500" class="input-xlarge "/>
					 <span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right"><strong>产品类型：</strong></td>
					<td>
						<form:select path="type" class="input-xlarge ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('product_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select><span class="help-inline"><font color="red">*</font></span>
					</td>
				</tr>
				<tr>
					<td align="right" width="100px"><strong>市场价：</strong></td>
					<td><form:input path="marketPriceFloat" htmlEscape="false" class="input-xlarge " />
						<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td><label class="control-label"><strong>产品图片：</strong></label>
					</td>
					<td colspan="3">		
						<sys:ckfinder input="picUrl" type="images" uploadPath="/photo" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden path="picUrl" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" />
					</td>
				</tr>
		</table>
		<fieldset title="套餐内产品信息" style="width: 90%">
			<div>
				<table id="btnTable">
					<tr>
						<td>&nbsp;&nbsp;</td>
						<td><a href="javascript:addSingle()"
							class="btn btn-info marR10" id="newPlan">增加单品</a></td>
						<td>&nbsp;&nbsp;</td>
						<td><a href="javascript:deleteGood()"
							class="btn btn-info marR10" id="copyPlan">删除</a></td>
						<td>&nbsp;&nbsp;</td>
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
							<th align="center">货物类型</th>
							<th align="center">市场价</th>
							<th align="center">数量</th>
						</tr>
					</thead>
					<tbody id="goodTbody">
					</tbody>
				</table>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="suite:mallItemSuite:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</fieldset>
	</form:form>
</body>
</html>