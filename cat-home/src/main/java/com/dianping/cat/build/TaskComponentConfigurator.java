package com.dianping.cat.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.cat.app.AppCommandDataDao;
import com.dianping.cat.app.AppSpeedDataDao;
import com.dianping.cat.app.CrashLogContentDao;
import com.dianping.cat.app.CrashLogDao;
import com.dianping.cat.config.app.AppCommandConfigManager;
import com.dianping.cat.config.app.MobileConfigManager;
import com.dianping.cat.config.app.AppSpeedConfigManager;
import com.dianping.cat.config.business.BusinessConfigManager;
import com.dianping.cat.config.server.ServerConfigManager;
import com.dianping.cat.config.server.ServerFilterConfigManager;
import com.dianping.cat.config.web.WebSpeedConfigManager;
import com.dianping.cat.config.web.url.UrlPatternConfigManager;
import com.dianping.cat.consumer.config.ProductLineConfigManager;
import com.dianping.cat.consumer.metric.MetricConfigManager;
import com.dianping.cat.core.config.ConfigDao;
import com.dianping.cat.core.dal.DailyReportContentDao;
import com.dianping.cat.core.dal.DailyReportDao;
import com.dianping.cat.core.dal.HourlyReportContentDao;
import com.dianping.cat.core.dal.HourlyReportDao;
import com.dianping.cat.core.dal.MonthlyReportContentDao;
import com.dianping.cat.core.dal.MonthlyReportDao;
import com.dianping.cat.core.dal.TaskDao;
import com.dianping.cat.core.dal.WeeklyReportContentDao;
import com.dianping.cat.core.dal.WeeklyReportDao;
import com.dianping.cat.home.dal.report.BaselineDao;
import com.dianping.cat.home.dal.report.OverloadDao;
import com.dianping.cat.home.dal.report.TopologyGraphDao;
import com.dianping.cat.report.alert.app.AppRuleConfigManager;
import com.dianping.cat.report.page.app.service.AppReportService;
import com.dianping.cat.report.page.app.task.AppDatabasePruner;
import com.dianping.cat.report.page.app.task.AppReportBuilder;
import com.dianping.cat.report.page.app.task.CommandAutoCompleter;
import com.dianping.cat.report.page.browser.task.WebDatabasePruner;
import com.dianping.cat.report.page.business.service.BusinessReportService;
import com.dianping.cat.report.page.business.task.BusinessBaselineReportBuilder;
import com.dianping.cat.report.page.business.task.BusinessKeyHelper;
import com.dianping.cat.report.page.business.task.BusinessPointParser;
import com.dianping.cat.report.page.cross.service.CrossReportService;
import com.dianping.cat.report.page.cross.task.CrossReportBuilder;
import com.dianping.cat.report.page.dependency.graph.TopologyGraphBuilder;
import com.dianping.cat.report.page.dependency.service.DependencyReportService;
import com.dianping.cat.report.page.dependency.task.DependencyReportBuilder;
import com.dianping.cat.report.page.event.service.EventReportService;
import com.dianping.cat.report.page.event.task.EventReportBuilder;
import com.dianping.cat.report.page.heartbeat.service.HeartbeatReportService;
import com.dianping.cat.report.page.heartbeat.task.HeartbeatReportBuilder;
import com.dianping.cat.report.page.matrix.service.MatrixReportService;
import com.dianping.cat.report.page.matrix.task.MatrixReportBuilder;
import com.dianping.cat.report.page.metric.service.BaselineService;
import com.dianping.cat.report.page.metric.service.DefaultBaselineService;
import com.dianping.cat.report.page.metric.service.MetricReportService;
import com.dianping.cat.report.page.metric.task.BaselineConfigManager;
import com.dianping.cat.report.page.metric.task.BaselineCreator;
import com.dianping.cat.report.page.metric.task.DefaultBaselineCreator;
import com.dianping.cat.report.page.metric.task.MetricBaselineReportBuilder;
import com.dianping.cat.report.page.metric.task.MetricPointParser;
import com.dianping.cat.report.page.network.config.NetGraphConfigManager;
import com.dianping.cat.report.page.network.nettopology.NetGraphBuilder;
import com.dianping.cat.report.page.network.service.NetTopologyReportService;
import com.dianping.cat.report.page.network.task.NetTopologyReportBuilder;
import com.dianping.cat.report.page.overload.task.CapacityUpdateStatusManager;
import com.dianping.cat.report.page.overload.task.CapacityUpdateTask;
import com.dianping.cat.report.page.overload.task.CapacityUpdater;
import com.dianping.cat.report.page.overload.task.DailyCapacityUpdater;
import com.dianping.cat.report.page.overload.task.HourlyCapacityUpdater;
import com.dianping.cat.report.page.overload.task.MonthlyCapacityUpdater;
import com.dianping.cat.report.page.overload.task.TableCapacityService;
import com.dianping.cat.report.page.overload.task.WeeklyCapacityUpdater;
import com.dianping.cat.report.page.problem.service.ProblemReportService;
import com.dianping.cat.report.page.problem.task.ProblemReportBuilder;
import com.dianping.cat.report.page.server.service.MetricGraphService;
import com.dianping.cat.report.page.server.task.MetricGraphPruner;
import com.dianping.cat.report.page.state.service.StateReportService;
import com.dianping.cat.report.page.state.task.StateReportBuilder;
import com.dianping.cat.report.page.statistics.service.ClientReportService;
import com.dianping.cat.report.page.statistics.service.HeavyReportService;
import com.dianping.cat.report.page.statistics.service.JarReportService;
import com.dianping.cat.report.page.statistics.service.ServiceReportService;
import com.dianping.cat.report.page.statistics.service.UtilizationReportService;
import com.dianping.cat.report.page.statistics.task.heavy.HeavyReportBuilder;
import com.dianping.cat.report.page.statistics.task.jar.JarReportBuilder;
import com.dianping.cat.report.page.statistics.task.service.ClientReportBuilder;
import com.dianping.cat.report.page.statistics.task.service.ServiceReportBuilder;
import com.dianping.cat.report.page.statistics.task.utilization.UtilizationReportBuilder;
import com.dianping.cat.report.page.storage.task.StorageReportBuilder;
import com.dianping.cat.report.page.storage.task.StorageReportService;
import com.dianping.cat.report.page.storage.transform.StorageMergeHelper;
import com.dianping.cat.report.page.transaction.service.TransactionReportService;
import com.dianping.cat.report.page.transaction.task.TransactionReportBuilder;
import com.dianping.cat.report.page.transaction.transform.TransactionMergeHelper;
import com.dianping.cat.report.task.DefaultTaskConsumer;
import com.dianping.cat.report.task.ReportFacade;
import com.dianping.cat.report.task.TaskBuilder;
import com.dianping.cat.report.task.cmdb.CmdbInfoReloadBuilder;
import com.dianping.cat.report.task.cmdb.ProjectUpdateTask;
import com.dianping.cat.report.task.current.CurrentReportBuilder;
import com.dianping.cat.service.HostinfoService;
import com.dianping.cat.service.ProjectService;
import com.dianping.cat.system.page.router.config.RouterConfigAdjustor;
import com.dianping.cat.system.page.router.config.RouterConfigHandler;
import com.dianping.cat.system.page.router.service.RouterConfigService;
import com.dianping.cat.system.page.router.task.RouterConfigBuilder;
import com.dianping.cat.web.AjaxDataDao;
import com.dianping.cat.web.JsErrorLogContentDao;
import com.dianping.cat.web.JsErrorLogDao;
import com.dianping.cat.web.WebSpeedDataDao;

public class TaskComponentConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(DefaultTaskConsumer.class) //
		      .req(TaskDao.class, ReportFacade.class));

		all.add(C(MetricPointParser.class));
		all.add(C(BusinessPointParser.class));
		all.add(C(BusinessKeyHelper.class));

		all.add(C(BaselineCreator.class, DefaultBaselineCreator.class));
		all.add(C(BaselineService.class, DefaultBaselineService.class).req(BaselineDao.class));
		all.add(C(BaselineConfigManager.class, BaselineConfigManager.class));

		all.add(C(TaskBuilder.class, MetricBaselineReportBuilder.ID, MetricBaselineReportBuilder.class)
		      .req(MetricReportService.class, MetricPointParser.class)//
		      .req(MetricConfigManager.class, ProductLineConfigManager.class)//
		      .req(BaselineCreator.class, BaselineService.class, BaselineConfigManager.class));

		all.add(C(TaskBuilder.class, BusinessBaselineReportBuilder.ID, BusinessBaselineReportBuilder.class)
		      .req(BusinessReportService.class, BusinessPointParser.class)//
		      .req(BusinessConfigManager.class, BusinessKeyHelper.class)//
		      .req(BaselineCreator.class, BaselineService.class, BaselineConfigManager.class));

		all.add(C(TaskBuilder.class, TransactionReportBuilder.ID, TransactionReportBuilder.class) //
		      .req(TransactionReportService.class));

		all.add(C(TaskBuilder.class, EventReportBuilder.ID, EventReportBuilder.class) //
		      .req(EventReportService.class));

		all.add(C(TaskBuilder.class, ProblemReportBuilder.ID, ProblemReportBuilder.class) //
		      .req(ProblemReportService.class));//

		all.add(C(TaskBuilder.class, HeartbeatReportBuilder.ID, HeartbeatReportBuilder.class) //
		      .req(HeartbeatReportService.class));

		all.add(C(TaskBuilder.class, ServiceReportBuilder.ID, ServiceReportBuilder.class).req(CrossReportService.class,
		      ServiceReportService.class, ServerFilterConfigManager.class));

		all.add(C(TaskBuilder.class, MatrixReportBuilder.ID, MatrixReportBuilder.class).req(MatrixReportService.class));

		all.add(C(TaskBuilder.class, CrossReportBuilder.ID, CrossReportBuilder.class).req(CrossReportService.class));

		all.add(C(TaskBuilder.class, StateReportBuilder.ID, StateReportBuilder.class) //
		      .req(ServerConfigManager.class, HostinfoService.class, ProjectService.class) //
		      .req(StateReportService.class, ServerFilterConfigManager.class));

		all.add(A(RouterConfigAdjustor.class));

		all.add(C(TaskBuilder.class, RouterConfigBuilder.ID, RouterConfigBuilder.class).req(RouterConfigService.class,
		      RouterConfigHandler.class, RouterConfigAdjustor.class, ServerConfigManager.class));

		all.add(C(TaskBuilder.class, HeavyReportBuilder.ID, HeavyReportBuilder.class).req(MatrixReportService.class,
		      HeavyReportService.class, ServerFilterConfigManager.class));

		all.add(C(TaskBuilder.class, UtilizationReportBuilder.ID, UtilizationReportBuilder.class).req(
		      UtilizationReportService.class, TransactionReportService.class, HeartbeatReportService.class,
		      CrossReportService.class, TransactionMergeHelper.class, ServerFilterConfigManager.class));

		all.add(C(TaskBuilder.class, DependencyReportBuilder.ID, DependencyReportBuilder.class).req(
		      DependencyReportService.class, TopologyGraphBuilder.class, TopologyGraphDao.class));

		all.add(C(TaskBuilder.class, NetTopologyReportBuilder.ID, NetTopologyReportBuilder.class).req(
		      NetTopologyReportService.class, MetricReportService.class, NetGraphBuilder.class,
		      NetGraphConfigManager.class));

		all.add(C(TaskBuilder.class, JarReportBuilder.ID, JarReportBuilder.class).req(HeartbeatReportService.class,
		      JarReportService.class, ServerFilterConfigManager.class));

		all.add(C(TaskBuilder.class, ClientReportBuilder.ID, ClientReportBuilder.class).req(ClientReportService.class,
		      TransactionReportService.class, ServerFilterConfigManager.class, ProjectService.class,
		      TransactionMergeHelper.class));

		all.add(C(TaskBuilder.class, CurrentReportBuilder.ID, CurrentReportBuilder.class).req(ProjectService.class,
		      ServerFilterConfigManager.class));

		all.add(C(TaskBuilder.class, StorageReportBuilder.ID, StorageReportBuilder.class).req(StorageReportService.class,
		      StorageMergeHelper.class));

		all.add(C(TaskBuilder.class, CmdbInfoReloadBuilder.ID, CmdbInfoReloadBuilder.class).req(ProjectUpdateTask.class));

		all.add(C(CapacityUpdateStatusManager.class).req(OverloadDao.class, ConfigDao.class));

		all.add(C(CapacityUpdater.class, HourlyCapacityUpdater.ID, HourlyCapacityUpdater.class).req(OverloadDao.class,
		      HourlyReportContentDao.class, HourlyReportDao.class, CapacityUpdateStatusManager.class));

		all.add(C(CapacityUpdater.class, DailyCapacityUpdater.ID, DailyCapacityUpdater.class).req(OverloadDao.class,
		      DailyReportContentDao.class, DailyReportDao.class, CapacityUpdateStatusManager.class));

		all.add(C(CapacityUpdater.class, WeeklyCapacityUpdater.ID, WeeklyCapacityUpdater.class).req(OverloadDao.class,
		      WeeklyReportContentDao.class, WeeklyReportDao.class, CapacityUpdateStatusManager.class));

		all.add(C(CapacityUpdater.class, MonthlyCapacityUpdater.ID, MonthlyCapacityUpdater.class).req(OverloadDao.class,
		      MonthlyReportContentDao.class, MonthlyReportDao.class, CapacityUpdateStatusManager.class));

		all.add(C(TableCapacityService.class).req(HourlyReportDao.class, DailyReportDao.class, WeeklyReportDao.class,
		      MonthlyReportDao.class, OverloadDao.class));

		all.add(C(TaskBuilder.class, CapacityUpdateTask.ID, CapacityUpdateTask.class)
		      .req(CapacityUpdater.class, HourlyCapacityUpdater.ID, "m_hourlyUpdater")
		      .req(CapacityUpdater.class, DailyCapacityUpdater.ID, "m_dailyUpdater")
		      .req(CapacityUpdater.class, WeeklyCapacityUpdater.ID, "m_weeklyUpdater")
		      .req(CapacityUpdater.class, MonthlyCapacityUpdater.ID, "m_monthlyUpdater"));

		all.add(C(AppDatabaseConfigurator.class).req(AppCommandDataDao.class, AppSpeedDataDao.class));

		all.add(C(TaskBuilder.class, AppDatabasePruner.ID, AppDatabasePruner.class).req(AppCommandDataDao.class,
		      AppSpeedDataDao.class, AppSpeedConfigManager.class, CrashLogDao.class, CrashLogContentDao.class,
		      AppCommandConfigManager.class));

		all.add(C(TaskBuilder.class, WebDatabasePruner.ID, WebDatabasePruner.class).req(AjaxDataDao.class,
		      WebSpeedDataDao.class, WebSpeedConfigManager.class, UrlPatternConfigManager.class, JsErrorLogDao.class,
		      JsErrorLogContentDao.class));

		all.add(C(TaskBuilder.class, MetricGraphPruner.ID, MetricGraphPruner.class).req(MetricGraphService.class));

		all.add(C(CommandAutoCompleter.class).req(TransactionReportService.class, AppCommandConfigManager.class));

		all.add(C(TaskBuilder.class, AppReportBuilder.ID, AppReportBuilder.class).req(AppCommandDataDao.class,
		      AppCommandConfigManager.class, AppReportService.class, TransactionReportService.class,
		      CommandAutoCompleter.class, AppRuleConfigManager.class, TransactionMergeHelper.class,
		      MobileConfigManager.class));

		all.add(C(ReportFacade.class));

		return all;
	}
}
