<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>红包申请管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
		$("#status").find("option[value='${mallWxRedpacketApply.merchantNo }']").attr("selected",true);
		$(".select2-chosen").html("${mallWxRedpacketApply.webchatPublicNo }");
		$("#merchantNoCode").val('${mallWxRedpacketApply.merchantNo }');
		$("#applyidCode").val('${mallWxRedpacketApply.applyid }');
		
		var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
        var nScrollTop = 0;   //滚动到的当前位置
     	var nDivHight = $("#scroll_head").height();

       	$("#scroll_head").scroll(function(){
			nScrollHight = $(this)[0].scrollHeight;
			nScrollTop = $(this)[0].scrollTop;
			if(nScrollTop + nDivHight >= nScrollHight){
				getMoreData();
			}
        });
		$("#btnImport").click(function(){
			if($("#merchantNoCode").val()==null||$("#merchantNoCode").val()==""){
				layer.alert("请先选择公众账号!");
				return;
			}
			$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
				bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
		});
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
	function changeMerchant(e){
		var changeValue = e.value;
		var merchantList = $("#merchantList").val();
		var merchantData = JSON.parse(merchantList);
		if(merchantData.length>0){
			for(var i=0;i<merchantData.length;i++){
				if(merchantData[i].merchantNo==changeValue){
					$("#merchantName").val(merchantData[i].merchantName);
					$("#merchantNo").val(merchantData[i].merchantNo);
					$("#provider").val(merchantData[i].provider);
					//导入文件传参
					$("#merchantNoCode").val(merchantData[i].merchantNo);
					$("#merchantNameCode").val(merchantData[i].merchantName);
					$("#providerCode").val(merchantData[i].provider);
					$("#webchatPublicNoCode").val(changeValue);
				}
			}
		}
	}
	function getMoreData(){
		var sendType = $("#sendType").val();
		var pageNo = $("#pageNo").val();
		var applyid = $("#applyid").val();
		var url = "${ctx}/hb/mallWxRedpacketApply/getMoreDetail";
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				pageNo : pageNo,
				applyid : applyid,
				type : "send"
			}, 
			success : function(data){
				if(data!=null&&data!=""){
					var flag = data.flag;
					if(flag=="false"){
						layer.alert("数据已经全部展示");
						return;
					}else if(flag=="success"){
						var pageNo = data.pageNo;
						$("#pageNo").val(pageNo);
						var details = data.json;
						if(details.length>0){
							var index = $("#details tbody tr").length;
							var str = "";
							for(var i=0;i<details.length;i++){
								if(sendType=="query"){
									str += "<tr>";
									str += "<td>"+details[i].merchantOrderNo+"</td>";
									str += "<td>"+details[i].redpacketNo+"</td>";
									str += "<td>"+details[i].referrerNo+"</td>";
									str += "<td>"+details[i].referrerName+"</td>";
									str += "<td>"+details[i].userOpenid+"</td>";
									str += "<td>"+details[i].redpacketStatus+"</td>";
									str += "<td>"+details[i].sendTime+"</td>";
									str += "<td>"+details[i].redpacketAmountYuan+"</td>";
									str += "<td>"+details[i].redpacketAmountFen+"</td>";
									str += "<td>"+details[i].sendTotalPeople+"</td>";
									str += "<td>"+details[i].redpacketGreetings+"</td>";
									str += "<td>"+details[i].hdName+"</td>";
									str += "<td>"+details[i].marker+"</td>";
									str += "</tr>";
								}else{
									str += "<tr>";
									str += "<td><input type='checkbox' name='detailCheck' value='"+details[i].redpacketNo+"'></td>";
									str += "<td>"+details[i].merchantOrderNo+"</td>";
									str += "<td>"+details[i].redpacketNo+"</td>";
									str += "<td>"+details[i].referrerNo+"</td>";
									str += "<td>"+details[i].referrerName+"</td>";
									str += "<td>"+details[i].userOpenid+"</td>";
									str += "<td>"+details[i].redpacketStatus+"</td>";
									str += "<td>"+details[i].sendTime+"</td>";
									str += "<td>"+details[i].redpacketAmountYuan+"</td>";
									str += "<td>"+details[i].redpacketAmountFen+"</td>";
									str += "<td>"+details[i].sendTotalPeople+"</td>";
									str += "<td>"+details[i].redpacketGreetings+"</td>";
									str += "<td>"+details[i].hdName+"</td>";
									str += "<td>"+details[i].marker+"</td>";
									str += "</tr>";
								}
								index++;
							}
							addTr("details",str);
						}else{
							layer.alert("数据已经全部展示！");
						}
					}
				}else{
					layer.alert("数据出错，请系统联系管理员!");
				}
			}
		});
		function addTr(tab, trHtml){
		    var $tbody =$("#"+tab+" tbody");
		    $tbody.append(trHtml);
		}
	}
	function clearData(){
		var trs = $("#details tbody tr");
		if(trs.length>0){
			for(var i=0;i<trs.length;i++){
				var navigatorName = "Microsoft Internet Explorer"; 
				if(navigator.appName == navigatorName){    
					trs[i].removeNode(true);
				}else{    
					trs[i].remove();
				}
			}
			$("#pageNo").val(0);
		}
	}
	function send(){
		debugger;
		var checkBox = $("input[name='detailCheck']:checked");
		if(checkBox.length>0){
			var checkValue = "";
			for(var i=0;i<checkBox.length;i++){
				var status = checkBox[i].parentNode.parentNode.cells[6].innerHTML;
				if(status=="已发放"){
					layer.alert("商户订单号 "+checkBox[i].parentNode.parentNode.cells[1].innerHTML+" 已发放！");
					return;
				}
				if(i==checkBox.length-1){
					checkValue += checkBox[i].value;
				}else{
					checkValue += checkBox[i].value+",";
				}
			}
		}
		var applyid = $("#applyid").val();
		var merchantNo = $("#merchantNo").val();
		var url = "${ctx}/hb/mallWxRedpacketApply/send";
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				checkValue : checkValue,
				applyid : applyid,
				mchid : merchantNo
			}, 
			success : function(data){
				debugger;
				if(data!=null&&data!=""){
					var flag = data.flag;
					if(flag == "success"){
						layer.alert("红包发送成功！");
					}else{
						var failMsg = data.failMsg;
						layer.alert(failMsg);
					}
				}else{
					layer.alert("数据出错，请系统联系管理员!");
				}
			}
		});
	}
	//刷新红包明细数据
	function refreshDetail(){
		var applyid = $("#applyid").val();
		var url="${ctx}/hb/mallWxRedpacketApply/form?id="+applyid+"&type=send";
		location.href=url;
	}
</script>	
<style type="text/css"> 
div.tableContainer { 
clear: both; 
border-collapse: collapse; 
border: 2px solid grey; 
height: expression(document.body.clientHeight*0.3); 
overflow: auto; 
width: 95%; 
} 
div.tableContainer table { 
float: left; 
width: 100%; 
border-collapse: collapse; 
empty-cells: hide; 
} 
thead.fixedHeader tr { 
position: relative; 
top: expression(document.getElementById("tableContainer").scrollTop); 
} 
thead.fixedHeader th { 
background: #39F; 
border-left: 1px solid grey; 
border-right: 1px solid grey; 
border-top: 1px solid grey; 
border-bottom: 1px solid grey; 
font-weight: normal; 
padding: 4px 3px; 
text-align: center; 
} 
tbody.scrollContent td, tbody.scrollContent tr td { 
border-bottom: 1px solid grey; 
border-left: 1px solid grey; 
border-right: 1px solid grey; 
border-top: 1px solid grey; 
padding: 2px 3px; 
} 
.fixedtd{ 
position: relative; 
z-index: 1; 
background: #009933; 
text-align: center; 
} 
</style> 	
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/hb/mallWxRedpacketApply/list?type=send">红包发放列表</a></li>
		<li class="active"><a href="#">红包发放</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="mallWxRedpacketApply"
		action="${ctx}/hb/mallWxRedpacketApply/aduitSave" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="applyid" />
		<form:hidden path="fileid" />
		<form:hidden path="applystatus" />
		<input type="hidden" id="sendType"  value='${sendType }'>
		<input type="hidden" id="merchantList" name="merchantList"
			value='${mallWxRedpacketApply.merchantJson }'>
		<input type="hidden" id="pageNo" name="pageNo"
			value='${pageNo }'>
		<sys:message content="${message}" />
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="8%"><strong>申请单编码</strong></td>
					<td width="17%"><form:input path="applyid"
							class="input-medium" readonly="true" placeholder="系统自动生成"/></td>
					<td width="8%"><strong>申请人编号</strong></td>
					<td width="17%"><form:input path="applyercode"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>申请人</strong></td>
					<td width="17%"><form:input path="applyername"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>申请日期</strong></td>
					<td width="17%"><input name="applydate" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${mallWxRedpacketApply.applydate}" pattern="yyyy-MM-dd" />" /></td>
				</tr>
				<tr>
					<td><strong>发放人编码</strong></td>
					<td><form:input path="sendCode"
							class="input-medium" readonly="true" /></td>
					<td><strong>发放人</strong></td>
					<td><form:input path="sendName"
							class="input-medium" readonly="true" /></td>
					<td colspan="4"></td>
				</tr>
			</table>
		</fieldset>
		<fieldset title="红包发放商户信息">
			<legend>红包发放商户信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="8%"><strong>公众账号</strong></td>
					<td width="17%">
						<form:input path="webchatPublicNo"
							class="input-medium" readonly="true" />
							<span class="help-inline"></td>
					<td width="8%"><strong>商户名称</strong></td>
					<td width="17%"><form:input path="merchantName"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>商户号</strong></td>
					<td width="17%"><form:input path="merchantNo"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>提供方名称</strong></td>
					<td width="17%"><form:input path="provider"
							class="input-medium" readonly="true" /></td>
				</tr>
			</table>
		</fieldset>
		<fieldset title="红包明细">
			<legend>红包明细</legend>
			<table style="width: 95%;">
				<tr>
					<td colspan="4" width="50%">
						<c:if test="${sendType ne 'query' }">
						<a class="btn btn-danger" href="javascript:send();" ><i
							class="icon-share-alt"></i>发放</a>
						<a class="btn btn-info" href="javascript:refreshDetail();" ><i
							class="icon-refresh"></i>刷新</a>
						</c:if>
					</td>
					<td width="8%"><strong>总个数</strong></td>
					<td width="17%"><form:input path="redpacketTotalCount"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>总金额</strong></td>
					<td width="17%"><form:input path="redpacketTotalAmount"
							class="input-medium" readonly="true" /></td>
				</tr>
			</table>
			<div id="scroll_head" class="tableContainer" style="width: 100%; height: 350px; overflow: scroll;" onscroll="gundong();">
				<script type="text/javascript">
					function selectAll(e){
						var a = $("input[name='detailCheck']");
						if(e.checked){
							for(var i = 0;i<a.length;i++){
								if(a[i].type == "checkbox") 
									a[i].checked = true;
							}
						}else{
							for(var i = 0;i<a.length;i++){
								if(a[i].type == "checkbox") 
									a[i].checked = false;
							}
						}
					}	
				</script>
				<table id="details" >
					<thead class="fixedHeader">
						<tr>
							<c:if test="${sendType ne 'query' }"><th>
								<input type="checkbox" onclick="javascripr:selectAll(this);"/>
							</th></c:if>
							<th>商户订单号</th>
							<th>红包编号</th>
							<th>推荐人编号</th>
							<th>推荐人姓名</th>
							<th>用户微信openID</th>
							<th>发放状态</th>
							<th>发放时间</th>
							<th>红包金额（元）</th>
							<th>红包金额（分）</th>
							<th>发放总人数</th>
							<th>祝福语</th>
							<th>活动名称</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody class="scrollContent">
						<c:forEach items="${page.list}" var="detail"
							varStatus="status">
							<tr>
								<c:if test="${sendType ne 'query' }">
								<td><input type="checkbox" name="detailCheck" value="${detail.redpacketNo }"> </td>
								</c:if>
								<td>${detail.merchantOrderNo }</td>
								<td>${detail.redpacketNo }</td>
								<td>${detail.referrerNo }</td>
								<td>${detail.referrerName }</td>
								<td>${detail.userOpenid }</td>
								<td>${fns:getDictLabel(detail.redpacketStatus, 'hb_detail_status', '')}</td>
								<td><fmt:formatDate value="${detail.sendTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${detail.redpacketAmountYuan }</td>
								<td>${detail.redpacketAmountFen }</td>
								<td>${detail.sendTotalPeople }</td>
								<td>${detail.redpacketGreetings }</td>
								<td>${detail.hdName }</td>
								<td>${detail.marker }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</fieldset>
		<div class="form-actions">
<!-- 			<input id="btnCancel" class="btn btn-primary" type="submit" value="保 存" onclick="return submitClick()"/>&nbsp; -->
			<input id="btnCancel" class="btn" type="button" value="取 消" onclick="reback()"/>
		</div>
		<script type="text/javascript">
			function submitClick(){
				var checkValue = $("input[name='checkValue']:checked").val();
				if(checkValue==null||checkValue==""){
					layer.alert("请选择审核结果");
					return false;
				}else if(checkValue==1){
					var backReason = $("#backReason").val();
					if(backReason==null||backReason==""){
						layer.alert("不通过请填写审核意见！");
						return false;
					}else{
						$("#applystatus").val(4);
					}
				}else{
					$("#applystatus").val(3);
				}
			}
			function reback(){
				location.href='${ctx}/hb/mallWxRedpacketApply/list?type=send';
			}
		</script>
	</form:form>
</body>
</html>