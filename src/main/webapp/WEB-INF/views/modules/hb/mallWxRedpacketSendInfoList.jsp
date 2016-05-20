<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>红包申请管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript"
src="${pageContext.request.contextPath}/static/layer-v1.9.2/layer/layer.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function(){
			top.$.jBox.confirm("确认要导出红包发放信息？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx}/hb/mallWxRedpacketSendInfo/export");
					$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
	});
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/hb/mallWxRedpacketSendInfo");
		$("#searchForm").submit();
    	return false;
    }
	function resetBtn(){
		$("#openid").val("");
		$("#appid").val("");
		$("#referrerNo").val("");
		$("#applyid").val("");
		$("#mchBillno").val("");
		$("#detailId").val("");
		$("#st").val("");
		$("#et").val("");
		$("#status").find("option[value='']").attr("selected",true);
		$(".select2-chosen").html("--请选择--");
	}
	var MaskUtil = (function(){  
	    var $mask,$maskMsg;  
	    var defMsg = '正在处理，请稍待。。。';  
	    function init(){  
	        if(!$mask){  
	            $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");  
	        }  
	        if(!$maskMsg){  
	            $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>").appendTo("body").css({'font-size':'12px'});
	        }  
	        $mask.css({width:"100%",height:$(document).height()});  
	        $maskMsg.css({  
	            left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2,
	        });   
	    }  

	    return {  
	        mask:function(msg){  
	            init();  
	            $mask.show();  
	            $maskMsg.html(msg||defMsg).show();  
	        } ,unmask:function(){  
	            $mask.hide();  
	            $maskMsg.hide();  
	        }  
	    }  
	}()); 
	function doLoading(){
		var lDiv = document.getElementById("loading");
		if(lDiv.style.display=='none'){
		    lDiv.style.display='block';
		}

	}
</script>
<style type="text/css">
.datagrid-mask-msg {
  position: absolute;
  top: 50%;
  margin-top: -20px;

  padding: 12px 5px 10px 30px;

  width: auto;

  height: 16px;

  border-width: 2px;

  border-style: solid;

  display: none;

}
	
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/hb/mallWxRedpacketSendInfo">红包发放列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="mallWxRedpacketSendinfo" action="${ctx}/hb/mallWxRedpacketSendInfo" 
		method="post" class="breadcrumb form-search" onsubmit="doLoading();" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>红包状态</label>
				<form:select path="status" class="input-medium" style="width:177px;">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('hb_send_info_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
 			</li> 
			<li><label>用户openID</label>
				<form:input path="openid" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>发放appID</label>
				<form:input path="appid" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>推荐人编码</label>
				<form:input path="referrerNo" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>申请单号</label>
				<form:input path="applyid" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>商户订单编号</label>
				<form:input path="mchBillno" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label>微信红包编号</label>
				<form:input path="detailId" htmlEscape="false"  class="input-medium"/>
			</li>
			<li style="width: 540px;"><label>发放日期</label>
				<input id="st" name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallWxRedpacketSendinfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					~
				<input id="et" name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mallWxRedpacketSendinfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input class="btn" type="button" value="重置" onclick="resetBtn()" /></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<input id="btnExport" class="btn btn-primary" type="button" value="下载EXCEL" /></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<div id="loading" style="display:none;position: absolute;width: 100%" align="center">
	    <div class="loading-indicator" style="width: 100%;background-color: white;" align="center">
	    	<img src="${pageContext.request.contextPath}/static/images/loading.gif" >
	    	正在操作中，请稍候……
	    </div>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>申请单号</th>
				<th>内部红包编号</th>
				<th>微信红包编号</th>
				<th>商户订单号</th>
				<th>发放时间</th>
				<th>红包状态</th>
				<th>推荐人编码</th>
				<th>红包金额（元）</th>
				<th>用户微信openID</th>
				<th>发放appID</th>
				<th>退款时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sendInfo" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${sendInfo.applyid}</td>
				<td>${sendInfo.redpackno}</td>
				<td>${sendInfo.detailId}</td>
				<td>${sendInfo.mchBillno}</td>
				<td><fmt:formatDate value="${sendInfo.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${fns:getDictLabel(sendInfo.status, 'hb_send_info_status', '')}</td>
				<td>${sendInfo.referrerNo}</td>
				<td>${sendInfo.totalAmount}</td>
				<td>${sendInfo.openid}</td>
				<td>${sendInfo.appid}</td>
				<td><fmt:formatDate value="${sendInfo.refundTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>