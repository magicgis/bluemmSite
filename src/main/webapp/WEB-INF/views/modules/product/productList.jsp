<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js">
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
/* 			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/product/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			}); */
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/product/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/product/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/product/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/product/productList">单品产品管理</a></li>
		<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/suite/SuiteList">套餐产品管理</a></li></shiro:hasPermission>
	</ul>
	<%-- <form:form id="searchForm" modelAttribute="user" action="${ctx}/product/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
		</ul>
	</form:form> --%>
	<div class="btn-toolbar breadcrumb">
			<div class="btn-group">
				<a class="btn" href="javascript:add();"><i class="icon-file"></i>新建</a>
				<a class="btn" href="javascript:edit();"><i class="icon-edit"></i>修改</a>
				<a class="btn" href="javascript:dele();" onclick="return confirmx('确认要删除该测试吗？', this.href)"><i class="icon-remove"></i>删除</a>
			</div>
	</div>

	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>check</th><th>单品SKU</th><th>单品名称</th><th >条码</th><th >箱规</th><th>市场价</th><th>创建人</th><th>创建时间</th><th>修改人</th><th>修改时间</th><%--<th>角色</th> --%><shiro:hasPermission name="sys:user:edit"></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="product">
			<tr>
				<td><input type="checkbox" value="${product.productId}" name="productId" ></td>
				<td>${product.productSku}</td>
				<td>${product.productName}</td>
				<td>${product.barCode}</td>
				<td>${product.carton}</td>
				<td>${product.marketPrice}</td>
 				<td>${product.createByWho}</td>
				<td><fmt:formatDate value="${product.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${product.modifyBy}</td>
				<td><fmt:formatDate value="${product.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	
	<script type="text/javascript">
		function edit() {
			debugger;
			var cbox=document.getElementsByName("productId");
			check_val = [];
			for(k in cbox){
				if(cbox[k].checked)
					check_val.push(cbox[k].value);
			}
			var productId=check_val[0];
			var objarray=check_val.length;
			if (objarray > 1 ) {
				layer.msg("只能选中一个");
				return;
			}
			pageii = layer.open({
				title : "编辑",
				type : 2,
				area : [ "600px", "60%" ],
				content :'${ctx}/product/productEdit?productId='+productId
			});
		}
		function add() {
			debugger;
  			layer.open({
				title : "新增",
				type : 2,
				area : [ "700px", "60%" ],
				content :  '${ctx}/product/productAdd' 
			}); 

		}
		function dele() {
			var cbox = grid_pro.getSelectedCheckbox();
			if (cbox == "") {
				layer.msg("请选择删除项！！");
				return;
			}
			layer.confirm('是否删除？', function(index) {
				var url = rootPath + '/item/delProductUI.shtml';
				var s = CommnUtil.ajax(url, {
					ids : cbox.join(",")
				}, "json");
				if (s == "success") {
					layer.msg('删除成功');
					grid_pro.loadData();
				} else {
					layer.msg('删除失败');
				}
			});
		}
	</script>	
</body>
</html>