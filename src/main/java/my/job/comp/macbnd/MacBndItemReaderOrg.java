package my.job.comp.macbnd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import my.cmn.item.AbstractCouchItemReader;
import my.cmn.utils.couchdbutils.CouchResponseMapper;
import my.cmn.utils.couchdbutils.CouchResponseRowMapper;
import my.cmn.utils.couchdbutils.ViewResults;
import my.job.comp.macbnd.vo.CfMacBndVO;

@Component("macBndItemReaderOrg")
@StepScope
public class MacBndItemReaderOrg extends AbstractCouchItemReader<CfMacBndVO> {

	private Logger logger = LoggerFactory.getLogger(MacBndItemReaderOrg.class);

	@Override
	public List<CfMacBndVO> fetchData() throws IOException {
		List<CfMacBndVO> dataList = new ArrayList<>();

		logger.info("MacBnd.reader =================================================");

		ViewResults results = couchSessionConnector.getViewResults();
		CouchResponseMapper<CfMacBndVO> couchResponse = new Gson().fromJson(results.toString(),
				new TypeToken<CouchResponseMapper<CfMacBndVO>>() {
				}.getType());

		for (CouchResponseRowMapper<CfMacBndVO> row : couchResponse.getRows()) {
			dataList.add(row.getValue());
		}
		logger.info("MacBnd.reader data: {}", dataList.size());

		return dataList;
	}

	@Autowired
	public void setConfig(@Value("rsrcconf")String dbName, @Value("cfmacbnddoc")String docName,
			@Value("cfmacbndview")String viewName, @Value("cfmacbnd")String dbType) {
		logger.info("setConfig > couchSessionConnector: {}", couchSessionConnector);
		super.setDbName(dbName);
		super.setDocName(docName);
		super.setViewName(viewName);
		super.setDbType(dbType);
	}
	/**
	private void setConfig() {
		logger.info("1. couchSessionConnector: {}", couchSessionConnector);
		super.setDbName("rsrcconf");
		super.setDocName("cfmacbnddoc");
		super.setViewName("cfmacbndview");
		super.setDbType("cfmacbnd");
	}
	 */

	@PostConstruct
	public void configInfo() {
		logger.info("2. couchSessionConnector: {}", couchSessionConnector);
	}

}
