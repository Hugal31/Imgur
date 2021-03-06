package com.github.Hugal31.imgur;

import com.github.scribejava.core.model.OAuthRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

final class GalleryUtil {

    static List<ImgurItem> createGallery(JSONArray data) {
        List<ImgurItem> itemList = new ArrayList<ImgurItem>();

        for (int i = 0; i < data.length(); i++) {
            Object o = data.get(i);
            if (o instanceof JSONObject) {
                JSONObject object = (JSONObject) o;
                if ((object).has("cover"))
                    itemList.add(AlbumUtil.createAlbum(object));
                else
                    itemList.add(ImageUtil.createImage(object));
            }
        }
        return itemList;
    }

    static List<ImgurItem> requestGallery(Imgur imgur, OAuthRequest request) throws ImgurException {
        try {
            return createGallery(imgur.executeJSONRequest(request).getJSONArray("data"));
        } catch (JSONException e) {
            throw new ImgurException(e);
        }
    }

}
