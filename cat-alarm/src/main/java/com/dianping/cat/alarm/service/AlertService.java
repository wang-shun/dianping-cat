package com.dianping.cat.alarm.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.unidal.dal.jdbc.DalException;
import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.cat.alarm.Alert;
import com.dianping.cat.alarm.AlertDao;
import com.dianping.cat.alarm.spi.AlertEntity;
import com.dianping.cat.alarm.spi.AlertType;
import com.dianping.cat.alarm.spi.sender.SendMessageEntity;

public class AlertService {

	@Inject
	private AlertDao m_alertDao;

	private Alert buildAlert(AlertEntity alertEntity, SendMessageEntity message) {
		Alert alert = new Alert();

		alert.setDomain(alertEntity.getDomain());
		alert.setAlertTime(alertEntity.getDate());
		alert.setCategory(alertEntity.getType().getName());
		alert.setType(alertEntity.getLevel().getLevel());
		alert.setContent(message.getTitle() + "<br/>" + message.getContent());
		alert.setMetric(alertEntity.getMetric());

		return alert;
	}

	public List<Alert> query(Date start, Date end, String type) {
		List<Alert> alerts = new LinkedList<Alert>();

		try {
			alerts = m_alertDao.queryAlertsByTimeCategory(start, end, type,
			      com.dianping.cat.alarm.AlertEntity.READSET_FULL);
		} catch (DalNotFoundException e) {
			// ignore
		} catch (Exception e) {
			Cat.logError(e);
		}

		return alerts;
	}

	public void insert(AlertEntity alertEntity, SendMessageEntity message) {
		if (alertEntity.getType().equals(AlertType.FrontEndException.getName())) {
			return;
		}
		Alert alert = buildAlert(alertEntity, message);

		try {
			int count = m_alertDao.insert(alert);

			if (count != 1) {
				Cat.logError("insert alert error: " + alert.toString(), new RuntimeException());
			}
		} catch (DalException e) {
			Cat.logError(e);
		}
	}
}