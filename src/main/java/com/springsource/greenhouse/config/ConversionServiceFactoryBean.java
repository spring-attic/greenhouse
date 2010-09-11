package com.springsource.greenhouse.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.format.datetime.joda.JodaTimeFormattingConfigurer;
import org.springframework.format.support.FormattingConversionService;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService> {

	public Class<?> getObjectType() {
		return ConversionService.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public ConversionService getObject() throws Exception {
		FormattingConversionService conversionService = new FormattingConversionService();
		ConversionServiceFactory.addDefaultConverters(conversionService);
		conversionService.addConverter(new EmptyStringToNullConverter());
		new JodaTimeFormattingConfigurer().installJodaTimeFormatting(conversionService);			
		return conversionService;
 	}

	private static class EmptyStringToNullConverter implements Converter<String, String> {
		public String convert(String source) {
			return source.isEmpty() ? null : source;
		}
	}
}
