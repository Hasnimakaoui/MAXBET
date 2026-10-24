package com.goalbet.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.content.SharedPreferences;
import java.util.Locale;
import java.util.Random;

public class ArcadeGameActivity extends Activity {
  int bg=Color.rgb(6,10,9), panel=Color.rgb(17,26,23), green=Color.rgb(0,239,129), white=Color.rgb(245,249,247), muted=Color.rgb(145,163,154), red=Color.rgb(255,78,92);
  String game="Crash"; long credits; SharedPreferences prefs; LinearLayout root; TextView balance,status,primary; EditText stakeInput;
  boolean running=false, crashed=false; double multiplier=1.0, crashAt=2.0; long roundStart; Handler handler=new Handler(); Random random=new Random();
  Runnable tick=new Runnable(){ public void run(){ if(!running)return; double sec=(System.currentTimeMillis()-roundStart)/1000.0; multiplier=Math.exp(sec*0.22); if(multiplier>=crashAt){ multiplier=crashAt; running=false; crashed=true; status.setText("CRASHED  •  ×"+fmt(multiplier)); status.setTextColor(red); primary.setText("PLAY AGAIN"); } else { status.setText("LIVE MULTIPLIER"); primary.setText("CASH OUT  ×"+fmt(multiplier)); handler.postDelayed(this,45); } }};
  String fmt(double v){return String.format(Locale.US,"%.2f",v);}
  GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(radius);return d;}
  TextView label(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
  int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
  LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w<0?w:dp(w),h<0?h:dp(h));}
  TextView button(String s){TextView t=label(s,15,bg,true);t.setBackground(shape(green,dp(14)));t.setPadding(dp(12),dp(15),dp(12),dp(15));return t;}
  public void onCreate(Bundle b){super.onCreate(b);game=getIntent().getStringExtra("game");if(game==null)game="Crash";prefs=getSharedPreferences("maxbet_arcade",MODE_PRIVATE);credits=prefs.getLong("credits",10000);getWindow().setStatusBarColor(bg);getWindow().setNavigationBarColor(bg);build();}
  void save(){prefs.edit().putLong("credits",credits).apply();balance.setText("DEMO CREDITS   "+credits);}
  void build(){root=new LinearLayout(this);root.setOrientation(1);root.setPadding(dp(18),dp(12),dp(18),dp(18));root.setBackgroundColor(bg);
    TextView back=label("‹    MAXBET ARCADE",16,green,true);back.setGravity(Gravity.CENTER_VERTICAL);back.setOnClickListener(v->finish());root.addView(back,lp(-1,48));
    root.addView(label(game.toUpperCase(Locale.ROOT),27,white,true),lp(-1,58));
    TextView info=label("PLAYABLE DEMO  •  NO REAL MONEY",11,muted,true);root.addView(info,lp(-1,28));
    balance=label("",14,green,true);LinearLayout.LayoutParams bp=lp(-1,48);bp.topMargin=dp(10);root.addView(balance,bp);save();
    if(game.toLowerCase(Locale.ROOT).contains("mine")) buildMines(); else if(game.toLowerCase(Locale.ROOT).contains("dice")) buildDice(); else buildCrash();
    setContentView(root);
  }
  void buildCrash(){
    TextView arena=label("✦\n\n1.00×",30,green,true);arena.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{Color.rgb(15,72,49),Color.rgb(11,23,19),Color.rgb(8,12,11)}));arena.setElevation(dp(4));LinearLayout.LayoutParams ap=lp(-1,230);ap.topMargin=dp(12);root.addView(arena,ap);
    status=label("PLACE YOUR DEMO BET",14,white,true);root.addView(status,lp(-1,48));
    stakeInput=new EditText(this);stakeInput.setSingleLine(true);stakeInput.setInputType(2);stakeInput.setText("100");stakeInput.setTextColor(white);stakeInput.setHintTextColor(muted);stakeInput.setHint("Demo stake");stakeInput.setPadding(dp(14),dp(12),dp(14),dp(12));stakeInput.setBackground(shape(panel,dp(12)));root.addView(stakeInput,lp(-1,52));
    primary=button("START ROUND");LinearLayout.LayoutParams pp=lp(-1,54);pp.topMargin=dp(12);root.addView(primary,pp);
    TextView rules=label("Multiplier rises over time. Cash out before the random crash to win demo credits.",12,muted,false);rules.setPadding(dp(8),dp(18),dp(8),dp(8));root.addView(rules,lp(-1,-2));
    primary.setOnClickListener(v->{if(running){long stake=stake();if(stake<=0)return;long win=(long)(stake*multiplier);credits+=win;save();running=false;handler.removeCallbacks(tick);status.setText("CASHED OUT  •  +"+win+" credits");status.setTextColor(green);primary.setText("START ROUND");return;} if(crashed){crashed=false;multiplier=1;status.setText("READY FOR NEXT ROUND");primary.setText("START ROUND");return;}long s=stake();if(s<=0){toast("Enter a valid stake");return;}if(s>credits){toast("Not enough demo credits");return;}credits-=s;save();multiplier=1;crashAt=1.15+random.nextDouble()*5.8;roundStart=System.currentTimeMillis();running=true;status.setText("ROUND IN PROGRESS");status.setTextColor(white);primary.setText("CASH OUT");handler.post(tick);});
  }
  long stake(){try{return Math.max(0,Long.parseLong(stakeInput.getText().toString().trim()));}catch(Exception e){return 0;}}
  void buildDice(){
    TextView dice=label("⚄    1 — 6    ⚄",27,green,true);dice.setBackground(shape(panel,dp(18)));LinearLayout.LayoutParams p=lp(-1,150);p.topMargin=dp(14);root.addView(dice,p);
    TextView result=label("Choose a number, then roll",15,white,true);root.addView(result,lp(-1,54));
    LinearLayout nums=new LinearLayout(this);for(int i=1;i<=6;i++){final int n=i;TextView b=label(""+i,18,green,true);b.setBackground(shape(panel,dp(12)));LinearLayout.LayoutParams q=new LinearLayout.LayoutParams(0,dp(48),1);q.setMargins(dp(3),0,dp(3),0);nums.addView(b,q);b.setOnClickListener(v->{for(int j=0;j<nums.getChildCount();j++)nums.getChildAt(j).setBackground(shape(panel,dp(12)));b.setBackground(shape(Color.rgb(30,80,56),dp(12)));nums.setTag(n);});}root.addView(nums,lp(-1,54));
    stakeInput=new EditText(this);stakeInput.setSingleLine(true);stakeInput.setInputType(2);stakeInput.setText("100");stakeInput.setTextColor(white);stakeInput.setBackground(shape(panel,dp(12)));root.addView(stakeInput,lp(-1,50));
    TextView roll=button("ROLL DICE");LinearLayout.LayoutParams rp=lp(-1,54);rp.topMargin=dp(12);root.addView(roll,rp);roll.setOnClickListener(v->{long s=stake();if(s<=0||s>credits){toast("Invalid stake or insufficient credits");return;}credits-=s;int value=1+random.nextInt(6);int chosen=nums.getTag() instanceof Integer?(Integer)nums.getTag():1;boolean win=value==chosen;if(win)credits+=s*5;save();dice.setText("⚄      "+value+"      ⚄");result.setText(win?"YOU WIN  •  +"+(s*4)+" credits":"NO WIN THIS ROUND");result.setTextColor(win?green:red);});
  }
  void buildMines(){
    TextView info=label("Find gems. Avoid the hidden mine.",14,muted,false);root.addView(info,lp(-1,48));
    stakeInput=new EditText(this);stakeInput.setSingleLine(true);stakeInput.setInputType(2);stakeInput.setText("100");stakeInput.setTextColor(white);stakeInput.setBackground(shape(panel,dp(12)));root.addView(stakeInput,lp(-1,50));
    GridLayout grid=new GridLayout(this);grid.setColumnCount(5);int mine=random.nextInt(25);final boolean[] ended={false};final int[] safe={0};final long[] bet={0};
    TextView result=label("START A ROUND",14,white,true);root.addView(result,lp(-1,48));
    TextView start=button("START MINES ROUND");LinearLayout.LayoutParams sp=lp(-1,50);root.addView(start,sp);
    start.setOnClickListener(v->{long s=stake();if(s<=0||s>credits){toast("Invalid stake or insufficient credits");return;}credits-=s;save();bet[0]=s;mine=random.nextInt(25);safe[0]=0;ended[0]=false;result.setText("ROUND ACTIVE • DEMO");for(int i=0;i<grid.getChildCount();i++){TextView c=(TextView)grid.getChildAt(i);c.setText("✦");c.setTextColor(green);c.setBackground(shape(panel,dp(8)));c.setTag(i);} });
    for(int i=0;i<25;i++){final int idx=i;TextView cell=label("?",18,green,true);cell.setBackground(shape(panel,dp(8)));GridLayout.LayoutParams gp=new GridLayout.LayoutParams();gp.width=0;gp.height=dp(49);gp.columnSpec=GridLayout.spec(i%5,1f);gp.setMargins(dp(3),dp(3),dp(3),dp(3));grid.addView(cell,gp);cell.setOnClickListener(v->{if(ended[0]||bet[0]<=0)return;if(idx==mine){cell.setText("✹");cell.setTextColor(red);ended[0]=true;result.setText("MINE HIT • ROUND LOST");result.setTextColor(red);for(int k=0;k<grid.getChildCount();k++)if(k==mine)((TextView)grid.getChildAt(k)).setText("✹");}else{safe[0]++;cell.setText("◆");cell.setTextColor(green);cell.setBackground(shape(Color.rgb(18,62,43),dp(8)));long potential=(long)(bet[0]*(1+safe[0]*0.18));result.setText("GEMS: "+safe[0]+"   CASHOUT: "+potential);}});}
    LinearLayout.LayoutParams gp=lp(-1,-2);gp.topMargin=dp(10);root.addView(grid,gp);
    TextView cash=button("CASH OUT");LinearLayout.LayoutParams cp=lp(-1,52);cp.topMargin=dp(10);root.addView(cash,cp);cash.setOnClickListener(v->{if(ended[0]||safe[0]==0||bet[0]<=0){toast("Find a gem first");return;}long win=(long)(bet[0]*(1+safe[0]*0.18));credits+=win;save();ended[0]=true;result.setText("CASHED OUT • +"+(win-bet[0])+" credits");result.setTextColor(green);});
  }
  void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
  @Override protected void onDestroy(){handler.removeCallbacks(tick);super.onDestroy();}
}