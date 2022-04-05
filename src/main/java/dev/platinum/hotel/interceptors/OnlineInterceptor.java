package dev.platinum.hotel.interceptors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.nio.charset.Charset;

/**
 * OnlineInterceptor for intercepting online access on service
 * @author Jakub Pažej
 */
@RestController
public class OnlineInterceptor {
	/**
	 * Rest Interceptor for online access on /graphql
	 * @return streaming util for displaying webpage
	 * @throws IOException exception from webpage html file not being found
	 */
	@GetMapping("/graphql")
	public String fromClasspathAsResEntity() throws IOException
	{
		System.out.println("--------SERVER ACCESSED ONLINE--------");
		ClassPathResource file = new ClassPathResource("webpage.html");
		return StreamUtils.copyToString(file.getInputStream(), Charset.defaultCharset());
	}
}