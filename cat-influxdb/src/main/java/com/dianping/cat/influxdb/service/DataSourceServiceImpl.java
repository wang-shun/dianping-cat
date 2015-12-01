package com.dianping.cat.influxdb.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.cat.influxdb.InfluxDB;
import com.dianping.cat.influxdb.InfluxDBFactory;
import com.dianping.cat.influxdb.config.InfluxDBConfigManager;
import com.dianping.cat.influxdb.config.entity.Influxdb;
import com.dianping.cat.influxdb.config.entity.InfluxdbConfig;
import com.dianping.cat.metric.DataSourceService;

public class DataSourceServiceImpl implements DataSourceService<InfluxDBConnection> {

	@Inject
	private InfluxDBConfigManager m_configManager;

	private InfluxdbConfig m_influxdbConfig;

	private Map<String, InfluxDBConnection> m_connections = new ConcurrentHashMap<String, InfluxDBConnection>();

	public final static String DEFAULT = "default";

	private Map<String, InfluxDBConnection> buildConnections(InfluxdbConfig influxdbConfig) {
		Map<String, InfluxDBConnection> connections = new ConcurrentHashMap<String, InfluxDBConnection>();

		for (Entry<String, Influxdb> entry : influxdbConfig.getInfluxdbs().entrySet()) {
			try {
				Influxdb config = entry.getValue();
				String url = String.format("http://%s:%s", config.getHost(), config.getPort());
				InfluxDB influxDB = InfluxDBFactory.connect(url, config.getUsername(), config.getPassword());
				String database = config.getDatabase();

				influxDB.createDatabase(database);
				connections.put(entry.getKey(), new InfluxDBConnection(influxDB, database));
			} catch (Exception e) {
				Cat.logError(e);
			}
		}
		return connections;
	}

	@Override
	public InfluxDBConnection getConnection(String category) {
		InfluxDBConnection conn = m_connections.get(category);

		if (conn == null) {
			conn = m_connections.get(DEFAULT);
		}
		return conn;
	}

	public Map<String, InfluxDBConnection> getConnections() {
		return m_connections;
	}

	@Override
	public void initialize() throws InitializationException {
		m_influxdbConfig = m_configManager.getConfig();
		m_connections = buildConnections(m_influxdbConfig);
	}

	@Override
	public void refresh() {
		InfluxdbConfig config = m_configManager.getConfig();

		if (!m_influxdbConfig.equals(config)) {
			m_influxdbConfig = m_configManager.getConfig();
			m_connections = buildConnections(m_influxdbConfig);
		}
	}
}
