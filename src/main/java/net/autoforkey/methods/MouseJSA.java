package net.autoforkey.methods;

import java.awt.*;
import java.util.List;

/**
 * Created by andrei on 05.07.15.
 */
public interface MouseJSA {

    /**
     * click
     */
    public void click();

    /**
     * @param x
     * @param y
     */
    public void click(int x, int y);

    /**
     * click мышкой
     * @param button 0 - левая, 1 - средняя, 2 - правая кнопка мышы
     */
    public void click(int button);

    /**
     * @param button 0 - левая, 1 - средняя, 2 - правая кнопка мышы
     * @param x
     * @param y
     */
    public void click(int button, int x, int y);

    /**
     * Двойной клик
     */
    public void dblClick();

    public void dblClick(int x, int y);

    /**
     * Двойной клик
     * @param button 0 - левая, 1 - средняя, 2 - правая кнопка мышы
     */
    public void dblClick(int button);

    /**
     *
     * @param button 0 - левая, 1 - средняя, 2 - правая кнопка мышы
     * @param x
     * @param y
     */
    public void dblClick(int button, int x, int y);

    /**
     * Зажать мышку
     */
    public void down();

    /**
     * Зажать мышку в координатах
     * @param x
     * @param y
     */
    public void down(int x, int y);

    /**
     * Зажать мышку
     * @param button 0 - левая, 1 - средняя, 2 - правая кнопка мышы
     */
    public void down(int button);

    public void down(int button, int x, int y);

    /**
     * Отпустить мышку
     */
    public void up();

    public void up(int x, int y);

    /**
     * Отпустить мышку
     * @param button 0 - левая, 1 - средняя, 2 - правая кнопка мышы
     */
    public void up(int button);

    public void up(int button, int x, int y);

    /**
     * Переместить мышку в координаты
     * @param x
     * @param y
     */
    public void move(int x, int y);

    /**
     * плавно передвигает за определённое время
     * @param x
     * @param y
     * @param time
     */
    public void move(int x, int y, int time);

    /**
     * получить координаты курсора
     */
    public Point getLoc();

}
