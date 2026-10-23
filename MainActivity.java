package com.goalbet.app;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
 private final int bg=Color.rgb(8,11,10), panel=Color.rgb(19,25,22), green=Color.rgb(184,255,59), muted=Color.rgb(151,164,155);
 public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(bg);getWindow().setNavigationBarColor(bg);
  LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(bg);
  LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);top.setPadding(dp(18),dp(16),dp(18),dp(12));
  TextView logo=txt("GOAL",25,green,true), logo2=txt("BET",25,Color.WHITE,true);top.addView(logo);top.addView(logo2);top.addView(new View(this),new LinearLayout.LayoutParams(0,1,1));
  TextView wallet=txt("◈  WALLET",12,green,true);wallet.setPadding(dp(12),dp(10),dp(12),dp(10));wallet.setBackground(round(panel,12));top.addView(wallet);root.addView(top);
  HorizontalScrollView hs=new HorizontalScrollView(this);hs.setHorizontalScrollBarEnabled(false);LinearLayout tabs=new LinearLayout(this);tabs.setPadding(dp(10),0,dp(10),dp(10));
  String[] names={"LIVE","FOOTBALL","BASKETBALL","TENNIS","CASINO"};for(int i=0;i<names.length;i++){final String n=names[i];TextView t=txt(n,11,i==0?bg:muted,true);t.setGravity(17);t.setPadding(dp(15),dp(11),dp(15),dp(11));t.setBackground(round(i==0?green:panel,10));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-2,-2);p.setMargins(dp(4),0,dp(4),0);tabs.addView(t,p);t.setOnClickListener(v->toast(n+" — demo"));}hs.addView(tabs);root.addView(hs);
  ScrollView scroll=new ScrollView(this);LinearLayout body=new LinearLayout(this);body.setOrientation(1);body.setPadding(dp(16),dp(8),dp(16),dp(18));scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  body.addView(txt("THE GAME STARTS HERE",24,Color.WHITE,true));TextView sub=txt("Your matchday. Your moment.",13,muted,false);sub.setPadding(0,dp(5),0,dp(16));body.addView(sub);
  LinearLayout hero=new LinearLayout(this);hero.setOrientation(1);hero.setPadding(dp(16),dp(16),dp(16),dp(16));hero.setBackground(round(Color.rgb(29,43,20),16));
  hero.addView(txt("●  MATCHDAY SPOTLIGHT",10,green,true));TextView match=txt("REAL MADRID   vs   BARCELONA",17,Color.WHITE,true);match.setPadding(0,dp(14),0,dp(6));hero.addView(match);hero.addView(txt("Preview match — live data provider not connected.",11,muted,false));
  LinearLayout odds=new LinearLayout(this);odds.setPadding(0,dp(14),0,0);for(String s:new String[]{"1   2.10","X   3.25","2   2.85"}){TextView o=txt(s,13,green,true);o.setGravity(17);o.setPadding(dp(8),dp(12),dp(8),dp(12));o.setBackground(round(bg,10));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.setMargins(0,0,dp(7),0);odds.addView(o,p);o.setOnClickListener(v->toast("Demo only — live odds not connected"));}hero.addView(odds);LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.setMargins(0,0,0,dp(20));body.addView(hero,hp);
  body.addView(txt("QUICK ACCESS",12,muted,true));LinearLayout row1=new LinearLayout(this);row1.addView(tile("⚽","Sports","Matches & markets"),new LinearLayout.LayoutParams(0,dp(112),1));row1.addView(new View(this),new LinearLayout.LayoutParams(dp(10),1));row1.addView(tile("✦","Casino","Provider integration"),new LinearLayout.LayoutParams(0,dp(112),1));body.addView(row1);
  LinearLayout row2=new LinearLayout(this);row2.setPadding(0,dp(10),0,0);row2.addView(tile("◈","USDT Wallet","Demo balance only"),new LinearLayout.LayoutParams(0,dp(112),1));row2.addView(new View(this),new LinearLayout.LayoutParams(dp(10),1));row2.addView(tile("◎","My Activity","Bet & transaction history"),new LinearLayout.LayoutParams(0,dp(112),1));body.addView(row2);
  TextView note=txt("DEMO BUILD • No real deposits, withdrawals, or betting enabled.",11,muted,false);note.setGravity(17);note.setPadding(dp(8),dp(18),dp(8),dp(8));body.addView(note);
  LinearLayout bottom=new LinearLayout(this);bottom.setGravity(17);bottom.setPadding(dp(8),dp(10),dp(8),dp(10));bottom.setBackgroundColor(Color.rgb(13,17,15));String[] bn={"⌂ Home","▤ Sports","✦ Casino","◈ Wallet","☻ Profile"};for(int i=0;i<bn.length;i++){final String n=bn[i];TextView t=txt(n,10,i==0?green:muted,i==0);t.setGravity(17);bottom.addView(t,new LinearLayout.LayoutParams(0,dp(34),1));t.setOnClickListener(v->toast(n+" — demo"));}root.addView(bottom);setContentView(root);
 }
 private View tile(String icon,String title,String desc){LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(dp(13),dp(12),dp(10),dp(10));box.setBackground(round(panel,14));box.addView(txt(icon,20,green,true));TextView t=txt(title,14,Color.WHITE,true);t.setPadding(0,dp(5),0,dp(3));box.addView(t);box.addView(txt(desc,10,muted,false));box.setOnClickListener(v->toast(title+" — demo preview"));return box;}
 private TextView txt(String s,int z,int c,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setTextColor(c);t.setTypeface(Typeface.create("sans-serif",bold?Typeface.BOLD:Typeface.NORMAL));return t;}
 private GradientDrawable round(int c,int r){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(r));return d;}
 private int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+0.5f);}
 private void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}
