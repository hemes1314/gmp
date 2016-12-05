package com.gome.gmp.common.util;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.gome.gmp.common.diamond.DiamondClient;

public class PropertyPlaceholderConfigurerUtil extends PropertyPlaceholderConfigurer {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);
	private List<String> diamondList;
	
	public List<String> getDiamondList() {
		return diamondList;
	}

	public void setDiamondList(List<String> diamondList) {
		this.diamondList = diamondList;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		Properties properties = DiamondClient.getProperties(diamondList);
		this.setProperties(properties);
		for (Iterator<Object> iterator = properties.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) properties.get(key);
			props.setProperty(key, value);
		}
		super.processProperties(beanFactoryToProcess, props);
	}

}
