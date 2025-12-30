package in.MohdSohel.Server_Background_Image_Remover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServerBackgroundImageRemoverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerBackgroundImageRemoverApplication.class, args);
	}

}
