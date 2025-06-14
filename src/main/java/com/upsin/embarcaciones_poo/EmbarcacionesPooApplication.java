package com.upsin.embarcaciones_poo;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.upsin.embarcaciones_poo.gui.VistaLogin;
import io.github.cdimascio.dotenv.Dotenv;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EmbarcacionesPooApplication {

	public static void main(String[] args) {
                Dotenv dotenv = Dotenv.load(); //cargar el archivo .env
                //setear variables de entorno en tiempo de ejecucion
                System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
                System.setProperty("DATABASE_USER", dotenv.get("DATABASE_USER"));
                System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
                
                // poner modo oscuro en las vistas
                try{
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                }catch(Exception e){
                    System.out.println("Error: "+e);
                }
            
                ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(EmbarcacionesPooApplication.class)
                        .headless(false)
                        .web(WebApplicationType.NONE)
                        .run(args);
                
                SwingUtilities.invokeLater(() -> {
                        VistaLogin vistaLogin = contextoSpring.getBean(VistaLogin.class);
                        vistaLogin.setVisible(true);
                });
                
		SpringApplication.run(EmbarcacionesPooApplication.class, args);
	}

}
