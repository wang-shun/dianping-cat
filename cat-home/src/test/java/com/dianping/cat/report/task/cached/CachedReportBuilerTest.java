package com.dianping.cat.report.task.cached;

import java.io.File;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.cat.config.server.ServerConfigManager;
import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.report.task.TaskBuilder;
import com.dianping.cat.report.task.cached.CurrentWeeklyMonthlyReportTask.CurrentWeeklyMonthlyTask;

public class CachedReportBuilerTest extends ComponentTestCase {

	private int m_index = 0;

	@Test
	public void test() throws Exception {
		ServerConfigManager manager = (ServerConfigManager) lookup(ServerConfigManager.class);

		manager.initialize(new File("/data/appdatas/cat/server.xml"));

		TaskBuilder builder = lookup(TaskBuilder.class, CachedReportBuilder.ID);
		CurrentWeeklyMonthlyReportTask.getInstance().register(new CurrentWeeklyMonthlyTask() {

			@Override
			public String getReportName() {
				return "Test";
			}

			@Override
			public void buildWeeklyTask(String name, String domain, Date start) {
				m_index++;
			}

			@Override
			public void buildMonthlyTask(String name, String domain, Date start) {
				m_index++;
			}
		});

		builder.buildDailyTask("test", "test", TimeHelper.getCurrentDay());

		Thread.sleep(1000 * 5);

		Assert.assertEquals(true, m_index > 0);
	}

}