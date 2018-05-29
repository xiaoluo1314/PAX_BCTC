package cn.com.pax.barcode;

import cn.com.pax.Constant;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SocketOperation;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Map;

public class GenCodeNew {
    private static String displayQrIntent = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.qrcode.SHOW\", null, null, {\"content\": \"%s\", \"bgcolor\": \"%s\"," +
            "\"version\": \"%s\", \"mode\": \"%s\", \"fgcolor\": \"%s\", \"degree\": \"%s\", \"margin\":" +
            "\"%s\", \"errlevel\": \"%s\", \"size\": \"%s\", \"sizeUnit\": \"%s\"}, [\"com.pax.paydroidtest.category.QRCODE\"], n" +
            "ull, null, null], \"id\": 1, \"package\": \"com.googlecode.android_scripting.facade\"}\n";
    private static String displayQrService = "{\"method\": \"startServiceIntentForResult\", \"class\": \"AndroidFacade\", \"params\": [{" +
            "\"action\": \"com.pax.paydroidtest.action.qrcode.SHOW\", \"extras\": {\"degree\": \"%s\"," +
            "\"errlevel\": \"%s\", \"content\": \"%s\", \"bgcolor\": \"%s\", \"version\": \"%s\", \"mod" +
            "e\": \"%s\", \"margin\": \"%s\", \"fgcolor\": \"%s\", \"size\": \"%s\",\"sizeUnit\": \"%s\"}, \"flags\": 26843545" +
            "6, \"categories\": [\"com.pax.paydroidtest.category.QRCODE\"]}], \"id\": 2, \"package\":" +
            "\"com.googlecode.android_scripting.facade\"}\n";

    private static String clearQrIntent = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.qrcode.DISMISS\", null, null, null, [\"com.pax.paydroidtest.category.QR" +
            "CODE\"], null, null, null], \"id\": 3, \"package\": \"com.googlecode.android_scripting" +
            ".facade\"}\n";
    private static String clearQrService = "{\"method\": \"startServiceIntentForResult\", \"class\": \"AndroidFacade\", \"params\": [{" +
            "\"action\": \"com.pax.paydroidtest.action.qrcode.DISMISS\", \"extras\": null, \"flags\":" +
            "268435456, \"categories\": [\"com.pax.paydroidtest.category.QRCODE\"]}], \"id\": 4, \"" +
            "package\": \"com.googlecode.android_scripting.facade\"}\n";
    private static String displayCode39Intent = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.code39.SHOW\", null, null, {\"ratio\": \"%s\", \"degree\": \"%s\", \"height\": \"%s" +
            "\", \"bgcolor\": \"%s\", \"data\": \"%s\", \"fgcolor\": \"%s\", \"size\": \"%s\",\"sizeUnit\": \"%s\", \"fixationWidth\": \"%s\"}," +
            "[\"com.pax.paydroidtest.category.onecode\"], null, null, null], \"id\": 5, \"package\"" +
            ": \"com.googlecode.android_scripting.facade\"}\n";
    private static String displayCode39Service = "{\"method\": \"startServiceIntentForResult\", \"class\": \"AndroidFacade\", \"params\": [{" +
            "\"action\": \"com.pax.paydroidtest.action.code39.SHOW\", \"extras\": {\"ratio\": \"%s\", \"d" +
            "egree\": \"%s\", \"height\": \"%s\", \"bgcolor\": \"%s\", \"data\": \"%s\", \"fgcolor\":" +
            "\"%s\", \"size\": \"%s\",\"sizeUnit\": \"%s\", \"fixationWidth\": \"%s\"}, \"flags\": 268435456, \"categories\": [\"com.pax.paydroidte" +
            "st.category.onecode\"]}], \"id\": 6, \"package\": \"com.googlecode.android_scripting.f" +
            "acade\"}\n";
    private static String clearCode39Intent = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.code39.DISMISS\", null, null, null, [\"com.pax.paydroidtest.category.on" +
            "ecode\"], null, null, null], \"id\": 7, \"package\": \"com.googlecode.android_scriptin" +
            "g.facade\"}\n";
    private static String clearCode39Service = "{\"method\": \"startServiceIntentForResult\", \"class\": \"AndroidFacade\", \"params\": [{" +
            "\"action\": \"com.pax.paydroidtest.action.code39.DISMISS\", \"extras\": null, \"flags\":" +
            "268435456, \"categories\": [\"com.pax.paydroidtest.category.onecode\"]}], \"id\": 8," +
            "\"package\": \"com.googlecode.android_scripting.facade\"}\n";
    private static String displayCode128Intent = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.code128.SHOW\", null, null, {\"bgcolor\": \"%s\", \"height\": \"%s\", \"dat" +
            "a\": \"%s\", \"fgcolor\": \"%s\", \"size\": \"%s\",\"sizeUnit\": \"%s\", \"fixationWidth\": \"%s\"}, [\"com.pax.paydroidtest.category.o" +
            "necode\"], null, null, null], \"id\": 9, \"package\": \"com.googlecode.android_scripti" +
            "ng.facade\"}\n";
    private static String displayCode128Service = "{\"method\": \"startServiceIntentForResult\", \"class\": \"AndroidFacade\", \"params\": [{" +
            "\"action\": \"com.pax.paydroidtest.action.code128.SHOW\", \"extras\": {\"bgcolor\": \"%s" +
            "\", \"size\": \"%s\", \"data\": \"%s\", \"fgcolor\": \"%s\", \"height\": \"%s\",\"sizeUnit\": \"%s\", \"fixationWidth\": \"%s\"}, \"flags\"" +
            ": 268435456, \"categories\": [\"com.pax.paydroidtest.category.onecode\"]}], \"id\": 10" +
            ", \"package\": \"com.googlecode.android_scripting.facade\"}\n";
    private static String clearCode128Intent = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
            "est.action.code128.DISMISS\", null, null, null, [\"com.pax.paydroidtest.category.o" +
            "necode\"], null, null, null], \"id\": 11, \"package\": \"com.googlecode.android_script" +
            "ing.facade\"}\n";
    private static String clearCode128Service = "{\"method\": \"startServiceIntentForResult\", \"class\": \"AndroidFacade\", \"params\": [{" +
            "\"action\": \"com.pax.paydroidtest.action.code128.DISMISS\", \"extras\": null, \"flags\"" +
            ": 268435456, \"categories\": [\"com.pax.paydroidtest.category.onecode\"]}], \"id\": 12" +
            ", \"package\": \"com.googlecode.android_scripting.facade\"}\n";

    public static String  displayQrCode(C4Socket socket, String content, String version, String encoding, String margin,
                                        String errlevel, String size, String sizeUnit, String bgcolor,
                                        String fgcolor, String degree) {
        String intentString  = String.format(displayQrIntent, content, bgcolor, version, encoding, fgcolor, degree, margin, errlevel, size, sizeUnit);
        String startString= String.format(displayQrService, degree, errlevel, content, bgcolor, version, encoding, margin, fgcolor, size, sizeUnit);
       // return socket.sendCodeCmd(intentString, startString).contains("SUCCEED");
        return socket.sendCodeCmd(intentString, startString);
    }
    public static boolean clearQrCode(C4Socket socket) {
        return socket.sendCodeCmd(clearQrIntent, clearQrService).contains("SUCCEED");
    }

    public static String displayCode39(C4Socket socket, String content, String ratio, String degree, String height, String bgcolor, String fgcolor,
                                        String size, String sizeUnit, String fixationWidth) {
        String intentString  = String.format(displayCode39Intent, ratio, degree, height, bgcolor, content, fgcolor, size, sizeUnit, fixationWidth);
        String startString= String.format(displayCode39Service, ratio, degree, height, bgcolor, content, fgcolor, size, sizeUnit, fixationWidth);
        return socket.sendCodeCmd(intentString, startString);
    }
    public static boolean clearCode39(C4Socket socket) {
        return socket.sendCodeCmd(clearCode39Intent, clearCode39Service).contains("SUCCEED");
    }

    public static String displayCode128(C4Socket socket, String content, String height, String bgcolor, String fgcolor,
                                         String size, String sizeUnit, String fixationWidth) {
        String intentString  = String.format(displayCode128Intent, bgcolor, height, content, fgcolor, size, sizeUnit, fixationWidth);
        String startString= String.format(displayCode128Service, bgcolor, size, content, fgcolor, height, sizeUnit, fixationWidth);
        return socket.sendCodeCmd(intentString, startString);
    }
    public static boolean clearCode128(C4Socket socket) {
        return socket.sendCodeCmd(clearCode128Intent, clearCode128Service).contains("SUCCEED");
    }

    public static String displayQrCode(String contend,String version,String errlevel, String precision,String bg,String fg,String encoding,String sizeUnit){
        return "generatecode " + Constant.QR_CODE  + " " + contend + " " + errlevel + " " + "2"  + " " + encoding + " " + version
                + " " + precision + " " + bg + " "+ fg + " " + "0" + " " + sizeUnit;
    }

    public static String displayCode39(String contend,String fixwid,String sizeunit, String ratio,String precision,String bg,String fg,String height ){
        //generatecode Code39 content size height ratio fg bg fixwid sizeunit
        return "generatecode "+Constant.CODE_39 +" "+contend + " " + precision + " " + height + " " + ratio + " " + fg + " "+ bg +" " + fixwid + " " + sizeunit ;
    }

    public static String displayCode128(String contend,String fixwid,String sizeunit, String precision,String bg,String fg,String height ){
        //generatecode Code128 content size height fg bg fixwid sizeunit
        return "generatecode " + Constant.CODE_128 + " " + contend + " " + precision + " " + height + " " + fg + " "+ bg +" " + fixwid + " "+ sizeunit ;
    }

    private static String brightService = "{\"method\": \"setScreenBrightness\", \"class\": \"SettingsFacade\", \"params\": [%s], \"i" +
            "d\": 5, \"package\": \"com.googlecode.android_scripting.facade\"}\n";

    public static String setBrightness(C4Socket socket, String brightValue) {
        String brightString = String.format(brightService, brightValue);
        return socket.sendCodeCmd(brightString, null);
    }

    public static void main(String[] args) {
//        try {
//            for (int i = 0;i<10;i++){
//                if (i == 5){
//                    System.out.println("break");
//                    break;
//              }
//// else{
////                    System.out.println("heheh");
////                    return;
////                }
//            }
//
//        }catch (Exception e){
//
//        }finally {
//            System.out.println("haha");
//        }
//        System.out.println("heheda");


        boolean suc1 = false;
        try {
            suc1 = SocketOperation.openPhoneSocKet();
            System.out.println(suc1);
            // System.out.println(process("112223","4","H","90"));
            C4Socket pc2Phone = SocketOperation.getPc2Phone();
            String cmd ;
            String revPhone;

            cmd = "showimage 032 Code 128 Auto_1920x1080.png";
            cmd = "showimage 001-Code 128空白条黑_1920x1080.png";
            cmd = "showimage BCTC_Code39-1_1920x1080.jpg";
            cmd = "showimage 072  PDF417_1920x1080.png";
            //System.out.println("032 Code 128 AUTO_1920x1080.png".equalsIgnoreCase("032 Code 128 Auto_1920x1080.png"));
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println(revPhone);

//            Thread.sleep(10000);
//            cmd = "clearscreen";
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println("clear:"+revPhone);
//            Thread.sleep(500);
//
//            cmd = displayQrCode("[[NUMERIC]]6227012345678901","1","M","45",
//                    "ffffff","000000","BYTE","mil");
//            System.out.println(cmd);
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println(revPhone);
//            cmd = displayQrCode("[[NUMERIC]]6227012345678901[[ALPHANUMERIC]]CAF2A0D4CD18B2EBA000000333010101000107020103900000010A010000004997E63325D77C00112233446230810314221301D261122000000956000F6227012345678901020[[BYTE]];6213531230000000061=30112200715500220?;[[BYTE]]QR码测试，个人化数据，中国汉字模式。","14","L","22",
//                    "ffffff","000000","BYTE","mil");
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println(revPhone);
//
//            Thread.sleep(1000);
//            cmd = "clearscreen";
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println("clear:"+revPhone);
//            Thread.sleep(500);

//           cmd = displayCode39("123456789012345678","0","mil", "2",
//                    "9.5","ffffff","000000","16" );
//            System.out.println("cmd"+cmd);
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println(revPhone);
//            Thread.sleep(1000);

//            cmd = "clearscreen";
//            revPhone =  pc2Phone.writeReadOneLine(cmd);
//            System.out.println("clear:"+revPhone);
//            Thread.sleep(500);
//
            cmd = displayCode128("PMFKz5k(|unkOxvOq","0","mil", "16",
                    "000000","ffffff","16" );
            revPhone =  pc2Phone.writeReadOneLine(cmd);
            System.out.println(revPhone);

            SocketOperation.pc2PhoneClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
