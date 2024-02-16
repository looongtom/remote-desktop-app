package service.impl;

import java.io.*;

import service.ConnectService;
import service.tcp.ShareClipboardClient;
import view.CreateFrameTCP;

import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.ClientFileSend;
import view.SendFile;

import javax.crypto.NoSuchPaddingException;

public class ConnectClientServiceImpl implements ConnectService {

    private Socket cSocket = null;
    private Socket cSocketClipboard = null;
    DataOutputStream psswrchk = null;
    DataInputStream verification = null;
    String verify = "";
    String width = "", height = "";
    private String ip = null;
    private String pass = "";
    private Date timeCreated;
    private List<String>driveNames=new ArrayList<>();
    private PrintWriter pw;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("port");

    public ConnectClientServiceImpl(String ip, String pass) {
        this.ip = ip;
        this.pass = pass;
//        start();
    }

    @Override
    @Deprecated
    public void InitConnectRemote() {
        System.out.println("is requesting connection to server");
        String portClient = resourceBundle.getString("client-port");
        try {
            cSocket = new Socket(this.ip,Integer.parseInt(portClient));
            cSocketClipboard = new Socket(this.ip, 9011);
            System.out.println("Connecting to the Server");
            psswrchk = new DataOutputStream(cSocket.getOutputStream());
            verification = new DataInputStream(cSocket.getInputStream());
            psswrchk.writeUTF(pass);
            verify = verification.readUTF();
            this.timeCreated = new Date();
        } catch (IOException e) {
            System.out.println("socket can not connect");
            e.printStackTrace();
            Thread.currentThread().stop();
        }
        if (verify.equals("valid")) {
            try {
                width = verification.readUTF();
                height = verification.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CreateFrameTCP abc = new CreateFrameTCP(this.ip, cSocket, width, height, timeCreated,driveNames);
            SendFile clientSendFile = new SendFile(this.ip, abc);
            clientSendFile.setVisible(true);            //            ShareClipboardClient shareClipboardClient = new ShareClipboardClient(cSocketClipboard);
            // add thread resolve share clipboard
            receiveDrives();
            initThreadShareClipboard();
        } else {
            System.out.println("enter the valid password");
            try {
                cSocket.close();
                Thread.currentThread().stop();
                // throw infor error pass
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Deprecated
    private void initThreadShareClipboard() {
        try {
            ShareClipboardClient shareClipboard = new ShareClipboardClient(cSocketClipboard);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
            System.out.println("error init thread share clipboard");
            try {
                cSocketClipboard.close();
                System.out.println("socket close");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Thread.currentThread().stop();
            System.out.println("thread destroy");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shareDrives(List<String> list) {

    }

    public void receiveDrives() {
        try {
            Socket socket = new Socket(this.ip, 9012);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                this.driveNames.add(line);
                runShareDriveWindow(line);
            }

            socket.close();
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                System.err.println("Connection reset by the server. Make sure the server is keeping the connection open.");
            } else {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void runShareDriveWindow(String command) {
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
            Logger.getLogger(ConnectClientServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
