package my.cmn.utils.couchdbutils;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class CouchResponseMapper<T> implements Serializable {

	List<CouchResponseRowMapper<T>> rows;

	public List<CouchResponseRowMapper<T>> getRows() {
		return rows;
	}

	public void setRows(List<CouchResponseRowMapper<T>> rows) {
		this.rows = rows;
	}

}
