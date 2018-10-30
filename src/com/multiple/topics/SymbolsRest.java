package com.multiple.topics;
import static spark.Spark.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;


public class SymbolsRest {
	
	
	public static void getSymbols(Map<String, String> map) {
	get("symbols", (req, res) -> {
		ArrayList<String> symbols = new ArrayList<String>();
		symbols.addAll(map.keySet());
	    Map<String, Object> model = new HashMap<>();
	    model.put("message", "List Of Symbols");
	    model.put("key", symbols);
	    model.put("string", " ");
	    return new VelocityTemplateEngine().render(
	        new ModelAndView(model, "message.vm")
	    );
	});
	}

}
