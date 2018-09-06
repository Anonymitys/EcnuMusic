package Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by linbingjie on 2017/5/19.
 */

public class GlideImgManager {

    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }
    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoaderCircle(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).
                transform(new CenterCrop(context), new GlideRoundTransform(context)).crossFade().into(iv);
    }


    public static void glideLoaderCircleCorner(Context context, String url, int erroImg, int emptyImg, float corner , ImageView iv) {
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).
                transform(new CenterCrop(context), new GlideRoundTransform(context,corner)).crossFade().into(iv);
    }



}
