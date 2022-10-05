package my.job.comp.macbnd.vo;

import my.job.cmn.CfBaseVO;
import java.math.BigDecimal;


public class CfMacBndVO extends CfBaseVO {

	private String regionId;      /* 센터 */
	private String zoneId;        /* 존 */
	private String netId;         /* 망 */
	private String rsrcPoolId;    /* 자원풀ID */
	private String macBndUuid;    /* MAC대역UUID */
	private String macBndNm;      /* MAC대역명 */
	private String dc;            /* 설명 */
	private String macStrtAddr;   /* MAC시작주소 */
	private String macEndAddr;    /* MAC종료주소 */
	private String creDt;         /* 생성일자 */

	private BigDecimal macBndSeq ;


	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getRsrcPoolId() {
		return rsrcPoolId;
	}
	public void setRsrcPoolId(String rsrcPoolId) {
		this.rsrcPoolId = rsrcPoolId;
	}
	public String getMacBndUuid() {
		return macBndUuid;
	}
	public void setMacBndUuid(String macBndUuid) {
		this.macBndUuid = macBndUuid;
	}
	public String getMacBndNm() {
		return macBndNm;
	}
	public void setMacBndNm(String macBndNm) {
		this.macBndNm = macBndNm;
	}
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getMacStrtAddr() {
		return macStrtAddr;
	}
	public void setMacStrtAddr(String macStrtAddr) {
		this.macStrtAddr = macStrtAddr;
	}
	public String getMacEndAddr() {
		return macEndAddr;
	}
	public void setMacEndAddr(String macEndAddr) {
		this.macEndAddr = macEndAddr;
	}
	public String getCreDt() {
		return creDt;
	}
	public void setCreDt(String creDt) {
		this.creDt = creDt;
	}
	public BigDecimal getMacBndSeq()
	{
		return macBndSeq;
	}
	public void setMacBndSeq(BigDecimal macBndSeq)
	{
		this.macBndSeq = macBndSeq;
	}

}
