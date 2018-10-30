package com.multiple.topics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class FileWrite extends Thread {
	private static Logger logger = Logger.getLogger(FileWrite.class);
	private BufferedWriter bufferWriter = null;
	private boolean doRun = true;
	String fileName;

	private ConcurrentHashMap<String, String> map;

	public FileWrite(String fileName, ConcurrentHashMap<String, String> map) {
		this.map = map;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		logger.info("RUN start: "+Thread.currentThread().getName());
		try {

			/*
			 * while dorun bufferWriter = new BuffeWriter 
			 * for all key,value in map write to
			 * bufferWriter close bufferWriter 
			 * Sleep for 5 
			 * min close while
			 */

			Thread.sleep(2800);
			logger.info("Map: "+fileName.toString());
			//logger.info("MAp Size: "+map.size());
			while (doRun) {
				bufferWriter = new BufferedWriter(new FileWriter(fileName));
				if (map.size() > 0) {
					for (Entry<String, String> string : map.entrySet()) {
						bufferWriter.write(string.getKey() + ' ' + string.getValue());
						bufferWriter.newLine();
						bufferWriter.flush();
					}
					Thread.sleep(300000);
				} else {
					Thread.sleep(2000);
				}
				//SymbolsRest.getSymbols(map);
				bufferWriter.close();
			}
			logger.info("while loop over: "+Thread.currentThread().getName());
			logger.info("RUN Over: "+Thread.currentThread().getName());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	public void stopExecution() {
		doRun = false;
	}
}
