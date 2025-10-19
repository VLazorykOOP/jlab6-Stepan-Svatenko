package tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.Random;

public class SatelliteOrbit extends JPanel implements ActionListener {
    private double angle = 0;
    private final Timer timer;
    private final Point[] stars;
    private static final int STAR_COUNT = 200;

    public SatelliteOrbit() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        // зірки
        Random rand = new Random();
        stars = new Point[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Point(rand.nextInt(800), rand.nextInt(600));
        }

        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;

        // фон градієнт
        g2d.setPaint(new GradientPaint(0, 0, Color.BLACK, 0, h, new Color(10, 10, 40)));
        g2d.fillRect(0, 0, w, h);

        for (Point s : stars) {
            int brightness = 180 + (int) (Math.random() * 75); // невелике мерехтіння
            g2d.setColor(new Color(brightness, brightness, brightness));
            g2d.fillRect(s.x, s.y, 1, 1);
        }

        // планета і супутник
        int a = 200; // велика піввісь
        int b = 75; // мала піввісь
        int planetRadius = 100;
        int baseRadius = 30;

        // Координати супутника
        int x = (int) (cx + a * Math.cos(angle));
        int y = (int) (cy + b * Math.sin(angle));

        // Масштабування розміру супутника
        double scale = 1.0 - 0.3 * Math.sin(-angle);
        int radius = (int) (baseRadius * scale);

        // Планета з градієнтом
        float[] dist = { 0.0f, 1.0f };
        Color[] colors = {
                new Color(70, 130, 180), // світліший центр
                new Color(10, 20, 40) // темніші краї
        };
        RadialGradientPaint planetGrad = new RadialGradientPaint(
                new Point2D.Float(cx, cy),
                planetRadius,
                dist,
                colors);
        RadialGradientPaint moonGrad = new RadialGradientPaint(
                new Point2D.Float(x, y),
                baseRadius,
                dist,
                colors);

        // Пунктирна орбіта
        float[] dashPattern = { 8, 6 };
        g2d.setStroke(new BasicStroke(
                2.0f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                10.0f,
                dashPattern,
                0.0f));

        GradientPaint orbitGrad = new GradientPaint(
                cx, cy - b, new Color(60, 60, 80),
                cx, cy + b, new Color(150, 150, 150));
        g2d.setPaint(orbitGrad);

        Shape backOrbit = new Arc2D.Double(cx - a, cy - b, a * 2, b * 2, 0, 180, Arc2D.OPEN);
        Shape frontOrbit = new Arc2D.Double(cx - a, cy - b, a * 2, b * 2, 180, 180, Arc2D.OPEN);
        g2d.draw(backOrbit);

        // Супутник позаду планети
        if (y < cy) {
            g2d.setPaint(moonGrad);
            g2d.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        }

        g2d.setPaint(planetGrad);
        g2d.fillOval(cx - planetRadius, cy - planetRadius, planetRadius * 2,
                planetRadius * 2);

        // Блиск зверху
        g2d.setPaint(new RadialGradientPaint(
                new Point2D.Float(cx - 30, cy - 30),
                planetRadius / 2f,
                new float[] { 0f, 1f },
                new Color[] { new Color(255, 255, 255, 60), new Color(0, 0, 0, 0) }));
        g2d.fillOval(cx - planetRadius, cy - planetRadius, planetRadius * 2,
                planetRadius * 2);

        // Передня частина орбіти
        g2d.setPaint(orbitGrad);
        g2d.draw(frontOrbit);

        // Супутник спереду планети
        if (y > cy) {
            g2d.setPaint(moonGrad);
            g2d.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        }

        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 0.05;
        if (angle > 2 * Math.PI)
            angle -= 2 * Math.PI;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Orbit Simulation");
        frame.add(new SatelliteOrbit());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
