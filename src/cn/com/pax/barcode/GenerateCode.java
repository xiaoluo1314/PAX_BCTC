package cn.com.pax.barcode;

import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SocketOperation;

public class GenerateCode {
    private static String cmd1 = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.qrcode.%s\", null, null, {\"content\": \"%s\", \"bgcolor\": \"%s\"," +
            "\"version\": \"%s\", \"mode\": \"%s\", \"fgcolor\": \"%s\", \"degree\": \"%s\", \"margin\":" +
            "\"%s\", \"errlevel\": \"%s\", \"size\": \"%s\"}, [\"com.pax.paydroidtest.category.%s\"]," +
            "null, null, null], \"id\": 1, \"package\": \"com.googlecode.android_scripting.facade\"" +
            "}\n";
    private static String cmd2 = "{\"method\": \"startServiceIntent\", \"class\": \"AndroidFacade\", \"params\": [{\"action\":" +
            "\"com.pax.paydroidtest.action.qrcode.%s\", \"extras\": {\"degree\": \"%s\", \"errleve" +
            "l\": \"%s\", \"content\": \"%s\", \"bgcolor\": \"%s\", \"version\": \"%s\", \"mode\": \"%s" +
            "\", \"margin\": \"%s\", \"fgcolor\": \"%s\", \"size\": \"%s\"}, \"flags\": 268435456, \"categories\": " +
            "[\"com.pax.paydroidtest.category.%s\"]}], \"id\": 2, \"package\": \"com.googlecode.android_scripting.facade\"}\n";
    private static String cmd3 = "{\"method\": \"setScreenBrightness\", \"class\": \"SettingsFacade\", \"params\": [%s], \"i" +
            "d\": 5, \"package\": \"com.googlecode.android_scripting.facade\"}\n";


    public static String processCode(C4Socket socket, String action, String content, String version, String encoding, String margin,
                                     String errlevel, String size, String bgcolor,
                                     String fgcolor, String degree, String codeType) {
        String intentString = String.format(cmd1, action, content, bgcolor, version, encoding, fgcolor, degree, margin, errlevel, size, codeType);
        String startString = String.format(cmd2, action, degree, errlevel, content, bgcolor, version, encoding, margin, fgcolor,size, codeType);
        return socket.sendCodeCmd(intentString, startString);
    }

    public static String processCode(C4Socket socket, String action, String content, String version, String encoding, String margin,
                                   String errlevel, String size, String degree) {
        String intentString = String.format(cmd1, action, content, "ffffff", version, encoding, "000000", degree, margin, errlevel, size, "QRCODE");
        String startString = String.format(cmd2, action, degree, errlevel, content, "ffffff", version, encoding, margin, "000000",size, "QRCODE");
        return socket.sendCodeCmd(intentString, startString);
    }
    public static String dismissUI(C4Socket socket){
       return GenerateCode.processCode(socket, "DISMISS", "123456", "15", "BYTE", "2", "M",
                "40", "90");
    }

    public static String setBrightness(C4Socket socket, String brightValue) {
        String brightString = String.format(cmd3, brightValue);
        return socket.sendCodeCmd(brightString, null);
    }
    public static String process(String contend,String version,String errlevel, String precision,String bg,String fg,String encoding ){
        return "generatecode "+contend+" "+version+" "+ 2 +" "+errlevel+" "+precision+" "+ 180 + " " + bg + " "+ fg +" "+ encoding;
    }

    public static void main(String[] args) {
        boolean suc1 = SocketOperation.openPhoneSocKet();
        System.out.println(suc1);
       // System.out.println(process("112223","4","H","90"));
        C4Socket pc2Phone = SocketOperation.getPc2Phone();
        //String revPhone =  pc2Phone.writeReadOneLine(process("11222SS3","5","H","0"));
      //  System.out.println(revPhone);
        SocketOperation.pc2PhoneClose();
    }
}
