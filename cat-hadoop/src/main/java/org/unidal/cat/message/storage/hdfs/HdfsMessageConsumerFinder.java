package org.unidal.cat.message.storage.hdfs;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.unidal.cat.message.storage.FileType;
import org.unidal.cat.message.storage.PathBuilder;
import org.unidal.lookup.annotation.Inject;
import org.unidal.lookup.annotation.Named;

import com.dianping.cat.Cat;
import com.dianping.cat.helper.TimeHelper;

@Named(type = MessageConsumerFinder.class, value = "hdfs")
public class HdfsMessageConsumerFinder implements MessageConsumerFinder {

	@Inject("hdfs")
	private PathBuilder m_pathBuilder;

	@Inject
	private FileSystemManager m_fileSystemManager;

	private Map<String, Set<String>> m_caches = new LinkedHashMap<String, Set<String>>();

	@Override
	public Set<String> findConsumerIps(final String domain, int hour) {
		String key = domain + '-' + hour;
		Set<String> ips = m_caches.get(key);

		if (ips == null) {
			synchronized (m_caches) {
				ips = m_caches.get(key);

				if (ips == null) {
					ips = findfromHdfs(domain, hour);
				}
			}
		}

		return ips;
	}

	private Set<String> findfromHdfs(final String domain, int hour) {
	   Date start = new Date(hour * TimeHelper.ONE_HOUR);
	   String parent = m_pathBuilder.getPath(domain, start, null, FileType.PARENT);
	   FileSystem fs;

	   try {
	   	fs = m_fileSystemManager.getFileSystem();
	   } catch (IOException e) {
	   	Cat.logError(e);
	   	return null;
	   }

	   final Set<String> result = new HashSet<String>();

	   try {
	   	final Path basePath = new Path(parent);

	   	if (fs != null) {
	   		fs.listStatus(basePath, new PathFilter() {
	   			@Override
	   			public boolean accept(Path p) {
	   				String name = p.getName();

	   				if (name.contains(domain) && name.endsWith(".dat")) {
	   					int start = name.lastIndexOf('-');
	   					int end = name.length() - 4;

	   					result.add(name.substring(start + 1, end));
	   				}
	   				return false;
	   			}
	   		});
	   	}
	   } catch (IOException e) {
	   	Cat.logError(e);
	   }
	   return result;
   }

}
