package tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

// -- Для програми розробленої у ході виконання лабораторної роботи №1
// (частина 3) розробити візуальний інтерфейс засобами бібліотеки Swing. У
// програмі використати такі компоненти JFrame, JPanel, JButton, JLable,
// JTextField, JTable. Передбачити читання вхідних даних з файлу.
// Програма повинна включати обробку виключних ситуацій. Обробити 3
// типи виключень: 2 стандартних і 1 власне. Зі стандартних виключень можна
// взяти виключення пов’язане з відкриттям файлу і невірним форматом вхідних
// даних. Для власного виключення потрібно розробити клас наслідуваний від
// класу ArithmeticException. В програмі передбачити генерацію власного
// виключення при певних вхідних даних (придумати самостійно).++

// Власне виключення 
class MyArithmeticException extends ArithmeticException {
    public MyArithmeticException(String message) {
        super(message);
    }
}

// Вікно
public class Task3GUI extends JFrame {
    private JTable table;
    private JTextField fileField;
    private JLabel resultLabel;
    private JLabel inpLabel;

    public Task3GUI() {
        super("Перевірка симетрії матриці (з анімацією)");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Фон
        SatelliteOrbit background = new SatelliteOrbit();
        background.setLayout(new BorderLayout());
        // Панель
        JPanel uiPanel = new JPanel(new BorderLayout(10, 10));
        uiPanel.setOpaque(false);
        uiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Верхня панель
        JPanel top = new JPanel(new FlowLayout());
        top.setOpaque(false);
        inpLabel = new JLabel("Введіть ім’я файлу: ");
        inpLabel.setForeground(Color.WHITE);
        top.add(inpLabel);
        fileField = new JTextField(25);
        top.add(fileField);
        JButton loadButton = new JButton("Зчитати");
        top.add(loadButton);
        uiPanel.add(top, BorderLayout.NORTH);

        // Таблиця
        table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        uiPanel.add(scroll, BorderLayout.CENTER);

        // Нижня частина
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        JButton checkButton = new JButton("Перевірити симетрію");
        resultLabel = new JLabel("Результат: ");
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setOpaque(false);
        bottom.add(checkButton, BorderLayout.WEST);
        bottom.add(resultLabel, BorderLayout.CENTER);
        uiPanel.add(bottom, BorderLayout.SOUTH);

        background.add(uiPanel, BorderLayout.CENTER);
        setContentPane(background);

        // Обробники подій
        loadButton.addActionListener(e -> {
            try {
                readMatrixFromFile(fileField.getText());
                resultLabel.setText("Результат: матрицю зчитано успішно!");
            } catch (FileNotFoundException ex) {
                showError("Файл не знайдено!");
            } catch (NumberFormatException ex) {
                showError("Невірний формат даних!");
            } catch (MyArithmeticException ex) {
                showError(ex.getMessage());
            }
        });

        checkButton.addActionListener(e -> checkSymmetry());
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }

    private void readMatrixFromFile(String path)
            throws FileNotFoundException, NumberFormatException, MyArithmeticException {
        Scanner in = new Scanner(new File(path));
        int n = Integer.parseInt(in.nextLine().trim());
        if (n > 10)
            throw new MyArithmeticException("Розмір матриці занадто великий (>10)!");

        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            String[] parts = in.nextLine().trim().split("\\s+");
            if (parts.length != n)
                throw new NumberFormatException();
            for (int j = 0; j < n; j++)
                matrix[i][j] = Integer.parseInt(parts[j]);
        }
        in.close();

        DefaultTableModel model = new DefaultTableModel(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                model.setValueAt(matrix[i][j], i, j);

        table.setModel(model);
    }

    private void checkSymmetry() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int n = model.getRowCount();
        if (n == 0)
            return;

        StringBuilder result = new StringBuilder("<html>");
        for (int i = 0; i < n; i++) {
            boolean sym = true;
            for (int j = 0; j < n; j++) {
                int left = Integer.parseInt(model.getValueAt(i, j).toString());
                int right = Integer.parseInt(model.getValueAt(i, n - j - 1).toString());
                if (left != right) {
                    sym = false;
                    break;
                }
            }
            if (sym)
                result.append("Рядок ").append(i + 1).append(" симетричний.<br>");
        }

        if (result.toString().equals("<html>"))
            resultLabel.setText("Результат: немає симетричних рядків.");
        else {
            result.append("</html>");
            resultLabel.setText(result.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task3GUI().setVisible(true));
    }
}
