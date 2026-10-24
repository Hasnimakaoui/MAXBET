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

        // Detailed miniature aircraft with swept wings, cockpit, tail and twin engine trails.
        c.save(); c.translate(endX,endY); c.rotate(-25*progress-12);
        p.setStyle(Paint.Style.FILL); p.setColor(neon); p.setAlpha(160);
        p.setShadowLayer(d(8),-d(15),0,neon);
        c.drawRoundRect(new RectF(-d(24+4*pulse),-d(5),-d(7),-d(2)),d(2),d(2),p);
        c.drawRoundRect(new RectF(-d(24+4*pulse),d(2),-d(7),d(5)),d(2),d(2),p);
        p.clearShadowLayer(); p.setAlpha(255);
        Path wings=new Path(); wings.moveTo(d(5),-d(2)); wings.lineTo(-d(8),-d(18)); wings.lineTo(-d(16),-d(20)); wings.lineTo(-d(12),-d(4)); wings.lineTo(-d(22),0); wings.lineTo(-d(12),d(4)); wings.lineTo(-d(16),d(20)); wings.lineTo(-d(8),d(18)); wings.lineTo(d(5),d(2)); wings.close();
        p.setColor(Color.rgb(155,190,180)); p.setShadowLayer(d(7),0,0,neon); c.drawPath(wings,p); p.clearShadowLayer();
        Path tail=new Path(); tail.moveTo(-d(13),-d(3)); tail.lineTo(-d(21),-d(10)); tail.lineTo(-d(23),-d(10)); tail.lineTo(-d(20),-d(2)); tail.lineTo(-d(20),d(2)); tail.lineTo(-d(23),d(10)); tail.lineTo(-d(21),d(10)); tail.lineTo(-d(13),d(3)); tail.close();
        p.setColor(Color.rgb(90,135,122)); c.drawPath(tail,p);
        Path body=new Path(); body.moveTo(d(20),0); body.lineTo(d(8),-d(3)); body.lineTo(-d(16),-d(3.5f)); body.lineTo(-d(20),-d(2)); body.lineTo(-d(20),d(2)); body.lineTo(-d(16),d(3.5f)); body.lineTo(d(8),d(3)); body.close();
        p.setShader(new LinearGradient(-d(20),0,d(20),0,new int[]{Color.rgb(50,80,73),Color.rgb(245,255,250),Color.rgb(105,160,142)},null,Shader.TileMode.CLAMP)); c.drawPath(body,p); p.setShader(null);
        Path cockpit=new Path(); cockpit.moveTo(d(11),-d(1.5f)); cockpit.lineTo(d(3),-d(2)); cockpit.lineTo(-d(3),-d(1.5f)); cockpit.lineTo(d(1),d(1.5f)); cockpit.close();
        p.setColor(Color.rgb(20,115,125)); p.setShadowLayer(d(3),0,0,Color.CYAN); c.drawPath(cockpit,p); p.clearShadowLayer();
        p.setColor(neon); p.setStrokeWidth(d(1)); p.setStyle(Paint.Style.STROKE); c.drawLine(-d(14),0,d(11),0,p); p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE); c.drawCircle(-d(10),-d(13),d(1.3f),p); c.drawCircle(-d(10),d(13),d(1.3f),p);
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
