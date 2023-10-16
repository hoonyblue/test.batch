package my.job.comp.macbnd.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import my.job.cmn.CfBaseVO;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;


@Builder
@Getter @Setter
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

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
