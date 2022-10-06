/*
 * copyright 2016 NCIS Cloud Portal System
 * @description
 * <pre></pre>
 *
 * @filename RestHeaders.java
 *
 * @author jhchoi
 * @lastmodifier jhchoi
 * @created 2016. 10. 13.
 * @lastmodified 2016. 10. 13.

 *
 */
package my.cmn.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Rest API 헤더 VO
 * @author 최진호
 *
 */
public class RestHeaders {

	private String areaId;

	private String zoneId;

	private String networkId;

	private String managerId;

	private String authorization;

	private String seq;

	private String action;

	private String machineIp;

	private String objectname;

	private String reason;

	private String tenant;

	/**
	 * @return the areaId
	 */
	public String getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public void setRegionId(String areaId) {
		this.areaId = areaId;
	}

	/**
	 * @return the zoneId
	 */
	public String getZoneId() {
		return zoneId;
	}

	/**
	 * @param zoneId
	 *            the zoneId to set
	 */
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	/**
	 * @return the networkId
	 */
	public String getNetworkId() {
		return networkId;
	}

	/**
	 * @param networkId
	 *            the networkId to set
	 */
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public void setNetClCd(String networkId) {
		this.networkId = networkId;
	}

	/**
	 * @return the managerId
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId
	 *            the managerId to set
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public void setRsrcPoolId(String managerId) {
		this.managerId = managerId;
	}

	/**
	 * @return the authorization
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * @param authorization
	 *            the authorization to set
	 */
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 *            the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}


	/**
	 * @return the machineIp
	 */
	public String getMachineIp() {
		return machineIp;
	}

	/**
	 * @param machineIp the machineIp to set
	 */
	public void setMachineIp(String machineIp) {
		this.machineIp = machineIp;
	}

	/**
	 * @return the objectname
	 */
	public String getObjectname() {
		return objectname;
	}

	/**
	 * @param objectname
	 *            the objectname to set
	 */
	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the tenant
	 */
	public String getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	@Override
    public String toString() {

    	return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
