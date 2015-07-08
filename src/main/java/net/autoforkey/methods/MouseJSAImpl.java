package net.autoforkey.methods;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Работа с мышкой
 * Created by andrei on 05.07.15.
 */
public class MouseJSAImpl extends SystemJSAImpl implements MouseJSA {

    Robot robot = SystemJSAImpl.robot;
    private int[] mouseButton = {InputEvent.BUTTON1_MASK, InputEvent.BUTTON2_MASK, InputEvent.BUTTON3_MASK};

    @Override
    public void click() {
        down();
        up();
    }

    @Override
    public void click(int x, int y) {
        move(x, y);
        click();
    }

    @Override
    public void click(int button) {
        down(mouseButton[button]);
        up(mouseButton[button]);
    }

    @Override
    public void click(int button, int x, int y) {
        move(x, y);
        click(mouseButton[button]);
    }

    @Override
    public void dblClick() {
        click();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {}

        click();
    }

    @Override
    public void dblClick(int x, int y) {
        click(x, y);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {}
        click();
    }

    @Override
    public void dblClick(int button) {
        click(mouseButton[button]);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {}
        click(mouseButton[button]);
    }

    @Override
    public void dblClick(int button, int x, int y) {
        click(mouseButton[button], x, y);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {}
        click(mouseButton[button]);
    }

    @Override
    public void down() {
        robot.mousePress(mouseButton[0]);
    }

    @Override
    public void down(int x, int y) {
        move(x, y);
        down();
    }

    @Override
    public void down(int button) {
        robot.mousePress(mouseButton[button]);

    }

    @Override
    public void down(int button, int x, int y) {
        move(x, y);
        down(mouseButton[button]);
    }

    @Override
    public void up() {
        robot.mouseRelease(mouseButton[0]);
    }

    @Override
    public void up(int x, int y) {
        move(x, y);
        up();
    }

    @Override
    public void up(int button) {
        robot.mouseRelease(mouseButton[button]);
    }

    @Override
    public void up(int button, int x, int y) {
        move(x, y);
        up(mouseButton[button]);
    }

    @Override
    public void move(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void move(int x, int y, int time) {
        int timeSleep = 10;
        Point startPoint = getLoc();

        for (int i = 0; i <= time; i += timeSleep){
            int newX = (int) (startPoint.getX() + (x - startPoint.getX()) * ((float) i / time));
            int newY = (int) (startPoint.getY() + (y - startPoint.getY()) * ((float) i / time));
            move(newX, newY);

            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {}
        }

    }

    @Override
    public Point getLoc() {
        return MouseInfo.getPointerInfo().getLocation();

    }

}
