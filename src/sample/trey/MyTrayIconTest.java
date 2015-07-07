package sample.trey;

import junit.framework.TestCase;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import  sample.trey.MyTrayIcon;

import static org.junit.Assert.*;

/**
 * Created by tanyastarshova on 07.07.2015.
 */
public class MyTrayIconTest extends TestCase {

    @Test
    public void testCreateTrayIcon() throws Exception {
        SystemTray tray = SystemTray.getSystemTray(); //подгрузили систем трей для работы

        java.awt.Image image = null; //мутим изображение иконки
        try {
            image = ImageIO.read(new File("/src/sample/image/nophoto.jpg"));
        } catch (IOException ex) {
            System.out.println(ex);
        }



    }

    @Test
    public void testNewAd() throws IOException {

        int count = 7;
        TrayIcon trayIcon;
        java.awt.Image image = ImageIO.read(new File("C:/Users/tanyastarshova/Desktop/mate.png"));
        System.out.println("Success!");

        PopupMenu popup = new PopupMenu();

        MenuItem pos1 = new MenuItem("Position1");
        MenuItem pos2 = new MenuItem("Position2");
        MenuItem pos3 = new MenuItem("Position3");

        popup.add(pos1);
        popup.add(pos2);
        popup.add(pos3);
        trayIcon = new TrayIcon(image, "Avito App", popup);
        trayIcon.displayMessage("Уведомление", "Новых объявлений: " + count, java.awt.TrayIcon.MessageType.INFO);

    }
}