package sample.trey;

/**
 * Created by MrTreb on 03.07.2015.
 */


import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    public void newAdd(){

    }
    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray(); //���������� ������ ���� ��� ������

            java.awt.Image image = null; //����� ����������� ������
            try {
                image = ImageIO.read(new File("src/sexy_girl.png"));
            } catch (IOException ex) {
                System.out.println(ex);
            }
            //����� ������ �������� ����
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    hide(stage);
                }
            });
            // �� �������� ���� ������ � ����
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };
            //������� �� ������� �������� �� ����
            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            // ���� ����
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);//���������� �������
            popup.add(showItem);
            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);//���������� �������
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

    public void newAd(){
        trayIcon.displayMessage("NOVOE","SMOTRI SUKA", java.awt.TrayIcon.MessageType.INFO);
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

