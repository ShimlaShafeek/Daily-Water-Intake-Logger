# 🎨 History UI - Color & Style Reference Guide

## 📋 Quick Reference

### Color Codes:
```
PRIMARY BLUE:       #1D4EFF  ◼️  (Primary actions & values)
SECONDARY BLUE:     #2563EB  ◼️  (Header gradient)
DARK BLUE:          #0F4BB8  ◼️  (Header gradient)
SUCCESS GREEN:      #10B981  ◼️  (Positive metrics)
INFO CYAN:          #06B6D4  ◼️  (Secondary stats)
DARK TEXT:          #0B1B3F  ◼️  (Primary text)
MUTED TEXT:         #7B8BB8  ◼️  (Secondary text)
LIGHT BLUE:         #B0C9FF  ◼️  (Subtitle)
WHITE:              #FFFFFF  ◼️  (Card backgrounds)
LIGHT BG:           #F8FAFF  ◼️  (Page background)
BORDER LIGHT:       #E0ECFF  ◼️  (Card borders)
BORDER SUBTLE:      #E5EEFF  ◼️  (Chart borders)
```

---

## 📐 Component Dimensions

### Header Section:
- **Height**: 280dp
- **Back Button**: 44x44dp (padding: 10dp)
- **Title Size**: 28sp, Bold
- **Subtitle Size**: 13sp, Regular
- **Tab Bar Height**: 56dp
- **Tab Margin**: 16dp (start/end)

### Card Components:
- **Stat Card Radius**: 20dp
- **Chart Card Radius**: 24dp
- **Card Padding**: 16dp
- **Card Border**: 1dp (light blue)
- **Row Spacing**: 8dp between items

### Typography:
- **H1 (Title)**: 28sp, Bold, White
- **H2 (Subtitle)**: 13sp, Regular, Light Blue
- **H3 (Card Title)**: 16sp, Bold, Dark
- **H4 (Value)**: 20sp, Bold, Blue
- **Body (Label)**: 12sp, Regular, Muted
- **Chart Title**: 14sp, Bold, Dark

### Spacing:
- **Screen Padding**: 16dp
- **Section Gap**: 16dp
- **Component Gap**: 8dp
- **Internal Padding**: 14-16dp
- **Text Margins**: 4-6dp

---

## 🎯 Emoji Reference

| Icon | Usage | Where |
|------|-------|-------|
| 💧 | Water intake total | Stats cards |
| 🎯 | Goal/Target tracking | Day progress |
| 📈 | Average/Trend | Week statistics |
| 🏆 | Achievement/Best | Week best day |
| 📅 | Calendar/Date | Range display |
| 📊 | Chart/Analytics | Chart titles |
| ◄ / ► | Navigation | Date controls |

---

## 🎨 Drawable Resources

### bg_history_gradient.xml
**Purpose**: Header background gradient
```xml
Start Color: #1D4EFF (Vibrant Blue)
Center Color: #2563EB (Medium Blue)
End Color: #0F4BB8 (Dark Blue)
Angle: 135° (Dynamic)
```

### bg_stat_card.xml
**Purpose**: Statistics card background
```xml
Start Color: #FFFFFF (White)
End Color: #F0F9FF (Very Light Blue)
Radius: 20dp
Border Color: #E0ECFF (1dp)
```

### bg_chart_card.xml
**Purpose**: Chart container background
```xml
Start Color: #FFFFFF (White)
End Color: #F8FAFF (Clean Light)
Radius: 24dp
Border Color: #E5EEFF (1dp)
```

---

## 📱 Layout Structure

### Activity: activity_history.xml
```
ConstraintLayout (Match Parent)
├─ View: header_background (280dp, Blue Gradient)
├─ Guideline: 32% height
├─ ImageView: ivBackButton (44x44)
├─ TextView: tvHistoryTitle (Water History, 28sp)
├─ TextView: tvHistorySubtitle (Track hydration, 13sp)
├─ TabLayout: tabLayout
│   ├─ TabItem: 📅 DAY
│   └─ TabItem: 📊 WEEK
├─ ViewPager2: viewPager
└─ BottomNavigationView: bottom_navigation
```

### Fragment: fragment_history_day.xml
```
ConstraintLayout
├─ LinearLayout: dateNavigation (Card)
│   ├─ ImageView: ivPreviousDay
│   ├─ TextView: tvSelectedDate
│   └─ ImageView: ivNextDay
├─ LinearLayout: statsRow
│   ├─ CardView: cardTotalWater
│   │   └─ LinearLayout: 💧 Total
│   └─ CardView: cardGoalProgress
│       └─ LinearLayout: 🎯 Progress
├─ CardView: chartCard
│   └─ LinearLayout
│       ├─ TextView: 📊 Daily Intake
│       └─ BarChart: barChartDay
```

### Fragment: fragment_history_week.xml
```
ConstraintLayout
├─ CardView: cardDateRange
│   └─ LinearLayout: 📅 Week Range
├─ LinearLayout: statsRow
│   ├─ CardView: cardTotalWaterWeek (💧 Total)
│   └─ CardView: cardAverageDaily (📈 Average)
├─ CardView: cardBestDay
│   └─ LinearLayout: 🏆 Best Day Info
├─ CardView: chartCard
│   └─ LinearLayout
│       ├─ TextView: 📊 Weekly Trend
│       └─ BarChart: barChartWeek
```

---

## 🎯 Style Classes & Patterns

### Card Pattern:
```xml
<androidx.cardview.widget.CardView
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    app:cardCornerRadius="20dp"
    app:cardElevation="0dp"
    app:cardBackgroundColor="@android:color/transparent">
    <LinearLayout
        android:background="@drawable/bg_stat_card"
        android:padding="16dp">
        <!-- Content -->
    </LinearLayout>
</androidx.cardview.widget.CardView>
```

### Text Pattern:
```xml
<!-- Label -->
<TextView
    android:textSize="12sp"
    android:textColor="#7B8BB8"/>

<!-- Value -->
<TextView
    android:textSize="20sp"
    android:textStyle="bold"
    android:textColor="#1D4EFF"/>
```

### Navigation Button Pattern:
```xml
<ImageView
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:padding="8dp"
    android:tint="#1D4EFF"
    android:background="?android:attr/selectableItemBackgroundBorderless"/>
```

---

## 📊 Data Display Values (Examples)

### Day View:
```
Date: Today, Mar 3, 2026
Total Water: 1850 ml (Color: #1D4EFF)
Progress: 92% (Color: #10B981)
Goal: 2000 ml
```

### Week View:
```
Date Range: Mar 1 - Mar 7, 2026
Total Water: 12,950 ml (Color: #1D4EFF)
Average Daily: 1,850 ml (Color: #06B6D4)
Best Day: Friday - 2,350 ml (117%) (Color: #10B981)
```

---

## ✨ Visual Features

### Gradients:
1. **Header**: 135° angle, 3-color blue gradient
2. **Cards**: Subtle white-to-blue gradient
3. **Backgrounds**: Clean, light blue tint

### Borders:
1. **Stat Cards**: Light blue 1dp border
2. **Chart Cards**: Subtle blue 1dp border
3. **Creates visual separation without harshness

### Shadows:
- **Disabled**: Flat design approach
- **Alternative**: Uses borders for definition

### Rounded Corners:
- **Stat Cards**: 20dp radius
- **Chart Cards**: 24dp radius
- **Creates modern, friendly appearance

---

## 🎓 Design System Summary

### Color Usage Rules:
1. **Primary Blue (#1D4EFF)**: All primary metrics and actions
2. **Green (#10B981)**: Positive outcomes (success, achievements)
3. **Cyan (#06B6D4)**: Secondary information and trends
4. **Dark (#0B1B3F)**: Primary text content
5. **Muted (#7B8BB8)**: Secondary labels and hints

### Size Hierarchy:
1. **28sp**: Page titles (largest, most important)
2. **20sp**: Key metrics and values
3. **16sp**: Card titles and section headers
4. **14sp**: Chart labels
5. **13sp**: Subtitles
6. **12sp**: Secondary labels and hints (smallest)

### Spacing Consistency:
- All outer margins: 16dp
- All card padding: 16dp
- All section gaps: 16dp
- All component gaps: 8dp
- Creates visual rhythm and balance

---

## 🚀 Animation Ready

### Recommended Animations:
1. **Slide Enter**: Fragments slide in from sides
2. **Fade In**: Stats cards fade in on load
3. **Swipe Navigation**: Smooth tab/date navigation
4. **Tap Feedback**: Ripple on button clicks
5. **Chart Animation**: Bars animate from bottom

### Transition Timing:
- Short transitions: 200-300ms
- Medium transitions: 300-500ms
- Long transitions: 500-800ms

---

## ♿ Accessibility Features

### Color Contrast:
- ✅ Dark text on light: WCAG AAA
- ✅ Light text on dark: WCAG AA
- ✅ Blue elements: High contrast

### Touch Targets:
- ✅ Min 40x40dp (navigation buttons)
- ✅ Min 48x48dp recommended (buttons)
- ✅ Proper spacing between targets

### Text:
- ✅ 12sp minimum size
- ✅ Bold for hierarchy
- ✅ Clear, readable fonts

---

## 📱 Responsive Behavior

### Phone Layout (< 600dp):
- Single column layout
- Full-width cards
- Stacked statistics
- Readable text sizes

### Tablet Layout (≥ 600dp):
- Can use landscape mode
- Side-by-side cards
- More chart detail
- Larger touch targets

---

## 🔄 Component Reusability

### Stat Card Template:
Can be reused for:
- Daily statistics
- Weekly statistics
- Goals and targets
- Achievements

### Chart Card Template:
Can be reused for:
- Daily charts
- Weekly charts
- Monthly charts
- Trend analysis

---

## ✅ Quality Checklist

### Design:
- ✅ Consistent color palette
- ✅ Clear typography hierarchy
- ✅ Proper spacing throughout
- ✅ Modern rounded corners
- ✅ Professional appearance

### Functionality:
- ✅ Responsive layouts
- ✅ Touch-friendly sizes
- ✅ Proper constraints
- ✅ No overlapping elements
- ✅ Clear navigation

### Code:
- ✅ Clean XML structure
- ✅ Proper ID naming
- ✅ Resource references
- ✅ No hardcoded values (mostly)
- ✅ Maintainable design

---

Generated: March 1, 2026  
Version: 1.0  
Status: ✅ Complete Reference Guide

