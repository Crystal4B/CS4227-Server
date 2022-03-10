package dev.platinum.hotel.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class interceptorRegistry extends WebMvcConfigurerAdapter {
	@Autowired
	idAuthenticationInterceptor idAuthenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(idAuthenticationInterceptor);
	}

	/*@Bean
	public static HandlerInterceptor getHandlerInterceptor() {
		return new idAuthenticationInterceptor();
	}*/
}