package com.xunlei.library.segment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.android.volley.DefaultRetryPolicy;
import com.niuza.android.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SegmentedControlView extends RadioGroup {
    private int defaultSelection = -1;
    private boolean equalWidth = false;
    private String identifier = "";
    private LinkedHashMap<String, String> itemMap = new LinkedHashMap();
    private int itemMinWidth = 0;
    private Context mCtx;
    private OnSelectionChangedListener mListener;
    private int mSdk;
    private ArrayList<RadioButton> options;
    private int selectedColor = Color.parseColor("#0099CC");
    private int selectedTextColor = -1;
    private OnCheckedChangeListener selectionChangedlistener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (SegmentedControlView.this.mListener != null) {
                SegmentedControlView.this.mListener.newSelection(SegmentedControlView.this.identifier, (String) SegmentedControlView.this.itemMap.get(((RadioButton) group.findViewById(checkedId)).getText().toString()));
            }
        }
    };
    private boolean stretch = false;
    private ColorStateList textColorStateList;
    private int unselectedColor = 0;
    private int unselectedTextColor = Color.parseColor("#0099CC");

    public interface OnSelectionChangedListener {
        void newSelection(String str, String str2);
    }

    public void setItemMinWidth(int itemMinWidth) {
        this.itemMinWidth = itemMinWidth;
    }

    public SegmentedControlView(Context context) {
        super(context, null);
        init(context);
        update();
    }

    public SegmentedControlView(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);
        int i = 0;

        init(context);
        this.itemMinWidth = (int) TypedValue.applyDimension(1, 2.0f, getResources().getDisplayMetrics());
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultipleSelectionButton, 0, 0);
        try {
            this.selectedColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_selectedColor, this.selectedColor);
            this.selectedTextColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_selectedTextColor, this.selectedTextColor);
            this.unselectedColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_unselectedColor, this.unselectedColor);
            this.unselectedTextColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_unselectedTextColor, this.selectedColor);
            int[][] r7 = new int[2][];
            r7[0] = new int[]{-16842912};
            r7[1] = new int[]{16842912};
            this.textColorStateList = new ColorStateList(r7, new int[]{this.unselectedTextColor, this.selectedTextColor});
            this.defaultSelection = attributes.getInt(R.styleable.MultipleSelectionButton_ascv_defaultSelection, this.defaultSelection);
            this.equalWidth = attributes.getBoolean(R.styleable.MultipleSelectionButton_ascv_equalWidth, this.equalWidth);
            this.stretch = attributes.getBoolean(R.styleable.MultipleSelectionButton_ascv_stretch, this.stretch);
            this.identifier = attributes.getString(R.styleable.MultipleSelectionButton_ascv_identifier);
            CharSequence[] itemArray = attributes.getTextArray(R.styleable.MultipleSelectionButton_ascv_items);
            CharSequence[] valueArray = attributes.getTextArray(R.styleable.MultipleSelectionButton_ascv_values);
            if (isInEditMode()) {
                itemArray = new CharSequence[]{"YES", "NO", "MAYBE", "DON'T KNOW"};
            }
            if (itemArray == null || valueArray == null || itemArray.length == valueArray.length) {
                if (itemArray != null) {
                    if (valueArray != null) {
                        for (int i2 = 0; i2 < itemArray.length; i2++) {
                            this.itemMap.put(itemArray[i2].toString(), valueArray[i2].toString());
                        }
                    } else {
                        int length = itemArray.length;
                        while (i < length) {
                            CharSequence item = itemArray[i];
                            this.itemMap.put(item.toString(), item.toString());
                            i++;
                        }
                    }
                }
                attributes.recycle();
                update();
                return;
            }
            throw new Exception("Item labels and value arrays must be the same size");
        } catch (Throwable th) {
            attributes.recycle();
        }
    }

    private void init(Context context) {
        this.mCtx = context;
        this.mSdk = VERSION.SDK_INT;
        setPadding(10, 10, 10, 10);
    }

    @TargetApi(16)
    private void update() {
        removeAllViews();
        int oneDP = (int) TypedValue.applyDimension(1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, getResources().getDisplayMetrics());
        setOrientation(0);
        float textWidth = 0.0f;
        this.options = new ArrayList();
        int i = 0;
        for (Entry<String, String> item : this.itemMap.entrySet()) {
            RadioButton radioButton = new RadioButton(this.mCtx);
            radioButton.setTextColor(this.textColorStateList);
            LayoutParams params = new LayoutParams(-2, -1);
            if (this.stretch) {
                params.weight = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            }
            if (i > 0) {
                params.setMargins(-oneDP, 0, 0, 0);
            }
            radioButton.setLayoutParams(params);
            radioButton.setButtonDrawable(new StateListDrawable());
            if (i == 0) {
                GradientDrawable leftUnselected = (GradientDrawable) this.mCtx.getResources().getDrawable(R.drawable.segment_left_option).mutate();
                leftUnselected.setStroke(oneDP, this.selectedColor);
                leftUnselected.setColor(this.unselectedColor);
                GradientDrawable leftSelected = (GradientDrawable) this.mCtx.getResources().getDrawable(R.drawable.segment_left_option_selected).mutate();
                leftSelected.setColor(this.selectedColor);
                leftSelected.setStroke(oneDP, this.selectedColor);
                StateListDrawable leftStateListDrawable = new StateListDrawable();
                leftStateListDrawable.addState(new int[]{-16842912}, leftUnselected);
                leftStateListDrawable.addState(new int[]{16842912}, leftSelected);
                if (this.mSdk < 16) {
                    radioButton.setBackgroundDrawable(leftStateListDrawable);
                } else {
                    radioButton.setBackground(leftStateListDrawable);
                }
            } else if (i == this.itemMap.size() - 1) {
                GradientDrawable rightUnselected = (GradientDrawable) this.mCtx.getResources().getDrawable(R.drawable.segment_right_option).mutate();
                rightUnselected.setStroke(oneDP, this.selectedColor);
                rightUnselected.setColor(this.unselectedColor);
                GradientDrawable rightSelected = (GradientDrawable) this.mCtx.getResources().getDrawable(R.drawable.segment_right_option_selected).mutate();
                rightSelected.setColor(this.selectedColor);
                rightSelected.setStroke(oneDP, this.selectedColor);
                StateListDrawable rightStateListDrawable = new StateListDrawable();
                rightStateListDrawable.addState(new int[]{-16842912}, rightUnselected);
                rightStateListDrawable.addState(new int[]{16842912}, rightSelected);
                if (this.mSdk < 16) {
                    radioButton.setBackgroundDrawable(rightStateListDrawable);
                } else {
                    radioButton.setBackground(rightStateListDrawable);
                }
            } else {
                GradientDrawable middleUnselected = (GradientDrawable) this.mCtx.getResources().getDrawable(R.drawable.segment_middle_option).mutate();
                middleUnselected.setStroke(oneDP, this.selectedColor);
                middleUnselected.setDither(true);
                middleUnselected.setColor(this.unselectedColor);
                GradientDrawable middleSelected = (GradientDrawable) this.mCtx.getResources().getDrawable(R.drawable.segment_middle_option_selected).mutate();
                middleSelected.setColor(this.selectedColor);
                middleSelected.setStroke(oneDP, this.selectedColor);
                StateListDrawable middleStateListDrawable = new StateListDrawable();
                middleStateListDrawable.addState(new int[]{-16842912}, middleUnselected);
                middleStateListDrawable.addState(new int[]{16842912}, middleSelected);
                if (this.mSdk < 16) {
                    radioButton.setBackgroundDrawable(middleStateListDrawable);
                } else {
                    radioButton.setBackground(middleStateListDrawable);
                }
            }
            radioButton.setMinWidth(this.itemMinWidth);
            radioButton.setGravity(17);
            radioButton.setLayoutParams(params);
            radioButton.setTextSize(2, 12.0f);
            radioButton.setText((CharSequence) item.getKey());
            textWidth = Math.max(radioButton.getPaint().measureText((String) item.getKey()), textWidth);
            this.options.add(radioButton);
            i++;
        }
        int width = (int) (((float) (((int) TypedValue.applyDimension(1, 2.0f, getResources().getDisplayMetrics())) * 20)) + textWidth);
        if (width < this.itemMinWidth) {
            width = this.itemMinWidth;
        }
        Iterator it = this.options.iterator();
        while (it.hasNext()) {
            RadioButton option = (RadioButton) it.next();
            if (this.equalWidth) {
                option.setWidth(width);
            }
            addView(option);
        }
        setOnCheckedChangeListener(this.selectionChangedlistener);
        if (this.defaultSelection > -1) {
            check(((RadioButton) getChildAt(this.defaultSelection)).getId());
        }
    }

    public String[] getCheckedWithIdentifier() {
        return new String[]{this.identifier, (String) this.itemMap.get(((RadioButton) findViewById(getCheckedRadioButtonId())).getText().toString())};
    }

    public String getChecked() {
        return (String) this.itemMap.get(((RadioButton) findViewById(getCheckedRadioButtonId())).getText().toString());
    }

    public void setItems(String[] itemArray, String[] valueArray) throws Exception {
        this.itemMap.clear();
        if (itemArray == null || valueArray == null || itemArray.length == valueArray.length) {
            if (itemArray != null) {
                if (valueArray != null) {
                    for (int i = 0; i < itemArray.length; i++) {
                        this.itemMap.put(itemArray[i].toString(), valueArray[i].toString());
                    }
                } else {
                    for (CharSequence item : itemArray) {
                        this.itemMap.put(item.toString(), item.toString());
                    }
                }
            }
            update();
            return;
        }
        throw new Exception("Item labels and value arrays must be the same size");
    }

    public void setItems(String[] items, String[] values, int defaultSelection) throws Exception {
        if (defaultSelection > items.length - 1) {
            throw new Exception("Default selection cannot be greater than the number of items");
        }
        this.defaultSelection = defaultSelection;
        setItems(items, values);
    }

    public void setDefaultSelection(int defaultSelection) throws Exception {
        if (defaultSelection > this.itemMap.size() - 1) {
            throw new Exception("Default selection cannot be greater than the number of items");
        }
        this.defaultSelection = defaultSelection;
        update();
    }

    public void setColors(int primaryColor, int secondaryColor) {
        this.selectedColor = primaryColor;
        this.selectedTextColor = secondaryColor;
        this.unselectedColor = secondaryColor;
        this.unselectedTextColor = primaryColor;
        int[][] r1 = new int[2][];
        r1[0] = new int[]{-16842912};
        r1[1] = new int[]{16842912};
        this.textColorStateList = new ColorStateList(r1, new int[]{this.unselectedTextColor, this.selectedTextColor});
        update();
    }

    public void setColors(int selectedColor, int selectedTextColor, int unselectedColor, int unselectedTextColor) {
        this.selectedColor = selectedColor;
        this.selectedTextColor = selectedTextColor;
        this.unselectedColor = unselectedColor;
        this.unselectedTextColor = unselectedTextColor;
        int[][] r1 = new int[2][];
        r1[0] = new int[]{-16842912};
        r1[1] = new int[]{16842912};
        this.textColorStateList = new ColorStateList(r1, new int[]{unselectedTextColor, selectedTextColor});
        update();
    }

    public void setByValue(String value) {
        String buttonText = "";
        if (this.itemMap.containsValue(value)) {
            for (String entry : this.itemMap.keySet()) {
                if (((String) this.itemMap.get(entry)).equalsIgnoreCase(value)) {
                    buttonText = entry;
                }
            }
        }
        Iterator it = this.options.iterator();
        while (it.hasNext()) {
            RadioButton option = (RadioButton) it.next();
            if (option.getText().toString().equalsIgnoreCase(buttonText)) {
                check(option.getId());
            }
        }
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.mListener = listener;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setEqualWidth(boolean equalWidth) {
        this.equalWidth = equalWidth;
        update();
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
        update();
    }
}
