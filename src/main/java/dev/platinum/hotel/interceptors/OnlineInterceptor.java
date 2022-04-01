package dev.platinum.hotel.interceptors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.nio.charset.Charset;

@RestController
public class OnlineInterceptor {
	@GetMapping("/graphql")
	public String fromClasspathAsResEntity() throws IOException //All requests to the /graphql endpoint will go through Interceptor methods first.
	{
		System.out.println("--------SERVER ACCESSED ONLINE--------");
		ClassPathResource file = new ClassPathResource("webpage.html");
		String msg = StreamUtils.copyToString(new ClassPathResource("webpage.html").getInputStream(), Charset.defaultCharset());
		return msg;
	}
	/*public ResponseEntity<byte[]> fromClasspathAsResEntity() throws IOException //All requests to the /graphql endpoint will go through Interceptor methods first.
	{
		System.out.println("--------SERVER ACCESSED ONLINE--------");
		ClassPathResource imageFile = new ClassPathResource("shrek.jpg");
		byte[] imageBytes = StreamUtils.copyToByteArray(imageFile.getInputStream());
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}*/
}