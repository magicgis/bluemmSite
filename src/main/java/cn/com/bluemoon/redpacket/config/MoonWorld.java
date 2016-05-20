package cn.com.bluemoon.redpacket.config;
/**
 * 蓝月亮的世界 公众号  配置文件
 * @author kouzhiqiang
 *
 */
public class MoonWorld extends ConfigInterface {

	public MoonWorld() {
		///opt/puyuan/moonMall/primeton/eos7_2/onlinepay/weixin/
		super("wx547eeee78eb998f9", "1286224401", "w6t78ikl0oplyujnm6yhntg43eert56y"
				, "fbe1e8a7c708b8c71dc80959f7b1eb61","D:/1286224401.p12");
	}

	public MoonWorld(String aPPID, String mCHID, String kEY, String aPPSECRET,
			String sSLCERT_PATH) {
		super(aPPID, mCHID, kEY, aPPSECRET, sSLCERT_PATH);
	}

	
}
