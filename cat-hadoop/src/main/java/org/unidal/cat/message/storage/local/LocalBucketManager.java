package org.unidal.cat.message.storage.local;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.cat.message.storage.Bucket;
import org.unidal.cat.message.storage.BucketManager;
import org.unidal.cat.message.storage.FileBuilder;
import org.unidal.cat.message.storage.FileBuilder.FileType;
import org.unidal.cat.metric.Benchmark;
import org.unidal.cat.metric.BenchmarkManager;
import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Inject;
import org.unidal.lookup.annotation.Named;

import com.dianping.cat.Cat;
import com.dianping.cat.message.spi.MessageTree;

@Named(type = BucketManager.class, value = "local")
public class LocalBucketManager extends ContainerHolder implements BucketManager, LogEnabled {
	@Inject
	private BenchmarkManager m_benchmarkManager;

	@Inject("local")
	private FileBuilder m_bulider;

	private Map<Integer, Map<String, Bucket>> m_buckets = new LinkedHashMap<Integer, Map<String, Bucket>>();

	private Logger m_logger;

	private boolean bucketFilesExsits(String domain, String ip, int hour) {
		long timestamp = hour * 3600 * 1000L;
		Date startTime = new Date(timestamp);
		File dataPath = m_bulider.getFile(domain, startTime, ip, FileType.DATA);
		File indexPath = m_bulider.getFile(domain, startTime, ip, FileType.INDEX);

		return dataPath.exists() && indexPath.exists();
	}

	@Override
	public void closeBuckets(int hour) {
		Set<Integer> removed = new HashSet<Integer>();

		for (Entry<Integer, Map<String, Bucket>> e : m_buckets.entrySet()) {
			int h = e.getKey().intValue();

			if (h <= hour) {
				removed.add(h);
			}
		}

		for (Integer h : removed) {
			Map<String, Bucket> buckets = m_buckets.remove(h);

			for (Bucket bucket : buckets.values()) {
				try {
					bucket.close();

					Benchmark benchmark = bucket.getBechmark();
					m_benchmarkManager.remove(benchmark.getType());
					m_logger.info("Close bucket " + bucket);
				} catch (Exception e) {
					Cat.logError(e);
				} finally {
					super.release(bucket);
				}
			}
		}
	}

	@Override
	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	private Map<String, Bucket> findOrCreateMap(Map<Integer, Map<String, Bucket>> map, int hour) {
		Map<String, Bucket> m = map.get(hour);

		if (m == null) {
			synchronized (map) {
				m = map.get(hour);

				if (m == null) {
					m = new LinkedHashMap<String, Bucket>();
					map.put(hour, m);
				}
			}
		}

		return m;
	}

	@Override
	public Bucket getBucket(String domain, String ip, int hour, boolean createIfNotExists) throws IOException {
		Map<String, Bucket> map = findOrCreateMap(m_buckets, hour);
		Bucket bucket = map.get(domain);
		boolean shouldCreate = createIfNotExists && bucket == null || !createIfNotExists
		      && bucketFilesExsits(domain, ip, hour);

		if (shouldCreate) {
			synchronized (map) {
				bucket = map.get(domain);

				if (bucket == null) {
					String benchmarkId = domain + ":" + hour;
					Benchmark benchmark = m_benchmarkManager.get(benchmarkId);

					bucket = lookup(Bucket.class, "local");
					bucket.setBenchmark(benchmark);
					bucket.initialize(domain, ip, hour);
					map.put(domain, bucket);
				}
			}
		}

		return bucket;
	}

	@Override
	public MessageTree loadMessage(String messageId) {
		throw new RuntimeException("unsupport operation");
	}
}
