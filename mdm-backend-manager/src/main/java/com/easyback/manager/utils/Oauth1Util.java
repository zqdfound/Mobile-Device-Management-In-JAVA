package com.easyback.manager.utils;

import cn.hutool.json.JSONUtil;
import com.alipay.api.internal.util.file.IOUtils;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Oauth1请求
 * @Author: zhuangqingdian
 * @Date:2023/7/28
 */
@Slf4j
public class Oauth1Util {

    private OAuthConsumer oAuthConsumer;
    private static String realmID = "ADM";

    public Oauth1Util(String consumerKey,String consumerSecret, String accessToken,String accessTokenSecret) {
        realmID = "ADM";
//        String consumerKey = "CK_f55c066fffbc344a1f8a639e82e01e08861fc3bc5a6e59eff9ef75b702294f5260a3b43db0a5910e4d295a244053782e";
//        String consumerSecret = "CS_e50cd0b30e6c830c592d20b54cf67638c2d8bc1a";
//        String accessToken = "AT_O18328322013Obb742028cbee165192c3ca2528c6ace1d2ab3171O1689575801726";
//        String accessTokenSecret = "AS_2546ad44e047d590070f834821284b42d63bc765";

        setupContext(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    public void setupContext(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        oAuthConsumer.setTokenWithSecret(accessToken, accessTokenSecret);
        oAuthConsumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
    }

    public void authorize(HttpRequestBase httpRequest) {
        try {
            oAuthConsumer.sign(httpRequest);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }
    }

    public String executeGetRequest(String customURIString){
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

        HttpRequestBase httpRequest = null;
        URI uri = null;

        try {
            uri = new URI(customURIString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

            httpRequest = new HttpGet(uri);

        httpRequest.addHeader("content-type", "application/xml");
        httpRequest.addHeader("Accept","application/xml");

        try {
            authorize(httpRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


        CloseableHttpResponse httpResponse = null;
        try {
            HttpHost target = new HttpHost(uri.getHost(), -1, uri.getScheme());
            httpResponse = client.execute(target, httpRequest);
            System.out.println("Connection status : " + httpResponse.getStatusLine());

            InputStream inputStraem = httpResponse.getEntity().getContent();

            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStraem, writer, "UTF-8");
            String output = writer.toString();
            log.info("OAUTH1.0 调用返回值：{}", JSONUtil.toJsonStr(output));
            return output;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

//    public static void main(String args[]) {
//        Oauth1Util withoutDevkitClient = new Oauth1Util();
//        withoutDevkitClient.executeGetRequest("https://mdmenrollment.apple.com/session");
//    }

}
