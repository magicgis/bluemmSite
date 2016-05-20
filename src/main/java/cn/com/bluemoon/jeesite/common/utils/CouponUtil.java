package cn.com.bluemoon.jeesite.common.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.bluemoon.jeesite.modules.coupon.dao.MallNoCodeDao;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallNoCode;


public class CouponUtil {
	
	@Autowired
	private MallNoCodeDao mallNoCodeDao;
	
	public MallNoCodeDao getMallNoCodeDao() {
		return mallNoCodeDao;
	}

	public void setMallNoCodeDao(MallNoCodeDao mallNoCodeDao) {
		this.mallNoCodeDao = mallNoCodeDao;
	}

	public static String getCouponCode(String commandType, String commandName, String typeCode, String codeNumStr ){
		CouponUtil couponUtil = new CouponUtil();
		String str = couponUtil.generateCouponCardCode(commandType, commandName, typeCode, codeNumStr);
		return str;	
	}
	
	/**
	 * 获取ID
	 * @param commandType	类型
	 * @param commandName	名称
	 * @param typeCode		
	 * @param codeNumStr
	 * @return
	 */
	public String generateCouponCardCode(String commandType, String commandName, 
			String typeCode, String format){
		StringBuffer cardId = new StringBuffer();
		try {
			cardId = new StringBuffer();
			String newNum = "";
			String increasseRule = DateUtil.getDateString(new Date(), format);
			MallNoCode mallNoCode = new MallNoCode();
			mallNoCode.setCommandType(commandType);
			mallNoCode.setIncreasseRule(increasseRule);
			List<MallNoCode> mallNoCodeList = mallNoCodeDao.findList(mallNoCode);
			if(mallNoCodeList.size()==1){
				mallNoCode = mallNoCodeList.get(0);
				String codeNum = mallNoCode.getCodeNum();
				int num = Integer.parseInt(codeNum);
				num++;
				newNum = String.valueOf(num);
				while (newNum.length()<5) {
					newNum = "0"+newNum;
				}
				mallNoCode.setCodeNum(newNum);
				mallNoCodeDao.update(mallNoCode);
			}else if(mallNoCodeList.size()==0){
				mallNoCode = new MallNoCode();
				int codeNum = 1;
				newNum = String.valueOf(codeNum);
				while (newNum.length()<5) {
					newNum = "0"+newNum;
				}
				mallNoCode.setCommandName(commandName);
				mallNoCode.setTypeCode(typeCode);
				mallNoCode.setCommandType(commandType);
				mallNoCode.setIncreasseRule(increasseRule);
				mallNoCode.setLastTime(new Date());
				mallNoCode.setCodeNum(newNum);
				mallNoCodeDao.insert(mallNoCode);
			}else{
				System.out.println("------------存在多条 : "+commandType + " " +increasseRule +" ------------------");
			}
			//拼接字符串
			cardId.append(commandType);
			cardId.append(increasseRule);
			cardId.append(newNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cardId.toString();
	}
	
	public static void main(String[] args) {
		String str = getCouponCode("MP", "测试门票编码", "test", "yyyyMMdd");
		System.out.println(str);
	}

}
