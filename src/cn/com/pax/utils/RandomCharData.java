package cn.com.pax.utils;

import java.util.Random;

public class RandomCharData {
        //根据指定长度生成字母和数字的随机数
        //0~9的ASCII为48~57
        //A~Z的ASCII为65~90
        //a~z的ASCII为97~122
    public static String createRandomCharData(int length)
    {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index)
            {
                case 0:
                    data=randdata.nextInt(10);//仅仅会生成0~9
                    sb.append(data);
                    break;
                case 1:
                    data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                    sb.append((char)data);
                    break;
                case 2:
                    data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
                    sb.append((char)data);
                    break;
            }
        }
        return sb.toString();
    }

    public static int createVersion(){
        int []err ={2,3,4,5,6,7,8};
        return err[new Random().nextInt(7)];
    }
    public static String createErrLevel(){
        String [] str ={"L","M","Q","H"};
        return str[new Random().nextInt(4)];
    }
    //根据指定长度生成纯数字的随机数
    public static String createData(int length) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
    //生成随机数字和字母,第二中方法
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    //可以将字符转换赋值给int类型，查看其ASCII码
    public static void main(String[] args) {
        //随机生成纯数字
        for(int i=0;i<15;i++)
            System.out.println(createData(10));
        System.out.println("---------------");

        //生成数字字母
        for(int i=0;i<15;i++)
            System.out.println(createRandomCharData(10));
        System.out.println("---------------");
        //生成数字字母
        for(int i=0;i<15;i++)
            System.out.println(getStringRandom(10));
    }
}
    