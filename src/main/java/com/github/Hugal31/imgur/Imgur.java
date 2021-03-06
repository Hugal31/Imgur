package com.github.Hugal31.imgur;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.json.JSONObject;

public class Imgur {

    public static final String API_URL = "https://api.imgur.com/3";

    private final String apiKey;

    private final String apiSecret;

    private final OAuth20Service oAuthService;

    private OAuth2AccessToken accessToken;

    private AccountInterface accountInterface;

    private AlbumInterface albumInterface;

    private AuthInterface authInterface;

    private GalleryInterface galleryInterface;

    private ImageInterface imageInterface;

    public Imgur(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, null);
    }

    public Imgur(String apiKey, String apiSecret, String callback) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        ServiceBuilder serviceBuilder = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret);
        if (callback != null)
            serviceBuilder.callback(callback);
        oAuthService = serviceBuilder.build(com.github.scribejava.apis.ImgurApi.instance());
    }

    public AccountInterface getAccountInterface() {
        if (accountInterface == null) {
            accountInterface = new AccountInterface(this);
        }
        return accountInterface;
    }

    public AlbumInterface getAlbumInterface() {
        if (albumInterface == null) {
            albumInterface = new AlbumInterface(this);
        }
        return albumInterface;
    }

    public AuthInterface getAuthInterface() {
        if (authInterface == null) {
            authInterface = new AuthInterface(this);
        }
        return authInterface;
    }

    public GalleryInterface getGalleryInterface() {
        if (galleryInterface == null) {
            galleryInterface = new GalleryInterface(this);
        }
        return galleryInterface;
    }

    public ImageInterface getImageInterface() {
        if (imageInterface == null) {
            imageInterface = new ImageInterface(this);
        }
        return imageInterface;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public OAuth20Service getOAuthService() {
        return oAuthService;
    }

    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public JSONObject executeJSONRequest(OAuthRequest request) throws ImgurException {
        getOAuthService().signRequest(getAccessToken(), request);

        try {
            Response response = getOAuthService().execute(request);

            JSONObject jsonResponse = new JSONObject(response.getBody());
            if (! jsonResponse.optBoolean("success", false)) {
                String errorMsg = response.getBody();
                if (jsonResponse.optJSONObject("data") != null)
                    errorMsg = jsonResponse.optJSONObject("data").optString("error", errorMsg);
                throw new ImgurException(String.format("Imgur return code %d and error: '%s')",
                        jsonResponse.optInt("code", response.getCode()), errorMsg));
            }
            return jsonResponse;
        } catch (ImgurException e) {
            throw e;
        } catch (Exception e) {
            throw new ImgurException(e);
        }
    }

}
