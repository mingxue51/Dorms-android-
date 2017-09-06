package entity.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.google.android.gms.maps.model.Marker;

import entity.Property.Property;
import helper.App;


public class MapResourceProvider {
    public static final Config BITMAP_CONF = Config.ARGB_8888;
    public static final Typeface mMarkerTypeface = Typeface.create("Helvetica", 0);
    private  Bitmap mThumb=null;
    private Marker mMarkerToRefresh;

    public Bitmap getmThumb() {
        return mThumb;
    }

    public static Bitmap getPropertyBitmap(int drawableResource) {
        int px = App.getInstance().getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        Drawable shape = App.getInstance().getResources().getDrawable(drawableResource);
        shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
        shape.draw(canvas);

        return mDotMarkerBitmap;
    }

    public static Bitmap getMarker(Context paramContext, String paramString) {
        Resources localResources = paramContext.getResources();
        Paint localPaint = new Paint();
        localPaint.setTypeface(mMarkerTypeface);
        localPaint.setTextSize(paramContext.getResources().getDimensionPixelSize(R.dimen.map_marker_font_size));
        localPaint.setColor(-1);
        localPaint.setTextAlign(Align.CENTER);
        float[] arrayOfFloat = new float[paramString.length()];
        localPaint.getTextWidths(paramString, arrayOfFloat);
        float f1 = 0.0F;
        int i = arrayOfFloat.length;
        for (int j = 0; j < i; j++)
            f1 += arrayOfFloat[j];
        NinePatchDrawable localNinePatchDrawable = (NinePatchDrawable) localResources.getDrawable(R.drawable.ic_map_marker);
        int k = localResources.getDimensionPixelSize(R.dimen.map_marker_height);
        float f2 = 2.0F * paramContext.getResources().getDimension(R.dimen.map_marker_padding);
        int m = localResources.getDimensionPixelSize(R.dimen.map_marker_width);
        int n = Math.max((int) (f1 + f2), m);
        Bitmap localBitmap = Bitmap.createBitmap(n, k, BITMAP_CONF);
        Canvas localCanvas = new Canvas(localBitmap);
        Rect localRect = new Rect(0, 0, n, k);
        localNinePatchDrawable.setBounds(localRect);
        localNinePatchDrawable.draw(localCanvas);
        int i1 = localResources.getDimensionPixelSize(R.dimen.map_marker_text_y);
        localCanvas.drawText(paramString, localCanvas.getWidth() / 2, i1, localPaint);
        return localBitmap;
    }
}
