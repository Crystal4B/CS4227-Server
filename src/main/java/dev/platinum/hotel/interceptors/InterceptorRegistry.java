package dev.platinum.hotel.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registry for creation of interceptors on service
 * @author Jakub Pažej
 */
@Configuration
public class InterceptorRegistry implements WebMvcConfigurer {

	@Override
	public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor());
	}
}
