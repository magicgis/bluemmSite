package cn.com.bluemoon.redpacket.config;

/**
 *  公众号：月亮小屋10021546的配置信息
 * @author kouzhiqiang
 *
 */
public class YLXW extends ConfigInterface {
	public YLXW() {
		///opt/puyuan/moonMall/primeton/eos7_2/onlinepay/weixin/
		super("wx2b5f8f135210796d", "10021546", "441558a4e7d4098286ab6bcfe18e5ef8"
				, "00983328f72e40ab5799848414938305","D:/10021546.p12");	
	}

	public YLXW(String aPPID, String mCHID, String kEY, String aPPSECRET,
			String sSLCERT_PATH) {
		super(aPPID, mCHID, kEY, aPPSECRET, sSLCERT_PATH);
	}

}
