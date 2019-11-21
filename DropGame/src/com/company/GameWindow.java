package com.company;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {
    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image gameOver;
    private static Image drop;
    private static Image drop1;
    private static Image restart;
    private static float drop_left = 300.0F;
    private static float drop_top = -100.0F;
    private static float drop_left1 = 300.0F;
    private static float drop_top1 = -100.0F;
    private static float drop_v = 10.0F;
    private static float drop_v1 = 10.0F;
    private static int score = 1;
    private static boolean end;
    private static float drop_width = 100.0F;
    private static float drop_height = 150.0F;
    private static float drop_width1 = 100.0F;
    private static float drop_height1 = 150.0F;
    private static boolean pause = false;
    private static float drop_speed_saved;
    private static float drop_speed_saved2;
    private static double mousecordX = 0.0D;
    private static double mousecordY = 0.0D;
    private static int direction = -1;
    private static boolean isRecorded;
    private static Entry nameEntry;
    private static Database db;
    public static boolean drawRecords = false;
    public static ArrayList<String> recordsLast = new ArrayList();

    public GameWindow() {
    }

    public static void main(String[] args) throws IOException {
        db = new Database("jdbc:mysql://localhost/kaplja?useLegacyDatetimeCode=false&serverTimezone=Europe/Helsinki", "root", "");
        db.init();
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("gameOver.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png")).getScaledInstance((int)drop_width, (int)drop_height, 1);
        drop1 = ImageIO.read(GameWindow.class.getResourceAsStream("drop1.png")).getScaledInstance((int)drop_width1, (int)drop_height1, 1);
        restart = ImageIO.read(GameWindow.class.getResourceAsStream("restart.png")).getScaledInstance(100, 100, 1);
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(3);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        last_frame_time = System.nanoTime();
        final GameWindow.GameField game_field = new GameWindow.GameField();
        onDirection();
        game_field.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 3) {
                    if (GameWindow.pause) {
                        GameWindow.pause = false;
                        GameWindow.drop_v = GameWindow.drop_speed_saved;
                        GameWindow.drop_v1 = GameWindow.drop_speed_saved2;

                        try {
                            Robot r = new Robot();
                            r.mouseMove((int)GameWindow.mousecordX, (int)GameWindow.mousecordY);
                        } catch (AWTException var13) {
                        }
                    } else {
                        GameWindow.drop_speed_saved = GameWindow.drop_v;
                        GameWindow.drop_speed_saved2 = GameWindow.drop_v1;
                        GameWindow.drop_v = 0.0F;
                        GameWindow.drop_v1 = 0.0F;
                        GameWindow.mousecordX = MouseInfo.getPointerInfo().getLocation().getX();
                        GameWindow.mousecordY = MouseInfo.getPointerInfo().getLocation().getY();
                        GameWindow.pause = true;
                    }
                }

                if (e.getButton() == 1) {
                    if (GameWindow.pause) {
                        return;
                    }

                    int x = e.getX();
                    int y = e.getY();
                    float drop_right = GameWindow.drop_left + (float)GameWindow.drop.getWidth((ImageObserver)null);
                    float drop_bottom = GameWindow.drop_left + (float)GameWindow.drop.getHeight((ImageObserver)null);
                    boolean is_drop = (float)x >= GameWindow.drop_left && (float)x <= drop_right && (float)y >= GameWindow.drop_top && (float)y <= drop_bottom;
                    float drop_right1 = GameWindow.drop_left1 + (float)GameWindow.drop.getWidth((ImageObserver)null);
                    float drop_bottom1 = GameWindow.drop_left1 + (float)GameWindow.drop.getHeight((ImageObserver)null);
                    boolean is_drop1 = (float)x >= GameWindow.drop_left1 && (float)x <= drop_right1 && (float)y >= GameWindow.drop_top1 && (float)y <= drop_bottom1;
                    if (is_drop) {
                        if (GameWindow.drop_height > 25.0F && GameWindow.drop_width > 50.0F) {
                            --GameWindow.drop_width;
                            GameWindow.drop_height -= 2.0F;

                            try {
                                GameWindow.dropResize();
                            } catch (IOException var12) {
                            }
                        }

                        GameWindow.drop_top = -100.0F;
                        GameWindow.drop_left = (float)((int)(Math.random() * (double)(game_field.getWidth() - GameWindow.drop.getWidth((ImageObserver)null))));
                        GameWindow.drop_v += 10.0F;
                        ++GameWindow.score;
                        GameWindow.onDirection();
                        GameWindow.game_window.setTitle("Score: " + GameWindow.score);
                    }

                    if (is_drop1) {
                        if (GameWindow.drop_height1 > 25.0F && GameWindow.drop_width1 > 50.0F) {
                            --GameWindow.drop_width1;
                            GameWindow.drop_height1 -= 2.0F;

                            try {
                                GameWindow.dropResize1();
                            } catch (IOException var11) {
                            }
                        }

                        GameWindow.drop_top1 = -100.0F;
                        GameWindow.drop_left1 = (float)((int)(Math.random() * (double)(game_field.getWidth() - GameWindow.drop1.getWidth((ImageObserver)null))));
                        GameWindow.drop_v1 += 10.0F;
                        ++GameWindow.score;
                        GameWindow.game_window.setTitle("Score: " + GameWindow.score);
                    }

                    if (GameWindow.end) {
                        boolean isRestart = x >= 175 && x <= 175 + GameWindow.restart.getWidth((ImageObserver)null) && y >= 300 && y <= 300 + GameWindow.restart.getHeight((ImageObserver)null);
                        if (isRestart) {
                            GameWindow.end = false;
                            GameWindow.score = 0;
                            GameWindow.game_window.setTitle("Score: " + GameWindow.score);
                            GameWindow.drop_top = -100.0F;
                            GameWindow.drop_left = (float)((int)(Math.random() * (double)(game_field.getWidth() - GameWindow.drop.getWidth((ImageObserver)null))));
                            GameWindow.drop_v = 10.0F;
                            GameWindow.drop_top1 = -100.0F;
                            GameWindow.drop_left1 = (float)((int)(Math.random() * (double)(game_field.getWidth() - GameWindow.drop1.getWidth((ImageObserver)null))));
                            GameWindow.drop_v1 = 10.0F;
                            GameWindow.drop_width = 100.0F;
                            GameWindow.drop_height = 150.0F;
                            GameWindow.drop_width1 = 100.0F;
                            GameWindow.drop_height1 = 150.0F;
                            GameWindow.isRecorded = false;
                            GameWindow.drawRecords = false;
                        }
                    }
                }

            }
        });
        nameEntry = new Entry();
        game_window.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                GameWindow.nameEntry.keyPress(e);
                if (GameWindow.nameEntry.isActive && !GameWindow.isRecorded && e.getKeyCode() == 10) {
                    GameWindow.db.addRecord(GameWindow.nameEntry.text, GameWindow.score);
                    GameWindow.isRecorded = true;
                    GameWindow.recordsLast = GameWindow.db.getRecords();
                    GameWindow.drawRecords = true;
                }

            }

            public void keyReleased(KeyEvent e) {
            }
        });
        game_window.add(game_field);
        game_window.setResizable(false);
        game_window.setVisible(true);
    }

    private static void dropResize() throws IOException {
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png")).getScaledInstance((int)drop_width, (int)drop_height, 1);
    }

    private static void dropResize1() throws IOException {
        drop1 = ImageIO.read(GameWindow.class.getResourceAsStream("drop1.png")).getScaledInstance((int)drop_width1, (int)drop_height1, 1);
    }

    private static int onDirection() {
        int rand = (int)(Math.random() * 2.0D + 1.0D);
        if (rand == 2) {
            direction = 1;
        } else {
            direction = -1;
        }

        return direction;
    }

    private static void onRepaint(Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (float)(current_time - last_frame_time) * 1.0E-9F;
        last_frame_time = current_time;
        drop_top += drop_v * delta_time;
        drop_left += (float)direction * drop_v * delta_time;
        drop_top1 += drop_v1 * delta_time;
        drop_left1 += (float)direction * drop_v1 * delta_time;
        g.drawImage(background, 0, 0, (ImageObserver)null);
        g.drawImage(drop, (int)drop_left, (int)drop_top, (ImageObserver)null);
        if (score >= 5) {
            g.drawImage(drop1, (int)drop_left1, (int)drop_top1, (ImageObserver)null);
        }

        if (drop_top > (float)game_window.getHeight() || drop_top1 > (float)game_window.getHeight()) {
            g.drawImage(gameOver, 280, 120, (ImageObserver)null);
            g.drawImage(restart, 175, 300, (ImageObserver)null);
            end = true;
        }

        if ((double)drop_left > 0.0D && drop_left + drop_width <= (float)game_window.getWidth()) {
            if ((double)drop_left1 <= 0.0D || drop_left1 + drop_width1 > (float)game_window.getWidth()) {
                if (direction == -1) {
                    direction = 1;
                } else {
                    direction = -1;
                }
            }
        } else if (direction == -1) {
            direction = 1;
        } else {
            direction = -1;
        }

        if (drawRecords) {
            g.setColor(new Color(255, 255, 255));

            for(int i = 0; i < recordsLast.size(); ++i) {
                g.drawString((String)recordsLast.get(i), 200, 25 + 25 * i);
            }
        }

        nameEntry.isActive = end;
        nameEntry.update(g);
    }

    private static class GameField extends JPanel {
        private GameField() {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            GameWindow.onRepaint(g);
            this.repaint();
        }
    }
}