package com.goalbet.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/** Custom high-contrast glowing dice illustration. */
public class NeonDiceView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float density;
    private int face=1;
    private boolean win=false;
    public NeonDiceView(Context c){super(c);density=getResources().getDisplayMetrics().density;setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    private float d(float n){return n*density;}
    public void setFace(int n,boolean success){face=Math.max(1,Math.min(6,n));win=success;invalidate();}
    @Override protected void onDraw(Canvas c){
        super.onDraw(c);float w=getWidth(),h=getHeight();
        p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(5,12,10));c.drawRoundRect(new RectF(0,0,w,h),d(22),d(22),p);
        p.setColor(Color.rgb(14,50,36));p.setAlpha(100);
        for(int i=0;i<7;i++)c.drawLine(d(12),d(16+i*19),w-d(12),d(16+i*19),p);
        p.setAlpha(255);
        float size=Math.min(h-d(28),d(116)),cx=w/2,cy=h/2-d(2);
        RectF r=new RectF(cx-size/2,cy-size/2,cx+size/2,cy+size/2);
        p.setShadowLayer(d(20),0,d(3),win?Color.rgb(0,255,145):Color.rgb(0,190,110));
        p.setColor(Color.rgb(18,39,30));c.drawRoundRect(r,d(19),d(19),p);p.clearShadowLayer();
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(d(2));p.setColor(win?Color.rgb(0,255,145):Color.rgb(0,222,123));c.drawRoundRect(r,d(19),d(19),p);p.setStyle(Paint.Style.FILL);
        float a=size*.25f;
        float[][] dots={{-a,-a},{a,-a},{-a,0},{0,0},{a,0},{-a,a},{a,a}};
        int[][] pattern={{3},{0,6},{0,3,6},{0,1,5,6},{0,1,3,5,6},{0,1,2,4,5,6}};
        int[] ids=pattern[face-1];
        for(int id:ids){float x=cx+dots[id][0],y=cy+dots[id][1];p.setColor(Color.rgb(0,255,145));p.setShadowLayer(d(7),0,0,p.getColor());c.drawCircle(x,y,d(6),p);p.clearShadowLayer();}
        p.setTextAlign(Paint.Align.CENTER);p.setTextSize(d(10));p.setTypeface(android.graphics.Typeface.create("sans-serif-medium",1));p.setColor(Color.rgb(139,183,159));
        c.drawText(win?"LUCKY HIT":"ROLL THE DICE",w/2,h-d(12),p);
    }
}
