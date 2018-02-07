package cn.wolfcode.p2p;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by seemygo on 2018/1/21.
 */
public class APITest {
    public static void main(String[] args) throws Exception {
        //复制�?个地�?
        URL url = new URL("https://way.jd.com/turing/turing");
        //打开浏览�?,在地�?栏输入的地址
        HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
        //设置请求的方�?
        conn.setRequestMethod("POST");
        //是否�?要输出内�?
        conn.setDoOutput(true);
        //给地�?栏添加参数信�?
        StringBuilder param = new StringBuilder(50);
        //info=今晚吃什�?&loc=北京市海�?区信息路28�?&userid=222&appkey=e50b3303a2fa65774b440c0f084a82b9
        param.append("info=").append("今晚吃什�?")
                .append("&loc=").append("广州市天河区")
                .append("&userid=").append("222")
                .append("&appkey=").append("e50b3303a2fa65774b440c0f084a82b9");
        //输入数据
        conn.getOutputStream().write(param.toString().getBytes("utf-8"));
        //按下回车�?.
        conn.connect();
        //获取到服务器响应的内�?
        String responseStr = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("utf-8"));
        System.out.println(responseStr);

    }
}
