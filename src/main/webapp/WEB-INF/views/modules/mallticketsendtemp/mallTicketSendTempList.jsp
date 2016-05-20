<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>派券管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出派券吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/mallticketsendtemp/mallTicketSendTemp/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/mallticketsendtemp/mallTicketSendTemp/");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/mallticketsendtemp/mallTicketSendTemp/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/">派券列表</a></li>
		<shiro:hasPermission name="mallticketsendtemp:mallTicketSendTemp:edit"><li><a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/form">派发门票添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mallTicketSendTemp" action="${ctx}/mallticketsendtemp/mallTicketSendTemp/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="16" class="input-medium" />
			</li>
			<li><label>是否发送：</label>
				<form:select path="isSend" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>发票类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
			<input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>收票人手机号</th>
				<th>发票数</th>
				<th>活动编号</th>
				<th>券简称</th>
				<th>发送是否成功</th>
				<th>发票类型</th>
				<shiro:hasPermission name="mallticketsendtemp:mallTicketSendTemp:edit1"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mallTicketSendTemp" varStatus="status">
			<tr>
				<td>
					${status.index+1 }
				</td>
				<td>
					${mallTicketSendTemp.mobile}
				</td>
				<td>
					${mallTicketSendTemp.sendNum}
				</td>
				<td>
					${mallTicketSendTemp.actId}
				</td>
				<td>
					${mallTicketSendTemp.couponSName}
				</td>
				<td>
					${fns:getDictLabel(mallTicketSendTemp.isSend, 'yes_no', '')}
				</td>
				<td>
					${mallTicketSendTemp.type}
				</td>
				<shiro:hasPermission name="mallticketsendtemp:mallTicketSendTemp:edit1"><td>
    				<a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/form?id=${mallTicketSendTemp.id}">修改</a>
					<a href="${ctx}/mallticketsendtemp/mallTicketSendTemp/delete?id=${mallTicketSendTemp.id}" onclick="return confirmx('确认要删除该派发门票吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>