package net.autoforkey.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by andrei on 09.07.15.
 */
public class Alert implements Runnable {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JFrame frame = null;

    private String text;
    private int time = 0;

    public Alert(JFrame frame, int time){
        this.time = time;
        this.frame = frame;
    }

    public Alert(String text, int time){
        this.time = time;
        this.text = text;
    }

    public Alert(String text){
        this.text = text;
    }

    public void show() {


        frame = new JFrame("INFORMATION");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            //    System.exit(0);
            }
        });


        //Создается панель,
        //которая будет содржать информацию о IP адресе
        JPanel panel = new JPanel();
        //добавление границы к панели
        panel.setBorder(BorderFactory.createTitledBorder("INFORMATION"));
        panel.add(new JLabel("          " + text + "          "));

        Button btn = new Button("OK");
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.setVisible(false);
            }
        });
        panel.add(btn);

        //Добавление панели к фрейму
        frame.getContentPane().add(panel);


        //метод рack(); сообщает Swing о том,
        //что нужно придать компонентам необходимые размеры для
        //правильного помещения их в форму.
        //Другой способ - вызвать setSize(int width, int height).
        frame.pack();

        //Перемещаем наше окно в центр
        frame.setBounds((int) ((screenSize.getWidth() - frame.getWidth()) / 2), (int) ((screenSize.getHeight() - frame.getHeight()) / 2), frame.getWidth(), frame.getHeight());
        //Для того, чтобы увидеть окно на экране
        //вы должны вызвать метод setVisible(true)
        frame.setVisible(true);

        if (time != 0){
            new Thread(new Alert(frame, time)).start();
        }


    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }

        frame.setVisible(false);
        //System.exit(0);

    }
}
