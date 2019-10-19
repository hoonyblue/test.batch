package my.job.cmn;

/**
 * 배치에서 사용하는 상수 정의
 * @author ihj
 *
 */
public class BatchSyncConstant
{
	/* 배치 사용자 ID */
	public static final String BATCH_USER_ID = "batch";

	/* 삭제 상태 문자열 ( DEL_YN ) */
	public static final String STATUS_DELETE = "Y";

	/* 사용 상태 문자열 ( DEL_YN ) */
	public static final String STATUS_NOT_DELETE = "N";

	/* 가상서버 구분 코드 */
	public static final String VM_TYPE_NORMAL = "01"; // 일반 가상서버
	public static final String VM_TYPE_NETWORK = "02"; // 네트워크 가상서버

	/* 디스크 구분코드  */
	public static final String VR_DSK_CL_CD_UNUSE = "01"; // 미할당
	public static final String VR_DSK_CL_CD_VMDSK = "02"; // 가상서버 디스크
	public static final String VR_DSK_CL_CD_SNAPSHTDSK = "03"; // 스냅샷디스크
	public static final String VR_DSK_CL_CD_TMPLATE = "04"; // 템플릿디스크(vmware)

	/* 스냅샷 구분코드 */
	public static final String SNAPSHT_CL_CD_VMSNAPSHT = "01"; // 가상서버 스냅샷
	public static final String SNAPSHT_CL_CD_DSKSNAPSHT = "02"; // 가상디스크 스냅샷
	/* IP대역 용도 코드  */
	public static final String IP_BND_CL_CD_WEB = "01"; // WEB
	public static final String IP_BND_CL_CD_WAS = "02"; // WAS
	public static final String IP_BND_CL_CD_DB = "03"; // DB
	public static final String IP_BND_CL_CD_WEB_BACKUP = "05"; // WEB 백업
	public static final String IP_BND_CL_CD_WAS_BACKUP = "06"; // WAS 백업
	public static final String IP_BND_CL_CD_DB_BACKUP = "07"; // DB 백업
	public static final String IP_BND_CL_CD_WEB_HB = "09"; // WEB HEARBEAT
	public static final String IP_BND_CL_CD_WAS_HB = "10"; // WAS HEARBEAT
	public static final String IP_BND_CL_CD_DB_HB = "11"; // DB HEARBEAT


	/* 네트워크 용도 코드  */
	public static final String NIC_CL_CD_SERVICE = "01"; // 서비스
	public static final String NIC_CL_CD_BACKUP = "02"; // 백업
	public static final String NIC_CL_CD_HB = "03"; // HEARTBEAT


	/* NTOPS 지역구분 코드  */
	public static final String NTOPS_REGION_DJ = "01"; // 대전
	public static final String NTOPS_REGION_GJ = "02"; // 광주
	public static final String NTOPS_REGION_DG = "04"; // 대구

	/* 자원요청유형코드 */
	public static final String REQ_TYPE_CODE_VM_CRE = "01"; // 가상서버 생성
	public static final String REQ_TYPE_CODE_VM_DEL = "02"; // 가상서버 삭제
	public static final String REQ_TYPE_CODE_VM_MOD = "03"; // 가상서버 변경
	public static final String REQ_TYPE_CODE_SLB = "04"; // SLB 설정
	public static final String REQ_TYPE_CODE_PM_CRE = "05"; // 물리서버 추가
	public static final String REQ_TYPE_CODE_PM_DEL = "06"; // 물리서버 삭제
	public static final String REQ_TYPE_CODE_CLSTR_CRE = "07"; // 클러스터 추가
	public static final String REQ_TYPE_CODE_CLSTR_DEL = "08"; // 클러스터 삭제
	public static final String REQ_TYPE_CODE_VM_STRG_ADD = "09"; // 가상서버 디스크추가
	public static final String REQ_TYPE_CODE_VM_STRG_DEL = "10"; // 가상서버 디스크삭제

	/* 가상서버 변경 구분코드 */
	public static final String VM_CHG_CODE_VM_CHG_SPEC = "01"; // 가상서버 변경구분코드 - 스펙변경
	public static final String VM_CHG_CODE_VM_STRG_ADD = "02"; // 가상서버 변경구분코드 - 스토리지추가
	public static final String VM_CHG_CODE_VM_DEL = "03"; // 가상서버 변경구분코드 - 가상서버 삭제


	public enum BatchActionTypeEnum {
		INSERT("I"),
		UPDATE("U"),
		DELETE("D")
		;

		String actionType;
		BatchActionTypeEnum(String actionType) {
			this.actionType = actionType;
		}
		public String cd() {
			return this.actionType;
		}

		public static BatchActionTypeEnum of(String actionType) {
			for (BatchActionTypeEnum v : BatchActionTypeEnum.values()) {
				if (v.cd().contentEquals(actionType)) {
					return v;
				}
			}
			return null;
		}
	}
}
