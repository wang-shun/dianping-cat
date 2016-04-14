package com.dianping.cat.report.page.app.display;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dianping.cat.configuration.mobile.entity.Item;
import com.dianping.cat.report.ErrorMsg;
import com.dianping.cat.report.graph.PieChart;
import com.dianping.cat.report.page.app.service.CrashLogService.FieldsInfo;

public class CrashLogDisplayInfo {

	private Collection<Item> m_appNames;

	private FieldsInfo m_fieldsInfo;

	private int m_totalCount;

	private List<ErrorMsg> m_errors;

	private Map<String, PieChart> m_distributions;

	private Map<String, PieChart> m_msgDistributions;

	public Map<String, PieChart> getDistributions() {
		return m_distributions;
	}

	public void setDistributions(Map<String, PieChart> distributions) {
		m_distributions = distributions;
	}

	public Map<String, PieChart> getMsgDistributions() {
		return m_msgDistributions;
	}

	public void setMsgDistributions(Map<String, PieChart> msgDistributions) {
		m_msgDistributions = msgDistributions;
	}

	public Collection<Item> getAppNames() {
		return m_appNames;
	}

	public List<ErrorMsg> getErrors() {
		return m_errors;
	}

	public FieldsInfo getFieldsInfo() {
		return m_fieldsInfo;
	}

	public void setFieldsInfo(FieldsInfo fieldsInfo) {
		m_fieldsInfo = fieldsInfo;
	}

	public int getTotalCount() {
		return m_totalCount;
	}

	public void setAppNames(Collection<Item> appNames) {
		m_appNames = appNames;
	}

	public void setErrors(List<ErrorMsg> errors) {
		m_errors = errors;
	}

	public void setTotalCount(int totalCount) {
		m_totalCount = totalCount;
	}

}
