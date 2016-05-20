<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>单品产品</title>
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
	function checkAllProduct() {
		var ischeck = $("#SingleCheckAll").attr("checked");
		if (ischeck == undefined) {
			$(":checkbox").removeAttr("checked");
		} else {
			$(":checkbox").attr("checked", "checked");
		}
	}
	function chooseProduct(){
		var str="";
        $("input[name='productCheckBox']:checkbox").each(function(){ 
            if($(this).attr("checked")){
                str += $(this).val().trim()+","
            }
        });
        if(str==""){
        	layer.alert("请至少选择一个产品!");
        	return;
        }
        var url = '${ctx}/item/getGoods';
        $.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				goodSkus : str
			}, 
			success : function(data){
				parent.$('#goodDetail').val(data);
				closeSingle();
			}
		});
	}
	function closeSingle(){
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
	<form:form id="searchForm" modelAttribute="mallProductInfo"
		action="${ctx}/item/single" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li><label>产品SKU</label> <form:input path="productSku"
					htmlEscape="false" class="input-medium" /></li>
			<li><label>产品名称</label> <form:input path="productName"
					htmlEscape="false" class="input-medium" /></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-info" type="submit" value="查询" onclick="setPage();"/></li>
			<li class="btns"><input class="btn btn-success" type="button" value="选择" onclick="chooseProduct()"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="SingleCheckAll" onclick="checkAllProduct()">
				<th>单品SKU</th>
				<th>单品名称</th>
				<th>市场价</th>
				<th>条码</th>
				<th>箱规</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="product">
			<tr>
				<td><input type="checkbox" name="productCheckBox" value="${product.productSku}"></td>
				<td><label class="productSku">${product.productSku}</label></td>
				<td>${product.productName}</td>
				<td>${product.marketPrice/100}</td>
				<td>${product.barCode}</td>
				<td>${product.carton}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<div class="pagination">${page}</div>
</body>
</html>