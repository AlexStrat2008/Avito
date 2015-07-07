package sample.trey;

/**
 * Created by MrTreb on 03.07.2015.
 */


import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;


/**
 * Created by MrTreb on 03.07.2015.
 */
public class MyTrayIcon extends JDialog {

    private TrayIcon trayIcon;
    private App app;

    public MyTrayIcon(App app) {
        this.app = app;
        createTrayIcon();
    }

    private void createTrayIcon() {
        if (SystemTray.isSupported()) {

            Stage stage = app.getPrimaryStage();

            SystemTray tray = SystemTray.getSystemTray(); //подгрузили систем трей для работы
            java.awt.Image image = null; //мутим изображение иконки
            try {
                image = ImageIO.read(new File("src/sexy_girl.png"));
            } catch (IOException ex) {
                System.out.println(ex);
            }
            //ловим момент закрытия окна
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    hide(stage);
                }
            });
            // по закрытию окна уходим в трей
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };
            //листнер на событие открытия из трея
            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("ASFSASFFSAFAF");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                app.openMainWindow();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
            };
            // меню трея
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);//обработчик события
            popup.add(showItem);
            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);//обработчик события
            popup.add(closeItem);
            trayIcon = new TrayIcon(image, "Avito App", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(showListener);
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
//            MouseAdapter mouseAdapter = new MouseAdapter() {
//
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    //systemTray.remove(trayIcon);
//
//                    trayIcon.displayMessage("System Tray App","Simple Demo", java.awt.TrayIcon.MessageType.INFO);
//                }
//            };

//            trayIcon.addMouseListener(mouseAdapter);
        }
    }

    public void newAd(int count){
        trayIcon.displayMessage("Уведомление", "Новых объявлений: " + count, java.awt.TrayIcon.MessageType.INFO);
    }

    public void showProgramIsMinimizedMsg() {
        if (true) {
            trayIcon.displayMessage("Some message.",
                    "Some other message.",
                    TrayIcon.MessageType.INFO);
           // firstTime = false;
        }
    }
    private void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            }
        });
    }
}

