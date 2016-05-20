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
// 			var fileid = $("#fileid").val();
// 			if(!(fileid==""||fileid==null)){
// 				layer.alert("请先清空数据后再导入!");
// 				return;
// 			}
			$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
				bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
		});
		$("#inputForm").validate({
			rules : {
				webchatPublicNo : {
					required : true
				},
				merchantName : {
					required : true
				},
				merchantNo : {
					required : true
				},
				provider : {
					required : true
				}
			},
			messages : {
				webchatPublicNo : {
					required : "请选择公众号"
				},
				merchantName : {
					required : "请选择公众号"
				},
				merchantNo : {
					required : "请选择公众号"
				},
				provider : {
					required : "请选择公众号"
				}
			},
			submitHandler: function(form){
				
				var merchantNo = $("#merchantNo").val();
				if(merchantNo==null||merchantNo==""){
					layer.alert("请选择公众号！");
					return;
				}
				var table = $("#details").find("tr");
				if(table.length==1){
					layer.alert("请导入红包明细数据!");
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
	function changeMerchant(e){
		debugger;
		var changeValue = e.value;
		var merchantList = $("#merchantList").val();
		var merchantData = JSON.parse(merchantList);
		if(merchantData.length>0){
			for(var i=0;i<merchantData.length;i++){
				if(merchantData[i].webchatPublicNo==changeValue){
					$("#merchantName").val(merchantData[i].merchantName);
					$("#merchantNo").val(merchantData[i].merchantNo);
					$("#provider").val(merchantData[i].provider);
					//导入文件传参
					$("#merchantNoCode").val(merchantData[i].merchantNo);
					$("#merchantNameCode").val(merchantData[i].merchantName);
					$("#providerCode").val(merchantData[i].provider);
					$("#webchatPublicNoCode").val(merchantData[i].webchatPublicNo);
				}
			}
		}
	}
	function getMoreData(){
		var pageNo = $("#pageNo").val();
		var fileid = $("#fileid").val();
		var url = "${ctx}/hb/mallWxRedpacketApply/getMoreDetail";
		$.ajax({
			type : "POST",
			async : true,
			url : url,
			data : {
				pageNo : pageNo,
				fileid : fileid
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
								str += "<tr>";
								str += "<td>"+(index+1)+"</td>";
								str += "<td>"+details[i].merchantOrderNo+"</td>";
								str += "<td>"+details[i].redpacketNo+"</td>";
								str += "<td>"+details[i].referrerNo+"</td>";
								str += "<td>"+details[i].referrerName+"</td>";
								str += "<td>"+details[i].userOpenid+"</td>";
								str += "<td>"+details[i].redpacketAmountYuan+"</td>";
								str += "<td>"+details[i].redpacketAmountFen+"</td>";
								str += "<td>"+details[i].sendTotalPeople+"</td>";
								str += "<td>"+details[i].redpacketGreetings+"</td>";
								str += "<td>"+details[i].hdName+"</td>";
								str += "<td>"+details[i].marker+"</td>";
								str += "</tr>";
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
	}
	
	function addTr(tab, trHtml){
	    var $tbody =$("#"+tab+" tbody");
	    $tbody.append(trHtml);
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
			$("#redpacketTotalCount").val("");
			$("#redpacketTotalAmount").val("");
		}
		var fileid = $("#fileid").val();
		var applyid = $("#applyid").val();
		if(fileid!=""&&fileid!=null){
			var fileid = $("#fileid").val();
			var url = "${ctx}/hb/mallWxRedpacketApply/clearFileData";
			$.ajax({
				type : "POST",
				async : true,
				url : url,
				data : {
					fileid : fileid,
					applyid : applyid
				}, 
				success : function(data){
					var flag = data.flag;
					if(flag=="success"){
						
					}else{
						layer.alert("清空数据失败,请联系管理员!");
					}
				}
			});
		}
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
		<li><a href="#" onclick="reback();">红包申请列表</a></li>
		<li class="active"><a href="${ctx}/hb/mallWxRedpacketApply/form?id=${mallWxRedpacketApply.id}">红包申请<shiro:hasPermission
			name="hb:mallWxRedpacketApply:edit">${not empty mallWxRedpacketApply.id?'修改':'添加'}</shiro:hasPermission>
		<shiro:lacksPermission name="hb:mallWxRedpacketApply:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/hb/mallWxRedpacketApply/import" enctype="multipart/form-data" class="form-search"
			method="post" style="padding-left: 20px; text-align: center;" onsubmit="loading('正在导入，请稍等...');"
			modelAttribute="mallWxRedpacketApply"><br/> 
			<input id="uploadFile" name="file" type="file" style="width: 330px;" /><br/> <br/> 
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   " onclick="return existFile()"/>
			<script type="text/javascript">
				function existFile(){
					debugger;
					var str = $("input[type='file']")[1].value;
					if(str == null||str == ""){
						layer.alert("请选择导入文件！");						
						return false;
					}else{
						return true;
					}
				}
			</script>
			<a href="${ctx}/hb/mallWxRedpacketApply/import/template">下载模板</a>
			<input type="hidden" id="merchantNoCode" name="merchantNoCode" value='${mallWxRedpacketApply.merchantNo }'/>
			<input type="hidden" id="webchatPublicNoCode" name="webchatPublicNoCode" value='${mallWxRedpacketApply.webchatPublicNo }'/>
			<input type="hidden" id="merchantNameCode" name="merchantNameCode" value='${mallWxRedpacketApply.merchantName }'/>
			<input type="hidden" id="providerCode" name="providerCode" value='${mallWxRedpacketApply.provider }'/>
			<input type="hidden" id="applyidCode" name="applyidCode" value='${mallWxRedpacketApply.applyid }'/>
			<input type="hidden" id="fileidCode" name="fileidCode" value='${mallWxRedpacketApply.fileid }'/>
			
		</form>
	</div>
	<form:form id="inputForm" modelAttribute="mallWxRedpacketApply"
		action="${ctx}/hb/mallWxRedpacketApply/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="fileid" />
		<form:hidden path="applystatus" />
		<input type="hidden" id="pageType" value='${type }'>
		<input type="hidden" id="merchantList" name="merchantList"
			value='${mallWxRedpacketApply.merchantJson }'>
		<input type="hidden" id="pageNo" name="pageNo"
			value='${pageNo }'>
		<input type="hidden" id="oldFileId" value="${mallWxRedpacketApply.fileid }">
		<sys:message content="${message}" />
		<sys:message content="${failureMsg}" />
		<sys:message content="${falseMsg}" />
		<fieldset title="基本信息">
			<legend>基本信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="8%"><strong>申请单号</strong></td>
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
			</table>
		</fieldset>
		<fieldset title="红包发放商户信息">
			<legend>红包发放商户信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="8%"><strong>公众账号</strong></td>
					<td width="17%">
						
						
							<form:select path="webchatPublicNo" 
								onchange="changeMerchant(this);" style="width:177px;">
								<form:option value="" label="--请选择--"></form:option>
								<form:options items="${merchantList}" itemLabel="webchatPublicNo"
									itemValue="webchatPublicNo" />
							</form:select>
							
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
					<td width="8%"><strong>总个数</strong></td>
					<td width="17%"><form:input path="redpacketTotalCount"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>总金额</strong></td>
					<td width="17%"><form:input path="redpacketTotalAmount"
							class="input-medium" readonly="true" /></td>
					<td width="8%"><strong>数据导入</strong></td>
					<td colspan="3">
						<input id="btnImport" class="btn btn-primary" type="button" value="导入数据" /> 
						<input class="btn" type="button" value="清空数据" onclick="clearData();"/>
<!-- 						<input class="btn" type="button" value="加载数据" onclick="getMoreData();"/> -->
					</td>
				</tr>
			</table>
			<div id="scroll_head" class="tableContainer" style="width: 100%; height: 350px; overflow: scroll;" onscroll="gundong();">
				<table id="details" >
					<thead class="fixedHeader">
						<tr>
						<th>序号</th>
						<th>商户订单号</th>
						<th>红包编号</th>
						<th>推荐人编号</th>
						<th>推荐人姓名</th>
						<th>用户微信openID</th>
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
								<td>${status.index+1 }</td>
								<td>${detail.merchantOrderNo }</td>
								<td>${detail.redpacketNo }</td>
								<td>${detail.referrerNo }</td>
								<td>${detail.referrerName }</td>
								<td>${detail.userOpenid }</td>
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
		<shiro:hasPermission name="hb:mallWxRedpacketApply:aduit">
		<c:if test="${mallWxRedpacketApply.applystatus eq 3 }">
		<fieldset title="审核信息">
			<legend>审核信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="8%"><strong>审核结果</strong></td>
					<td align="left">
						<form:radiobuttons path="checkValue" items="${fns:getDictList('check_accross')}" 
							itemLabel="label" itemValue="value" htmlEscape="false"/>
					</td>
				</tr>
				<tr>
					<td width="8%"><strong>审核结果</strong></td>
					<td align="left">
						<form:textarea path="backReason" cols="100" rows="3" 
							placeholder="请填写审核意见" class="input-xxlarge"/>
					</td>
				</tr>
			</table>
		</fieldset>
		</c:if>
		<c:if test="${mallWxRedpacketApply.applystatus eq 4 }">
		<fieldset title="审核信息">
			<legend>审核信息</legend>
			<table style="width: 95%;">
				<tr>
					<td width="8%"><strong>审核结果</strong></td>
					<td align="left">
						<form:radiobuttons path="checkValue" items="${fns:getDictList('check_accross')}" 
							itemLabel="label" itemValue="value" htmlEscape="false" disabled="true"/>
					</td>
				</tr>
				<tr>
					<td width="8%"><strong>审核结果</strong></td>
					<td align="left">
						<form:textarea path="backReason" cols="100" rows="3" 
							placeholder="请填写审核意见" class="input-xxlarge" readonly="true"/>
					</td>
				</tr>
			</table>
		</fieldset>
		</c:if>
		</shiro:hasPermission>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="saveClick()"/>&nbsp;
			<input id="btnCancel" class="btn btn-primary" type="submit" value="提 交" onclick="submitClick()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="rebackClearData()"/>
		</div>
		<script type="text/javascript">
			function saveClick(){
				$("#applystatus").val(1);
			}
			function submitClick(){
				$("#applystatus").val(2);
			}
			function reback(){
				debugger;
				var applyid = $("#applyid").val();
				if(applyid==""||applyid==null){
					clearData();
				}
				location.href='${ctx}/hb/mallWxRedpacketApply/';
			}
			function rebackClearData(){
				debugger;
				var applyid = $("#applyid").val();
				if(applyid==""||applyid==null){
					clearData();
					location.href='${ctx}/hb/mallWxRedpacketApply/';
					return;
				}
				var fileid = $("#fileid").val();
				if(fileid!=""&&fileid!=null){
					var url = "${ctx}/hb/mallWxRedpacketApply/rebackClearData";
					$.ajax({
						type : "POST",
						async : true,
						url : url,
						data : {
							applyid : applyid,
							fileid : fileid
						}, 
						success : function(data){
							debugger;
							location.href='${ctx}/hb/mallWxRedpacketApply/';
						}
					});
				}
			}
		</script>
	</form:form>
</body>
</html>