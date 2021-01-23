import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

/**
 *
 */
public class Lottery {
    static RestTemplate template = new RestTemplate();
    static String urlRoot = "http://127.0.0.1:8082";

    public static void main(String[] args) throws Exception {
        // 注册一个幸运用户
        String theLuckyOne = randomName();
        String uuid = register(theLuckyOne);
        String apiToken = login(theLuckyOne);
        String enc = buy(apiToken);
        byte[] encBytes = Base64.getDecoder().decode(enc);
        byte[] userId = Arrays.copyOfRange(encBytes, 32, 96);
        int userCoin = 300;
        System.out.println("幸运用户：" + theLuckyOne);

        // 随机注册一批用户，并买3张彩票，用彩票的前64字节（包含彩票id）
        int count = 0;
        while (userCoin < 999) {
            String rName = randomName();
            register(rName);
            String rApiToken = login(rName);

            for (int i = 0; i < 3; i++) {
                String rEnc = buy(rApiToken);
                byte[] rEncBytes = Base64.getDecoder().decode(rEnc);
                byte[] rLotteryId = Arrays.copyOfRange(rEncBytes, 0, 64);
                byte[] rCoin = Arrays.copyOfRange(rEncBytes, 96, 128);
                charge(uuid, apiToken, rLotteryId, userId, rCoin);
                count++;
            }
            userCoin = getUserCoin(apiToken);
            System.out.println("兑换彩票数量：" + count + "\t当前Coin：" + userCoin);
        }

        getFlag(apiToken);
    }

    private static int getUserCoin(String apiToken) throws UnsupportedEncodingException {
        String url = urlRoot + "/user/info?api_token=" + URLEncoder.encode(apiToken, "UTF-8");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, null, String.class);
        JSONObject regResult = JSONObject.parseObject(response.getBody());
        return regResult.getJSONObject("user").getInteger("coin");
    }

    static private String randomName() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        String name = Hex.encodeHexString(randomBytes);
        return name;
    }

    private static String register(String name) {
        String registryUrl = urlRoot + "/user/register";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", name);
        params.add("password", name);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, null);
        ResponseEntity<String> regResponse = template.postForEntity(registryUrl, httpEntity, String.class);
        JSONObject regResult = JSONObject.parseObject(regResponse.getBody());
        String uuid = regResult.getJSONObject("user").getString("uuid");
        return uuid;
    }

    private static String login(String name) {
        String loginUrl = urlRoot + "/user/login";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", name);
        params.add("password", name);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, null);
        ResponseEntity<String> loginResponse = template.postForEntity(loginUrl, httpEntity, String.class);
        JSONObject loginResult = JSONObject.parseObject(loginResponse.getBody());
        String apiToken = loginResult.getJSONObject("user").getString("api_token");
        return apiToken;
    }

    private static String buy(String apiToken) {
        String buyUrl = urlRoot + "/lottery/buy";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_token", apiToken);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("api_token=" + apiToken);
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> buyResponse = template.postForEntity(buyUrl, httpEntity, String.class);
        JSONObject buyResult = JSONObject.parseObject(buyResponse.getBody());
        String enc = buyResult.getString("enc");
        return enc;
    }

    private static void charge(String uuid, String apiToken, byte[] lotteryId, byte[] userId, byte[] coin) {
        byte[] encBytes = new byte[160];
        System.arraycopy(lotteryId, 0, encBytes, 0, 64);
        System.arraycopy(userId, 0, encBytes, 64, 64);
        System.arraycopy(coin, 0, encBytes, 128, 32);
        String enc = Base64.getEncoder().encodeToString(encBytes);
        // getLotteryIfo(enc); // 能解开说明是合法的
        charge(enc, apiToken, uuid);
    }

    private static void charge(String enc, String apiToken, String uuid) {
        String chargeUrl = urlRoot + "/lottery/charge";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("enc", enc);
        params.add("user", uuid);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("api_token=" + apiToken);
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> chargeResponse = template.postForEntity(chargeUrl, httpEntity, String.class);
    }

    private static void getLotteryIfo(String enc) {
        String infoUrl = urlRoot + "/lottery/info";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("enc", enc);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, null);
        ResponseEntity<String> infoResponse = template.postForEntity(infoUrl, httpEntity, String.class);
        System.out.println("彩票信息：" + infoResponse.getBody());
    }

    private static void getFlag(String apiToken) throws UnsupportedEncodingException {
        String url = urlRoot + "/flag?api_token=" + URLEncoder.encode(apiToken, "UTF-8");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, null, String.class);
        System.out.println(response.getBody());
    }
}
