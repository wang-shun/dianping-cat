package com.dianping.cat.report.alert.storage;

import com.dianping.cat.report.alert.spi.AlertType;

public class StorageSQLContactor extends AbstractStorageContactor {

	public static final String ID = AlertType.STORAGE_SQL.getName();

	@Override
	public String getId() {
		return ID;
	}
}
