package second_lab.gui;

import jakarta.annotation.PostConstruct;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import second_lab.open_cv.ContrastEnhancer;
import second_lab.open_cv.HistogramBuilder;
import second_lab.utils.ImageUtils;
import second_lab.open_cv.PhotoHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class MainFrame extends JFrame {
    JButton lightButton = new JButton("brightness fluctuations");
    JButton pointsButton = new JButton("find points");
    JButton edgesButton = new JButton("find edges");
    JButton histogramButton = new JButton("build histogram");
    JButton equalizeImgButton = new JButton("equalize image");
    JButton equalizeHistButton = new JButton("equalize histogram");
    JButton linContrastButton = new JButton("linear contrast");
    JButton linBuildHistButton = new JButton("hist for linear");
    JButton loadImageButton = new JButton("load an image");
    JButton saveButton = new JButton("save image");

    JTextField alpha = new JTextField(10);
    JTextField beta = new JTextField(10);

    JLabel curImgLabel = new JLabel();
    JLabel procImgLabel = new JLabel();

    static int labelWidth = 400;
    static int labelHeight = 500;

    PhotoHandler photoHandler;
    HistogramBuilder builder;
    ContrastEnhancer contrastEnhancer;

    @Autowired
    public MainFrame(PhotoHandler photoHandler, HistogramBuilder builder, ContrastEnhancer contrastEnhancer) {
        this.contrastEnhancer = contrastEnhancer;
        this.builder = builder;
        this.photoHandler = photoHandler;

        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addListeners() {
        lightButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            Mat processedMat = photoHandler.light(mat);
            setImgToSecondLabel(ImageUtils.matToImg(processedMat));
        });

        pointsButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            Mat processedMat = photoHandler.findPoints(mat);
            setImgToSecondLabel(ImageUtils.matToImg(processedMat));
        });

        equalizeImgButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            Mat processedMat = photoHandler.equalizeImage(mat);
            setImgToSecondLabel(ImageUtils.matToImg(processedMat));
        });

        edgesButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            Mat processedMat = photoHandler.findLines(mat);
            setImgToSecondLabel(ImageUtils.matToImg(processedMat));
        });

        linContrastButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            try {
                Mat processedMat = contrastEnhancer.enhanceContrast(mat,
                        Double.parseDouble(alpha.getText()),
                        Double.parseDouble(beta.getText()));
                setImgToSecondLabel(ImageUtils.matToImg(processedMat));
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, "Something wrong with input!");
            }
        });

        linBuildHistButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            try {
                Mat processedMat = builder.linBuildHistogram(mat,
                        Double.parseDouble(alpha.getText()),
                        Double.parseDouble(beta.getText()));
                setImgToSecondLabel(ImageUtils.matToImg(processedMat));
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, "Something wrong with input!");
            }
        });

        loadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Изображения", "jpg", "png", "jpeg", "bmp", "gif");
            fileChooser.setFileFilter(filter);
            int fileChooserResult = fileChooser.showOpenDialog(null);
            if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
                File img = fileChooser.getSelectedFile();
                try {
                    BufferedImage bufferedImage = ImageIO.read(img);
                    setImgToFirstLabel(bufferedImage);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "You're allowed to choose only images!");
                }
            }
        });

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int fileChooserResult = fileChooser.showOpenDialog(null);
            if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageUtils.save(((ImageIcon)procImgLabel.getIcon()).getImage(), fileChooser.getSelectedFile().toPath());
                }
                catch (IOException ex){
                    JOptionPane.showMessageDialog(null, "Something went wrong with autosave! Check chosen folder.");
                }
            }
        });

        histogramButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            Mat processedMat = builder.buildHistogram(mat);
            setImgToSecondLabel(ImageUtils.matToImg(processedMat));
        });

        equalizeHistButton.addActionListener(e -> {
            Mat mat = ImageUtils.imgToMat(((ImageIcon)curImgLabel.getIcon()).getImage());
            Mat processedMat = builder.equalizeHistogram(mat);
            setImgToSecondLabel(ImageUtils.matToImg(processedMat));
        });
    }

    @PostConstruct
    private void buildGui() throws IOException {
        setImgToFirstLabel(ImageIO.read(getClass().getResource("/blackScreen.jpg")));
        setImgToSecondLabel(ImageIO.read(getClass().getResource("/blackScreen.jpg")));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        curImgLabel.setPreferredSize(new Dimension(labelWidth, labelHeight));
        procImgLabel.setPreferredSize(new Dimension(labelWidth, labelHeight));
        alpha.setPreferredSize(new Dimension(30, 20));
        beta.setPreferredSize(new Dimension(30, 20));

        Border border = BorderFactory.createLineBorder(Color.RED, 2);
        curImgLabel.setBorder(border);
        procImgLabel.setBorder(border);

        this.add(curImgLabel, gbc);
        gbc.gridx = 3;
        this.add(procImgLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        this.add(lightButton, gbc);
        gbc.gridx++;
        this.add(pointsButton, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(edgesButton, gbc);
        gbc.gridx++;
        this.add(linContrastButton, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(histogramButton, gbc);
        gbc.gridx++;
        this.add(equalizeHistButton, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(loadImageButton, gbc);
        gbc.gridx++;
        this.add(saveButton, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(equalizeImgButton, gbc);
        gbc.gridx++;
        this.add(linBuildHistButton, gbc);
        gbc.gridy++;
        gbc.gridx--;
        this.add(alpha, gbc);
        gbc.gridx++;
        this.add(beta, gbc);
        gbc.gridx++;

        addListeners();
    }

    private void setImgToFirstLabel(Image image) {
        curImgLabel.setIcon(new ImageIcon(image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH)));
    }

    private void setImgToSecondLabel(Image image) {
        procImgLabel.setIcon(new ImageIcon(image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH)));
    }
}
