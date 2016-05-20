<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function rebackValue(){
		debugger;
		var chooseItem = $('input:checkbox[name="itemIdRadio"]:checked');
		if(chooseItem.length==0){
			layer.alert("请选择适用商品!");
			return;
		}
		var chooseValue = "";
		for(var i=0;i<chooseItem.length;i++){
			chooseValue+=chooseItem[i].value+",";
		}
		var url = '${ctx}/recommend/mallItemRecommend/getItemById';
        $.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				itemId : chooseValue
			}, 
			success : function(data){
				debugger;
				parent.$('#itemData').val(data);
				closePage();
			}
		});
	}
	function closePage(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引 
		parent.layer.close(index);
	}
	function setPage(){
		$("#pageNo").val(1);
		return true;
	}
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="mallItemInfo"
		action="${ctx}/coupon/mallPromotionCouponInfo/itemList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>商品SKU</label> <form:input path="itemSku"
					htmlEscape="false" class="input-medium" /></li>
			<li><label>商品名称</label> <form:input path="itemName"
					htmlEscape="false" class="input-medium" /></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" onclick="setPage();"/><input class="btn btn-success"
				type="button" value="选择" onclick="rebackValue();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th class="sort-column item_sku">商品SKU</th>
				<th class="sort-column item_name">商品名称</th>
			</tr>
		</thead>
		<tbody id="itemTbody">
			<c:forEach items="${page.list}" var="item">
				<tr>
					<td><input type="checkbox" name="itemIdRadio" value="${item.itemId }"></td>
					<td>${item.itemSku }</td>
					<td>${item.itemName }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>