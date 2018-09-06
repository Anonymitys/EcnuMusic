package Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.widget.Toast;

public class RenderScriptUtil {
    public static Bitmap rsBlur(Context context,Bitmap source,int radius,float scale){
        Log.i("Blur","origin size:"+source.getWidth()+"*"+source.getHeight());
        int width=Math.round(source.getWidth()*scale);
        int height=Math.round(source.getHeight()*scale);
        Log.i("Blur","origin size:"+width+"*"+height);
        Bitmap inputBmp = Bitmap.createScaledBitmap(source,width,height,false);

        RenderScript renderScript=RenderScript.create(context);
        final Allocation input=Allocation.createFromBitmap(renderScript,inputBmp);
        final Allocation output=Allocation.createTyped(renderScript,input.getType());

        ScriptIntrinsicBlur scriptIntrinsicBlur=ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(inputBmp);
        renderScript.destroy();
        return inputBmp;
    }
}
