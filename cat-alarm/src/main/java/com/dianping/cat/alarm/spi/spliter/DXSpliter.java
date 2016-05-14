package com.dianping.cat.alarm.spi.spliter;

import java.util.regex.Pattern;

import com.dianping.cat.alarm.spi.AlertChannel;

public class DXSpliter implements Spliter {

	public static final String ID = AlertChannel.DX.getName();

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String process(String content) {
		String dxContent = content.replaceAll("<br/>", " ");
		return Pattern.compile("<div.*(?=</div>)</div>", Pattern.DOTALL).matcher(dxContent).replaceAll("");
	}

}
