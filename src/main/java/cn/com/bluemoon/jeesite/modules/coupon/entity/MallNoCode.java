
package cn.com.bluemoon.jeesite.modules.coupon.entity;

import java.util.Date;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

public class MallNoCode extends DataEntity<MallNoCode> {
		
		private static final long serialVersionUID = 1L;
		
		private int codeId;
		private String commandName;
		private String typeCode;
		private String commandType;
		private String increasseRule;
		private Date lastTime;
		private String codeNum;
		private String remark;
		
		public int getCodeId() {
			return codeId;
		}
		public void setCodeId(int codeId) {
			this.codeId = codeId;
		}
		public String getCommandName() {
			return commandName;
		}
		public void setCommandName(String commandName) {
			this.commandName = commandName;
		}
		public String getTypeCode() {
			return typeCode;
		}
		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}
		public String getCommandType() {
			return commandType;
		}
		public void setCommandType(String commandType) {
			this.commandType = commandType;
		}
		public String getIncreasseRule() {
			return increasseRule;
		}
		public void setIncreasseRule(String increasseRule) {
			this.increasseRule = increasseRule;
		}
		public Date getLastTime() {
			return lastTime;
		}
		public void setLastTime(Date lastTime) {
			this.lastTime = lastTime;
		}
		public String getCodeNum() {
			return codeNum;
		}
		public void setCodeNum(String codeNum) {
			this.codeNum = codeNum;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
}
