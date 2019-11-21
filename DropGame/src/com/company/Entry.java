package com.company;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Entry {
    public int x;
    public int y;
    public int textOffsetX = 10;
    public int textOffsetY;
    public int width = 100;
    public int height = 25;
    public String text = "";
    public String font = "TimesRoman";
    public int fontSize = 12;
    public int fontStyle = 2;
    public boolean isActive;

    public Entry() {
        this.textOffsetY = this.height / 2;
        this.isActive = false;
    }

    public void update(Graphics g) {
        if (this.isActive) {
            g.setColor(new Color(255, 0, 0));
            g.drawRect(this.x, this.y, this.width, this.height);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font(this.font, this.fontStyle, this.fontSize));
            g.drawString(this.text, this.x + this.textOffsetX, this.y + this.textOffsetY);
        }
    }

    public void keyPress(KeyEvent e) {
        if (this.isActive) {
            try {
                if (e.getKeyCode() == 8) {
                    this.text = this.text.substring(0, this.text.length() - 1);
                } else {
                    String var10001 = this.text;
                    this.text = var10001 + e.getKeyChar();
                }
            } catch (Exception var3) {
            }

        }
    }
}

