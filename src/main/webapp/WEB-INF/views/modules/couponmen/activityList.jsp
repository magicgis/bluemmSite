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
		$("input:radio[name='activityIdRadio']").change(function() {
			var chooseValue = $('input:radio[name="activityIdRadio"]:checked').val();
			parent.$('#activityId').val(chooseValue);
		});
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function rebackValue(){
		debugger;
		var chooseValue = $('input:radio[name="activityIdRadio"]:checked').val();
		if(chooseValue==null||chooseValue==""){
			layer.alert("请选择一个推券活动！");
			return;
		}
		var chooseTr = $('input:radio[name="activityIdRadio"]:checked')[0].parentNode.parentNode;
		var tds = chooseTr.cells;
		parent.$('#activityId').val(chooseValue);
		parent.$('#activityCode').val(tds[2].innerHTML);
		parent.$('#activityName').val(tds[3].innerHTML);
		parent.$('#activitySName').val(tds[4].innerHTML);
		parent.$('#st').val(tds[5].innerHTML);
		parent.$('#et').val(tds[6].innerHTML);
		closePage();
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
	<div style="height: 330px;width: 100%;">
		<table id="activityTable" class="table table-striped table-bordered table-condensed" 
			style="padding-left: 10px;padding-right: 10px;">
			<thead>
				<tr>
					<th></th>
					<th class="sort-column item_sku">序号</th>
					<th class="sort-column item_name">活动编码</th>
					<th class="sort-column categoryId">活动名称</th>
					<th class="sort-column member_price">活动简称</th>
					<th class="sort-column market_price">开始时间</th>
					<th class="sort-column position">结束时间</th>
				</tr>
			</thead>
			<tbody id="itemTbody">
				<c:forEach items="${activityList}" var="activity" varStatus="status">
					<tr>
						<td><input type="radio" name="activityIdRadio" value="${activity.activityId }"><input type="hidden" value="${activity}"></td>
						<td>${status.index+1 }</td>
						<td>${activity.activityCode }</td>
						<td>${activity.activityName }</td>
						<td>${activity.activitySName}</td>
						<td><fmt:formatDate value="${activity.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><fmt:formatDate value="${activity.endTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="pagination">${page}</div>
<!-- 	<div class="form-actions" align="center" style="position: absolute;background-color: white;width: 100%;"> -->
<!-- 		<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="javascript:rebackValue();" value="确 定"/>&nbsp; -->
<!-- 		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="javascript:closePage();"/> -->
<!-- 	</div> -->
</body>
</html>