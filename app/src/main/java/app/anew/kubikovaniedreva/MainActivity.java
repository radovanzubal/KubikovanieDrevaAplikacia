package app.anew.kubikovaniedreva;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    private int screenSirka,screenVyska;
    private short scKmen=1;
    private byte ktore=0;
    private Context context;
    private ArrayList<String> obsahSuboru=new ArrayList<>(0);
    private static String popisStlpcov="|Log number|Diameter in middle|Length|m3/invoiced|\n";
    private boolean zobrazInfo=false,mamInternet=false;
    private String hlavicka="";
    private double suma=0;
    private int ktoraFotka=0;
    private String[] menaFotiek= {"","",""};
    private  boolean poslany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenSirka= this.getResources().getDisplayMetrics().widthPixels;
        screenVyska= this.getResources().getDisplayMetrics().heightPixels;
        final TextView priemer = (TextView) this.findViewById(R.id.priemer);
        final TextView dlzka = (TextView) this.findViewById(R.id.dlzka);
        final Button send = (Button) this.findViewById(R.id.sendit);
        final EditText emailZadany = (EditText) this.findViewById(R.id.email);
        final TextView posledny = (TextView) this.findViewById(R.id.posledny);
        final TextView cisloKmena = (TextView) this.findViewById(R.id.cisloKmena);
        final EditText edSealN = (EditText) findViewById(R.id.sealN);
        boolean ziadost=true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ziadost=false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ziadost=false;
        }
        if(!ziadost){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


        this.context=this;
        send.setWidth(screenSirka/4);
        send.setHeight(screenVyska/7/*screenSirka/4*/);
        send.setX(3*screenSirka/4);
        send.setTextSize(TypedValue.COMPLEX_UNIT_PX,/*screenSirka/48*/screenVyska/35);
        send.setY(screenVyska-4*screenVyska/7-screenVyska/14/*screenSirka-screenSirka/8*/);
        send.setText("Email");
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(send.getText().equals("Email")){
                    send.setText("Poslať?");
                }else {
                    Toast.makeText(context, "Čakaj posielam Email!", Toast.LENGTH_LONG).show();
                    send.setText("Cakaj");
                    isOnline();
                                EditText edContract = (EditText) findViewById(R.id.contract);
                    EditText edContainer = (EditText) findViewById(R.id.cont);
                    EditText edSealN = (EditText) findViewById(R.id.sealN);
                    boolean mamUdaje = edContract.getText().length() > 1 && edContainer.getText().length() > 1&& edSealN.getText().length() > 1;
                     if (mamUdaje) {
                         Date date = Calendar.getInstance().getTime();
                         DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        final String today = formatter.format(date);
                         final String contS=edContract.getText().toString();
                         final String contaS=edContainer.getText().toString();
                        hlavicka = "Date:" + today + " Contract N:"+edContract.getText().toString()+" Container N:"+edContainer.getText().toString()+" Seal N:"+edSealN.getText();
                    if (mamInternet) {
                        if (emailZadany.getText().toString().equals("pov1@gmail.com")/*povoleny email1*/||emailZadany.getText().toString().equals("pov2@gmail.com")/*povoleny email2*/) {
                            try {
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                GmailSender sender = new GmailSender("sender@gmail.com","senderPassword",context);
                                String textObsah = popisStlpcov;
                                suma=0;
                                double average=0;

                                for (int t = 0; t < obsahSuboru.size(); t++) {
                                    textObsah += obsahSuboru.get(t);
                                    String pom=obsahSuboru.get(t);
                                    try {
                                        double rp=Double.valueOf(pom.substring(pom.lastIndexOf("m") + 2, pom.lastIndexOf("|")));
                                        suma+=rp;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                    try {
                                        int index=pom.indexOf("|",pom.indexOf("|",2));
                                        int rp=Integer.valueOf(pom.substring(index+1,pom.indexOf("|",index+1)).trim());
                                        average+=rp;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                BigDecimal av = new BigDecimal(average/obsahSuboru.size());
                                av = av.setScale(2, RoundingMode.HALF_UP);

                                BigDecimal bd = new BigDecimal(suma);
                                bd = bd.setScale(2, RoundingMode.HALF_UP);
                                 poslany = sender.sendMail(("Date: " + today+"  Contract N: "+contS+"  Container N: "+contaS),
                                        ("Contract N: "+contS+"  Container N: "+contaS),
                                        "sender@gmail.com",
                                        emailZadany.getText().toString(),
                                        hlavicka,
                                        textObsah,
                                        ("Date:" + today+"  Contract:"+contS+"  Container:"+contaS),
                                        "Average:"+av+"                        SUM:"+bd.toString(),
                                        menaFotiek
                                );

                                if (poslany) {
                                    vymazVsetko();
                                    suma=0;
                                    ktoraFotka=0;
                                    final Button foto=(Button)findViewById(R.id.foto);
                                    foto.post(new Runnable() {
                                        public void run() {
                                            foto.setText("FOTO1");
                                        }
                                    });
                                    final EditText seal=(EditText)findViewById(R.id.sealN);
                                    seal.post(new Runnable() {
                                        public void run() {
                                            seal.setText("");
                                        }
                                    });
                                    final Button se=(Button)findViewById(R.id.sendit);
                                    se.post(new Runnable() {
                                        public void run() {
                                            se.setText("Email");
                                        }
                                    });
                                    final EditText edContai=(EditText)findViewById(R.id.cont);
                                    edContai.post(new Runnable() {
                                        public void run() {
                                            edContai.setText("");
                                        }
                                    });
                                    menaFotiek=new String[3];
                                    menaFotiek[0]="";
                                    menaFotiek[1]="";
                                    menaFotiek[2]="";
                                    scKmen = 1;
                                    final TextView csk=(TextView)findViewById(R.id.cisloKmena);
                                    csk.post(new Runnable() {
                                        public void run() {
                                            csk.setText("Číslo gulatiny: " + scKmen);
                                        }
                                    });
                                    final TextView pos=(TextView)findViewById(R.id.posledny);
                                    pos.post(new Runnable() {
                                        public void run() {
                                            pos.setText("Posledna gulatina");
                                        }
                                    });
                                    obsahSuboru = new ArrayList<>(0);
                                } else {
                                }
                                        }catch(Exception ee){ee.printStackTrace();
                                                final Button se=(Button)findViewById(R.id.sendit);
                                                se.post(new Runnable() {
                                                    public void run() {
                                                        se.setText("Chyba");
                                                    }
                                                });}

                                    }
                                    });
                                    thread.start();
                                    //thread.join();
                            } catch (Exception e) {
                                Toast.makeText(context, "Chyba pri poslani Emailu!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "Zadaj email vlastníka aplikácie!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                         Toast.makeText(context, "Zadaj údaje!", Toast.LENGTH_SHORT).show();
                         edContainer.setHintTextColor(Color.RED);
                         edContract.setHintTextColor(Color.RED);
                         edSealN.setHintTextColor(Color.RED);
                     }

                }
            }
        });

        cisloKmena.setWidth(screenSirka/2);
        cisloKmena.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        cisloKmena.setText("Číslo gulatiny:"+scKmen);
        cisloKmena.setX(screenSirka/2);
        cisloKmena.setTextColor(Color.BLACK);
        cisloKmena.setY(0);

        final TextView informacie = (TextView) this.findViewById(R.id.info);
        informacie.setWidth(screenSirka);
        informacie.setHeight(screenVyska-4*screenVyska/7-screenVyska/14);
        informacie.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        informacie.setX(0);
        informacie.setTextColor(Color.BLACK);
        informacie.setY(0);
        informacie.setMovementMethod(new ScrollingMovementMethod());



        final Button infotl = (Button) this.findViewById(R.id.zobrazinfo);
        infotl.setWidth(screenSirka/4);
        infotl.setHeight(screenVyska/7);
        infotl.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        infotl.setX(3*screenSirka/4);
        infotl.setText("Info");
        infotl.setY(screenVyska-2*screenVyska/7-screenVyska/14);
        infotl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!zobrazInfo){
                    zobrazInfo=true;
                    informacie.setVisibility(View.VISIBLE);
                    String textObsah="";
                    if(obsahSuboru.size()==0){
                        textObsah="Zadaj najprv gulatinu!";
                    }else{
                        double sum=0;
                        double average=0;

                        for(int t=0;t<obsahSuboru.size();t++){
                            textObsah+=obsahSuboru.get(t);
                            String pom=obsahSuboru.get(t);
                            try {
                                double rp=Double.valueOf(pom.substring(pom.lastIndexOf("m") + 2, pom.lastIndexOf("|")));
                                sum+=rp;
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            try {
                                int index=pom.indexOf("|",pom.indexOf("|",2));
                                int rp=Integer.valueOf(pom.substring(index+1,pom.indexOf("|",index+1)).trim());
                                average+=rp;
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        BigDecimal av = new BigDecimal(average/obsahSuboru.size());
                        av = av.setScale(2, RoundingMode.HALF_UP);

                        BigDecimal bd = new BigDecimal(sum);
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        textObsah+="\nAverage:"+av+"                        SUM:"+bd.toString();
                    }
                    informacie.setText(textObsah);
                }else{
                    zobrazInfo=false;
                    informacie.setVisibility(View.INVISIBLE);

                }
            }
        });

        final Button tl0 = (Button) this.findViewById(R.id.tl0);
        tl0.setWidth(screenSirka/4);
        tl0.setHeight(screenVyska/7);
        tl0.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl0.setX(0);
        tl0.setY(screenVyska-screenVyska/14-screenVyska/7);
        tl0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("0");
            }
        });


        final Button tl1 = (Button) this.findViewById(R.id.tl1);
        tl1.setWidth(screenSirka/4);
        tl1.setHeight(screenVyska/7);
        tl1.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl1.setX(0);
        tl1.setY(screenVyska-2*screenVyska/7-screenVyska/14);
        tl1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("1");
            }
        });

        final Button tl2 = (Button) this.findViewById(R.id.tl2);
        tl2.setWidth(screenSirka/4);
        tl2.setHeight(screenVyska/7);
        tl2.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl2.setX(screenSirka/4);
        tl2.setY(tl1.getY());
        tl2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("2");

            }
        });
        final Button tl3 = (Button) this.findViewById(R.id.tl3);
        tl3.setWidth(screenSirka/4);
        tl3.setHeight(screenVyska/7);
        tl3.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl3.setX(screenSirka/2);
        tl3.setY(tl1.getY());
        tl3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("3");

            }
        });
        final Button tl4 = (Button) this.findViewById(R.id.tl4);
        tl4.setWidth(screenSirka/4);
        tl4.setHeight(screenVyska/7);
        tl4.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl4.setX(0);
        tl4.setY(screenVyska-3*screenVyska/7-screenVyska/14);
        tl4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("4");

            }
        });
        final Button tl5 = (Button) this.findViewById(R.id.tl5);
        tl5.setWidth(screenSirka/4);
        tl5.setHeight(screenVyska/7);
        tl5.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl5.setX(screenSirka/4);
        tl5.setY(tl4.getY());
        tl5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("5");

            }
        });
        final Button tl6 = (Button) this.findViewById(R.id.tl6);
        tl6.setWidth(screenSirka/4);
        tl6.setHeight(screenVyska/7);
        tl6.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl6.setX(screenSirka/2);
        tl6.setY(tl4.getY());
        tl6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("6");
            }
        });
        final Button tl7 = (Button) this.findViewById(R.id.tl7);
        tl7.setWidth(screenSirka/4);
        tl7.setHeight(screenVyska/7);
        tl7.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl7.setX(0);
        tl7.setY(screenVyska-4*screenVyska/7-screenVyska/14);
        tl7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("7");
            }
        });
        final Button tl8 = (Button) this.findViewById(R.id.tl8);
        tl8.setWidth(screenSirka/4);
        tl8.setHeight(screenVyska/7);
        tl8.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl8.setX(screenSirka/4);
        tl8.setY(tl7.getY());
        tl8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("8");
            }
        });
        final Button tl9 = (Button) this.findViewById(R.id.tl9);
        tl9.setWidth(screenSirka/4);
        tl9.setHeight(screenVyska/7);
        tl9.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        tl9.setX(screenSirka/2);
        tl9.setY(tl8.getY());
        tl9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pridajCislo("9");
            }
        });

        final Button zapis = (Button) this.findViewById(R.id.zapisuj);
        zapis.setWidth(screenSirka/4);
        zapis.setHeight(screenVyska/7);
        zapis.setX(3*screenSirka/4);
        zapis.setY(tl6.getY());
        zapis.setText("Zapíš");
        zapis.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        zapis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float dl=0;
                int pr=0;
                boolean potvrdDL=true,potvrdPR=true;
                try {
                    pr=Integer.valueOf(priemer.getText().toString().substring(priemer.getText().toString().indexOf(":")+1));
                }catch(Exception e){
                    Toast.makeText(context,"Priemer CHYBA!",Toast.LENGTH_SHORT).show();
                    potvrdPR=false;
                }
                if(pr<20){
                    potvrdPR=false;
                    Toast.makeText(context,"Minimálny priemer je 20cm!",Toast.LENGTH_SHORT).show();
                }else if(pr>80){
                    potvrdPR=false;
                    Toast.makeText(context,"Maximálny priemer je 80cm!",Toast.LENGTH_SHORT).show();
                }

                try {
                    dl=Float.valueOf(dlzka.getText().toString().substring(dlzka.getText().toString().indexOf(":")+1).replace(",","."));
                }catch(Exception e){
                    Toast.makeText(context,"Dĺžka CHYBA!",Toast.LENGTH_SHORT).show();
                    potvrdDL=false;
                }
                if(dl<2){
                    potvrdDL=false;
                    Toast.makeText(context,"Minimálna dĺžka je 2m!",Toast.LENGTH_SHORT).show();
                }else if(dl>12){
                    potvrdDL=false;
                    Toast.makeText(context,"Maximálna dĺžka je12m!",Toast.LENGTH_SHORT).show();
                }
                if(!potvrdDL){dlzka.setTextColor(Color.RED);}else{dlzka.setTextColor(Color.BLACK);}
                if(!potvrdPR){priemer.setTextColor(Color.RED);}else{priemer.setTextColor(Color.BLACK);}
                if(potvrdDL&&potvrdPR) {

                    //Date date = Calendar.getInstance().getTime();
                    dlzka.setTextColor(Color.BLACK);
                    priemer.setTextColor(Color.BLACK);
                    priemer.setText("");
                    dlzka.setText("");

                    String stlpec1=String.valueOf(scKmen);
                    int velkost=stlpec1.length();
                    for(int t=0;t<(10-velkost);t++){
                        stlpec1+=" ";
                    }

                    String stlpec2=String.valueOf(pr)+"                ";
                    String stlpec3=String.valueOf(dl);
                    velkost=stlpec3.length();
                    for(int t=0;t<(4-velkost);t++){
                        stlpec3+=" ";
                    }
                    stlpec3+=" m";

                    String stlpec4="";

                        stlpec4=String.valueOf((new SmrekObycajny()).getObjemN(pr,dl));
                        suma+=SmrekObycajny.getObjemN(pr,dl);
                    Toast.makeText(context,"Pridane!",Toast.LENGTH_SHORT).show();

                    velkost=stlpec4.length();
                    for(int t=0;t<(11-velkost);t++){
                        stlpec4+=" ";
                    }
                    obsahSuboru.add("|"+stlpec1+"|"+stlpec2+"|"+stlpec3+"|"+stlpec4+"|\n");
                    scKmen++;
                    cisloKmena.setText("Číslo gulatiny: " + scKmen);
                    posledny.setText(obsahSuboru.get(obsahSuboru.size()-1).replace(" ",""));

                }
            }
        });
        final Button ciarka = (Button) this.findViewById(R.id.ciarka);
        ciarka.setWidth(screenSirka/4);
        ciarka.setHeight(screenVyska/7);
        ciarka.setX(screenSirka/4);
        ciarka.setY(tl0.getY());
        ciarka.setText(",");
        ciarka.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/14);
        ciarka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ktore==2) {
                    if(dlzka.getText().length()==8){
                        Toast.makeText(context,"Najprv zadaj číslo!",Toast.LENGTH_SHORT).show();
                    }else if(dlzka.getText().toString().contains(",")){
                        Toast.makeText(context,"Čiarka už je!",Toast.LENGTH_SHORT).show();
                    }else{
                        dlzka.setText(dlzka.getText().toString()+",");
                    }
                }else{
                    Toast.makeText(context,"Musí byť celé číslo!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        /////////////////////////////////////

        final Button foto = (Button) this.findViewById(R.id.foto);
        foto.setWidth(screenSirka/4);
        foto.setHeight(screenVyska/7);
        foto.setX(3*screenSirka/4);
        foto.setY(tl0.getY());
        foto.setText("FOTO1");
        foto.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        foto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(foto.getText().equals("ODFOT")) {
                    captureImage(ktoraFotka);

                }else{
                    foto.setText("ODFOT");
                }
            }
        });
        /////////////////////////////////////
        final Button vymaz = (Button) this.findViewById(R.id.vymaz);
        vymaz.setWidth(screenSirka/4);
        vymaz.setHeight(screenVyska/7);
        vymaz.setX(2*screenSirka/4);
        vymaz.setY(tl0.getY());
        vymaz.setText("Vymaž");
        vymaz.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        vymaz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ktore==1) {

                    int lenP=priemer.getText().toString().length();
                if(lenP>1){
                    priemer.setText(priemer.getText().toString().substring(0,lenP-1));
                }else{
                    priemer.setText("");
                }
                }else if(ktore==2) {
                    int lenP=dlzka.getText().toString().length();
                    if(lenP>1){
                        dlzka.setText(dlzka.getText().toString().substring(0,lenP-1));
                    }else{
                        dlzka.setText("");
                    }

                }else if(ktore==3) {
                    if(scKmen>1){
                        scKmen--;
                        obsahSuboru.remove(obsahSuboru.size()-1);
                        cisloKmena.setText("Číslo gulatiny:"+scKmen);
                        if(obsahSuboru.size()>0) {
                            posledny.setText(obsahSuboru.get(obsahSuboru.size()-1).replace(" ", ""));
                        }else{
                            posledny.setText("Posledna gulatina");
                        }
                        Toast.makeText(context,"Vymazané!",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        dlzka.setWidth(screenSirka/2-5);
        dlzka.setHeight(screenVyska/14);
        dlzka.setX(0);
        dlzka.setY(screenVyska-5*screenVyska/7);
        dlzka.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/20);
        dlzka.setText("");
        dlzka.setTextColor(Color.BLACK);
        dlzka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ktore=(byte)2;
                dlzka.setBackgroundColor(Color.parseColor("#FFaF5F"));
                priemer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                posledny.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        priemer.setWidth(screenSirka/2-5);
        priemer.setHeight(screenVyska/14);
        priemer.setX(0);
        priemer.setY(dlzka.getY()-screenVyska/7);
        priemer.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/20);
        priemer.setText("");
        priemer.setTextColor(Color.BLACK);
        priemer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ktore=(byte)1;
                priemer.setBackgroundColor(Color.parseColor("#FFaF5F"));
                dlzka.setBackgroundColor(Color.parseColor("#FFFFFF"));
                posledny.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


        EditText editContract=(EditText) this.findViewById(R.id.contract);
        editContract.setWidth(screenSirka/2-5);
        editContract.setHeight(screenVyska/14);
        editContract.setX(screenSirka/2+5);
        editContract.setY(dlzka.getY()-screenVyska/14-3);
        editContract.setHintTextColor(Color.GRAY);

        EditText editContainer=(EditText) this.findViewById(R.id.cont);
        editContainer.setWidth(screenSirka/2-5);
        editContainer.setHeight(screenVyska/14);
        editContainer.setX(screenSirka/2+5);
        editContainer.setY(dlzka.getY());
        editContainer.setHintTextColor(Color.GRAY);

        emailZadany.setWidth(screenSirka/2-5);
        emailZadany.setHeight(screenVyska/14);
        emailZadany.setX(screenSirka/2+5);
        emailZadany.setY(editContract.getY()-3-screenVyska/14);
        emailZadany.setHintTextColor(Color.GRAY);

        edSealN.setWidth(screenSirka/2-5);
        edSealN.setHeight(screenVyska/14);
        edSealN.setX(0);
        edSealN.setY(0);
        edSealN.setHintTextColor(Color.GRAY);

        posledny.setWidth(screenSirka/2-5);
        posledny.setHeight(screenVyska/14);
        posledny.setX(screenSirka/2+5);
        posledny.setY(emailZadany.getY()-2-screenVyska/14);
        posledny.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        posledny.setText("Posledna gulatina");
        posledny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ktore=(byte)3;
                posledny.setBackgroundColor(Color.parseColor("#FFaF5F"));
                priemer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                dlzka.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        TextView textDlzka=(TextView) this.findViewById(R.id.dlzkaText);
        textDlzka.setWidth(screenSirka/2-5);
        textDlzka.setHeight(screenVyska/14);
        textDlzka.setX(0);
        textDlzka.setY(dlzka.getY()-screenVyska/14);
        textDlzka.setText("Dĺžka m:");
        textDlzka.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        textDlzka.setTextColor(Color.BLACK);

        TextView textPriemer=(TextView) this.findViewById(R.id.priemerText);
        textPriemer.setWidth(screenSirka/2-5);
        textPriemer.setHeight(screenVyska/14);
        textPriemer.setX(0);
        textPriemer.setY(priemer.getY()-screenVyska/14);
        textPriemer.setText("Priemer cm:");
        textPriemer.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenVyska/35);
        textPriemer.setTextColor(Color.BLACK);
        System.out.println("onCreate");
    }
    public void isOnline() {
     try{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int timeoutMs = 1500;
                    Socket sock = new Socket();
                    SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

                    sock.connect(sockaddr, timeoutMs);
                    sock.close();

                    mamInternet = true;
                } catch (IOException e) {
                    mamInternet = false;
                }
            }
        });
        thread.start();
        thread.join();
    }catch(InterruptedException ee){mamInternet=false;ee.printStackTrace();}
    }
    private void pridajCislo(String sc){
        if(ktore==1){
            TextView priemer=(TextView) findViewById(R.id.priemer);
            String pom=priemer.getText().toString();
            pom= pom+sc;
            try{
                int p=Integer.valueOf(pom);
                if(p>80){
                    Toast.makeText(context,"Maximálny priemer je 80cm!",Toast.LENGTH_SHORT).show();
                }else{
                    if(p<20){
                        Toast.makeText(context,"Minimálny priemer je 20cm!",Toast.LENGTH_SHORT).show();
                    }
                    priemer.setText(pom);
                }
            }catch(Exception e){
                Toast.makeText(context,pom,Toast.LENGTH_SHORT).show();
            }
        }else if(ktore==2) {
            TextView dlzka=(TextView) findViewById(R.id.dlzka);

            String pom = dlzka.getText().toString();
            if (pom.substring(pom.indexOf(",") + 1).length() == 0 || pom.indexOf(",") < 0){
                pom = pom + sc;
                    float p = Float.valueOf(pom.replace(",", "."));
                    BigDecimal bd = new BigDecimal(p);
                    bd = bd.setScale(1, RoundingMode.HALF_UP);
                    p = bd.floatValue();
                    if (p > 12) {
                        Toast.makeText(context, "Maximálna dĺžka je 12m!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (p <2) {
                            Toast.makeText(context, "Minimálna dĺžka je 2m!", Toast.LENGTH_SHORT).show();
                        }
                        if (p == Math.round(p)) {
                            dlzka.setText( String.valueOf(Math.round(p)));
                        } else {
                            dlzka.setText(String.valueOf(p).replace(".", ","));
                        }
                    }
            }
        }else{
            Toast.makeText(context,"Klikni na priemer alebo dĺžku!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");


        TextView priemer = (TextView) findViewById(R.id.priemer);
        priemer.setText(ObsluhaSuboru.nacitajUlozene("priemer", this));

        TextView dlzka = (TextView) findViewById(R.id.dlzka);
        dlzka.setText(ObsluhaSuboru.nacitajUlozene("dlzka", this));

        EditText edSealN = (EditText) findViewById(R.id.sealN);
        edSealN.setText(ObsluhaSuboru.nacitajUlozene("seal", this));

        ArrayList<String> p = ObsluhaSuboru.readByLine(this, "ObsahSuboru");
        obsahSuboru = new ArrayList<>(0);
        if (p.size() != 0) {
            for (int t = 0; t < p.size(); t++) {
                obsahSuboru.add(p.get(t) + "\n");
                System.out.println("Skuska nacitany arr" + p.get(t));
            }
            scKmen = (short) (obsahSuboru.size() + 1);
            TextView cisloKmena = (TextView) findViewById(R.id.cisloKmena);
            cisloKmena.setText("Číslo gulatiny:" + scKmen);

            TextView posledny = (TextView) findViewById(R.id.posledny);
            posledny.setText(obsahSuboru.get(obsahSuboru.size() - 1).replace(" ", ""));
        }

        EditText email = (EditText) findViewById(R.id.email);
        email.setText(ObsluhaSuboru.nacitajUlozene("email", this));

        EditText contract = (EditText) findViewById(R.id.contract);
        contract.setText(ObsluhaSuboru.nacitajUlozene("contract", this));

        EditText container = (EditText) findViewById(R.id.cont);
        container.setText(ObsluhaSuboru.nacitajUlozene("container", this));

        boolean prvyKrat = false;
        int pd = 0;
        if (menaFotiek[0].equals("")&&menaFotiek[1].equals("")&&menaFotiek[2].equals("")) {
            prvyKrat = true;
        }

        if(prvyKrat) {
            menaFotiek = new String[3];
            String n = ObsluhaSuboru.nacitajUlozene("fotky", this);
            System.out.println("menoFotkyCele"+n);
            ktoraFotka = 0;
            if (n != null && !n.equals("")) {
                String meno = n.substring(1, n.indexOf("*", 2));
                menaFotiek[0] = meno;
                n = n.substring(n.indexOf("*", 2) + 1);
                System.out.println("meno0Fotky" + meno);
                ktoraFotka = 1;
            } else {
                menaFotiek[0] = "";
            }

            if (n != null && !n.equals("")) {
                String meno = n.substring(0, n.indexOf("*"));
                menaFotiek[1] = meno;
                n = n.substring(n.indexOf("*") + 1);
                ktoraFotka = 2;
            } else {
                menaFotiek[1] = "";
            }

            if (n != null && !n.equals("")) {
                String meno = n.substring(0, n.indexOf("*"));
                menaFotiek[2] = meno;
                ktoraFotka = 3;
            } else {
                menaFotiek[2] = "";
            }
            final Button foto = (Button) this.findViewById(R.id.foto);

            foto.setText("FOTO" + ktoraFotka);
            ktoraFotka--;
            if (ktoraFotka < 0) {
                ktoraFotka = 0;
            }
        }
    }
private void vymazVsetko(){
    ObsluhaSuboru.zapisDoSuboru("","priemer",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","dlzka",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","ObsahSuboru",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","email",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","contract",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","container",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","suma",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","seal",Context.MODE_PRIVATE,this);
    ObsluhaSuboru.zapisDoSuboru("","fotky",Context.MODE_PRIVATE,this);

    }

    @Override
    protected void onPause(){
        super.onPause();
        String zapis="";

        EditText edSealN=(EditText)findViewById(R.id.sealN);
        zapis=edSealN.getText().toString();
        ObsluhaSuboru.zapisDoSuboru(zapis,"seal",Context.MODE_PRIVATE,this);

        zapis="*";
        for(int i=0;i<3;i++){
            if(!menaFotiek[i].equals("")){
        zapis+= menaFotiek[i]+"*";
            }
        }
        if(zapis.equals("*")){zapis="";}
        ObsluhaSuboru.zapisDoSuboru(zapis,"fotky",Context.MODE_PRIVATE,this);

        zapis="";
        TextView priemer=(TextView)findViewById(R.id.priemer);
        zapis=priemer.getText().toString();
        ObsluhaSuboru.zapisDoSuboru(zapis,"priemer",Context.MODE_PRIVATE,this);

        TextView dlzka=(TextView)findViewById(R.id.dlzka);
        zapis=dlzka.getText().toString();
        ObsluhaSuboru.zapisDoSuboru(zapis,"dlzka",Context.MODE_PRIVATE,this);

        zapis="";
        for(int t=0;t<obsahSuboru.size();t++){
            zapis+=obsahSuboru.get(t);
        }
        ObsluhaSuboru.zapisDoSuboru(zapis,"ObsahSuboru",Context.MODE_PRIVATE,this);

        EditText email=(EditText) findViewById(R.id.email);
        zapis=email.getText().toString();
        ObsluhaSuboru.zapisDoSuboru(zapis,"email",Context.MODE_PRIVATE,this);

        EditText contract=(EditText) findViewById(R.id.contract);
        zapis=contract.getText().toString();
        ObsluhaSuboru.zapisDoSuboru(zapis,"contract",Context.MODE_PRIVATE,this);

        EditText container=(EditText) findViewById(R.id.cont);
        zapis=container.getText().toString();
        ObsluhaSuboru.zapisDoSuboru(zapis,"container",Context.MODE_PRIVATE,this);

    }
    private void captureImage(int ktory) {
            String imageFolderPath = (Environment.getExternalStorageDirectory().toString() + "/Kubikovanie").replace("file","content");
            File imagesFolder = new File(imageFolderPath);
            imagesFolder.mkdirs();

            menaFotiek[ktory] = (new Date().toString() + ".jpg").replace(" ", "").replace(":", "").replace("+", "");

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri ur;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                 ur = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", new File(imageFolderPath, menaFotiek[ktory]));
                System.out.println("URI pri chybe"+ur.toString());

            }else{
              ur=  Uri.fromFile(new File(imageFolderPath, menaFotiek[ktory]));
            }


            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,ur );

            startActivityForResult(takePictureIntent, ktory);

        }catch(Exception e){
            e.printStackTrace();
            }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
try{
        if (resultCode == Activity.RESULT_OK &&( requestCode == 0||requestCode==1||requestCode==2)) {

            ktoraFotka++;
            if (ktoraFotka == 3) {
                ktoraFotka = 0;
            }
            Button foto=(Button)findViewById(R.id.foto);
            foto.setText("FOTO" + (ktoraFotka+1));
            //Scan new image added
            MediaScannerConnection.scanFile(context, new String[]{new File(Environment.getExternalStorageDirectory()
                    + "/Kubikovanie/" + menaFotiek[requestCode]).getPath()}, new String[]{"image/jpg"}, null);

            // Work in few phones
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory()
                        + "/Kubikovanie/" + menaFotiek[requestCode])));

            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(Environment.getExternalStorageDirectory()
                        + "/Kubikovanie/" + menaFotiek[requestCode])));
            }
        } else {
            Button foto=(Button)findViewById(R.id.foto);
            foto.setText("FOTO" + (ktoraFotka+1));
            Toast.makeText(context, "Take Picture Failed or canceled",Toast.LENGTH_SHORT).show();
        }
}catch(Exception e){
    System.out.println("CHYBA"+e.toString());
}
}
    @Override
    public void onRequestPermissionsResult (int requestCode,String[] permissions, int[] grantResults){
        if(grantResults[0]==PackageManager.PERMISSION_DENIED||grantResults[1]==PackageManager.PERMISSION_DENIED/*||grantResults[2]==PackageManager.PERMISSION_DENIED*/){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE/*,Manifest.permission.CAMERA*/},1);
        }
    }
}

