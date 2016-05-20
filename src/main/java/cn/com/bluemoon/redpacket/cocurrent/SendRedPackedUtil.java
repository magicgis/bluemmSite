package cn.com.bluemoon.redpacket.cocurrent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApplyDetails;
import cn.com.bluemoon.redpacket.RedPacketUtil;

import com.bluemoon.business.pay.util.MD5Util;
import com.weixin.bm.redPacket.model.CreateRedPacket;


/**
 * 批量发送红包
 * @author linyihao
 *
 */
public class SendRedPackedUtil {
	public static void main(String[] args) {
		new SendRedPackedUtil().send(null, null, null, null, null,null);
	} 
	
	
	//封装list
	public JSONObject sendObj(List list, String mchid){
		HashMap<String, Object> mchidInfor = RedPacketUtil.getWeixinNoInfor(mchid);
		if(mchidInfor.containsKey("returncode")){
			return null;
		}
		String  appid = mchidInfor.get("appid").toString();
		String  key = mchidInfor.get("key").toString();
		List<CreateRedPacket> redPackets = parseDataObjectArrayToList(list,appid,mchid);
		ArrayList<HashMap<String, String>> result=new ArrayList<HashMap<String,String>>();
		//String urlString="http://tmallapi.bluemoon.com.cn/moonMall-proxy/redpacket/send?client=pc&version=v3.1&cuid=5E-E0-C5-FA-2B-48&format=json&time=356345634563456365&sign=795b8d6c8d5e5f7440c9c42f6e1346bb";
		List<String> senddataList=new ArrayList<String>();
		for(int i = 0; i < redPackets.size(); i++){
			CreateRedPacket createRedPacket=redPackets.get(i);
			SortedMap<String, String> signParams = getSignParams(createRedPacket,appid,mchid);
			String sign = createSign(signParams,key);//获取签名
			signParams.put("sign", sign);
			senddataList.add(parseXML(signParams));
		}
		JSONObject para=new JSONObject();
		para.putAll(mchidInfor);
//		JSONArray datalist =JSONArray.fromObject(senddataList);
//		System.out.println(datalist.toString());
		//获取当前电脑IP
	
		JSONArray arr = new JSONArray();
		arr.addAll(senddataList);
		para.put("datas", arr.toString());
		
		return para;
	}
	
	//封装obj
	public JSONObject searchObj(MallWxRedpacketApplyDetails detail, String mchid){

		HashMap<String, Object> mchidInfor = RedPacketUtil.getWeixinNoInfor(mchid);
		if(mchidInfor.containsKey("returncode")){
			return null;
		}
		String  appid = mchidInfor.get("appid").toString();
		String  key = mchidInfor.get("key").toString();
		
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid",appid);
		signParams.put("mch_id",mchid);
		signParams.put("nonce_str", getRandomStringByLength(32));
		signParams.put("mch_billno", detail.getMerchantOrderNo());//商户发放红包的商户订单号
		signParams.put("bill_type", "MCHT");
		

		signParams.put("mch_id", "122269960");
		
		String sign = createSign(signParams,key);//获取签名//获取签名
		signParams.put("sign", sign);
		JSONObject para=new JSONObject();
		para.putAll(mchidInfor);
		para.put("datas", parseXML(signParams));
		return para;
	}
	
	
	public void send(List list,String appid,String mchid,String key,String p12path,String urlString){
		List<CreateRedPacket> redPackets=parseDataObjectArrayToList(list,appid,mchid);
		ArrayList<HashMap<String, String>> result=new ArrayList<HashMap<String,String>>();
		//String urlString="http://tmallapi.bluemoon.com.cn/moonMall-proxy/redpacket/send?client=pc&version=v3.1&cuid=5E-E0-C5-FA-2B-48&format=json&time=356345634563456365&sign=795b8d6c8d5e5f7440c9c42f6e1346bb";
		List<String> senddataList=new ArrayList<String>();
		for(int i = 0; i < redPackets.size(); i++){
			CreateRedPacket createRedPacket=redPackets.get(i);
			SortedMap<String, String> signParams = getSignParams(createRedPacket,appid,mchid);
			String sign = createSign(signParams,key);//获取签名
			signParams.put("sign", sign);
			senddataList.add(parseXML(signParams));
		}
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(urlString);
		JSONObject para=new JSONObject();
		para.put("appid", appid); 
		para.put("mchid", mchid);
		para.put("key", key);
		para.put("p12path", p12path);
		para.put("datas", senddataList);
		//System.out.println(para.toString());
		StringEntity myEntity = new StringEntity(para.toString(), ContentType.APPLICATION_JSON);// 构造请求数据
		post.setEntity(myEntity);// 设置请求体
		String responseContent = null; // 响应内容
		CloseableHttpResponse response = null;
		try {
			
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			
			//System.err.println(responseContent);
			if (response.getStatusLine().getStatusCode() == 200) {
				
				JSONArray datalist =JSONArray.fromObject(responseContent);
				for(int i=0;i<datalist.size();i++){
					JSONObject row=datalist.getJSONObject(i);
					HashMap<String, String> row1=new HashMap<String, String>();
					row1.put("returncode",row.getString("returncode"));//是否成功
					row1.put("send_time", row.getString("send_time"));//发放时间
					row1.put("send_listid",row.getString("send_listid"));//流水号
					row1.put("mch_billno", row.getString("mch_billno"));//商户订单号
					result.add(row1);
				}
				handleResult(result);
				System.out.println("[红包@See SendRedPackedUtil]红包查询结束，结果共:"+result.size());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
		
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
		
	}
	/**
	 * 更新发放状态和红包发放明细的状态
	 * @param result
	 * @throws SQLException
	 */
	private void handleResult(ArrayList<HashMap<String, String>> result)  {
		PreparedStatement ppstmt=null;
		PreparedStatement ppstmt2=null;
		Connection connection=null;
		String sql="update `mall_wx_redpacket_sendinfo` set send_time=?,detail_id=?,status=? where mch_billno=?";
		String sql2="update `mall_wx_redpacket_apply_details` set redpacket_status=? ,send_listid=?,send_time=?  where merchant_order_no=?";
		int count=0;
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowStr=df.format(new Date());
		try{
			connection.setAutoCommit(false);
			ppstmt=connection.prepareStatement(sql);
			ppstmt2=connection.prepareStatement(sql2);
			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String>  temp= result.get(i);
				String returncode=temp.get("returncode");
				if("SUCCESS".equals(returncode)){//发放失败
					ppstmt.setString(1,nowStr);//发放日期
					ppstmt.setString(2,temp.get("send_listid"));//hb流水号
					ppstmt.setString(3,"1");//发送成功，status=1  发送中
					ppstmt.setString(4,temp.get("mch_billno"));//根据商户号更新
					//==========================apply_detail===================
					ppstmt2.setString(1, "2");//该红包已发放
					ppstmt2.setString(2,temp.get("send_listid"));//hb流水号
					ppstmt2.setString(3,nowStr);//发放日期
					ppstmt2.setString(4,temp.get("mch_billno"));//根据商户号更新
				}else{
					ppstmt.setString(1,nowStr);//发放日期
					ppstmt.setString(2,"1");//失败时 hb流水号都是1
					ppstmt.setString(3,"3");//发送失败，status=3  发送失败
					ppstmt.setString(4,temp.get("mch_billno"));//根据商户号更新
					//==========================apply_detail===================
					ppstmt2.setString(1, "1");//该红包未发放
					ppstmt2.setString(2,temp.get("send_listid"));//hb流水号
					ppstmt2.setString(3,nowStr);//发放日期
					ppstmt2.setString(4,temp.get("mch_billno"));//根据商户号更新
				}
				System.err.println(temp);
				ppstmt.addBatch();
				ppstmt2.addBatch();
				count++;
				if (count % 1000 == 0) {
					ppstmt.executeBatch();
					ppstmt.clearBatch();
					ppstmt2.executeBatch();
					ppstmt2.clearBatch();
					connection.commit();
				}
			}
			ppstmt.executeBatch();
			ppstmt.clearBatch();
			ppstmt2.executeBatch();
			ppstmt2.clearBatch();
			connection.commit();
			connection.setAutoCommit(true);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (ppstmt != null) {
				try {
					ppstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ppstmt2 != null) {
				try {
					ppstmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}
	 //输出XML
	  private  String parseXML(SortedMap<String, String> parameters) {
		  StringBuffer sb = new StringBuffer();
	      sb.append("<xml>");
	      Set es = parameters.entrySet();
	      Iterator it = es.iterator();
	      while(it.hasNext()) {
	    	  Map.Entry entry = (Map.Entry)it.next();
	    	  String k = (String)entry.getKey();
	    	  String v = (String)entry.getValue();
	    	  if(null != v && !"".equals(v) && !"appkey".equals(k)) {
	    		  sb.append("<" + k +">" + v + "</" + k + ">\n");
//	    		  sb.append("<" + k +"><![CDATA["+ v + "]]></" + k + ">\n");
	    	  }
	      }//
	      sb.append("</xml>");
	      System.out.println("---------------------------------------->");
	      System.out.println(sb.toString());
	      return sb.toString();
	}
	/**
	 * 取一定长度的随机字符串
	 */
	private  String getRandomStringByLength(int length){
		String base = "abcdefghigklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	private String createSign(SortedMap<String, String> packageParams,String key) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
//		System.out.println("md5 sb:" + sb);
		String sign = MD5Util.MD5Encode(sb.toString(),"UTF-8").toUpperCase();
//		System.out.println("packge签名:" + sign);
		return sign;
	}
	
	
	
	
	private List<CreateRedPacket> parseDataObjectArrayToList(List list,String appid,String mch_id) {
		List<CreateRedPacket> redPackets=new ArrayList<CreateRedPacket>();
		for(Object obj:list){
			MallWxRedpacketApplyDetails row = (MallWxRedpacketApplyDetails) obj;
			
			CreateRedPacket createredpacket=new CreateRedPacket();
			createredpacket.setMch_id(mch_id);
			createredpacket.setWxappid(appid);
			if("1286224401".equals(mch_id)){//蓝月亮的世界
				createredpacket.setNick_name("蓝月亮的世界");
				createredpacket.setSend_name("蓝月亮的世界");
			}else{
				createredpacket.setNick_name("月亮小屋");
				createredpacket.setSend_name("月亮小屋");
			}
			createredpacket.setMch_billno(row.getMerchantOrderNo());
			createredpacket.setRe_openid(row.getUserOpenid());
			createredpacket.setTotal_amount(Integer.valueOf(row.getRedpacketAmountFen()));
			createredpacket.setMin_value(Integer.valueOf(row.getRedpacketMinAmount()));
			createredpacket.setMax_value(Integer.valueOf(row.getRedpacketMaxAmount()));
			createredpacket.setTotal_num(Integer.valueOf(row.getSendTotalPeople()));
			createredpacket.setWishing(row.getRedpacketGreetings());
			createredpacket.setAct_name(row.getHdName());
			createredpacket.setRemark(row.getMarker());
			try {
				InetAddress address = InetAddress.getLocalHost();
	            String sIP = address.getHostAddress(); 
				createredpacket.setClient_ip(sIP);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} 
			
			redPackets.add(createredpacket);
		}
		return redPackets;
	}
	
	
	private CreateRedPacket parseDataObjectArrayToList2(MallWxRedpacketApplyDetails entity,String appid,String mch_id) {
		CreateRedPacket redPacket=new CreateRedPacket();
		redPacket.setMch_id(mch_id);
		redPacket.setWxappid(appid);
		if("1286224401".equals(mch_id)){//蓝月亮的世界
			redPacket.setNick_name("蓝月亮的世界");
			redPacket.setSend_name("蓝月亮的世界");
		}else{
			redPacket.setNick_name("月亮小屋");
			redPacket.setSend_name("月亮小屋");
		}
		redPacket.setMch_billno(entity.getMerchantOrderNo());
		redPacket.setRe_openid(entity.getUserOpenid());
		redPacket.setTotal_amount(Integer.valueOf(entity.getRedpacketAmountFen()));
		redPacket.setMin_value(Integer.valueOf(entity.getRedpacketMinAmount()));
		redPacket.setMax_value(Integer.valueOf(entity.getRedpacketMaxAmount()));
		redPacket.setTotal_num(Integer.valueOf(entity.getSendTotalPeople()));
		redPacket.setWishing(entity.getRedpacketGreetings());
		redPacket.setAct_name(entity.getHdName());
		redPacket.setRemark(entity.getMarker());
		redPacket.setClient_ip("172.16.2.129");
		return redPacket;
	}

	/**
	 * 设置红包签名参数
	 * @return
	 */
	private SortedMap<String, String> getSignParams(CreateRedPacket createRedPacket,String appid,String mchid){
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		/*signParams.put("wxappid", Configure.APPID);
		signParams.put("mch_id", Configure.MCHID);*/
		signParams.put("wxappid",appid);
		signParams.put("mch_id",mchid);
		signParams.put("nonce_str", getRandomStringByLength(32));
		signParams.put("mch_billno", createRedPacket.getMch_billno());
		signParams.put("nick_name", createRedPacket.getNick_name());
		signParams.put("send_name", createRedPacket.getSend_name());
		signParams.put("re_openid", createRedPacket.getRe_openid());
		signParams.put("total_amount", String.valueOf(createRedPacket.getTotal_amount()));
		signParams.put("min_value", String.valueOf(createRedPacket.getMin_value()));
		signParams.put("max_value", String.valueOf(createRedPacket.getMax_value()));
		signParams.put("total_num", String.valueOf(createRedPacket.getTotal_num()));
		signParams.put("wishing", createRedPacket.getWishing());
		signParams.put("client_ip", createRedPacket.getClient_ip());
		signParams.put("act_name", createRedPacket.getAct_name());
		signParams.put("remark", createRedPacket.getRemark());
		
		return signParams;
	}
}
