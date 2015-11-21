package com.dianping.cat.report.page.server;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.unidal.helper.Splitters;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.mvc.AbstractReportPayload;
import com.dianping.cat.report.ReportPage;

public class Payload extends AbstractReportPayload<Action, ReportPage> {

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("timeRange")
	private int m_timeRange = 24;

	@FieldMeta("screen")
	private String m_screen;

	@FieldMeta("graph")
	private String m_graph;

	@FieldMeta("content")
	private String m_content;

	@FieldMeta("graphId")
	private long m_graphId = System.currentTimeMillis();

	@FieldMeta("interval")
	private String m_interval;

	@FieldMeta("keywords")
	private String m_keywords;

	@FieldMeta("endPoints")
	private List<String> m_endPoints;

	@FieldMeta("measurements")
	private List<String> m_measurements;

	@FieldMeta("view")
	private String m_view;

	private ReportPage m_page;

	private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public Payload() {
		super(ReportPage.SERVER);
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	public String getContent() {
		return m_content;
	}

	public List<String> getEndPoints() {
		return m_endPoints;
	}

	public String getGraph() {
		return m_graph;
	}

	public long getGraphId() {
		return m_graphId;
	}

	public Date getHistoryEndDate() {

		try {
			if (m_customEnd != null && m_customEnd.length() > 0) {
				return m_format.parse(m_customEnd);
			} else {
				return TimeHelper.getCurrentMinute();
			}
		} catch (Exception e) {
			return TimeHelper.getCurrentMinute();
		}
	}

	public Date getHistoryStartDate() {
		try {
			if (m_customStart != null && m_customStart.length() > 0) {

				return m_format.parse(m_customStart);
			} else {
				return TimeHelper.getCurrentHour(-2);
			}
		} catch (Exception e) {
			return TimeHelper.getCurrentHour(-2);
		}
	}

	public String getInterval() {
		return m_interval;
	}

	public String getKeywords() {
		return m_keywords;
	}

	public List<String> getKeywordsList() {
		if (m_keywords != null) {
			String[] keywordArray = m_keywords.split(" +");

			return Arrays.asList(keywordArray);
		} else {
			return Collections.emptyList();
		}
	}

	public List<String> getMeasurements() {
		return m_measurements;
	}

	@Override
	public ReportPage getPage() {
		return m_page;
	}

	public String getScreen() {
		return m_screen;
	}

	public int getTimeRange() {
		return m_timeRange;
	}

	public String getView() {
		return m_view;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	public void setContent(String content) {
		m_content = content;
	}

	public void setEndPoints(String endPoints) {
		m_endPoints = Splitters.by(",").noEmptyItem().split(endPoints);
	}

	public void setGraph(String graph) {
		m_graph = graph;
	}

	public void setGraphId(long graphId) {
		m_graphId = graphId;
	}

	public void setInterval(String interval) {
		m_interval = interval;
	}

	public void setKeywords(String keywords) {
		m_keywords = keywords;
	}

	public void setMeasurements(String measurements) {
		m_measurements = Splitters.by(",").noEmptyItem().split(measurements);
	}

	@Override
	public void setPage(String page) {
		m_page = ReportPage.getByName(page, ReportPage.SERVER);
	}

	public void setScreen(String screen) {
		m_screen = screen;
	}

	public void setTimeRange(int timeRange) {
		m_timeRange = timeRange;
	}

	public void setView(String view) {
		m_view = view;
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}
	}
}
