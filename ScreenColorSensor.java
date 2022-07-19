/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Taavet
 */
public class ScreenColorSensor extends JFrame implements MouseMotionListener {

    String strTitle;
    boolean isEnabled;
    Robot robot;
    Thread thread;
    ArrayList<ScreenColorSensorListener> listeners;

    public ScreenColorSensor(String strTitle) {
        super();
        try {
            robot = new Robot();
            listeners = new ArrayList();
            this.strTitle = strTitle;
            createGUI();

        } catch (AWTException ex) {
            Logger.getLogger(ScreenColorSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createGUI() {
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBackground(new Color(0, 0, 0, 1));
        setSize(50, 50);
        setLayout(null);
        setCursor(new Cursor(Cursor.MOVE_CURSOR));
        createTitle();
        createRect();
        addMouseMotionListener(this);
        setLocationRelativeTo(null);
        setEnabled(true);
    }

    private void createTitle() {
        JLabel lblTitle = new JLabel(strTitle, SwingConstants.CENTER);
        lblTitle.setForeground(Color.GREEN);
        lblTitle.setBounds(0, 0, 50, 30);
        add(lblTitle);
    }

    private void createRect() {
        JLabel lblLine = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                //g.setColor(new Color(0,0,0, 10));
                g.fillRect(15, 0, 20, 20);
                g.clearRect(25 - 2, 10 - 2, 5, 5);
            }
        };
        lblLine.setBounds(0, 30, 50, 70);
        add(lblLine);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int X = me.getX() + getX() - 25;
        int Y = me.getY() + getY() - 15;
        setLocation(X, Y);
    }

    @Override
    public void mouseMoved(MouseEvent me) {

    }

    @Override
    public void setEnabled(boolean isEnabled) {
        if (!this.isEnabled) {
            enableSensor();
        }
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void enableSensor() {
        thread = new Thread() {
            @Override
            @SuppressWarnings("SleepWhileInLoop")
            public void run() {
                Color lastColor = new Color(0, 0, 0);
                while (isEnabled()) {
                    Color colorAtSensor = getColorAtSensor();
                    if (differentColors(lastColor, colorAtSensor)) {
                        lastColor = colorAtSensor;
                        notifyListeners(colorAtSensor);
                    }
                }
            }
        };
        thread.start();
    }

    public Color getColorAtSensor() {
        return robot.getPixelColor(getX() + 25, getY() + 30 + 10);
    }

    private boolean differentColors(Color color1, Color color2) {
        return color1.getRed() != color2.getRed()
                && color1.getGreen() != color2.getGreen()
                && color1.getBlue() != color2.getBlue();
    }

    public void addSensorListener(ScreenColorSensorListener sensorListener) {
        listeners.add(sensorListener);
    }

    public void removeSensorListener(ScreenColorSensorListener sensorListener) {
        listeners.remove(sensorListener);
    }

    private void notifyListeners(Color color) {
        listeners.forEach(listener -> listener.onSensorEvent(color));
    }

}
