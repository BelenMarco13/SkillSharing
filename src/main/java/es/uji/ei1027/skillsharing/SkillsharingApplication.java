package es.uji.ei1027.skillsharing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.logging.Logger;

@SpringBootApplication
public class SkillsharingApplication {
	private static final Logger log =
			Logger.getLogger(SkillsharingApplication.class.getName());
	public static void main(String[] args) {
		new SpringApplicationBuilder(SkillsharingApplication.class).run(args);
	}
}
