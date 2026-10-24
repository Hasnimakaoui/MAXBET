package com.goalbet.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/** Custom animated neon arena for the playable Crash demo. */
public class NeonArenaView extends View {
    private final Paint p = new Paint(3);
    private final float density;
    private double multiplier = 1.0;
    private boolean live = false, crashed = false;
    private long started = System.currentTimeMillis();

    public NeonArenaView(Context c) {
        super(c);
        density = getResources().getDisplayMetrics().density;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setBackgroundColor(Color.rgb(5, 12, 10));
    }
    public void setState(double m, boolean running, boolean didCrash) {
        multiplier = m; live = running; crashed = didCrash;
        if (running) started = System.currentTimeMillis();
        invalidate();
    }
    private float d(float v) { return v * density; }
    private void line(Canvas c, float x1,float y1,float x2,float y2,int color,float width) {
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(d(width)); p.setColor(color);
        p.setStrokeCap(Paint.Cap.ROUND); p.setStrokeJoin(Paint.Join.ROUND);
        c.drawLine(x1,y1,x2,y2,p);
    }
    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w=getWidth(), h=getHeight();
        if(w<=0||h<=0)return;
        float pad=d(18), bottom=h-d(24), top=d(24), right=w-d(18);
        p.setStyle(Paint.Style.FILL);
        p.setShader(new android.graphics.LinearGradient(0,0,w,h,
            new int[]{Color.rgb(10,48,35),Color.rgb(7,20,17),Color.rgb(4,10,10)},null,android.graphics.Shader.TileMode.CLAMP));
        c.drawRoundRect(new RectF(0,0,w,h),d(22),d(22),p); p.setShader(null);
        p.setColor(Color.rgb(14,49,36)); p.setAlpha(85);
        for(int i=0;i<9;i++) c.drawLine(pad+i*(right-pad)/8,top,pad+i*(right-pad)/8,bottom,p);
        for(int i=0;i<5;i++) c.drawLine(pad,top+i*(bottom-top)/4,right,top+i*(bottom-top)/4,p);
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(d(1));p.setColor(Color.rgb(0,239,129));p.setAlpha(65);
        c.drawRoundRect(new RectF(d(1),d(1),w-d(1),h-d(1)),d(22),d(22),p);p.setAlpha(255);
        float progress=(float)Math.min(1,Math.max(.08,(multiplier-1)/5.2));
        Path curve=new Path(); curve.moveTo(pad,bottom);
        for(int i=1;i<=32;i++){
            float t=i/32f, x=pad+t*(right-pad);
            float y=bottom-(float)Math.pow(t,1.65)*(bottom-top)*Math.min(1.15f,.25f+progress*1.1f);
            curve.lineTo(x,y);
        }
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(d(10));p.setColor(Color.rgb(0,239,129));p.setAlpha(28);p.setShadowLayer(d(18),0,0,Color.rgb(0,255,139));c.drawPath(curve,p);p.clearShadowLayer();
        p.setAlpha(255);p.setStrokeWidth(d(3));p.setColor(crashed?Color.rgb(255,65,90):Color.rgb(0,255,145));c.drawPath(curve,p);
        float t=progress, x=pad+t*(right-pad), y=bottom-(float)Math.pow(t,1.65)*(bottom-top)*Math.min(1.15f,.25f+progress*1.1f);
        p.setStyle(Paint.Style.FILL);p.setColor(crashed?Color.rgb(255,65,90):Color.rgb(0,255,145));p.setShadowLayer(d(22),0,0,p.getColor());c.drawCircle(x,y,d(6),p);p.clearShadowLayer();
        p.setTextAlign(Paint.Align.CENTER);p.setTypeface(android.graphics.Typeface.create("sans-serif-black",1));
        p.setTextSize(d(35));p.setColor(crashed?Color.rgb(255,79,95):Color.WHITE);
        c.drawText(crashed?"CRASHED":String.format(java.util.Locale.US,"%.2f×",multiplier),w/2, h/2+d(4),p);
        p.setTextSize(d(10));p.setColor(Color.rgb(143,185,163));
        c.drawText(crashed?"ROUND OVER":live?"MULTIPLIER RISING":"READY TO LAUNCH",w/2,h-d(12),p);
        p.setTextAlign(Paint.Align.LEFT);p.setTypeface(android.graphics.Typeface.DEFAULT);
        if(live){ postInvalidateDelayed(40); }
    }
}
