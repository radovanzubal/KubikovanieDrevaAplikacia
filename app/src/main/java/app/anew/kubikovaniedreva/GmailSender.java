package app.anew.kubikovaniedreva;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;


import com.tutego.jrtf.Rtf;
import com.tutego.jrtf.RtfPicture;
import com.tutego.jrtf.RtfText;
import com.tutego.jrtf.RtfTextPara;
import com.tutego.jrtf.RtfUnit;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfDocfmt.*;
import static com.tutego.jrtf.RtfHeader.*;
import static com.tutego.jrtf.RtfInfo.*;
import static com.tutego.jrtf.RtfFields.*;
import static com.tutego.jrtf.RtfPara.*;
import static com.tutego.jrtf.RtfSectionFormatAndHeaderFooter.*;
import static com.tutego.jrtf.RtfText.*;
import static com.tutego.jrtf.RtfUnit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.Security;
import java.util.Properties;
//import com.tutego.jrtf.RtfUnit;
//import static com.tutego.jrtf.RtfPicture.PictureType.AUTOMATIC;
//import static com.tutego.jrtf.RtfText.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GmailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private boolean poslanyMail=false;
    private String pom1,suma,pom2,pom3,pom4,pom5,hlavicka,nazovSuboru;
private Context context;
private String[] menaFotiek;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GmailSender(String user, String password,Context context) {
        this.user = user;
        this.password = password;
        this.context=context;
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized boolean sendMail(String subject, String body, String sender, String recipients,String hlavicka,String obsahSuboru,String nazovSuboru,String suma,String[] menaFotiek){
        pom1=subject;
        pom2=body;
        pom3=sender;
        pom4=recipients;
        pom5=obsahSuboru;
        this.suma=suma;
        this.hlavicka=hlavicka;
        this.nazovSuboru=nazovSuboru;
        //boolean navrat=true;
        this.menaFotiek=menaFotiek;
        try{
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //try  {
                        posliMail();
                   /* }catch(Exception e){
                    System.out.println("CHYBA"+e.toString());
                    String[] pom={"","",""};
                    GmailSender sender = new GmailSender("radovanzubal1@gmail.com", "lubimzdenku",context);
                    sender.sendMail("Chyba",
                            "Chyba3",
                            "radovanzubal1@gmail.com",
                            "radovanzubal1@gmail.com",
                            "toto je chyba\n",
                            e.toString()+"prazdne\n",
                            "nnn",
                            "\nKoniec chyby",
                            pom
                    );
                }*/
                }
            });
            thread.start();
            thread.join();
        }catch(Exception e){
            System.out.println("Chyba pri poslani mailu");
            e.printStackTrace();
            System.out.println("CHYBA"+e.toString());
            String[] pom={"","",""};
            GmailSender s = new GmailSender("radovanzubal1@gmail.com", "lubimzdenku",context);
            s.sendMail("Chyba",
                    "Chyba3",
                    "radovanzubal1@gmail.com",
                    "radovanzubal1@gmail.com",
                    "toto je chyba\n",
                    e.toString()+"prazdne\n",
                    "nnn",
                    "\nKoniec chyby",
                    pom
            );
        poslanyMail=false;
        }
        return poslanyMail;
    }
    private void posliMail() {
       try {
            MimeMessage message = new MimeMessage(session);
            //DataHandler handler = new DataHandler(new ByteArrayDataSource(pom2.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(pom3));
            message.setSubject(pom1);
            //message.setDataHandler(handler);
            if (pom4.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(pom4));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(pom4));


            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(pom2);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            File f=File.createTempFile("text",".rtf");

           URL u0=null;
           URL u1=null;
           URL u2=null;
           int pocFot=0;
           int wid0=0,hei0=0,wid1=0,wid2=0,hei1=0,hei2=0;
       //    File filef1=null;
       // Bitmap b1=null,b2=null;
           InputStream in1=null,in2=null,in3=null;
         //  ByteArrayInputStream bs=null;
for(int t=0;t<3;t++){
    if(menaFotiek[t]!=null&&!menaFotiek[t].equals("")){
        pocFot++;
        try {


            if (t == 0) {
                Bitmap b1=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Kubikovanie/" + menaFotiek[t]);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                b1=Bitmap.createScaledBitmap(b1,b1.getWidth()/3,b1.getHeight()/3,false);
                b1.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                in1 = new ByteArrayInputStream(bos.toByteArray());
                wid0 = b1.getWidth();//options.outWidth;
                hei0 = b1.getHeight();//options.outHeight;
                b1.recycle();
            }else if (t == 1) {
                Bitmap b2=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Kubikovanie/" + menaFotiek[t]);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                b2=Bitmap.createScaledBitmap(b2,b2.getWidth()/3,b2.getHeight()/3,false);
                b2.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                in2 = new ByteArrayInputStream(bos.toByteArray());
                wid1 = b2.getWidth();//options.outWidth;
                hei1 = b2.getHeight();//options.outHeight;
                b2.recycle();

            }else if (t == 2) {
                Bitmap b3=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Kubikovanie/" + menaFotiek[t]);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                b3=Bitmap.createScaledBitmap(b3,b3.getWidth()/3,b3.getHeight()/3,false);
                b3.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                in3 = new ByteArrayInputStream(bos.toByteArray());
                wid2 = b3.getWidth();//options.outWidth;
                hei2 = b3.getHeight();//options.outHeight;
                b3.recycle();
            }
        }catch(Exception e){e.printStackTrace();}
        }
}
           int count = countChar(pom5,"\n")+5;
System.out.println("count "+count);
String odriadkovanie="";
int kol=count%47;
if(kol>0){
    for(int t=0;t<47-kol;t++){
        odriadkovanie+="\n";
    }
}
             Rtf pomRTF=Rtf.rtf();
           if(pocFot==0) {

               pomRTF.header(
                       font( "Courier New" ).at( 0 ) )
                       .section(
                               p(RtfText.font( 0, (hlavicka + "\n" + pom5 + "\n" + suma) + "\n\n"+odriadkovanie) )

                       );
           }else if(pocFot==1){
               pomRTF.header(
                       font( "Courier New" ).at( 0 ) )
                       .section(
                               p(RtfText.font( 0, (hlavicka + "\n" + pom5 + "\n" + suma) + "\n\n"+odriadkovanie),
                                       RtfText.picture(in1).size(wid0, hei0, RtfUnit.POINT).scale(13,13).type(RtfPicture.PictureType.JPG)
               ));
           }else if(pocFot==2){
               pomRTF.header(
                       font( "Courier New" ).at( 0 ) )
                       .section(
                               p(RtfText.font( 0, (hlavicka + "\n" + pom5 + "\n" + suma) + "\n\n"+odriadkovanie),
                       RtfText.picture(in1).size(wid0, hei0, RtfUnit.POINT).scale(13,13).type(RtfPicture.PictureType.JPG),
                       " "/*"\n\n"*/,
                       RtfText.picture(in2).size(wid1, hei1, RtfUnit.POINT).scale(13,13).type(RtfPicture.PictureType.JPG)
               ));
           }else if(pocFot==3){
               pomRTF.header(
                       font( "Courier New" ).at( 0 ) )
                       .section(
                               p(RtfText.font( 0, (hlavicka + "\n" + pom5 + "\n" + suma) + "\n\n"+odriadkovanie),
                       RtfText.picture(in1).size(wid0, hei0, RtfUnit.POINT).scale(13,13).type(RtfPicture.PictureType.JPG),
                       " ",
                       RtfText.picture(in2).size(wid1, hei1, RtfUnit.POINT).scale(13,13).type(RtfPicture.PictureType.JPG),
                       "\n\n",
                       RtfText.picture(in3).size(wid2, hei2, RtfUnit.POINT).scale(13,13).type(RtfPicture.PictureType.JPG)
                       ));
           }
           pomRTF.out( new FileWriter(f) );

            DataSource source = new FileDataSource(f);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("text.rtf");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts

            message.setContent(multipart);
            Transport.send(message);
           // System.out.println("odoslane");
           poslanyMail= true;

        }catch(Exception e){
           e.printStackTrace();
            //System.out.println("Chyba pri poslani mailu funkcia"+e.toString());
            poslanyMail= false;
        }
    }
    public int countChar(String str, String c)
    {
        int count = 0;

        for(int i=0; i < str.length()-1; i++) {
            System.out.println("znak"+str.substring(i,i+1));
            if(str.substring(i,i+1).equals(c))
            count++;
        }

        return count;
    }
    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }


        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}