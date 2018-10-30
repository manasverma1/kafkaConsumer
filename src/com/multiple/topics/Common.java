package com.multiple.topics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.log4j.Logger;


public class Common {
	private static Logger logger = Logger.getLogger(Common.class);

	private static ConfigProperties prop = ConfigProperties.getInstance();
	private static String date_unformatted = new SimpleDateFormat("YYYY MM dd").format(new Date());
	private static String currentDate = date_unformatted.substring(0,4) + date_unformatted.substring(5,7) +date_unformatted.substring(8,10) ;
	private static Properties consumerProp = new Properties();
	
	private static String date = prop.getProperty("date");
	static {
		consumerProp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getProperty("bootstrap.servers"));
		consumerProp.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getProperty("group.id"));
		consumerProp.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, prop.getProperty("enable.auto.commit"));
		consumerProp.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, prop.getProperty("auto.commit.interval.ms"));
		consumerProp.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, prop.getProperty("key.deserializer"));
		consumerProp.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, prop.getProperty("value.deserializer"));
	}

	public static Properties getConsumerProperties() {
		return consumerProp;
	}

	public static String getTopicName(String fileName) {
		/*
		 * TODO - check id date is set in properties.
		 *  If so, use that date, else use
		 * current date.
		 */
		logger.info("currentDate: " +currentDate +" Date: " + date );
		logger.info(new Date().toString());
		if (!date.isEmpty()) {
			return fileName + "_meta_" + date;
		} else
			return fileName + "_meta_" + currentDate;
	}
}
