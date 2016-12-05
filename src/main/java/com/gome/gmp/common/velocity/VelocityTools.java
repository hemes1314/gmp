package com.gome.gmp.common.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class VelocityTools {

	private static String LOG_NAME = "velocity_log";

	private static Logger log = Logger.getLogger(LOG_NAME);

	private static ToolManager manager = new ToolManager();
	static {
		ClassPathResource tools = new ClassPathResource("velocity/velocity-tools.xml");
		manager.configure(tools.getPath());
		try {
			Velocity.init(PropertiesLoaderUtils.loadAllProperties("velocity/velocity.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 使用log4j为velocity的log
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		Velocity.setProperty("runtime.log.logsystem.log4j.logger", LOG_NAME);
	}

	// 填充velocity数据 parseByMerge，返回String
	public static String parseByMerge(String vmFullFilePath, Map<String, Object> context) {
		if (vmFullFilePath == null || vmFullFilePath.trim().length() < 1) {
			return "";
		}
		StringWriter writer = null;
		try {
			ClassPathResource resource = new ClassPathResource(vmFullFilePath);
			Template t = Velocity.getTemplate(resource.getPath());
			Context vc = manager.createContext();
			for (Map.Entry<String, Object> p : context.entrySet()) {
				vc.put(p.getKey(), p.getValue());
			}
			writer = new StringWriter();
			t.merge(vc, writer);
			return writer.toString();
		} catch (Exception e) {
			log.error("parse file : " + vmFullFilePath, e);
			return "";
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
			}
		}
	}

	// 填充velocity数据 parseByEvaluate，返回String
	public static String parseByEvaluate(String logTag, String vmTemplateContent, Map<String, Object> context) {
		StringWriter sw = new StringWriter();
		if (vmTemplateContent == null || "".equals(vmTemplateContent)) {
			return vmTemplateContent;
		}
		if (context == null || context.size() <= 0) {
			return vmTemplateContent;
		}
		Context vc = manager.createContext();
		for (String key : context.keySet()) {
			vc.put(key, context.get(key));
		}
		try {
			Velocity.evaluate(vc, sw, logTag, vmTemplateContent);
		} catch (Exception e) {
			log.debug(e);
		}
		String resultStr = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
		}
		return resultStr;
	}

	public static void main(String[] args) {
		String logTag = "test";
		String vmTemplateContent = "${name}${age}";
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("name", "linchengjun测试中文");
		context.put("age", 24);
		System.out.println(parseByEvaluate(logTag, vmTemplateContent, context));
	}
}
