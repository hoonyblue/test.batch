package my.service;

import org.quartz.SchedulerException;
import org.springframework.batch.core.configuration.DuplicateJobException;

/**
 * 스케줄러 서비스 인터페이스
 *
 * @author yhlee
 *
 */
public interface SchedulerService {


	/**
	 * job 추가
	 * @param jobClass 잡 클래스
	 * @param jobId 잡ID
	 * @param cronexpr 크론표현식
	 */
	String addJob(String jobClass, String jobId, String cronexpr) throws ClassNotFoundException, SchedulerException;

	/**
	 * job 삭제
	 * @param jobId 잡ID
	 */
	void removeJob(String jobId) throws SchedulerException;

	/**
	 * new job
	 * @param jobClass 잡클래스
	 * @return {@code JobDetail}
	 * /
	private JobDetail newJob(Class<? extends Job> jobClass);
	 */

	/**
	 * new trigger
	 * /
	private Trigger newTrigger(JobDetail jobDetail, String jobId, String cronexpr);
	 */

	/*
	@SuppressWarnings("unused")
	private void currentJob() throws SchedulerException;
	 */

	/**
	 * 테스트 모듈..
	 *  <p>- egov batch job + quartz(스케쥴)
	 */
	void test() throws SchedulerException;

	/**
	 * job scheduler 등록. (egov batch)
	 * @param jobId 잡ID
	 * @param cronExpr <expression :  예) 0/10 * * * * ?  => 10초마다.
	 */
	void addJobSchedule(String jobId, String cronExpr) throws SchedulerException;

	void addJobRegistry(String jobId) throws DuplicateJobException;

	void removeJobSchedule(String jobId) throws SchedulerException;

	/**
	 * trigger 갱신 (삭제 후, 재등록)
	 * @param jobId 잡ID
	 * @param cronExpr 크론표현식
	 */
	void updateJobSchedule(String jobId, String cronExpr) throws SchedulerException;
}
