package seguridad;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.logging.Logger;

@SpringBootApplication
public class SeguridadAplicacion {
	private static final Logger log =
			Logger.getLogger(SeguridadAplicacion.class.getName());
	public static void main(String[] args) {
		new SpringApplicationBuilder(SeguridadAplicacion.class).run(args);
	}
}
