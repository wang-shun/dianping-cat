package org.unidal.cat.message.storage;

import java.io.IOException;

public interface BlockDumper {
	public void initialize(int hour);
	
	public void awaitTermination() throws InterruptedException;

	public void dump(Block block) throws IOException;
}