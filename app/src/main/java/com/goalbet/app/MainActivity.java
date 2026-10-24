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
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(7, 12, 11);
    private final int surface = Color.rgb(17, 26, 23);
    private final int surface2 = Color.rgb(24, 36, 31);
    private final int green = Color.rgb(0, 239, 129);
    private final int white = Color.rgb(245, 249, 247);
    private final int muted = Color.rgb(145, 163, 154);
    private TextView selectedSportChip;
    private View matchesAnchor;
    private boolean arabic = true;
    private final java.util.Set<String> favorites = new java.util.HashSet<>();
    private SharedPreferences prefs;
    private boolean compactMode = false;

    private String tr(String value) {
        if (!arabic) return value;
        switch (value) {
            case "YOUR SPORTS HUB": return "عالمك الرياضي";
            case "THE GAME\nSTARTS HERE.": return "هنا تبدأ\nالمنافسة.";
            case "Follow the action. Explore every match.": return "تابع الأحداث واكتشف كل مباراة.";
            case "Explore sports": return "استكشف الرياضات";
            case "ALL SPORTS  ↗": return "كل الرياضات  ↗";
            case "⚽  Football": return "⚽  كرة القدم";
            case "🏀  Basketball": return "🏀  كرة السلة";
            case "🎾  Tennis": return "🎾  التنس";
            case "🏒  Hockey": return "🏒  الهوكي";
            case "Top matches": return "أهم المباريات";
            case "VIEW ALL  ↗": return "عرض الكل  ↗";
            case "● DEMO DATA": return "● بيانات تجريبية";
            case "   •   TODAY'S PICKS": return "   •   اختيارات اليوم";
            case "MAXBET  •  MATCH CENTER": return "MAXBET  •  مركز المباريات";
            case "DEMO ONLY — fictional sample odds. No real betting or payments.": return "نسخة تجريبية — احتمالات افتراضية، دون مراهنات أو مدفوعات حقيقية.";
            case "Added to favorites": return "أضيفت إلى المفضلة";
            case "Removed from favorites": return "حُذفت من المفضلة";
            case "Home": return "الرئيسية";
            case "Matches": return "المباريات";
            case "Favorites": return "المفضلة";
            case "Live notifications require a real sports data source": return "تنبيهات الأهداف الحية تتطلب ربط مصدر بيانات رياضي حقيقي.";
            case "Profile": return "الملف الشخصي";
            case "Your saved matches will appear here. Tap a match to add it to Favorites in a future update.": return "ستظهر المباريات المحفوظة هنا. ستتم إضافة إمكانية حفظ المباريات في تحديث لاحق.";
            case "OK": return "حسنًا";
            case "Profile & Settings": return "الملف الشخصي والإعدادات";
            case "Language: English": return "اللغة: العربية / English";
            case "Compact layout": return "العرض المضغوط";
            case "Notifications: unavailable": return "التنبيهات: تحتاج بيانات مباشرة";
            case "Favorites saved: ": return "المباريات المحفوظة: ";
            case "League standings": return "ترتيب الدوريات";
            case "Match analysis": return "إحصائيات وتحليل المباريات";
            case "Close": return "إغلاق";
            case "League tables are unavailable until a verified sports data provider is connected. No standings are fabricated.": return "ترتيب الدوريات غير متاح حتى ربط مصدر رياضي موثوق. لن نعرض ترتيبًا مختلقًا.";
            case "Real statistics and analysis require verified match data. The current match cards are demo examples, not live fixtures.": return "الإحصائيات والتحليل يحتاجان بيانات مباريات موثوقة. البطاقات الحالية أمثلة تجريبية وليست مباريات مباشرة.";
            case "Theme: Dark neon": return "المظهر: أسود وأخضر نيون";
            case "App: MAXBET demo": return "التطبيق: نسخة MAXBET التجريبية";
            case "Dark neon theme is active": return "المظهر الأسود والأخضر مفعل";
            case "MAXBET sports demo": return "نسخة MAXBET الرياضية التجريبية";
            case "Football matches selected": return "تم اختيار كرة القدم";
            case " matches will appear when sports data is connected": return " ستظهر عند ربط التطبيق ببيانات رياضية";
            default: return value;
        }
    }

    private String navLabel(String icon, String label) { return icon + "\n" + tr(label); }


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
        t.setText(tr(value));
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
        t.setTag(active ? "selected" : "normal");
        t.setOnClickListener(v -> {
            if (selectedSportChip != null) {
                selectedSportChip.setTextColor(muted);
                selectedSportChip.setBackground(shape(surface2, 50));
                selectedSportChip.setTag("normal");
            }
            selectedSportChip = (TextView) v;
            selectedSportChip.setTextColor(bg);
            selectedSportChip.setBackground(shape(green, 50));
            selectedSportChip.setTag("selected");
            String sport = value.replaceAll("[^A-Za-z ]", "").trim();
            if (sport.equalsIgnoreCase("Football")) {
                if (matchesAnchor != null) matchesAnchor.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Football matches selected", Toast.LENGTH_SHORT).show();
            } else {
                if (matchesAnchor != null) matchesAnchor.setVisibility(View.GONE);
                Toast.makeText(this, sport + " matches will appear when sports data is connected", Toast.LENGTH_LONG).show();
            }
        });
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

    private String initials(String team) {
        String[] parts = team.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase(java.util.Locale.ROOT);
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase(java.util.Locale.ROOT);
    }

    private void addMatch(LinearLayout parent, String league, String time,
                          String home, String away, String homeOdds, String drawOdds, String awayOdds) {
        LinearLayout card = column();
        pad(card, 15, compactMode ? 9 : 14, 15, compactMode ? 10 : 15);
        card.setBackground(outline(surface, Color.rgb(34, 49, 42), 18));
        LinearLayout.LayoutParams cp = lp(-1, -2);
        cp.setMargins(0, 0, 0, dp(12));
        parent.addView(card, cp);
        String favoriteKey = home + " vs " + away;
        card.setOnClickListener(v -> {
            if (favorites.contains(favoriteKey)) favorites.remove(favoriteKey); else favorites.add(favoriteKey);
            prefs.edit().putStringSet("favorites", new java.util.HashSet<>(favorites)).apply();
            Toast.makeText(this, tr(favorites.contains(favoriteKey) ? "Added to favorites" : "Removed from favorites"), Toast.LENGTH_SHORT).show();
        });

        LinearLayout meta = row();
        TextView leagueText = text("●  " + league, 10, green, true);
        meta.addView(leagueText, new LinearLayout.LayoutParams(0, -2, 1));
        TextView demoTag = text(arabic ? "تجريبي" : "DEMO", 9, muted, true);
        demoTag.setGravity(Gravity.CENTER);
        pad(demoTag, 8, 5, 8, 5);
        demoTag.setBackground(outline(surface2, Color.rgb(47, 69, 58), 20));
        meta.addView(demoTag);
        TextView timeText = text("◷  " + time, 11, white, true);
        LinearLayout.LayoutParams timeLp = lp(-2, -2);
        timeLp.setMargins(dp(8), 0, 0, 0);
        meta.addView(timeText, timeLp);
        card.addView(meta);

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(36, 51, 44));
        LinearLayout.LayoutParams dpLine = lp(-1, 1);
        dpLine.setMargins(0, dp(12), 0, dp(12));
        card.addView(divider, dpLine);

        LinearLayout teams = row();
        LinearLayout names = column();
        LinearLayout homeRow = row();
        TextView homeBadge = text(initials(home), 11, green, true);
        homeBadge.setGravity(Gravity.CENTER);
        homeBadge.setBackground(outline(surface2, Color.rgb(0, 130, 75), 10));
        homeRow.addView(homeBadge, lp(34, 34));
        TextView homeName = text(home, 14, white, true);
        LinearLayout.LayoutParams hn = lp(-1, 36);
        hn.setMargins(dp(9), 0, 0, 0);
        homeRow.addView(homeName, hn);
        names.addView(homeRow);
        LinearLayout awayRow = row();
        TextView awayBadge = text(initials(away), 11, green, true);
        awayBadge.setGravity(Gravity.CENTER);
        awayBadge.setBackground(outline(surface2, Color.rgb(0, 130, 75), 10));
        awayRow.addView(awayBadge, lp(34, 34));
        TextView awayText = text(away, 14, white, true);
        LinearLayout.LayoutParams an = lp(-1, 36);
        an.setMargins(dp(9), dp(7), 0, 0);
        awayRow.addView(awayText, an);
        awayRow.setPadding(0, dp(7), 0, 0);
        names.addView(awayRow);
        teams.addView(names, new LinearLayout.LayoutParams(0, -2, 1));
        LinearLayout side = column();
        side.setGravity(Gravity.CENTER);
        TextView vs = text("VS", 12, muted, true);
        vs.setGravity(Gravity.CENTER);
        side.addView(vs);
        TextView star = text(favorites.contains(favoriteKey) ? "★" : "☆", 24,
                favorites.contains(favoriteKey) ? green : muted, true);
        star.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams starLp = lp(42, 38);
        starLp.setMargins(0, dp(8), 0, 0);
        side.addView(star, starLp);
        star.setOnClickListener(v -> {
            if (favorites.contains(favoriteKey)) favorites.remove(favoriteKey); else favorites.add(favoriteKey);
            prefs.edit().putStringSet("favorites", new java.util.HashSet<>(favorites)).apply();
            star.setText(favorites.contains(favoriteKey) ? "★" : "☆");
            star.setTextColor(favorites.contains(favoriteKey) ? green : muted);
            Toast.makeText(this, tr(favorites.contains(favoriteKey) ? "Added to favorites" : "Removed from favorites"), Toast.LENGTH_SHORT).show();
        });
        teams.addView(side, lp(46, -2));
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


    private void addGamesCatalog(LinearLayout root) {
        sectionTitle(root, "Game library", "1XGAMES STYLE");
        TextView note = text(arabic
            ? "مكتبة ألعاب قابلة للبحث — اللعب الفعلي يحتاج مزود ألعاب مرخّص."
            : "Searchable game catalogue — playable games require a licensed provider.", 11, muted, false);
        LinearLayout.LayoutParams noteLp = lp(-1, -2);
        noteLp.setMargins(0, 0, 0, dp(12));
        root.addView(note, noteLp);
        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(14);
        search.setTextColor(white);
        search.setHintTextColor(muted);
        search.setHint(arabic ? "ابحث عن لعبة..." : "Search games...");
        search.setPadding(dp(14), dp(10), dp(14), dp(10));
        search.setBackground(outline(surface2, Color.rgb(47, 69, 58), 14));
        root.addView(search, lp(-1, 48));
        String[] games = {
            "Aviator", "JetX", "Crash", "Crash Point", "Red Rocket Crash", "Zombie Crash", "Air Crash",
            "Apple Of Fortune", "Plinko", "Dice", "X-Keno", "Keno", "Bingo", "Roulette", "African Roulette",
            "Blackjack", "Baccarat", "Poker", "Texas Hold'em", "Indian Poker", "Solitaire", "Dominoes",
            "Andar Bahar", "Red Dog", "Russian Roulette", "Five Dice Poker", "Card War", "Flip Card",
            "Higher vs Lower", "Fair Higher vs Lower", "Royal Hi-Lo", "Hi-lo Triple Chance", "Heads or Tails",
            "Thimbles", "Mines", "Gems & Mines", "Gems Bonanza", "Gems Odyssey", "Scratch Card",
            "Avengers Scratch Card", "Fruit Cocktail", "Fruit Blast", "Wild Fruits", "Lucky Slot", "777",
            "Starburst", "Book of Ra", "Wild West Gold", "Sugar Cascade", "Pandora's Slots", "Diamond Slots",
            "Pharaoh's Kingdom", "Dragon's Gold", "Olympus Slot", "Reels of Wealth", "Reels of Gods",
            "Mayan Tomb", "Treasure Tomb", "Sea Treasures", "Vikings", "Midgard Zombies", "Mafia's Riches",
            "Killer Clubs", "Steampunk Kingdom", "Eastern Nights", "Royal Feast", "Fortune 18", "Lucky Knight",
            "Lucky Card", "Money Wheel", "Jackpot Wheel", "Spin and Win", "Jackpot Game", "Star Lotto",
            "Russian Lotto", "Football Cup", "Penalties", "Goal!", "Derby Racing", "Mundial", "Fishing",
            "Yahtzee", "Super Mario", "Grand Theft Auto", "Cyber2077", "Battle Royale", "Cases", "Garage",
            "Muffins", "Circus Poker", "PF Poker Light", "Triple Pocket Holdem Poker", "Guess Which Hand",
            "Truth or Lie", "OXO", "Western Slot", "Respin Slot", "Swirly Spin", "Fear Or Win",
            "Wild Heist Showdown", "Wild Heist Ultimate", "Heroes of the Storm 21", "DOTA 21",
            "Solitaire Battle Arena", "Games Mania"
        };
        LinearLayout results = column();
        LinearLayout.LayoutParams resultsLp = lp(-1, -2);
        resultsLp.setMargins(0, dp(12), 0, 0);
        root.addView(results, resultsLp);
        Runnable render = () -> {
            results.removeAllViews();
            String query = search.getText().toString().trim().toLowerCase(java.util.Locale.ROOT);
            int count = 0;
            LinearLayout currentRow = null;
            for (String game : games) {
                if (!game.toLowerCase(java.util.Locale.ROOT).contains(query)) continue;
                if (count % 2 == 0) {
                    currentRow = row();
                    LinearLayout.LayoutParams rowLp = lp(-1, -2);
                    rowLp.setMargins(0, 0, 0, dp(8));
                    results.addView(currentRow, rowLp);
                }
                final String gameName = game;
                LinearLayout tile = column();
                tile.setGravity(Gravity.CENTER_VERTICAL);
                pad(tile, 11, 12, 9, 12);
                tile.setBackground(outline(surface, Color.rgb(34, 49, 42), 13));
                tile.addView(text("✦", 17, green, true));
                TextView name = text(game, 12, white, true);
                name.setMaxLines(2);
                LinearLayout.LayoutParams nameLp = lp(-1, -2);
                nameLp.setMargins(0, dp(5), 0, 0);
                tile.addView(name, nameLp);
                String lower = game.toLowerCase(java.util.Locale.ROOT);
                boolean playable = lower.equals("crash") || lower.equals("dice") || lower.equals("mines")
                    || lower.contains("slot") || lower.contains("spin") || lower.contains("jackpot")
                    || lower.contains("lucky") || lower.contains("fortune") || lower.contains("respin")
                    || lower.contains("swirly") || lower.contains("wheel") || lower.contains("lotto")
                    || lower.contains("penalt") || lower.equals("goal!") || lower.contains("football cup");
                TextView status = text(playable
                    ? (arabic ? "قابلة للعب • تجريبي" : "PLAYABLE • DEMO")
                    : (arabic ? "قيد التطوير" : "IN DEVELOPMENT"), 9, playable ? green : muted, true);
                LinearLayout.LayoutParams statusLp = lp(-1, -2);
                statusLp.setMargins(0, dp(4), 0, 0);
                tile.addView(status, statusLp);
                tile.setOnClickListener(v -> {
                    String chosen = gameName.toLowerCase(java.util.Locale.ROOT);
                    String selected = null;
                    if (chosen.equals("crash") || chosen.equals("dice") || chosen.equals("mines")
                        || chosen.contains("slot") || chosen.contains("spin") || chosen.contains("jackpot")
                        || chosen.contains("lucky") || chosen.contains("fortune") || chosen.contains("respin")
                        || chosen.contains("swirly") || chosen.contains("wheel") || chosen.contains("lotto")
                        || chosen.contains("penalt") || chosen.equals("goal!") || chosen.contains("football cup")) selected = gameName;
                    if (selected == null) {
                        new AlertDialog.Builder(this)
                            .setTitle(gameName)
                            .setMessage(arabic
                                ? "هذه اللعبة غير مطوّرة بعد. لن نفتح لعبة الطائرة بدلًا منها. الألعاب المتاحة حاليًا للتجربة تشمل Crash وDice وMines وSlots وWheel وPenalties."
                                : "This game has not been implemented yet. We will not open the plane game as a substitute. Currently playable demos include Crash, Dice, Mines, Slots, Wheel, and Penalties.")
                            .setPositiveButton(arabic ? "حسنًا" : "OK", null)
                            .show();
                        return;
                    }
                    android.content.Intent intent = new android.content.Intent(this, ArcadeGameActivity.class);
                    intent.putExtra("game", selected);
                    startActivity(intent);
                });
                LinearLayout.LayoutParams tileLp = new LinearLayout.LayoutParams(0, dp(108), 1);
                if (count % 2 == 0) tileLp.setMargins(0, 0, dp(7), 0);
                else tileLp.setMargins(dp(7), 0, 0, 0);
                currentRow.addView(tile, tileLp);
                count++;
            }
            if (count == 0) {
                TextView empty = text(arabic ? "لا توجد ألعاب بهذا الاسم" : "No games found", 13, muted, false);
                pad(empty, 8, 16, 8, 16);
                results.addView(empty);
            }
            if (count % 2 != 0 && currentRow != null) {
                currentRow.addView(new View(this), new LinearLayout.LayoutParams(0, dp(108), 1));
            }
        };
        render.run();
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { render.run(); }
            @Override public void afterTextChanged(Editable e) {}
        });
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
        prefs = getSharedPreferences("maxbet_settings", MODE_PRIVATE);
        arabic = prefs.getBoolean("arabic", true);
        compactMode = prefs.getBoolean("compact", false);
        favorites.addAll(prefs.getStringSet("favorites", new java.util.HashSet<>()));
        prefs = getSharedPreferences("maxbet_settings", MODE_PRIVATE);
        arabic = prefs.getBoolean("arabic", true);
        compactMode = prefs.getBoolean("compact", false);
        favorites.addAll(prefs.getStringSet("favorites", new java.util.HashSet<>()));
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(0);

        LinearLayout page = column();
        page.setLayoutDirection(arabic ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        page.setBackgroundColor(bg);

        LinearLayout top = row();
        pad(top, 20, 13, 20, 13);
        LinearLayout accountActions = row();
        TextView login = text(arabic ? "🔐 تسجيل الدخول" : "🔐 Login", 12, white, true);
        login.setGravity(Gravity.CENTER);
        pad(login, 10, 10, 10, 10);
        login.setBackground(outline(surface2, green, 12));
        LinearLayout.LayoutParams loginLp = new LinearLayout.LayoutParams(0, dp(42), 1);
        loginLp.setMargins(0, 0, dp(7), 0);
        accountActions.addView(login, loginLp);
        TextView create = text(arabic ? "👤 إنشاء حساب" : "👤 Sign up", 12, bg, true);
        create.setGravity(Gravity.CENTER);
        pad(create, 10, 10, 10, 10);
        create.setBackground(shape(green, 12));
        accountActions.addView(create, new LinearLayout.LayoutParams(0, dp(42), 1));
        login.setOnClickListener(v -> Toast.makeText(this, arabic ? "تسجيل الدخول يحتاج ربط خدمة الحسابات" : "Account service is not connected yet", Toast.LENGTH_SHORT).show());
        create.setOnClickListener(v -> Toast.makeText(this, arabic ? "إنشاء الحساب يحتاج ربط خدمة الحسابات" : "Account service is not connected yet", Toast.LENGTH_SHORT).show());
        top.addView(accountActions, new LinearLayout.LayoutParams(-1, dp(42)));
        page.addView(top);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        LinearLayout content = column();
        content.setLayoutDirection(arabic ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
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
        sportsScroll.setLayoutDirection(arabic ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        LinearLayout sports = row();
        sports.addView(chip("⚽  Football", true));
        sports.addView(chip("🏀  Basketball", false));
        sports.addView(chip("🎾  Tennis", false));
        sports.addView(chip("🏒  Hockey", false));
        sportsScroll.addView(sports);
        content.addView(sportsScroll);

        addGamesCatalog(content);

        sectionTitle(content, "Top matches", "VIEW ALL  ↗");
        matchesAnchor = content.getChildAt(content.getChildCount() - 1);
        LinearLayout live = row();
        TextView liveDot = text("● DEMO DATA", 10, green, true);
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
        String[] navItems = {navLabel("⌂", "Home"), navLabel("▦", "Matches"), navLabel("☆", "Favorites"), navLabel("☻", "Profile")};
        for (int i = 0; i < navItems.length; i++) {
            final int tab = i;
            TextView item = text(navItems[i], 11, i == 0 ? green : muted, i == 0);
            item.setGravity(Gravity.CENTER);
            item.setLineSpacing(dp(3), 1.0f);
            item.setOnClickListener(v -> {
                for (int j = 0; j < nav.getChildCount(); j++) {
                    TextView other = (TextView) nav.getChildAt(j);
                    other.setTextColor(j == tab ? green : muted);
                    other.setTypeface(Typeface.create("sans-serif", j == tab ? Typeface.BOLD : Typeface.NORMAL));
                }
                if (tab == 0) {
                    scroll.smoothScrollTo(0, 0);
                } else if (tab == 1) {
                    if (matchesAnchor != null) {
                        int y = matchesAnchor.getTop();
                        scroll.smoothScrollTo(0, Math.max(0, y - dp(12)));
                    }
                } else if (tab == 2) {
                    new AlertDialog.Builder(this)
                        .setTitle(tr("Favorites"))
                        .setMessage(favorites.isEmpty() ? tr("Your saved matches will appear here. Tap a match to add it to Favorites in a future update.") : String.join("\\n", favorites))
                        .setPositiveButton(tr("OK"), null).show();
                } else {
                    String[] settings = {tr("Language: English"), tr("Compact layout"), tr("Notifications: unavailable"), tr("Favorites saved: ") + favorites.size()};
                    new AlertDialog.Builder(this).setTitle(tr("Profile & Settings"))
                        .setItems(settings, (dialog, which) -> {
                            if (which == 0) { arabic = !arabic; recreate(); }
                            else if (which == 1) Toast.makeText(this, tr("Dark neon theme is active"), Toast.LENGTH_SHORT).show();
                            else Toast.makeText(this, tr("MAXBET sports demo"), Toast.LENGTH_SHORT).show();
                        }).setNegativeButton("Close", null).show();
                }
            });
            nav.addView(item, new LinearLayout.LayoutParams(0, dp(48), 1));
        }
        page.addView(nav);
        setContentView(page);
    }
}
