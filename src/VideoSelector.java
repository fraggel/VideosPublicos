import com.jcraft.jsch.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by u028978 on 08/02/2018.
 */
public class VideoSelector {
    private JList list1;
    private JButton borrarButton;
    private JTextField textField1;
    private JButton añadirButton;
    private JButton recargarListaReproducciónButton;
    private JPanel panel1;
    DefaultListModel dlm =null;
    private static JFrame frame = null;

    public VideoSelector() {
        dlm = new DefaultListModel();
        ejecutarComandoSSHRetorno("cat /home/pi/youtubeURLs.txt","192.168.1.16");
        list1.setModel(dlm);

        recargarListaReproducciónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyFile("192.168.1.16");
                //ejecutarComandoSSH("cat /root/youtubeURLs.txt","192.168.1.16");
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlm.removeElement(
                        list1.getSelectedValue() );
            }
        });
        añadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlm.addElement(textField1.getText());
                textField1.setText("");
            }
        });

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    try {
                        Desktop.getDesktop().browse(new URL((String) list1.getSelectedValue()).toURI());
                    }catch(Exception e2){

                    }
                }
            }
        });
    }

    private void copyFile(String s) {
        try {
            Runtime rt=Runtime.getRuntime();
            new File("c:\\HerliautomocionVideoScreen\\playlist.txt");
            FileOutputStream fos2 = new FileOutputStream(new File("c:\\HerliautomocionVideoScreen\\playlist.txt"));
            fos2.flush();
            fos2.close();
            fos2 = new FileOutputStream(new File("c:\\HerliautomocionVideoScreen\\playlist.txt"));
            FileOutputStream fos = new FileOutputStream(new File("c:\\HerliautomocionVideoScreen\\youtubeURLs.txt"));
            for (int x = 1; x < dlm.getSize(); x++) {
                Process exec = Runtime.getRuntime().exec("C:\\HerliautomocionVideoScreen\\youtube-dl -g -f 18 \""+dlm.getElementAt(x)+"\"");
                StreamGobbler inputGobbler = new StreamGobbler(exec.getInputStream(), "INPUT");
                inputGobbler.start();
                exec.waitFor();
                fos2.write(("omxplayer -b "+inputGobbler.getLinea().trim()+"\n").getBytes());
                fos.write((dlm.getElementAt(x)+"\n").getBytes());
            }
            fos.flush();
            fos.close();
            fos2.flush();
            fos2.close();

            rt.exec("C:\\HerliautomocionVideoScreen\\pscp -pw ak47cold C:\\HerliautomocionVideoScreen\\youtubeURLs.txt pi@192.168.1.16:/home/pi/youtubeURLs.txt");
            rt.exec("C:\\HerliautomocionVideoScreen\\pscp -pw ak47cold C:\\HerliautomocionVideoScreen\\playlist.txt pi@192.168.1.16:/home/pi/playlist.txt");

            ejecutarComandoSSH("pkill -f \"omxplayer\"","192.168.1.16");
            ejecutarComandoSSH("pkill -f \"playvideos.sh\"","192.168.1.16");
            ejecutarComandoSSH("nohup /home/pi/playvideos.sh &","192.168.1.16");

            dlm.clear();
            ejecutarComandoSSHRetorno("cat /home/pi/youtubeURLs.txt","192.168.1.16");
            list1.setModel(dlm);
            JOptionPane.showMessageDialog(frame, "Fichero Copiado correctamente, la lista se está actualizando.");
        }catch(Exception e){}
    }

    public static void main(String[] args) {
        frame = new JFrame("Selector Videos Herliautomoción");
        frame.setResizable(false);
        frame.setContentPane(new VideoSelector().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - frame.getWidth()) / 3);
                int y = (int) ((dimension.getHeight() - frame.getHeight()) / 3);
                frame.setLocation(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.pack();
        frame.setVisible(true);
    }
    public void ejecutarComandoSSH(String comando,String nombreMaquina){
        UserInfo ui = new MyUserInfo("pi", "ak47cold", null);
        JSch jsch = null;
        Session session = null;
        try {
            jsch = new JSch();
            session = jsch.getSession("pi", nombreMaquina, 22);
            session.setUserInfo(ui);
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(comando);

            // X Forwarding
            // channel.setXForwarding(true);

            //channel.setInputStream(System.in);
            channel.setInputStream(null);

            //channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            /*byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }*/
            channel.disconnect();
            session.disconnect();
        }catch (Exception e){
        } finally {
            if (session != null) {
                try {
                    session.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }
    public void ejecutarComandoSSHRetorno(String comando, String nombreMaquina){
        UserInfo ui = new MyUserInfo("pi", "ak47cold", null);
        JSch jsch = null;
        Session session = null;
        try {
            jsch = new JSch();
            session = jsch.getSession("pi", nombreMaquina, 22);
            session.setUserInfo(ui);
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(comando);

            // X Forwarding
            // channel.setXForwarding(true);

            //channel.setInputStream(System.in);
            channel.setInputStream(null);

            channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();


            channel.connect();
            String tmpStr="";
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    tmpStr=new String(tmp, 0, i);
                    //list1.ensureIndexIsVisible(dlm.getSize());
                }
                String[] split = tmpStr.split("\n");
                for(int x=0;x<split.length;x++){
                    dlm.addElement(split[x]);
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

            channel.disconnect();
            session.disconnect();
        }catch (Exception e){
        } finally {
            if (session != null) {
                try {
                    session.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }

}
