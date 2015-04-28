/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.nanotrader.web.configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.nanotrader.service.configuration.ServiceConfig;
import org.springframework.nanotrader.web.exception.ExtendedExceptionHandlerExceptionResolver;
import org.springframework.nanotrader.web.exception.GlobalExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Java configuration which bootstraps the web application context. Global error
 * handling is configured via
 * configureHandlerExceptionResolvers(List&lt;HandlerExceptionResolver&gt;exceptionResolvers) 
 * enabling consistent REST exception handling across Controllers.
 * 
 * 
 * @author Brian Dussault
 */

@Configuration
@ComponentScan(basePackages = { "org.springframework.nanotrader.web" })
@Import(ServiceConfig.class)
public class WebConfig extends WebMvcConfigurationSupport {

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		// Configure JSON support
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJacksonHttpMessageConverter.setSupportedMediaTypes(Arrays
				.asList(MediaType.APPLICATION_JSON));
		
		//this has been removed in jackson2. TODO: replace somehow? Is it needed?
		//mappingJacksonHttpMessageConverter.getObjectMapper().configure(
		//		Feature.WRITE_DATES_AS_TIMESTAMPS, true);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// There is no need to set the timezone as Jackson uses GMT and not the
		// local time zone (which is exactly what you want)
		// Note: While SimpleDateFormat is not threadsafe, Jackson Marshaller's
		// StdSerializerProvider clones the configured formatter for each thread
		mappingJacksonHttpMessageConverter.getObjectMapper().setDateFormat(
				format);
		mappingJacksonHttpMessageConverter.setPrettyPrint(true);
		// mappingJacksonHttpMessageConverter.getObjectMapper().getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		converters.add(mappingJacksonHttpMessageConverter);
	}

	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureHandlerExceptionResolvers(
			List<HandlerExceptionResolver> exceptionResolvers) {
		ExtendedExceptionHandlerExceptionResolver customResolver = new ExtendedExceptionHandlerExceptionResolver();
		customResolver.setExceptionHandler(new GlobalExceptionHandler());
		customResolver.setMessageConverters(getMessageConverters());
		customResolver.afterPropertiesSet();
		exceptionResolvers.add(customResolver);
	}

}
