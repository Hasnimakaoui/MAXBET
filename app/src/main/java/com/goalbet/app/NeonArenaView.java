package com.goalbet.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;

/** Animated neon Crash arena with particles, luminous trajectory and a flying rocket. */
public class NeonArenaView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float density;
    private double multiplier = 1.0;
    private boolean live = false, crashed = false;
    private final float[] starX = new float[38], starY = new float[38], starR = new float[38];
    private final int[] starA = new int[38];

    public NeonArenaView(Context c) {
        super(c);
        density = getResources().getDisplayMetrics().density;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        java.util.Random rnd = new java.util.Random(42);
        for (int i=0;i<starX.length;i++) {
            starX[i]=rnd.nextFloat(); starY[i]=rnd.nextFloat();
            starR[i]=1+rnd.nextFloat()*2.2f; starA[i]=55+rnd.nextInt(150);
        }
    }
    public void setState(double m, boolean running, boolean didCrash) {
        multiplier=m; live=running; crashed=didCrash; invalidate();
    }
    private float d(float v) { return v*density; }

    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        float w=getWidth(), h=getHeight();
        if(w<=0||h<=0)return;
        float pad=d(18), bottom=h-d(25), top=d(22), right=w-d(18);
        long now=System.currentTimeMillis();
        float pulse=(float)(.5+.5*Math.sin(now/190.0));
        int neon=crashed?Color.rgb(255,55,83):Color.rgb(0,255,151);

        p.setStyle(Paint.Style.FILL);
        p.setShader(new LinearGradient(0,0,w,h,new int[]{Color.rgb(8,45,32),Color.rgb(7,20,18),Color.rgb(3,8,10)},null,Shader.TileMode.CLAMP));
        c.drawRoundRect(new RectF(0,0,w,h),d(22),d(22),p); p.setShader(null);
        p.setShader(new RadialGradient(w*.72f,h*.22f,d(145),new int[]{crashed?0x55ff274e:0x5500ff96,0x0000ff96},null,Shader.TileMode.CLAMP));
        c.drawRect(0,0,w,h,p); p.setShader(null);

        // Drifting stars and tiny sparks.
        for(int i=0;i<starX.length;i++){
            float drift=live?(now/32f*(.08f+(i%4)*.025f))%1f:0;
            float sx=starX[i]*w, sy=((starY[i]+drift)%1f)*h;
            p.setColor(Color.argb((int)(starA[i]*(.55f+.45f*pulse)),120,255,196));
            c.drawCircle(sx,sy,d(starR[i]),p);
        }
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(d(.8f)); p.setColor(Color.rgb(17,83,59)); p.setAlpha(115);
        for(int i=0;i<9;i++) { float gx=pad+i*(right-pad)/8; c.drawLine(gx,top,gx,bottom,p); }
        for(int i=0;i<5;i++) { float gy=top+i*(bottom-top)/4; c.drawLine(pad,gy,right,gy,p); }
        p.setAlpha(255); p.setStrokeWidth(d(1)); p.setColor(neon); p.setAlpha(90);
        c.drawRoundRect(new RectF(d(1),d(1),w-d(1),h-d(1)),d(22),d(22),p); p.setAlpha(255);

        float progress=(float)Math.min(1,Math.max(.10,(multiplier-1)/5.2));
        float endX=pad+progress*(right-pad);
        float endY=bottom-(float)Math.pow(progress,1.65)*(bottom-top)*Math.min(1.15f,.25f+progress*1.1f);
        Path curve=new Path(); curve.moveTo(pad,bottom);
        for(int i=1;i<=48;i++){
            float t=i/48f*progress, x=pad+t*(right-pad);
            float y=bottom-(float)Math.pow(t,1.65)*(bottom-top)*Math.min(1.15f,.25f+progress*1.1f);
            curve.lineTo(x,y);
        }
        Path area=new Path(curve); area.lineTo(endX,bottom); area.lineTo(pad,bottom); area.close();
        p.setStyle(Paint.Style.FILL);
        p.setShader(new LinearGradient(0,top,0,bottom,new int[]{crashed?0x44ff234c:0x4400ff98,0x0000ff98},null,Shader.TileMode.CLAMP));
        c.drawPath(area,p); p.setShader(null);
        p.setStyle(Paint.Style.STROKE); p.setStrokeCap(Paint.Cap.ROUND); p.setStrokeJoin(Paint.Join.ROUND);
        p.setColor(neon); p.setStrokeWidth(d(14)); p.setAlpha(24); p.setShadowLayer(d(20),0,0,neon); c.drawPath(curve,p); p.clearShadowLayer();
        p.setAlpha(75); p.setStrokeWidth(d(6)); c.drawPath(curve,p);
        p.setAlpha(255); p.setStrokeWidth(d(2.8f)); c.drawPath(curve,p);

        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(d(1.5f)); p.setColor(neon); p.setAlpha(150);
        c.drawCircle(endX,endY,d(10+8*pulse),p); p.setAlpha(255);
        p.setStyle(Paint.Style.FILL); p.setColor(neon); p.setShadowLayer(d(17),0,0,neon);
        c.drawCircle(endX,endY,d(4.5f+1.5f*pulse),p); p.clearShadowLayer();

        // Sleek rocket with animated exhaust.
        c.save(); c.rotate(-25*progress-12,endX,endY);
        p.setColor(Color.WHITE); p.setStyle(Paint.Style.FILL);
        Path ship=new Path(); ship.moveTo(endX+d(12),endY); ship.lineTo(endX-d(6),endY-d(5)); ship.lineTo(endX-d(3),endY); ship.lineTo(endX-d(6),endY+d(5)); ship.close();
        p.setShadowLayer(d(8),0,0,neon); c.drawPath(ship,p); p.clearShadowLayer();
        p.setColor(neon); Path flame=new Path(); flame.moveTo(endX-d(5),endY); flame.lineTo(endX-d(12+3*pulse),endY-d(2)); flame.lineTo(endX-d(10),endY+d(2)); flame.close(); c.drawPath(flame,p);
        c.restore();

        p.setTextAlign(Paint.Align.CENTER); p.setTypeface(android.graphics.Typeface.create("sans-serif-black",1));
        p.setTextSize(d(35)); p.setColor(crashed?Color.rgb(255,82,100):Color.WHITE);
        p.setShadowLayer(d(12),0,0,crashed?Color.RED:neon);
        c.drawText(crashed?"CRASHED":String.format(java.util.Locale.US,"%.2f×",multiplier),w/2,h/2+d(5),p); p.clearShadowLayer();
        p.setTextSize(d(9)); p.setTypeface(android.graphics.Typeface.create("sans-serif-medium",1));
        p.setColor(crashed?Color.rgb(255,120,135):Color.rgb(147,220,181));
        c.drawText(crashed?"●  ROUND OVER":live?"●  MULTIPLIER RISING":"●  READY TO LAUNCH",w/2,h-d(11),p);
        p.setTextAlign(Paint.Align.LEFT); p.setTypeface(android.graphics.Typeface.DEFAULT);
        if(live) postInvalidateDelayed(35);
    }
}
