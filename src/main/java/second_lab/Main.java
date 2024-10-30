package second_lab;

import nu.pattern.OpenCV;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import second_lab.gui.MainFrame;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        OpenCV.loadLocally();

        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class).headless(false).run(args);
        MainFrame frame = context.getBean(MainFrame.class);
    }
}
