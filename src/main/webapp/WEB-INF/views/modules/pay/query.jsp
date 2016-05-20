<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1" /> 
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
    
    <title>订单查询</title>
<style type="text/css"> 
	body { 
font: normal 11px auto "Trebuchet MS ", Verdana, Arial, Helvetica, sans-serif; 
color: #4f6b72; 
background: #E6EAE9; 
} 

a { 
color: #c75f3e; 
} 

#mytable { 
width: 1000px; 
padding: 0; 
margin: 0; 
} 

caption { 
padding: 0 0 5px 0; 
width: 1000px; 
font: italic 11px "Trebuchet MS ", Verdana, Arial, Helvetica, sans-serif; 
text-align: right; 
} 

th { 
font: bold 11px "Trebuchet MS ", Verdana, Arial, Helvetica, sans-serif; 
color: #4f6b72; 
border-right: 1px solid #C1DAD7; 
border-bottom: 1px solid #C1DAD7; 
border-top: 1px solid #C1DAD7; 
letter-spacing: 2px; 
text-transform: uppercase; 
text-align: left; 
padding: 6px 6px 6px 12px; 
background: #CAE8EA  no-repeat; 
} 

th.nobg { 
border-top: 0; 
border-left: 0; 
border-right: 1px solid #C1DAD7; 
background: none; 
} 

td { 
border-right: 1px solid #C1DAD7; 
border-bottom: 1px solid #C1DAD7; 
background: #fff; 
font-size:11px; 
padding: 6px 6px 6px 12px; 
color: #4f6b72; 
} 


td.alt { 
background: #F5FAFA; 
color: #797268; 
} 

th.spec { 
border-left: 1px solid #C1DAD7; 
border-top: 0; 
background: #fff no-repeat; 
font: bold 10px "Trebuchet MS ", Verdana, Arial, Helvetica, sans-serif; 
} 

th.specalt { 
border-left: 1px solid #C1DAD7; 
border-top: 0; 
background: #f5fafa no-repeat; 
font: bold 10px "Trebuchet MS ", Verdana, Arial, Helvetica, sans-serif; 
color: #797268; 
} 
/*---------for IE 5.x bug*/ 
html>body td{ font-size:11px;} 
body,td,th { 
font-family: 宋体, Arial; 
font-size: 12px; 
}
</style> 
</head>
<body>  
	<form action="#" method="post">
        <div style="margin-left:2%;color:#f00">微信(支付宝)流水号和商户订单号选少填一个，流水号优先：</div><br/>
        <div style="margin-left:2%;">商户订单号：</div><br/>
        <input type="text" style="width:96%;height:35px;margin-left:2%;" name="out_trade_no" id="out_trade_no"/><br /><br />
        <div style="margin-left:2%;">微信(支付宝)流水号：</div><br/>
        <input type="text" style="width:96%;height:35px;margin-left:2%;" name="transaction_id" id="transaction_id" /><br /><br />
		<div align="center">
			<input  value="查询"
			 style="width:210px; height:50px; border-radius: 15px;background-color:#FE6714; border:0px #FE6714 solid; cursor: pointer;  color:white;  font-size:16px;" 
			 type="button" onclick="callpay()" />
		</div>
	</form>
	<table id="mytable" cellspacing="0 "> 
		
	</table> 
	<script type="text/javascript">
		function callpay(){
			var transaction_id=$("#transaction_id").val();
			var out_trade_no=$("#out_trade_no").val();
			if(transaction_id==""&&out_trade_no==""){
				alert("流水号和商户订单号至少填写一个!");
				return;
			}
			$("#mytable").html("查询中...");
			$.ajax({
				url:"${ctx}/modules/pay/list?transaction_id="+transaction_id +"&out_trade_no="+out_trade_no,
				type: "POST",
				contentType:  "application/json; charset=utf-8",
				//data:JSON.stringify({transaction_id:transaction_id,out_trade_no:out_trade_no}),
				success:function(re){
					var results=re;
					var str="<caption></caption><tr><th scope='col'>外部订单号</th><th scope='col'>支付平台</th><th scope='col '>支付时间</th>	<th scope='col '>流水号</th>"
						+"<th scope='col'>支付状态</th></tr> ";
					for(var i=0;i<results.length;i++){
						var row=results[i];
						str+="<tr>"+"<td>"+row.out_trade_no+"</td>"+"<td>"+row.pay_platform+"</td>"+"<td>"+row.trade_date+"</td>"
						+"<td>"+row.trade_no+"</td>"
						+"<td>"+row.stateStr + ":" + row.trade_state+"</td>"
						+"</tr>";
					}
					$("#mytable").html(str);
				}
			});
		}
	</script>
</body>
</html>