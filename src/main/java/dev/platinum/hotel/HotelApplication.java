package dev.platinum.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Springboot file
 * @author Marcin Sęk
 */
@SpringBootApplication
public class HotelApplication
{
	/**
	 * Main function for Springboot setup
	 * @param args arguments for service
	 */
	public static void main(String[] args)
	{
		SpringApplication.run(HotelApplication.class, args);
	}
}
