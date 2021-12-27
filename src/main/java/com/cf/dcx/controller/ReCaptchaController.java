package com.cf.dcx.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReCaptchaController {

    @ResponseBody
    @RequestMapping("recaptcha")
    public Map<String,String> recaptcha(@RequestParam Map<String, Object> param){

        Map<String, String> map = new HashMap<String, String>();
        String recaptchaResponse = (String) param.get("token"); // 토큰값 저장
        String remoteip = (String) param.get("remoteip"); // 아이피 저장
        System.out.println("토큰값==>"+recaptchaResponse);

        String targetURL = "https://www.google.com/recaptcha/api/siteverify";

        URL url;
        HttpURLConnection connection = null;
        boolean isSuccess = false;
        String params = "";
        String jsonData = "";
        String secretKey = "6LeJz7kdAAAAAPgbjdxA169vwht0GN_vCgCxd_D9";

        try {
            params = "secret=" + secretKey + "&response=" + recaptchaResponse + "&remoteip=" + remoteip;

// Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

// Send request
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params);
            os.flush();
            os.close();

// Get Response
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            while ((jsonData = br.readLine()) != null) {
                sb.append(jsonData);
            }
            br.close();

            JSONParser parser = new JSONParser();
            Object resvObj = parser.parse(sb.toString());
            JSONObject jsonObj = (JSONObject) resvObj;

            isSuccess = (boolean) jsonObj.get("success");

            if(!isSuccess) {
                System.out.println("reCaptcha error  : " + jsonObj.get("error-codes").toString());
                map.put("msg", "false");
            }else {
                map.put("msg", "true");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return map;
    }
}
