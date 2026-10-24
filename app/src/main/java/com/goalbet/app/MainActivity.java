package com.goalbet.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(8, 15, 13);
    private final int panel = Color.rgb(18, 30, 25);
    private final int green = Color.rgb(0, 230, 118);
    private TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(16, 12, 16, 12); return t;
    }
    private void match(LinearLayout root, String title, String detail) {
        LinearLayout c = new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setBackgroundColor(panel);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); p.setMargins(0, 8, 0, 8); root.addView(c, p);
        c.addView(label(title, 18, Color.WHITE, true)); c.addView(label(detail, 14, Color.LTGRAY, false));
    }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state); getWindow().setStatusBarColor(bg); getWindow().setNavigationBarColor(bg);
        ScrollView scroll = new ScrollView(this); scroll.setBackgroundColor(bg);
        LinearLayout root = new LinearLayout(this); root.setPadding(18, 18, 18, 18); root.setOrientation(LinearLayout.VERTICAL); scroll.addView(root);
        TextView logo = label("MAXBET", 30, green, true); logo.setGravity(Gravity.CENTER); root.addView(logo);
        TextView sub = label("SPORTS  •  LIVE  •  MATCH CENTER", 12, Color.LTGRAY, true); sub.setGravity(Gravity.CENTER); root.addView(sub);
        root.addView(label("MATCHES TODAY", 20, Color.WHITE, true));
        match(root, "⚽  Manchester City  vs  Arsenal", "Premier League  •  20:00\n1   2.10       X   3.20       2   3.40");
        match(root, "⚽  Real Madrid  vs  Barcelona", "La Liga  •  21:00\n1   2.35       X   3.10       2   2.85");
        match(root, "⚽  PSG  vs  Marseille", "Ligue 1  •  21:45\n1   1.90       X   3.50       2   3.80");
        root.addView(label("DEMO VERSION • Sample matches and odds only. No real betting or payments.", 12, Color.GRAY, false));
        setContentView(scroll);
    }
}
