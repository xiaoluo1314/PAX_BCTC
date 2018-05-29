package cn.com.pax.barcode;

import cn.com.pax.comm.C4Socket;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * {"method": "makeIntent", "class": "AndroidFacade", "params": ["com.pax.paydroidt
     est.action.qrcode.SHOW", null, null, {"content": "123456", "bgcolor": "ffffff",
     "version": "15", "mode": "BYTE", "fgcolor": "000000", "degree": "180", "margin":
     "2", "errlevel": "M", "size": "40"}, ["com.pax.paydroidtest.category.QRCODE"],
     null, null, null], "id": 1, "package": "com.googlecode.android_scripting.facade"
     }
     * @param args
     */
    public static void main(String[] args) throws Exception{

        Map<String, String> mp1 = new HashMap<>();
        mp1.put("content", "123456");
        mp1.put("bgcolor", "ffffff");
        mp1.put("version", "15");
        mp1.put("mode", "BYTE");
        mp1.put("fgcolor", "000000");
        mp1.put("degree", "180");
        mp1.put("margin", "2");
        mp1.put("errlevel", "M");
        mp1.put("size", "40");

        List<String> category = new ArrayList<>();
        category.add("com.pax.paydroidtest.category.QRCODE");

        Object o1 = null;

        JSONArray jsonArray = new JSONArray();
        jsonArray.put("com.pax.paydroidtest.action.qrcode.SHOW");
        jsonArray.put(o1);
        jsonArray.put(o1);
        jsonArray.put(mp1);
        jsonArray.put(category);
        jsonArray.put(o1);
        jsonArray.put(o1);
        jsonArray.put(o1);




        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "makeIntent");
        jsonObject.put("class", "AndroidFacade");
        jsonObject.put("params", jsonArray);
        jsonObject.put("id", 1);
        jsonObject.put("package", "com.googlecode.android_scripting.facade");

        String cmd1 = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
                "est.action.qrcode.SHOW\", null, null, {\"content\": \"123456\", \"bgcolor\": \"ffffff\"," +
                "\"version\": \"15\", \"mode\": \"BYTE\", \"fgcolor\": \"000000\", \"degree\": \"180\", \"margin\":" +
                "\"2\", \"errlevel\": \"M\", \"size\": \"40\"}, [\"com.pax.paydroidtest.category.QRCODE\"]," +
                "null, null, null], \"id\": 1, \"package\": \"com.googlecode.android_scripting.facade\"" +
                "}\n";
        String cmd2 = "{\"method\": \"startServiceIntent\", \"class\": \"AndroidFacade\", \"params\": [{\"action\":" +
                "\"com.pax.paydroidtest.action.qrcode.SHOW\", \"extras\": {\"degree\": \"180\", \"errleve" +
                "l\": \"M\", \"content\": \"123456\", \"bgcolor\": \"ffffff\", \"version\": \"15\", \"mode\": \"BYT" +
                "E\", \"margin\": \"2\", \"fgcolor\": \"000000\", \"size\": \"40\"}, \"flags\": 268435456, \"cate" +
                "gories\": [\"com.pax.paydroidtest.category.QRCODE\"]}], \"id\": 2, \"package\": \"com.go" +
                "oglecode.android_scripting.facade\"}\n";

        String cmd3 = "{\"method\": \"makeIntent\", \"class\": \"AndroidFacade\", \"params\": [\"com.pax.paydroidt" +
                "est.action.qrcode.DISMISS\", null, null, {\"content\": \"123456\", \"bgcolor\": \"ffffff\"," +
                "\"version\": \"15\", \"mode\": \"BYTE\", \"fgcolor\": \"000000\", \"degree\": \"180\", \"margin\":" +
                "\"2\", \"errlevel\": \"M\", \"size\": \"40\"}, [\"com.pax.paydroidtest.category.QRCODE\"]," +
                "null, null, null], \"id\": 1, \"package\": \"com.googlecode.android_scripting.facade\"" +
                "}\n";
        String cmd4 = "{\"method\": \"startServiceIntent\", \"class\": \"AndroidFacade\", \"params\": [{\"action\":" +
                "\"com.pax.paydroidtest.action.qrcode.DISMISS\", \"extras\": {\"degree\": \"180\", \"errleve" +
                "l\": \"M\", \"content\": \"123456\", \"bgcolor\": \"ffffff\", \"version\": \"15\", \"mode\": \"BYT" +
                "E\", \"margin\": \"2\", \"fgcolor\": \"000000\", \"size\": \"40\"}, \"flags\": 268435456, \"cate" +
                "gories\": [\"com.pax.paydroidtest.category.QRCODE\"]}], \"id\": 2, \"package\": \"com.go" +
                "oglecode.android_scripting.facade\"}\n";
        //String cmdString = "{\"method\": \"makeToast\", \"class\": \"AndroidFacade\", \"params\": [\"hello\"], \"id\": 0," +
         //       "\"package\": \"com.googlecode.android_scripting.facade\"}\n";//jsonObject.toString()  + "\n";
        String cmdString = jsonObject.toString() + "\n";
        try {
            C4Socket socket = new C4Socket("192.168.1.161", 55558);
            socket.openConn();
            //Socket socket = new Socket("127.0.0.1", 55557);
            System.out.println("open ok");
            //

            int i= 30;
            while(true) {

                    //String action, String content, String version, String encoding, String margin,
                    //        String errlevel, String size, String degree)
                    GenerateCode.processCode(socket, "SHOW", "123456", "15", "BYTE", "2", "M",
                            "40", "90");
                    Thread.sleep(1000);

                    GenerateCode.processCode(socket, "DISMISS", "123456", "15", "BYTE", "2", "M",
                        "40", "90");

                    Thread.sleep(1000);


                    System.out.println(GenerateCode.setBrightness(socket, i + ""));

                    i += 30;
                    i %= 255;
                    Thread.sleep(1000);
            }

            //socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
