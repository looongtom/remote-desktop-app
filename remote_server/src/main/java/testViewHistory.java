
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.LogConnectService;
import service.impl.ConnectClientServiceImpl;
import service.impl.LogConnectServiceImpl;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author trinh
 */
public class testViewHistory {

    /**
     * @param args the command line arguments
     */
    public void shareDriveWindow(String command) {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", command);
        try {
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(testViewHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteDrive(List<String> listDriveNames) {
        String temp = "net use %s: /delete";

        for (String driveName : listDriveNames) {
            String command = String.format(temp, driveName);
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", command);
            try {
                builder.redirectErrorStream(true);
                Process p = builder.start();
            } catch (IOException ex) {
                Logger.getLogger(testViewHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String[] args) {

    }

}
