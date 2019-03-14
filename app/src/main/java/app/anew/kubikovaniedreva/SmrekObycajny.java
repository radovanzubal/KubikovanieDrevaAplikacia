package app.anew.kubikovaniedreva;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmrekObycajny {//cm skontrolovane=20-68,80-70
    public static double getObjemN(int priemer,double dlzka){
        BigDecimal bdd = new BigDecimal(dlzka);
        bdd = bdd.setScale(2, RoundingMode.HALF_UP);
        dlzka=bdd.doubleValue();
        if(priemer==28&&dlzka==11.5){return 0.66;
        }else if(priemer==25&&dlzka==2) {return 0.1;
        }else if(priemer==26&&dlzka==2) {return 0.11;
        }else if(priemer==70&&dlzka==2) {return 0.73;
        }else if(priemer==26&&dlzka==2.1) {return 0.11;
        }else if(priemer==70&&dlzka==2.1) {return 0.77;
        }else if(priemer==25&&dlzka==2.2) {return 0.11;
        }else if(priemer==26&&dlzka==2.2) {return 0.12;

        }else if(priemer==70&&dlzka==2.2) {return 0.81;
        }else if(priemer==25&&dlzka==2.4) {return 0.12;
        }else if(priemer==25&&dlzka==2.6) {return 0.13;
        }else if(priemer==25&&dlzka==2.8) {return 0.14;
        }else if(priemer==25&&dlzka==3.0) {return 0.15;
        }else if(priemer==25&&dlzka==3.2) {return 0.16;
        }else if(priemer==25&&dlzka==3.4) {return 0.17;
        }else if(priemer==25&&dlzka==3.6) {return 0.18;
        }else if(priemer==25&&dlzka==3.8) {return 0.19;

        }else if(priemer==25&&dlzka==4.0) {return 0.2;
        }else if(priemer==25&&dlzka==4.2) {return 0.21;
        }else if(priemer==25&&dlzka==4.4) {return 0.22;
        }else if(priemer==26&&dlzka==4.4) {return 0.23;
        }else if(priemer==70&&dlzka==4.4) {return 1.61;
        }else if(priemer==25&&dlzka==4.5) {return 0.22;
        }else if(priemer==26&&dlzka==4.5) {return 0.24;
        }else if(priemer==70&&dlzka==4.5) {return 1.65;
        }else if(priemer==25&&dlzka==4.6) {return 0.23;

        }else if(priemer==26&&dlzka==4.6) {return 0.24;
        }else if(priemer==70&&dlzka==4.6) {return 1.68;
        }else if(priemer==25&&dlzka==4.7) {return 0.23;
        }else if(priemer==25&&dlzka==4.9) {return 0.24;
        }else if(priemer==25&&dlzka==5.1) {return 0.25;
        }else if(priemer==25&&dlzka==5.3) {return 0.26;
        }else if(priemer==25&&dlzka==5.5) {return 0.27;
        }else if(priemer==25&&dlzka==5.6) {return 0.27;
        }else if(priemer==25&&dlzka==5.8) {return 0.28;

        }else if(priemer==25&&dlzka==6) {return 0.29;
        }else if(priemer==25&&dlzka==6.2) {return 0.3;
        }else if(priemer==25&&dlzka==6.4) {return 0.31;
        }else if(priemer==25&&dlzka==6.6) {return 0.32;
        }else if(priemer==25&&dlzka==6.8) {return 0.33;
        }else if(priemer==25&&dlzka==7) {return 0.34;
        }else if(priemer==25&&dlzka==7.2) {return 0.35;
        }else if(priemer==25&&dlzka==7.4) {return 0.36;
        }else if(priemer==25&&dlzka==7.6) {return 0.37;
        }else if(priemer==25&&dlzka==7.8) {return 0.38;

        }else if(priemer==25&&dlzka==8) {return 0.39;
        }else if(priemer==25&&dlzka==8.2) {return 0.4;
        }else if(priemer==25&&dlzka==8.4) {return 0.41;
        }else if(priemer==25&&dlzka==8.6) {return 0.42;
        }else if(priemer==25&&dlzka==8.8) {return 0.43;
        }else if(priemer==25&&dlzka==9) {return 0.44;
        }else if(priemer==25&&dlzka==9.2) {return 0.45;
        }else if(priemer==25&&dlzka==9.4) {return 0.46;
        }else if(priemer==25&&dlzka==9.6) {return 0.47;
        }else if(priemer==25&&dlzka==9.8) {return 0.48;

        }else if(priemer==25&&dlzka==10) {return 0.49;
        }else if(priemer==25&&dlzka==10.2) {return 0.5;
        }else if(priemer==25&&dlzka==10.4) {return 0.51;
        }else if(priemer==25&&dlzka==10.6) {return 0.52;
        }else if(priemer==25&&dlzka==10.8) {return 0.53;
        }else if(priemer==25&&dlzka==11) {return 0.54;
        }else if(priemer==25&&dlzka==11.1) {return 0.54;
        }else if(priemer==25&&dlzka==11.3) {return 0.55;
        }else if(priemer==25&&dlzka==11.5) {return 0.56;
        }else if(priemer==25&&dlzka==11.7) {return 0.57;

        }else if(priemer==25&&dlzka==11.9) {return 0.58;
        }else if(priemer==25&&dlzka==12) {return 0.58;
        }else if(priemer==32&&dlzka==4.1) {return 0.31;
        }else if(priemer==33&&dlzka==7.9) {return 0.63;
        }else if(priemer==34&&dlzka==10.3) {return 0.87;
        }else if(priemer==37&&dlzka==9.2) {return 0.92;
        }else if(priemer==38&&dlzka==5.1) {return 0.54;
        }else if(priemer==42&&dlzka==11.9) {return 1.53;
        }else if(priemer==46&&dlzka==9.4) {return 1.45;
        }else if(priemer==47&&dlzka==10.5) {return 1.69;
        }else if(priemer==50&&dlzka==6.8) {return 1.24;
        }else if(priemer==51&&dlzka==8.6) {return 1.63;
        }else if(priemer==53&&dlzka==8.6) {return 1.76;
        }else if(priemer==59&&dlzka==5.2) {return 1.32;
        }else if(priemer==54&&dlzka==3.8) {return 0.81;
        }else if(priemer==54&&dlzka==11.4) {return 2.42;
        }else if(priemer==62&&dlzka==9.4) {return 2.63;
        }else if(priemer==63&&dlzka==9) {return 2.6;
        }else if(priemer==63&&dlzka==3) {return 0.87;
        }else if(priemer==66&&dlzka==7.6) {return 2.41;
        }else if(priemer==57&&dlzka==6.8) {return 1.61;
        }else if(priemer==67&&dlzka==6.7) {return 2.19;
        }else if(priemer==67&&dlzka==9) {return 2.94;
        }else if(priemer==58&&dlzka==9.8) {return 2.4;
        }else if(priemer==58&&dlzka==8.9) {return 2.18;
        }else if(priemer==69&&dlzka==9.7) {return 3.36;

        }
        double pom=((priemer/20000.0)*96.17479666);
        BigDecimal bd = new BigDecimal(Math.PI*dlzka*(pom*pom));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}