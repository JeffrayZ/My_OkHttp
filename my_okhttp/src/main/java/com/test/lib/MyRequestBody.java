package com.test.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: RequestBody
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/4 16:50
 */
public class MyRequestBody {
    // 表单提交Type application/x-www-form-urlencoded
    public static final String TYPE = "application/x-www-form-urlencoded";
    private final String ENC = "utf-8";
    // 请求体集合  a=123&b=666
    Map<String, String> bodys = new HashMap<>();

    public void addRequestBody(String key, String value){
        // 这里需要URL编码
        try {
            bodys.put(URLEncoder.encode(key,ENC),URLEncoder.encode(value,ENC));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 最终返回的格式是    a=123&b=234
     * @return
     */
    public String getBody(){
        StringBuffer stringBuffer = new StringBuffer();
        for(Map.Entry<String,String> entry : bodys.entrySet()){
            // 格式a=123& b=234&   多了一个&  要去除
            stringBuffer.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        // 删除最后的&
        if(stringBuffer.length() != 0){
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }
}
