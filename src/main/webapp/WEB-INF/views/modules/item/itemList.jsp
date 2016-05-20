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
	function setPage(){
		$("#pageNo").val(1);
		return true;
	}
	function checkAllItem() {
		var ischeck = $("#itemCheckAll").attr("checked");
		if (ischeck == undefined) {
			$(":checkbox").removeAttr("checked");
		} else {
			$(":checkbox").attr("checked", "checked");
		}
	}
	function add() {
		var url = '${ctx}/item/form';
		pageii = layer.open({
			title : "商品添加",
			type : 2,
			area : ["98%", "95%"],
			content : url,
			end : function(){
// 				location.reload();
				window.location.href=window.location.href;
			}
		});
	}
	function edit(){
		var checkItem = $("input[name='itemCheckBox']:checked");
		if(checkItem.length!=1){
			layer.alert("请选择一条数据");
			return;
		}
		var itemId = checkItem[0].defaultValue;
		var url = '${ctx}/item/form?itemId='+itemId;
		pageii = layer.open({
			title : "商品编辑",
			type : 2,
			area : ["98%", "95%"],
			content : url,
			end : function(){
// 				location.reload();
				window.location.href=window.location.href;
			}
		});
	}
	function off(){
		debugger;
		var str="";
		var checkItem = $("input[name='itemCheckBox']:checked");
        $("input[name='itemCheckBox']:checkbox").each(function(){ 
            if($(this).attr("checked")){
                str += $(this).val().trim()+","
            }
        });
        if(str==""){
        	layer.alert("请至少选择一个产品!");
        	return;
        }
		var url = '${ctx}/item/offItem?itemIds='+str;
		$.ajax({
            cache: true,
            type: "POST",
            url:url,
            success: function(msg) {
            	layer.alert(msg,1,function(){
            		window.location.href=window.location.href;
//             		location.reload();
            	});
            }
        });
	}
	function dele(){
		var str="";
        $("input[name='itemCheckBox']:checkbox").each(function(){ 
            if($(this).attr("checked")){
                str += $(this).val().trim()+","
            }
        });
        if(str==""){
        	layer.alert("请至少选择一个产品!");
        	return;
        }
		var url = '${ctx}/item/delItem?itemIds='+str;
		$.ajax({
            cache: true,
            type: "POST",
            url:url,
            success: function(msg) {
            	layer.alert(msg,1,function(){
            		window.location.href=window.location.href;
//             		location.reload();
            	});
            }
        });
	}
	function priceChange(){
		var memberPriceFloat = $("#memberPriceFloat").val();
		if(memberPriceFloat==null||memberPriceFloat==""){
			$("#memberPriceFloat").val(0);
		}else if(isNaN(memberPriceFloat)){
			layer.alert("体验价必须是数字");
			$("#memberPriceFloat").val(0);
		}else{}
	}
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="mallItemInfo"
		action="${ctx}/item/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>商品SKU</label> <form:input path="itemSku"
					htmlEscape="false" class="input-medium" /></li>
			<li><label>商品名称</label> <form:input path="itemName"
					htmlEscape="false" class="input-medium" /></li>
			<li><label>体验价</label> <form:input path="memberPriceFloat" onblur="priceChange()"
					htmlEscape="false" class="input-medium number" /></li>
			<li><label>商品类别</label><form:select path="categoryId"
					class="input-medium">
					<form:option value="" label="--请选择--" />
					<form:options items="${fns:getDictList('item_category')}"
						itemLabel="label" itemValue="value" />
				</form:select></li>
			<li><label>上架时间</label> <%-- 				<form:input path="onlineTime" htmlEscape="false" class="input-medium"/> --%>
				<input name="onlineTime" readonly="readonly" type="text"
				maxlength="20" class="input-medium Wdate" style="width: 163px;"
				value="<fmt:formatDate value="${mallItemInfo.onlineTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" /></li>
			<li><label>下架时间</label> <input name="offlineTime"
				readonly="readonly" type="text" maxlength="20"
				class="input-medium Wdate" style="width: 163px;"
				value="<fmt:formatDate value="${mallItemInfo.offlineTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" /></li>
			<li><label>商品状态</label> <form:select path="status"
					class="input-medium">
					<form:option value="" label="--请选择--" />
					<form:options items="${fns:getDictList('item_status')}"
						itemLabel="label" itemValue="value" />
				</form:select></li>
<!-- 			<li class="btns"><input class="btn" type="button" value="重置" -->
<!-- 				onclick="reset()" /></li> -->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" onclick="setPage();"/></li>
			<li class="clearfix"></li>
		</ul>
		<div class="btn-group">
			<a class="btn btn-primary" href="javascript:add();"><i
				class="icon-file"></i>新建</a> <a class="btn btn-info"
				href="javascript:edit();"><i class="icon-edit"></i>修改</a> <a
				class="btn btn-warning" href="javascript:off();"
				onclick="return confirmx('确认要下架该商品吗？', this.href)"><i
				class="icon-off"></i>下架</a> <a class="btn btn-danger"
				href="javascript:dele();"
				onclick="return confirmx('确认要删除该商品吗？', this.href)"><i
				class="icon-remove"></i>删除</a>
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="itemCheckAll" onclick="checkAllItem()"></th>
				<th class="sort-column item_sku">商品SKU</th>
				<th class="sort-column item_name">商品名称</th>
				<th class="sort-column categoryId">商品类别</th>
				<th class="sort-column member_price">体验价</th>
				<th class="sort-column market_price">市场价</th>
				<th class="sort-column online_time">上架时间</th>
				<th class="sort-column offline_time">下架时间</th>
				<th class="sort-column status">商品状态</th>
				<th class="sort-column position">全部排序</th>
				<th class="sort-column category_position">分类排序</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item">
				<tr>
					<td><input type="checkbox" name="itemCheckBox" value="${item.itemId }"></td>
					<td>${item.itemSku }</td>
					<td>${item.itemName }</td>
					<td>${fns:getDictLabel(item.categoryId, 'item_category', '')}</td>
					<td>${item.memberPrice/100 }</td>
					<td>${item.marketPrice/100 }</td>
					<td><fmt:formatDate value="${item.onlineTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.offlineTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${fns:getDictLabel(item.status, 'item_status', '')}</td>
					<td>${item.position }</td>
					<td>${item.categoryPosition }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>