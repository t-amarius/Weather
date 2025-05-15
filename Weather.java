import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import java.awt.*;

public class Weather {
    private static final Font mainFont = new Font("Segue UI", Font.PLAIN, 14);

    public static void main(String[] args) {
        JWindow loadingScreen = getJWindow();

        try {
            Thread.sleep(4000);
        }  catch (InterruptedException timerexcp) {
            throw new RuntimeException(timerexcp);
        }

        loadingScreen.dispose();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("Weather Data Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout(10, 10));

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(mainFont);
        outputArea.setBackground(new Color(255, 255, 255));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel topPanel = getJPanel(frame);

        ImageIcon openIcon;
        try {
            openIcon = new ImageIcon(Objects.requireNonNull(Weather.class.getResource("/icons/open.png")));
            if (openIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                openIcon = new ImageIcon(openIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            openIcon = new ImageIcon();
        }

        JButton openButton = new JButton("Open Weather Data File", openIcon);
        openButton.setFont(mainFont);
        openButton.setForeground(Color.WHITE);
        openButton.setBackground(new Color(33, 150, 243));
        openButton.setBorderPainted(false);
        openButton.setFocusPainted(false);
        openButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(mainFont);
        statusLabel.setForeground(Color.WHITE);

        topPanel.add(openButton);
        topPanel.add(statusLabel);

        JButton filterButton = new JButton("Filter Data");
        filterButton.setFont(mainFont);
        filterButton.setForeground(Color.WHITE);
        filterButton.setBackground(new Color(255, 152, 0));
        filterButton.setBorderPainted(false);
        filterButton.setFocusPainted(false);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topPanel.add(filterButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
                }
                public String getDescription() {
                    return "CSV Files (*.csv)";
                }
            });

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();
                String outputFile = "weather_summary.txt";
                processWeatherData(inputFile, outputFile, outputArea, statusLabel, frame, topPanel);
            }
        });

        filterButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, 
                "Enter month (YYYY-MM) or range (YYYY-MM-DD to YYYY-MM-DD):", 
                "Filter Data", 
                JOptionPane.PLAIN_MESSAGE);
            
            if (input != null && !input.trim().isEmpty()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
                    }
                    public String getDescription() {
                        return "CSV Files (*.csv)";
                    }
                });

                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File inputFile = fileChooser.getSelectedFile();
                    String outputFile = "weather_filtered_summary.txt";
                    processWeatherDataWithFilter(inputFile, outputFile, outputArea, statusLabel, frame, topPanel, input.trim());
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JWindow getJWindow() {
        JWindow loadingScreen = new JWindow();
        loadingScreen.setBackground(new Color(0, 0, 0, 0));
        loadingScreen.setSize(300, 300);

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 0));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(0, 255, 255));
                g2d.setStroke(new BasicStroke(8));

                int x = 50;
                int y = 50;
                int width = 200;
                int height = 200;

                g2d.drawLine(x, y, x + width/4, y + height);
                g2d.drawLine(x + width/4, y + height, x + width/2, y + height/2);
                g2d.drawLine(x + width/2, y + height/2, x + 3*width/4, y + height);
                g2d.drawLine(x + 3*width/4, y + height, x + width, y);
            }
        };

        logoPanel.setOpaque(false);
        loadingScreen.add(logoPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        loadingScreen.setLocation(
            (screenSize.width - loadingScreen.getWidth()) / 2,
            (screenSize.height - loadingScreen.getHeight()) / 2
        );

        loadingScreen.setVisible(true);
        return loadingScreen;
    }

    private static JPanel getJPanel(JFrame frame) {
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(100, 181, 246), w, h, new Color(30, 136, 229));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));
        return topPanel;
    }

    private static void processWeatherData(File inputFile, String outputFile, JTextArea outputArea, JLabel statusLabel, JFrame frame, JPanel topPanel) {
        double maxTemp = Double.MIN_VALUE;
        double minTemp = Double.MAX_VALUE;
        double totalAvgTemp = 0;
        ArrayList<Double> avgTemps = new ArrayList<>();
    
        StringBuilder display = new StringBuilder();
    
        try {
            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(outputFile);
    
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip header
            }
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
    
                String date = data[0];
                double highTemp = Double.parseDouble(data[1]);
                double lowTemp = Double.parseDouble(data[2]);
                double avgTemp = (highTemp + lowTemp) / 2;
    
                maxTemp = Math.max(maxTemp, highTemp);
                minTemp = Math.min(minTemp, lowTemp);
                totalAvgTemp += avgTemp;
                avgTemps.add(avgTemp);
    
                String record = String.format("%s: High: %.1f°F, Low: %.1f°F, Average: %.1f°F%n",
                        date, highTemp, lowTemp, avgTemp);
                writer.print(record);
                display.append(record);
            }
    
            // Calculate median
            Collections.sort(avgTemps);
            double medianTemp;
            int n = avgTemps.size();
            if (n % 2 == 0) {
                medianTemp = (avgTemps.get(n / 2 - 1) + avgTemps.get(n / 2)) / 2.0;
            } else {
                medianTemp = avgTemps.get(n / 2);
            }
    
            String summary = String.format("\nWeather Data Summary%n%n" +
                            "📊 Number of days analyzed: %d%n" +
                            "🌡️ Highest temperature: %.1f°F%n" +
                            "❄️ Lowest temperature: %.1f°F%n" +
                            "📈 Overall average temperature: %.1f°F%n" +
                            "📊 Median average temperature: %.1f°F%n",
                    avgTemps.size(), maxTemp, minTemp, totalAvgTemp / avgTemps.size(), medianTemp);
    
            writer.print(summary);
            display.append(summary);
    
            scanner.close();
            writer.close();
    
            outputArea.setText(display.toString());
            statusLabel.setText("✅ Analysis complete - " + inputFile.getName());
    
            JButton saveButton = getJButton(statusLabel, frame, display);
            topPanel.add(saveButton);
    
        } catch (FileNotFoundException weatherexception) {
            String error = "❌ Error: Could not access file - " + weatherexception.getMessage();
            outputArea.setText(error);
            statusLabel.setText("Error loading file");
        }
    }

    private static JButton getJButton(JLabel statusLabel, JFrame frame, StringBuilder display) {
        JButton saveButton = new JButton("Save Summary");
        saveButton.setFont(mainFont);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveButton.addActionListener(ev -> {
            JFileChooser fileSaver = new JFileChooser();
            fileSaver.setSelectedFile(new File("weather_summary.txt"));
            fileSaver.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
                }
                public String getDescription() {
                    return "Text Files (*.txt)";
                }
            });

            int saveResult = fileSaver.showSaveDialog(frame);
            if (saveResult == JFileChooser.APPROVE_OPTION) {
                try {
                    File saveFile = fileSaver.getSelectedFile();
                    PrintWriter saveWriter = new PrintWriter(saveFile);
                    saveWriter.print(display);
                    saveWriter.close();
                    statusLabel.setText("✅ Summary saved to " + saveFile.getName());
                } catch (FileNotFoundException e) {
                    statusLabel.setText("❌ Error saving summary file");
                }
            }
        });
        return saveButton;
    }

    private static boolean filterMatches(String date, String filter) {
        if (filter.contains("to")) {
            String[] parts = filter.split("to");
            return date.compareTo(parts[0].trim()) >= 0 && date.compareTo(parts[1].trim()) <= 0;
        } else {
            return date.startsWith(filter);
        }
    }

    private static void processWeatherDataWithFilter(File inputFile, String outputFile, JTextArea outputArea, JLabel statusLabel, JFrame frame, JPanel topPanel, String filter) {
        double maxTemp = Double.MIN_VALUE;
        double minTemp = Double.MAX_VALUE;
        double totalAvgTemp = 0;
        int recordCount = 0;

        StringBuilder display = new StringBuilder();

        try {
            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(outputFile);

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                String date = data[0];
                double highTemp = Double.parseDouble(data[1]);
                double lowTemp = Double.parseDouble(data[2]);
                double avgTemp = (highTemp + lowTemp) / 2;

                if (filterMatches(date, filter)) {
                    maxTemp = Math.max(maxTemp, highTemp);
                    minTemp = Math.min(minTemp, lowTemp);
                    totalAvgTemp += avgTemp;
                    recordCount++;

                    String record = String.format("%s: High: \u001B%.1f°F\u001B, Low: \u001B%.1f°F\u001B, Average: %.1f°F%n",
                            date, highTemp, lowTemp, avgTemp);
                    writer.print(record);
                    display.append(record);
                }
            }

            String summary = String.format("\nFiltered Weather Summary for %s%n%n" +
                            "📊 Number of days analyzed: %d%n" +
                            "🌡️ Highest temperature: \u001B%.1f°F\u001B%n" +
                            "❄️ Lowest temperature: \u001B%.1f°F\u001B%n" +
                            "📈 Overall average temperature: %.1f°F%n",
                    filter, recordCount, maxTemp, minTemp, recordCount == 0 ? 0 : totalAvgTemp / recordCount);

            writer.print(summary);
            display.append(summary);

            scanner.close();
            writer.close();

            outputArea.setText(display.toString());
            statusLabel.setText("✅ Filtered analysis complete - " + inputFile.getName());
            JButton saveButton = getJButton(statusLabel, frame, display);
            topPanel.add(saveButton);

        } catch (FileNotFoundException e) {
            String error = "❌ Error: Could not access file - " + e.getMessage();
            outputArea.setText(error);
            statusLabel.setText("Error loading file");
        }
    }
}
