package com.multiple.topics;

public class Test {
	public static void main(String[] args) {
		  ConfigProperties prop = ConfigProperties.getInstance();

		 String date = prop.getProperty("date");
		 System.err.println(date.isEmpty()+" da");
		 if ( !date.isEmpty()) {
				System.err.println("TRUE");
			}

	}
}
