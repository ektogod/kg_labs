package first_lab;

import javax.swing.*;

public class ColorConverter extends JFrame {
    public ColorConverter() {
        JColorChooser colorChooser = new JColorChooser();
        add(colorChooser);
        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        ColorConverter window = new ColorConverter();
    }
}


