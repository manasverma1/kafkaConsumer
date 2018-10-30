package com.multiple.topics;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

/*
 * TODO
 * name thread, add logs based on thread name
 * make duration time assigned from properties
 * check how many partitions are there and add all partitions in arraylist
 */
public class MetaConsumer extends Thread {
	private static Logger logger = Logger.getLogger(MetaConsumer.class);
	private ConcurrentHashMap<String, String> dataMap = null;
	private KafkaConsumer<String, String> consumer = null;
	private String topic = null;
	private boolean doRun = true;
	ConfigProperties prop = ConfigProperties.getInstance();

	private final Duration d = Duration.ofMillis(Long.parseLong(prop.getProperty("pollTime")));

	public MetaConsumer(ConcurrentHashMap<String, String> map, String topic) {
		this.topic = topic;
		this.dataMap = map;
		consumer = new KafkaConsumer<String, String>(Common.getConsumerProperties());
		logger.info("Topic Name: " + topic + ", Number of Partitions: " + consumer.partitionsFor(topic).size());
	}

	@Override
	public void run() {
		logger.info("RUN Start: " + Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
			List<TopicPartition> listTopicPartition = new ArrayList<>();
			for (int i = 0; i < consumer.partitionsFor(topic).size(); i++) {
				listTopicPartition.add(new TopicPartition(topic, i));
			}
			consumer.assign(listTopicPartition);
			consumer.seekToBeginning(listTopicPartition);
			while (doRun) {/*
				ConsumerRecords<String, String> records = consumer.poll(d);
				for (ConsumerRecord<String, String> record : records) {
					if (dataMap.containsKey(record.key())) {
						logger.info("DUP " + record.key());
					} 
					dataMap.put(record.key(), record.value());
				}
			*/
				ConsumerRecords<String, String> records = consumer.poll(d);
				for (ConsumerRecord<String, String> record : records) {
					if (dataMap.containsKey(record.key())) {
						logger.info("DUP " + record.key());
					} 
					dataMap.put(record.key(), record.value());
					consumer.commitSync();
				}
			}
			consumer.close();
			logger.info("RUN OVER: " + Thread.currentThread().getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void stopExecution() {
		doRun = false;
	}
}
