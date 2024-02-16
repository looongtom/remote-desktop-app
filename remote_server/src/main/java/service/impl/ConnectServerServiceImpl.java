package service.impl;

import service.ConnectService;
import service.tcp.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.ServerFileSend;

import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

public class ConnectServerServiceImpl implements ConnectService {

    ServerSocket socket = null;
    DataInputStream password = null;
    DataOutputStream verify = null;
    String width = "";
    String height = "";
    private String pass = "";
    private boolean isShareDrive = false;
    private java.util.List<String> listDrives=new ArrayList<>();
    private int serverWidthCurrentScreen = 0;
    private int serverHeightCurrentScreen = 0;
    private int serverDefaultWidthScreen = 1920;
    private int serverDefaultHeightScreen = 1080;
    private Process process;

    private SendScreen sendScreen = null;
    private ReceiveEvents receiveEvents = null;
    private Rectangle rectangle = null;
    private boolean isValidPassword = false;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("port");


    public ConnectServerServiceImpl(String pass, boolean isShareDrive, java.util.List<String> listDrives) {
        this.pass = pass;
        this.isShareDrive = isShareDrive;
        this.listDrives=listDrives;

        try {
            this.process = new ProcessBuilder("wmic", "path", "Win32_VideoController", "get", "CurrentHorizontalResolution,CurrentVerticalResolution").start();

            // Đọc output từ CMD
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append(" ");
            }

            String outputCMD = output.toString();
            String[] strArr = outputCMD.split("\\s+");

            this.serverDefaultWidthScreen = Integer.parseInt(strArr[2]);
            this.serverDefaultHeightScreen = Integer.parseInt(strArr[3]);

            this.serverWidthCurrentScreen = this.serverDefaultWidthScreen;
            this.serverHeightCurrentScreen = this.serverDefaultHeightScreen;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void InitConnectRemote() {

        // Gửi file
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerFileSend fileSend = new ServerFileSend();
                try {
                    fileSend.startServer();
                } catch (IOException ex) {
                    Logger.getLogger(ConnectServerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Gửi hình ảnh
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String portServer = resourceBundle.getString("serve-port");
                    socket = new ServerSocket(Integer.parseInt(portServer));
                    System.out.println("server is running with port = " + portServer + " Awaiting Connection from Client");
                    /*GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice gDev = gEnv.getDefaultScreenDevice();*/

                    /*Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    String width = "" + dim.getWidth();
                    String height = "" + dim.getHeight();
                    rectangle = new Rectangle(dim);*/

                    /*String width = "" + serverWidthCurrentScreen;
                    String height = "" + serverHeightCurrentScreen;
                    rectangle = new Rectangle(serverWidthCurrentScreen, serverHeightCurrentScreen);
                    robot = new Robot(gDev);*/

                    // initThreadShareClipBoardInServer
                    initThreadShareClipBoardInServer();

                    while (true) {
                        Socket sc = socket.accept();
                        password = new DataInputStream(sc.getInputStream());
                        verify = new DataOutputStream(sc.getOutputStream());
                        String pssword = password.readUTF();
                        if (pssword.equals(pass)) {
                            verify.writeUTF("valid");

                            /*GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
                            robot = new Robot(gDev);*/

                            Robot robot = new Robot();

                            String width = "" + serverDefaultWidthScreen;
                            String height = "" + serverDefaultHeightScreen;
                            verify.writeUTF(width);
                            verify.writeUTF(height);

                            rectangle = new Rectangle(serverWidthCurrentScreen, serverHeightCurrentScreen);
                            //init share drives
                            shareDrives(listDrives);
                            // init thread send screen
                            sendScreen = new SendScreen(sc, robot, rectangle);
                            // init thread receive screen
                            receiveEvents = new ReceiveEvents(sc, robot);
                            isValidPassword = true;
                        } else {
                            verify.writeUTF("Invalid");
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Vẽ
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try ( ServerSocket serv = new ServerSocket(25000)) {
                        System.out.println("waiting...");
                        try ( Socket socket = serv.accept()) {
                            BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                            System.out.println("client connected");
                            ImageIO.write(screencapture, "jpeg", socket.getOutputStream());
                            System.out.println("sent");
                        }
                    } catch (IOException | AWTException ex) {
                        System.out.println("Cannot get run screenshot server");
                    }
                }
            }

        });

        // Nhận resize từ client
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(9015);
                    boolean isFirstReceiveFromClient = true;
                    while (true) {
                        Socket socketReceiveDisplayResolution = serverSocket.accept();

                        if (isValidPassword && !isFirstReceiveFromClient) {
                            Scanner scanner = new Scanner(socketReceiveDisplayResolution.getInputStream());
                            serverWidthCurrentScreen = scanner.nextInt();
                            serverHeightCurrentScreen = scanner.nextInt();

                            String commandLine = "QRes.exe /x:" + serverWidthCurrentScreen + " /y:" + serverHeightCurrentScreen;
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", commandLine);
                            Process process = processBuilder.start();

                            int exitCode = process.waitFor();
                            System.out.println("Resize with exit code: " + exitCode);

                            /*GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
                            robot = new Robot(gDev);
                            sendScreen.setRobot(robot);
                            receiveEvents.setRobot(robot);*/

                            Robot robot = new Robot();
                            sendScreen.setRobot(robot);
                            receiveEvents.setRobot(robot);

                            rectangle = new Rectangle(serverWidthCurrentScreen, serverHeightCurrentScreen);
                            sendScreen.setRectangle(rectangle);

                            /*DisplayMode displayMode = gDev.getDisplayMode();
                            System.out.println("displayMode: " + displayMode.getWidth() + ", " + displayMode.getHeight());
                            System.out.println("client send resize: " + serverWidthCurrentScreen + ", " + serverHeightCurrentScreen);
                            System.out.println("robot hashcode1: " + robot.hashCode());*/
                        }

                        if (isFirstReceiveFromClient) {
                            PrintWriter pw = new PrintWriter(socketReceiveDisplayResolution.getOutputStream());
                            pw.println(serverDefaultWidthScreen);
                            pw.println(serverDefaultHeightScreen);
                            pw.flush();
                            isFirstReceiveFromClient = false;
                        }

                        socketReceiveDisplayResolution.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    private void initThreadShareClipBoardInServer() throws InterruptedException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        CountDownLatch serverReady = new CountDownLatch(1);
        // init thread share clipboard
        new Broadcast(serverReady).start();
        // wait main thread ready?
        serverReady.await();
        // init socket client for share clipboard - local server
        Socket socketClient = new Socket("127.0.0.1", 9011);
        // init thread share clipboard server equals client ShareClipboardClient
        new ShareClipboardClient(socketClient);
    }

    public void allowRdp(String command) {
        try {
            String portServer = resourceBundle.getString("serve-port");
            socket = new ServerSocket(Integer.parseInt(portServer));
            Socket s = socket.accept();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(command);
            bw.newLine();
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(ConnectServerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void shareDrives(java.util.List<String> list) {
        try {
            ServerSocket sc = new ServerSocket(9012);
            System.out.println("Server share clip start....");
            Socket s = sc.accept();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            for (String temp : list) {
                bw.write(temp);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception ex) {
            Logger.getLogger(ConnectServerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
