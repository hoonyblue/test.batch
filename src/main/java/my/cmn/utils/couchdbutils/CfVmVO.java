package my.cmn.utils.couchdbutils;

import java.math.BigDecimal;

public class CfVmVO {

	private String regionId;    /* 센터 */
	private String zoneId;      /* 존 */
	private String netId;       /* 망 */
	private String rsrcPoolId;  /* 자원풀ID */
	private String clstrUuid;   /* 클러스터UUID */
	private String pmUuid;      /* 물리서버UUID */
	private String vmUuid;      /* 가상서버UUID */
	private String vmNm;        /* 가상서버명 */
	private String ipAddr;      /* IP주소 */
	private String hstNm;       /* 호스트명 */
	private int cpuVcore;       /* CPU_VCORE */
	private double memCapa;        /* 메모리전체량 */
	private int storage;        /* 스토리지용량, 2017-07-26 [openstack] */

	private String creDt;       /* 생성일자 */
	private String strtupDttm;  /* 기동일시 */
	private String vmClCd;      /* 가상서버구분 */
	private String netwkTyClCd; /* 네트워크유형구분 */
	private String netwkNsSid;  /* 네트워크NSSID */
	private String netwkNfSid;  /* 네트워크NFSID */
	private String statCd;      /* 상태 */

	private String osTyCd;      /* OS타입코드 */
	private String tmplatUuid;      /* 템플릿UUID */

	private String minCpuVcore;	/* 최대CPU(vCore) */
	private String maxCpuVcore; /* 최소CPU(vCore)*/
	private String minMemCapa;  /* 최대할당메모리용량(GB)*/
	private String maxMemCapa;  /* 최소할당메모리용량(GB)*/
	private String vcpus;		/* CPU 엔타이틀먼트_기본값*/
	private String maxVcpus;		/* CPU 엔타이틀먼트_최대값*/
	private String minVcpus;		/* CPU 엔타이틀먼트_최소값*/

	private BigDecimal vmSeq;
	private BigDecimal pmSeq;
	private BigDecimal clstrSeq;
	private BigDecimal tmplatSeq;

	private String colctDttm;

	private boolean hasRnNetwk;

	private String cores;       /* CPU CORES */
	private String sockets;     /* CPU SOCKETS */

	public boolean isHasRnNetwk()
	{
		return hasRnNetwk;
	}

	public void setHasRnNetwk(boolean hasRnNetwk)
	{
		this.hasRnNetwk = hasRnNetwk;
	}

	public BigDecimal getVmSeq()
	{
		return vmSeq;
	}
	public void setVmSeq(BigDecimal vmSeq)
	{
		this.vmSeq = vmSeq;
	}
	public BigDecimal getPmSeq()
	{
		return pmSeq;
	}
	public void setPmSeq(BigDecimal pmSeq)
	{
		this.pmSeq = pmSeq;
	}
	public BigDecimal getClstrSeq()
	{
		return clstrSeq;
	}
	public void setClstrSeq(BigDecimal clstrSeq)
	{
		this.clstrSeq = clstrSeq;
	}
	public BigDecimal getTmplatSeq()
	{
		return tmplatSeq;
	}
	public void setTmplatSeq(BigDecimal tmplatSeq)
	{
		this.tmplatSeq = tmplatSeq;
	}
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
	public String getClstrUuid() {
		return clstrUuid;
	}
	public void setClstrUuid(String clstrUuid) {
		this.clstrUuid = clstrUuid;
	}
	public String getPmUuid() {
		return pmUuid;
	}
	public void setPmUuid(String pmUuid) {
		this.pmUuid = pmUuid;
	}
	public String getVmUuid() {
		return vmUuid;
	}
	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}
	public String getVmNm() {
		return vmNm;
	}
	public void setVmNm(String vmNm) {
		this.vmNm = vmNm;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getHstNm() {
		return hstNm;
	}
	public void setHstNm(String hstNm) {
		this.hstNm = hstNm;
	}
	public int getCpuVcore() {
		return cpuVcore;
	}
	public void setCpuVcore(int cpuVcore) {
		this.cpuVcore = cpuVcore;
	}
	public double getMemCapa() {
		return memCapa;
	}
	public void setMemCapa(double memCapa) {
		this.memCapa = memCapa;
	}
	public String getCreDt() {
		return creDt;
	}
	public void setCreDt(String creDt) {
		this.creDt = creDt;
	}
	public String getStrtupDttm() {
		return strtupDttm;
	}
	public void setStrtupDttm(String strtupDttm) {
		this.strtupDttm = strtupDttm;
	}
	public String getVmClCd() {
		return vmClCd;
	}
	public String getOsTyCd()
	{
		return osTyCd;
	}
	public void setOsTyCd(String osTyCd)
	{
		this.osTyCd = osTyCd;
	}
	public String getTmplatUuid()
	{
		return tmplatUuid;
	}
	public void setTmplatUuid(String tmplatUuid)
	{
		this.tmplatUuid = tmplatUuid;
	}
	public void setVmClCd(String vmClCd) {
		this.vmClCd = vmClCd;
	}
	public String getNetwkTyClCd() {
		return netwkTyClCd;
	}
	public void setNetwkTyClCd(String netwkTyClCd) {
		this.netwkTyClCd = netwkTyClCd;
	}
	public String getNetwkNsSid() {
		return netwkNsSid;
	}
	public void setNetwkNsSid(String netwkNsSid) {
		this.netwkNsSid = netwkNsSid;
	}
	public String getNetwkNfSid() {
		return netwkNfSid;
	}
	public void setNetwkNfSid(String netwkNfSid) {
		this.netwkNfSid = netwkNfSid;
	}
	public String getStatCd() {
		return statCd;
	}
	public void setStatCd(String statCd) {
		this.statCd = statCd;
	}

	public String getMinCpuVcore()
	{
		return minCpuVcore;
	}

	public void setMinCpuVcore(String minCpuVcore)
	{
		this.minCpuVcore = minCpuVcore;
	}

	public String getMaxCpuVcore()
	{
		return maxCpuVcore;
	}

	public void setMaxCpuVcore(String maxCpuVcore)
	{
		this.maxCpuVcore = maxCpuVcore;
	}

	public String getMinMemCapa()
	{
		return minMemCapa;
	}

	public void setMinMemCapa(String minMemCapa)
	{
		this.minMemCapa = minMemCapa;
	}

	public String getMaxMemCapa()
	{
		return maxMemCapa;
	}

	public void setMaxMemCapa(String maxMemCapa)
	{
		this.maxMemCapa = maxMemCapa;
	}

	public String getVcpus()
	{
		return vcpus;
	}

	public void setVcpus(String vcpus)
	{
		this.vcpus = vcpus;
	}

	public String getMaxVcpus()
	{
		return maxVcpus;
	}

	public void setMaxVcpus(String maxVcpus)
	{
		this.maxVcpus = maxVcpus;
	}

	public String getMinVcpus()
	{
		return minVcpus;
	}

	public void setMinVcpus(String minVcpus)
	{
		this.minVcpus = minVcpus;
	}

	/**
	 * @return the cores
	 */
	public String getCores()
	{
		return cores;
	}

	/**
	 * @param cores the cores to set
	 */
	public void setCores(String cores)
	{
		this.cores = cores;
	}

	/**
	 * @return the sockets
	 */
	public String getSockets()
	{
		return sockets;
	}

	/**
	 * @param sockets the sockets to set
	 */
	public void setSockets(String sockets)
	{
		this.sockets = sockets;
	}

	/**
	 * @return the colctDttm
	 */
	public String getColctDttm() {
		return colctDttm;
	}

	/**
	 * @param colctDttm the colctDttm to set
	 */
	public void setColctDttm(String colctDttm) {
		this.colctDttm = colctDttm;
	}

	/**
	 * @return the storage
	 */
	public int getStorage() { // 2017-07-26 [openstack]
		return storage;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(int storage) { // 2017-07-26 [openstack]
		this.storage = storage;
	}

}
