package com.xunlei.library.quickscroll;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;

@TargetApi(16)
public class QuickScroll extends View {
    public static final int BLUE_LIGHT = Color.parseColor("#FF33B5E5");
    public static final int BLUE_LIGHT_SEMITRANSPARENT = Color.parseColor("#8033B5E5");
    public static final int GREY_DARK = Color.parseColor("#e0585858");
    public static final int GREY_LIGHT = Color.parseColor("#f0888888");
    public static final int GREY_SCROLLBAR = Color.parseColor("#64404040");
    protected static final int ID_PIN = 512;
    protected static final int ID_PIN_TEXT = 513;
    protected static final int SCROLLBAR_MARGIN = 10;
    public static final int STYLE_HOLO = 1;
    public static final int STYLE_NONE = 0;
    protected static final int TEXT_PADDING = 4;
    public static final int TYPE_INDICATOR = 1;
    public static final int TYPE_INDICATOR_WITH_HANDLE = 3;
    public static final int TYPE_POPUP = 0;
    public static final int TYPE_POPUP_WITH_HANDLE = 2;
    protected AlphaAnimation fadeInAnimation;
    protected AlphaAnimation fadeOutAnimation;
    protected int groupPosition;
    protected View handleBar;
    protected boolean isInitialized = false;
    protected boolean isScrolling;
    protected int itemCount;
    protected ListView listView;
    protected RelativeLayout scrollIndicator;
    protected TextView scrollIndicatorTextView;
    protected Scrollable scrollable;
    protected int type;

    public QuickScroll(Context context) {
        super(context);
    }

    public QuickScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int type, ListView list, Scrollable scrollable, int style) {
        if (!this.isInitialized) {
            this.type = type;
            this.listView = list;
            this.scrollable = scrollable;
            this.groupPosition = -1;
            this.fadeInAnimation = new AlphaAnimation(0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.fadeInAnimation.setFillAfter(true);
            this.fadeOutAnimation = new AlphaAnimation(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, 0.0f);
            this.fadeOutAnimation.setFillAfter(true);
            this.fadeOutAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    QuickScroll.this.isScrolling = false;
                }
            });
            this.isScrolling = false;
            this.listView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (QuickScroll.this.isScrolling && (event.getAction() == 2 || event.getAction() == 0)) {
                        return true;
                    }
                    return false;
                }
            });
            LayoutParams containerParams = new LayoutParams(-1, -1);
            RelativeLayout container = new RelativeLayout(getContext());
            container.setBackgroundColor(0);
            containerParams.addRule(6, getId());
            containerParams.addRule(8, getId());
            container.setLayoutParams(containerParams);
            if (this.type == 0 || this.type == 2) {
                this.scrollIndicatorTextView = new TextView(getContext());
                this.scrollIndicatorTextView.setTextColor(-1);
                this.scrollIndicatorTextView.setVisibility(View.INVISIBLE);
                this.scrollIndicatorTextView.setGravity(17);
                LayoutParams popupParams = new LayoutParams(-2, -2);
                popupParams.addRule(13);
                this.scrollIndicatorTextView.setLayoutParams(popupParams);
                setPopupColor(GREY_LIGHT, GREY_DARK, 1, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                setTextPadding(4, 4, 4, 4);
                container.addView(this.scrollIndicatorTextView);
            } else {
                this.scrollIndicator = createPin();
                this.scrollIndicatorTextView = (TextView) this.scrollIndicator.findViewById(513);
                this.scrollIndicator.findViewById(512).getLayoutParams().width = 25;
                container.addView(this.scrollIndicator);
            }
            float density = getResources().getDisplayMetrics().density;
            getLayoutParams().width = (int) (30.0f * density);
            this.scrollIndicatorTextView.setTextSize(1, 32.0f);
            if (style != 0) {
                RelativeLayout layout = new RelativeLayout(getContext());
                LayoutParams params = new LayoutParams(-1, -1);
                params.addRule(5, getId());
                params.addRule(6, getId());
                params.addRule(7, getId());
                params.addRule(8, getId());
                layout.setLayoutParams(params);
                View view = new View(getContext());
                view.setBackgroundColor(GREY_SCROLLBAR);
                LayoutParams scrollBarParams = new LayoutParams(1, -1);
                scrollBarParams.addRule(14);
                scrollBarParams.topMargin = 10;
                scrollBarParams.bottomMargin = 10;
                view.setLayoutParams(scrollBarParams);
                layout.addView(view);
                ((ViewGroup) ViewGroup.class.cast(this.listView.getParent())).addView(layout);
                if (this.type == 3 || this.type == 2) {
                    this.handleBar = new View(getContext());
                    setHandlebarColor(BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT_SEMITRANSPARENT);
                    this.handleBar.setLayoutParams(new LayoutParams((int) (12.0f * density), (int) (36.0f * density)));
                    ((LayoutParams) this.handleBar.getLayoutParams()).addRule(14);
                    layout.addView(this.handleBar);
                    this.listView.setOnScrollListener(new OnScrollListener() {
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                        }

                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (!QuickScroll.this.isScrolling && totalItemCount - visibleItemCount > 0) {
                                QuickScroll.this.moveHandlebar((float) ((QuickScroll.this.getHeight() * firstVisibleItem) / (totalItemCount - visibleItemCount)));
                            }
                        }
                    });
                }
            }
            this.isInitialized = true;
            ((ViewGroup) ViewGroup.class.cast(this.listView.getParent())).addView(container);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        Adapter adapter = this.listView.getAdapter();
        if (adapter == null) {
            return false;
        }
        if (adapter instanceof HeaderViewListAdapter) {
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        this.itemCount = adapter.getCount();
        if (this.itemCount == 0) {
            return false;
        }
        if (event.getActionMasked() == 3) {
            if (this.type == 0 || this.type == 1) {
                this.scrollIndicatorTextView.startAnimation(this.fadeOutAnimation);
            } else {
                if (this.type == 3 || this.type == 2) {
                    this.handleBar.setSelected(false);
                }
                this.scrollIndicator.startAnimation(this.fadeOutAnimation);
            }
        }
        switch (event.getActionMasked()) {
            case 0:
                if (this.type == 1 || this.type == 3) {
                    this.scrollIndicator.startAnimation(this.fadeInAnimation);
                    this.scrollIndicator.setPadding(0, 0, getWidth(), 0);
                } else {
                    this.scrollIndicatorTextView.startAnimation(this.fadeInAnimation);
                }
                scroll(event.getY());
                this.isScrolling = true;
                return true;
            case 1:
                if (this.type == 3 || this.type == 2) {
                    this.handleBar.setSelected(false);
                }
                if (this.type == 1 || this.type == 3) {
                    this.scrollIndicator.startAnimation(this.fadeOutAnimation);
                } else {
                    this.scrollIndicatorTextView.startAnimation(this.fadeOutAnimation);
                }
                return true;
            case 2:
                scroll(event.getY());
                return true;
            default:
                return false;
        }
    }

    @SuppressLint({"NewApi"})
    protected void scroll(float height) {
        if (this.type == 1 || this.type == 3) {
            float move = height - ((float) (this.scrollIndicator.getHeight() / 2));
            if (move < 0.0f) {
                move = 0.0f;
            } else if (move > ((float) (getHeight() - this.scrollIndicator.getHeight()))) {
                move = (float) (getHeight() - this.scrollIndicator.getHeight());
            }
            ViewHelper.setTranslationY(this.scrollIndicator, move);
        }
        if (this.type == 3 || this.type == 2) {
            this.handleBar.setSelected(true);
            moveHandlebar(height - ((float) (this.handleBar.getHeight() / 2)));
        }
        int position = (int) ((height / ((float) getHeight())) * ((float) this.itemCount));
        if (this.listView instanceof ExpandableListView) {
            int groupPosition = ExpandableListView.getPackedPositionGroup(((ExpandableListView) this.listView).getExpandableListPosition(position));
            if (groupPosition != -1) {
                this.groupPosition = groupPosition;
            }
        }
        if (position < 0) {
            position = 0;
        } else if (position >= this.itemCount) {
            position = this.itemCount - 1;
        }
        this.scrollIndicatorTextView.setText(this.scrollable.getIndicatorForPosition(position, this.groupPosition));
        this.listView.setSelection(this.scrollable.getScrollPosition(position, this.groupPosition));
    }

    @SuppressLint({"NewApi"})
    protected void moveHandlebar(float where) {
        float move = where;
        if (move < 10.0f) {
            move = 10.0f;
        } else if (move > ((float) ((getHeight() - this.handleBar.getHeight()) - 10))) {
            move = (float) ((getHeight() - this.handleBar.getHeight()) - 10);
        }
        ViewHelper.setTranslationY(this.handleBar, move);
    }

    public void setFadeDuration(long millis) {
        this.fadeInAnimation.setDuration(millis);
        this.fadeOutAnimation.setDuration(millis);
    }

    public void setIndicatorColor(int background, int tip, int text) {
        if (this.type == 1 || this.type == 3) {
            ((Pin) this.scrollIndicator.findViewById(512)).setColor(tip);
            this.scrollIndicatorTextView.setTextColor(text);
            this.scrollIndicatorTextView.setBackgroundColor(background);
        }
    }

    public void setPopupColor(int backgroundcolor, int bordercolor, int borderwidthDPI, int textcolor, float cornerradiusDPI) {
        GradientDrawable popupbackground = new GradientDrawable();
        popupbackground.setCornerRadius(getResources().getDisplayMetrics().density * cornerradiusDPI);
        popupbackground.setStroke((int) (((float) borderwidthDPI) * getResources().getDisplayMetrics().density), bordercolor);
        popupbackground.setColor(backgroundcolor);
        if (VERSION.SDK_INT < 16) {
            this.scrollIndicatorTextView.setBackgroundDrawable(popupbackground);
        } else {
            this.scrollIndicatorTextView.setBackground(popupbackground);
        }
        this.scrollIndicatorTextView.setTextColor(textcolor);
    }

    public void setSize(int widthDP, int heightDP) {
        float density = getResources().getDisplayMetrics().density;
        this.scrollIndicatorTextView.getLayoutParams().width = (int) (((float) widthDP) * density);
        this.scrollIndicatorTextView.getLayoutParams().height = (int) (((float) heightDP) * density);
    }

    public void setTextPadding(int paddingLeftDP, int paddingTopDP, int paddingBottomDP, int paddingRightDP) {
        float density = getResources().getDisplayMetrics().density;
        this.scrollIndicatorTextView.setPadding((int) (((float) paddingLeftDP) * density), (int) (((float) paddingTopDP) * density), (int) (((float) paddingRightDP) * density), (int) (((float) paddingBottomDP) * density));
    }

    public void setFixedSize(int sizeEMS) {
        this.scrollIndicatorTextView.setEms(sizeEMS);
    }

    public void setTextSize(int unit, float size) {
        this.scrollIndicatorTextView.setTextSize(unit, size);
    }

    @TargetApi(16)
    public void setHandlebarColor(int inactive, int activebase, int activestroke) {
        if (this.type == 3 || this.type == 2) {
            float density = getResources().getDisplayMetrics().density;
            GradientDrawable bg_inactive = new GradientDrawable();
            bg_inactive.setCornerRadius(density);
            bg_inactive.setColor(inactive);
            bg_inactive.setStroke((int) (5.0f * density), 0);
            GradientDrawable bg_active = new GradientDrawable();
            bg_active.setCornerRadius(density);
            bg_active.setColor(activebase);
            bg_active.setStroke((int) (5.0f * density), activestroke);
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{16842913}, bg_active);
            states.addState(new int[]{16842910}, bg_inactive);
            if (VERSION.SDK_INT < 16) {
                this.handleBar.setBackgroundDrawable(states);
            } else {
                this.handleBar.setBackground(states);
            }
        }
    }

    protected RelativeLayout createPin() {
        RelativeLayout pinLayout = new RelativeLayout(getContext());
        pinLayout.setVisibility(View.INVISIBLE);
        Pin pin = new Pin(getContext());
        pin.setId(512);
        LayoutParams pinParams = new LayoutParams(-2, -2);
        pinParams.addRule(11);
        pinParams.addRule(8, 513);
        pinParams.addRule(6, 513);
        pin.setLayoutParams(pinParams);
        pinLayout.addView(pin);
        TextView indicatorTextView = new TextView(getContext());
        indicatorTextView.setId(513);
        LayoutParams indicatorParams = new LayoutParams(-2, -2);
        indicatorParams.addRule(0, 512);
        indicatorTextView.setLayoutParams(indicatorParams);
        indicatorTextView.setTextColor(-1);
        indicatorTextView.setGravity(17);
        indicatorTextView.setBackgroundColor(GREY_LIGHT);
        pinLayout.addView(indicatorTextView);
        return pinLayout;
    }
}
