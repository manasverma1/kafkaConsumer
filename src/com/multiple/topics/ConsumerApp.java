package com.multiple.topics;

import static spark.Spark.get;
import static spark.Spark.port;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumerApp {
	private static Logger logger = Logger.getLogger(ConsumerApp.class);

	public static void main(String[] args) {
		logger.info("Main method started");
		ConfigProperties prop = ConfigProperties.getInstance();
		ConcurrentHashMap<String, String> ctaaMap = new ConcurrentHashMap<String, String>();
		ConcurrentHashMap<String, String> ctabMap = new ConcurrentHashMap<String, String>();
		ConcurrentHashMap<String, String> utpMap = new ConcurrentHashMap<String, String>();
		ObjectMapper objectMapper = new ObjectMapper();

		String topicCtaa = Common.getTopicName("ctaa");
		String topicCtab = Common.getTopicName("ctab");
		String topicUtp = Common.getTopicName("utp");

		String fileCtaa = prop.getProperty("filePath") + topicCtaa + ".txt";
		String fileCtab = prop.getProperty("filePath") + topicCtab + ".txt";
		String fileUtp = prop.getProperty("filePath") + topicUtp + ".txt";

		MetaConsumer metaConsumerCtaa = new MetaConsumer(ctaaMap, topicCtaa);
		metaConsumerCtaa.setName("READING_CTAA");
		metaConsumerCtaa.start();

		MetaConsumer metaConsumerCtab = new MetaConsumer(ctabMap, topicCtab);
		metaConsumerCtab.setName("READING_CTAB");
		metaConsumerCtab.start();

		MetaConsumer metaConsumerUtp = new MetaConsumer(utpMap, topicUtp);
		metaConsumerUtp.setName("READING_UTP");
		metaConsumerUtp.start();

		FileWrite fileWriter = new FileWrite(fileCtaa, ctaaMap);
		fileWriter.setName("WRITER_CTAA");
		fileWriter.start();

		FileWrite fileWriteCtab = new FileWrite(fileCtab, ctabMap);
		fileWriteCtab.setName("WRITER_CTAB");
		fileWriteCtab.start();

		FileWrite fileWriteUtp = new FileWrite(fileUtp, utpMap);
		fileWriteUtp.setName("WRITER_UTP");
		fileWriteUtp.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				metaConsumerCtaa.stopExecution();
				metaConsumerCtab.stopExecution();
				metaConsumerUtp.stopExecution();
				fileWriter.stopExecution();
				fileWriteCtab.stopExecution();
				fileWriteUtp.stopExecution();
				try {
					metaConsumerCtaa.join();
					metaConsumerCtab.join();
					metaConsumerUtp.join();
					fileWriter.join();
					fileWriteCtab.join();
					fileWriteUtp.join();
					logger.info("Work Done !!");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		/*
		 * REST
		 */
		port(8012);
		logger.info("Spark is running on PORT: 8012");

		get("symbols/:channel", (req, res) -> {
			ConcurrentHashMap<String, String> map = null;
			String channel = req.params(":channel");
			logger.info(channel + " request");
			if ("ctaa".equalsIgnoreCase(channel)) {
				map = ctaaMap;
			} else if ("ctab".equalsIgnoreCase(channel)) {
				map = ctabMap;
			} else if ("utp".equalsIgnoreCase(channel)) {
				map = utpMap;
			} else {
				return channel.toUpperCase() + " channel is empty.";
			}
			return objectMapper.writeValueAsString(map.keySet());
		});

		logger.info("Main method over");
	}
}
