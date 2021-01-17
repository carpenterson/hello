import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Lottery {
    static byte[] coin75;
    static RestTemplate template = new RestTemplate();

    static {
        // 75分的加密串，取末尾的32字节
        String enc = "KSeI14LMMJsSVtO8CpDwvgeT9eG9rMa0IDGforS6ATpUuL8+16kNrCMlTuAslg7LszQrPsL3O6+aHkaPcdSTgzWXNqWF32P8Twug9uIVyOezSIIAG2XB76GGJggf4UuiDudyuXsa34qAZZjh8EApuMmVQNhyaLlSm0mJce6DJtE=";
        byte[] encBytes = Base64.getDecoder().decode(enc);
        coin75 = Arrays.copyOfRange(encBytes, 96, 128);
    }

    public static void main(String[] args) throws Exception {

        // 注册
        String registryUrl = "http://52.149.144.45:8080/user/register";
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String prefix = "xzh";
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        String name = prefix + Hex.encodeHexString(randomBytes);
        params.add("username", name);
        params.add("password", name);
        System.out.println("注册用户：\t" + name);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, null);
        ResponseEntity<String> regResponse = template.postForEntity(registryUrl, httpEntity, String.class);
        JSONObject regResult = JSONObject.parseObject(regResponse.getBody());
        String uuid = regResult.getJSONObject("user").getString("uuid");
        System.out.println("uuid\t" + uuid);

        // 登录
        String loginUrl = "http://52.149.144.45:8080/user/login";
        ResponseEntity<String> loginResponse = template.postForEntity(loginUrl, httpEntity, String.class);
        JSONObject loginResult = JSONObject.parseObject(loginResponse.getBody());
        String apiToken = loginResult.getJSONObject("user").getString("api_token");
        System.out.println("api_token\t" + apiToken);

        // 买彩票
        String buyUrl = "http://52.149.144.45:8080/lottery/buy";
        params.clear();
        params.add("api_token", apiToken);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("api_token=" + apiToken);
        headers.put(HttpHeaders.COOKIE, cookies);
        httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> buyResponse = template.postForEntity(buyUrl, httpEntity, String.class);
        JSONObject buyResult = JSONObject.parseObject(buyResponse.getBody());
        String enc = buyResult.getString("enc");
        System.out.println("enc:\t" +  enc);
        byte[] encBytes = Base64.getDecoder().decode(enc);
        byte[] lotteryId = Arrays.copyOfRange(encBytes, 0, 64);
        byte[] userId = Arrays.copyOfRange(encBytes, 64, 96);

        ResponseEntity<String> buyResponse2 = template.postForEntity(buyUrl, httpEntity, String.class);
        JSONObject buyResult2 = JSONObject.parseObject(buyResponse2.getBody());
        String enc2 = buyResult2.getString("enc");
        System.out.println("enc2:\t" +  enc2);
        byte[] encBytes2 = Base64.getDecoder().decode(enc2);
        byte[] lotteryId2 = Arrays.copyOfRange(encBytes2, 0, 64);

        ResponseEntity<String> buyResponse3 = template.postForEntity(buyUrl, httpEntity, String.class);
        JSONObject buyResult3 = JSONObject.parseObject(buyResponse2.getBody());
        String enc3 = buyResult3.getString("enc");
        System.out.println("enc3:\t" +  enc3);
        byte[] encBytes3 = Base64.getDecoder().decode(enc3);
        byte[] lotteryId3 = Arrays.copyOfRange(encBytes3, 0, 64);

        charge(uuid, apiToken, lotteryId, userId, coin75);
        charge(uuid, apiToken, lotteryId2, userId, coin75);
        charge(uuid, apiToken, lotteryId3, userId, coin75);

    }

    private static void charge(String uuid, String apiToken, byte[] lotteryId, byte[] userId, byte[] coin) throws UnsupportedEncodingException {
        String chargeUrl = "http://52.149.144.45:8080/lottery/charge";

        byte[]  encBytes  = new byte[128];
        System.arraycopy(lotteryId,0, encBytes,  0, 64);
        System.arraycopy(userId,0, encBytes, 64, 32);
        System.arraycopy(coin,0, encBytes,  96, 32);
        String enc = Base64.getEncoder().encodeToString(encBytes);
        System.out.println("拼enc:\t"  + enc);

        String infoUrl = "http://52.149.144.45:8080/lottery/info";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("enc", enc);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, null);

        ResponseEntity<String> infoResponse = template.postForEntity(infoUrl, httpEntity, String.class);
        System.out.println(infoResponse.getBody());
        JSONObject infoResult = JSONObject.parseObject(infoResponse.getBody());
        uuid = infoResult.getJSONObject("info").getString("user");

        params.add("user", uuid);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("api_token=" + apiToken);
        headers.put(HttpHeaders.COOKIE, cookies);
        System.out.println(URLEncoder.encode(enc, "UTF-8"));
//        ResponseEntity<String> chargeResponse = template.postForEntity(chargeUrl, httpEntity, String.class);
//        System.out.println(chargeResponse.getStatusCode());
//        System.out.println(chargeResponse.getBody());

    }


}
