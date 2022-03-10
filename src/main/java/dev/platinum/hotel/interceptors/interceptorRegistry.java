package dev.platinum.hotel.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class interceptorRegistry implements WebMvcConfigurer {

	@Autowired
	idAuthenticationInterceptor idAuthenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(idAuthenticationInterceptor);
	}
}
	/*@Bean
	public static HandlerInterceptor getHandlerInterceptor() {
		return new idAuthenticationInterceptor();
	}*/