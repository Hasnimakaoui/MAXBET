package com.goalbet.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(7, 12, 11);
    private final int surface = Color.rgb(17, 26, 23);
    private final int surface2 = Color.rgb(24, 36, 31);
    private final int green = Color.rgb(0, 239, 129);
    private final int white = Color.rgb(245, 249, 247);
    private final int muted = Color.rgb(145, 163, 154);

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    private GradientDrawable outline(int color, int stroke, int radius) {
        GradientDrawable d = shape(color, radius);
        d.setStroke(1, stroke);
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        t.setIncludeFontPadding(false);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout row() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        return l;
    }

    private void pad(View v, int left, int top, int right, int bottom) {
        v.setPadding(dp(left), dp(top), dp(right), dp(bottom));
    }

    private int dp(int n) {
        return (int)(n * getResources().getDisplayMetrics().density + 0.5f);
    }

    private LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h));
    }

    private TextView chip(String value, boolean active) {
        TextView t = text(value, 12, active ? bg : muted, true);
        t.setGravity(Gravity.CENTER);
        pad(t, 16, 10, 16, 10);
        t.setBackground(shape(active ? green : surface2, 50));
        LinearLayout.LayoutParams p = lp(-2, 38);
        p.setMargins(0, 0, dp(8), 0);
        t.setLayoutParams(p);
        return t;
    }

    private TextView odds(String value) {
        TextView t = text(value, 13, white, true);
        t.setGravity(Gravity.CENTER);
        pad(t, 4, 12, 4, 12);
        t.setBackground(outline(surface2, Color.rgb(47, 69, 58), 10));
        return t;
    }

    private void addMatch(LinearLayout parent, String league, String time,
                          String home, String away, String homeOdds, String drawOdds, String awayOdds) {
        LinearLayout card = column();
        pad(card, 15, 14, 15, 15);
        card.setBackground(outline(surface, Color.rgb(34, 49, 42), 18));
        LinearLayout.LayoutParams cp = lp(-1, -2);
        cp.setMargins(0, 0, 0, dp(12));
        parent.addView(card, cp);

        LinearLayout meta = row();
        TextView leagueText = text("●  " + league, 11, green, true);
        meta.addView(leagueText, new LinearLayout.LayoutParams(0, -2, 1));
        TextView timeText = text("◷  " + time, 11, muted, false);
        meta.addView(timeText);
        card.addView(meta);

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(36, 51, 44));
        LinearLayout.LayoutParams dpLine = lp(-1, 1);
        dpLine.setMargins(0, dp(12), 0, dp(12));
        card.addView(divider, dpLine);

        LinearLayout teams = row();
        LinearLayout names = column();
        names.addView(text(home, 15, white, true));
        TextView awayText = text(away, 15, white, true);
        awayText.setPadding(0, dp(10), 0, 0);
        names.addView(awayText);
        teams.addView(names, new LinearLayout.LayoutParams(0, -2, 1));
        LinearLayout score = column();
        score.setGravity(Gravity.CENTER);
        TextView vs = text("VS", 12, muted, true);
        vs.setGravity(Gravity.CENTER);
        score.addView(vs);
        teams.addView(score, lp(42, -2));
        card.addView(teams);

        LinearLayout market = row();
        market.setPadding(0, dp(14), 0, 0);
        String[] labels = {"1", "X", "2"};
        String[] values = {homeOdds, drawOdds, awayOdds};
        for (int i = 0; i < 3; i++) {
            LinearLayout cell = column();
            cell.setGravity(Gravity.CENTER);
            TextView label = text(labels[i], 10, muted, true);
            label.setGravity(Gravity.CENTER);
            cell.addView(label);
            TextView odd = odds(values[i]);
            LinearLayout.LayoutParams op = lp(-1, 42);
            op.setMargins(0, dp(6), 0, 0);
            cell.addView(odd, op);
            LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(0, -2, 1);
            if (i > 0) mp.setMargins(dp(7), 0, 0, 0);
            market.addView(cell, mp);
        }
        card.addView(market);
    }

    private void sectionTitle(LinearLayout root, String title, String action) {
        LinearLayout r = row();
        TextView t = text(title, 18, white, true);
        r.addView(t, new LinearLayout.LayoutParams(0, -2, 1));
        r.addView(text(action, 12, green, true));
        LinearLayout.LayoutParams p = lp(-1, -2);
        p.setMargins(0, dp(24), 0, dp(13));
        root.addView(r, p);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(0);

        LinearLayout page = column();
        page.setBackgroundColor(bg);

        LinearLayout top = row();
        pad(top, 20, 13, 20, 13);
        TextView brand = text("MAX", 26, white, true);
        TextView brandAccent = text("BET", 26, green, true);
        LinearLayout logo = row();
        logo.addView(brand);
        logo.addView(brandAccent);
        top.addView(logo, new LinearLayout.LayoutParams(0, -2, 1));
        TextView profile = text("●", 19, green, true);
        profile.setGravity(Gravity.CENTER);
        profile.setBackground(outline(surface2, Color.rgb(45, 65, 54), 50));
        top.addView(profile, lp(42, 42));
        page.addView(top);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        LinearLayout content = column();
        pad(content, 18, 8, 18, 24);
        scroll.addView(content);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout hero = column();
        pad(hero, 19, 20, 19, 20);
        GradientDrawable heroBg = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{Color.rgb(19, 69, 47), Color.rgb(12, 31, 24), Color.rgb(17, 27, 23)});
        heroBg.setCornerRadius(dp(22));
        hero.setBackground(heroBg);
        TextView eyebrow = text("YOUR SPORTS HUB", 10, green, true);
        hero.addView(eyebrow);
        TextView headline = text("THE GAME\nSTARTS HERE.", 28, white, true);
        headline.setLineSpacing(dp(2), 1.0f);
        LinearLayout.LayoutParams hp = lp(-1, -2);
        hp.setMargins(0, dp(10), 0, dp(7));
        hero.addView(headline, hp);
        hero.addView(text("Follow the action. Explore every match.", 12, muted, false));
        LinearLayout.LayoutParams heroLp = lp(-1, -2);
        heroLp.setMargins(0, 0, 0, dp(6));
        content.addView(hero, heroLp);

        sectionTitle(content, "Explore sports", "ALL SPORTS  ↗");
        HorizontalScrollView sportsScroll = new HorizontalScrollView(this);
        sportsScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout sports = row();
        sports.addView(chip("⚽  Football", true));
        sports.addView(chip("🏀  Basketball", false));
        sports.addView(chip("🎾  Tennis", false));
        sports.addView(chip("🏒  Hockey", false));
        sportsScroll.addView(sports);
        content.addView(sportsScroll);

        sectionTitle(content, "Top matches", "VIEW ALL  ↗");
        LinearLayout live = row();
        TextView liveDot = text("● LIVE", 10, green, true);
        live.addView(liveDot);
        TextView today = text("   •   TODAY'S PICKS", 10, muted, true);
        live.addView(today);
        LinearLayout.LayoutParams liveLp = lp(-1, -2);
        liveLp.setMargins(0, 0, 0, dp(12));
        content.addView(live, liveLp);

        addMatch(content, "PREMIER LEAGUE", "20:00", "Manchester City", "Arsenal", "2.10", "3.20", "3.40");
        addMatch(content, "LA LIGA", "21:00", "Real Madrid", "Barcelona", "2.35", "3.10", "2.85");
        addMatch(content, "LIGUE 1", "21:45", "Paris Saint-Germain", "Marseille", "1.90", "3.50", "3.80");

        LinearLayout footer = column();
        footer.setGravity(Gravity.CENTER);
        pad(footer, 10, 12, 10, 8);
        footer.addView(text("MAXBET  •  MATCH CENTER", 11, muted, true));
        TextView demo = text("DEMO ONLY — fictional sample odds. No real betting or payments.", 10, muted, false);
        demo.setGravity(Gravity.CENTER);
        demo.setPadding(0, dp(8), 0, 0);
        footer.addView(demo);
        content.addView(footer);

        LinearLayout nav = row();
        nav.setBackgroundColor(Color.rgb(11, 18, 15));
        pad(nav, 10, 10, 10, 10);
        String[] navItems = {"⌂\nHome", "▦\nMatches", "☆\nFavorites", "☻\nProfile"};
        for (int i = 0; i < navItems.length; i++) {
            TextView item = text(navItems[i], 11, i == 0 ? green : muted, i == 0);
            item.setGravity(Gravity.CENTER);
            item.setLineSpacing(dp(3), 1.0f);
            nav.addView(item, new LinearLayout.LayoutParams(0, dp(48), 1));
        }
        page.addView(nav);
        setContentView(page);
    }
}
