<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推荐商品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			var url ;
			var li;
			var urls ;
			url = '${mallItemRecommend.rePicUrl}';
			if(url!=null&&url!=""){
				$("#rePicUrlPreview").children().remove();
				li = "<li><img src=\""+url+"\" url=\""+url+"\" style=\"max-width:${empty maxWidth ? 200 : maxWidth}px;max-height:${empty maxHeight ? 200 : maxHeight}px;_height:${empty maxHeight ? 200 : maxHeight}px;border:0;padding:3px;\">";
				li += "<a href=\"javascript:\" onclick=\"rePicUrlDel(this);\">×</a></li>";
				$("#rePicUrlPreview").append(li);
			}
			$("#inputForm").validate({
				rules : {
					recommendType : {
						required : true
					},
					status : {
						required : true
					},
					position : {
						required : true,
						digits : true
					},
					onRecomDate : {
						required : true
					},
					offRecomDate : {
						required : true
					}
				},
				messages : {
					recommendType : {
						required : "请选择推荐商品类型"
					},
					status : {
						required : "请选择是否推荐"
					},
					position : {
						required : "请输入推荐商品排序号",
						digits : "请输入整数"
					},
					onRecomDate : {
						required : "请输入开始时间"
					},
					offRecomDate : {
						required : "请输入结束时间"
					}
				},
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
		function chooseItem(){
			$("#itemData").val("");
			var url = '${ctx}/recommend/mallItemRecommend/itemList';
			pageii = layer.open({
				title : "选择商品",
				type : 2,	
				area : [ "90%", "90%" ],
				content : url,
				end : function() {
					var itemData = $("#itemData").val();
					setItemData(itemData);
				}
			});
		}
		function setItemData(data) {
			debugger;
			if(data==""){
				return;
			}
			$("#itemTbody").empty();
// 			$("#itemId").val("");
// 			$("#itemSku").val("");
// 			$("#itemName").val("");
// 			$("#memberPrice").val("");
// 			$("#marketPrice").val("");
			var item = JSON.parse(data);
			var newStr = "<tr>";
			newStr += "<td id='"+item.itemId+"'>" + item.itemSku + "</td>";
			newStr += "<td>" + item.itemName + "</td>";
			newStr += "<td>" + (item.memberPrice/100).toFixed(2) + "</td>";
			newStr += "<td>" + (item.marketPrice/100).toFixed(2) + "</td>";
			newStr += "</tr>";
			$("#itemTbody").append(newStr);
			$("#itemId").val(item.itemId);
			$("#itemSku").val(item.itemSku);
			$("#itemName").val(item.itemName);
			$("#memberPrice").val((item.memberPrice/100).toFixed(2));
			$("#marketPrice").val((item.memberPrice/100).toFixed(2));
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/recommend/mallItemRecommend/">推荐商品列表</a></li>
		<li class="active"><a href="${ctx}/recommend/mallItemRecommend/form?id=${mallItemRecommend.id}">推荐商品<shiro:hasPermission name="recommend:mallItemRecommend:edit">${not empty mallItemRecommend.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="recommend:mallItemRecommend:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mallItemRecommend" action="${ctx}/recommend/mallItemRecommend/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>	
		<div>
			<form:hidden path="id"/>
			<form:hidden path="itemId" />
			<form:hidden path="itemSku" />
			<form:hidden path="itemName" />
			<form:hidden path="memberPrice" />
			<form:hidden path="marketPrice" />
		</div>	
		<fieldset title="基本信息">
			<table style="width: 90%;">
				<tr>
					<td align="right" width="100px"><strong>推荐类型：</strong></td>
					<td><form:select path="recommendType" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('recommend_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select> <span class="help-inline"><font color="red">*</font></span></td>
					<td align="right" width="100px"><strong>是否推荐：</strong></td>
					<td><form:select path="status" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select> <span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right" width="100px"><strong>排序：</strong></td>
					<td><form:input path="position" htmlEscape="false" maxlength="32" class="input-medium"/>
						<span class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td align="right"><strong>开始时间：</strong></td>
					<td><input id="onRecomDate" name="onRecomDate" readonly="readonly"
						class="input-medium Wdate" type="text"
						value="<fmt:formatDate value="${mallItemRecommend.onRecomDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" /> <span
						class="help-inline"><font color="red">*</font></span></td>
					<td align="right"><strong>结束时间：</strong></td>
					<td><input id="offRecomDate" name="offRecomDate" readonly="readonly"
						class="input-medium Wdate" type="text"
						value="<fmt:formatDate value="${mallItemRecommend.offRecomDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" /> <span
						class="help-inline"><font color="red">*</font></span></td>
				</tr>
				<tr>
					<td><label class="control-label"><strong>推荐图片：</strong></label>
					</td>
					<td colspan="3">		
						<sys:ckfinder input="rePicUrl" type="images" uploadPath="/photo" 
							selectMultiple="false" maxWidth="100" maxHeight="100" />
							<form:hidden id="rePicUrl" path="rePicUrl" readonly="true" 
							htmlEscape="false" maxlength="255" class="input-xlarge" />
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="table-responsive">
			<input id="btnChoose" class="btn btn-info" type="button" value="选 择" onclick="chooseItem()"/>
			<input type="hidden" id="itemData" name="itemData">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th align="center">产品SKU</th>
							<th align="center">产品名称</th>
							<th align="center">体验价</th>
							<th align="center">市场价</th>
						</tr>
					</thead>
					<tbody id="itemTbody">
						<tr>
							<td>${mallItemRecommend.itemSku}</td>
							<td>${mallItemRecommend.itemName}</td>
							<td>${mallItemRecommend.memberPrice/100}</td>
							<td>${mallItemRecommend.marketPrice/100}</td>
						</tr>
					</tbody>
				</table>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="recommend:mallItemRecommend:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>