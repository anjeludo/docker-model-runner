package dev.danvega.hub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.MimeTypeUtils;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(ChatClient.Builder builder) {
		return args -> {
		    var client = builder.build();
            prueba(client);
			/*String response = client.prompt("When was Docker created?")
					.call()
					.content();

			System.out.println(response);*/
		};
	}

    public record Dni(String nombre, String apellido1, String apellido2) {}

    private void prueba(ChatClient client) {
         InputStreamResource image = null;
        try (FileInputStream fis = new FileInputStream("/home/angel/Downloads/image.jpeg")) {
            image = new InputStreamResource(fis);
            InputStreamResource finalImage = image;
            System.out.println(logTime());
            var dni = client.prompt()
                    .user(userMessage -> userMessage
                            .text("""
                                Please read the attached identity card and return the value in provided format
                                """)
                            .media(MimeTypeUtils.parseMimeType("image/jpeg"), finalImage))
                    .call()
                    .entity(Dni.class);
            System.out.println(dni);
            System.out.println(logTime());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String logTime() {
        java.time.Instant ahora = java.time.Instant.now();
        java.time.LocalTime horaLocal = java.time.LocalTime.ofInstant(ahora, java.time.ZoneId.systemDefault());
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
        return horaLocal.format(formatter);
    }
}
