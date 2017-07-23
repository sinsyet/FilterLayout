package ui;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sin.myapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewHolder;

public class DropMenuLayout extends LinearLayout implements AdapterView.OnItemClickListener {

    private static final String TAG = "DropMenuLayout";

    private LinearLayout mTabContainer;         // 显示tab的布局
    private FrameLayout mFlContentContainer;    // 显示内容的布局

    private int mTabCount;                      // 记录有多少张tab标签
    private List<ListView> mMenus;              // 每一张Menu对应一个tab标签
    private List<View> mTabs;                   // 标签

    private int mCurTagPosition = -1;           // 当前显示tag的position
    private View mMaskView;
    private Toast toast;
    private FrameLayout menuContent;
    private MenuSelectListener mListener;


    public DropMenuLayout(Context context) {
        this(context, null);
    }

    public DropMenuLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DropMenuLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        // 标签区
        mTabContainer = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mTabContainer.setOrientation(HORIZONTAL);
        mTabContainer.setLayoutParams(params);
        mTabContainer.setBackgroundColor(Color.WHITE);
        addView(mTabContainer, 0);

        //下划线
        View divider = new View(getContext());
        divider.setBackgroundColor(0x888888);
        ViewGroup.LayoutParams dividerParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dividerParams.height = dp2px(1);
        divider.setBackgroundColor(Color.GRAY);
        divider.setLayoutParams(dividerParams);
        addView(divider, 1);

        // 正文区
        mFlContentContainer = new FrameLayout(getContext());
        ViewGroup.LayoutParams contentParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFlContentContainer.setLayoutParams(contentParams);
        addView(mFlContentContainer, 2);

        mMenus = new ArrayList<>();
        mTabs = new ArrayList<>();
    }

    private int dp2px(float dp) {
        // 获取屏幕分辨率
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // 将值转换为设备独立像素转换为
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics) + 0.5f);
    }

    public void setTabsAndMenu(Map<String, List<String>> tabs) {

        if (tabs.size() <= 0) return;
        int index = 0;
        for (Map.Entry<String, List<String>> entry : tabs.entrySet()) {
            addTab(entry.getKey());
            addMenuList(entry.getValue(),index++);
        }
    }

    private void addTab(String tab) {

        if(mTabs.size() > 0){
            View vDivider = new View(getContext());
            LayoutParams params = new LayoutParams(
                    dp2px(1),
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.bottomMargin = dp2px(10);
            params.topMargin = dp2px(10);
            params.leftMargin = dp2px(10);
            params.rightMargin = dp2px(10);
            vDivider.setLayoutParams(params);
            vDivider.setBackgroundColor(Color.argb(0x55,0xAA,0xAA,0xAA));
            mTabContainer.addView(vDivider);
        }

        TextView tabView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f);                // -1表示weight, 权重

        tabView.setLayoutParams(params);
        tabView.setGravity(Gravity.CENTER);
        tabView.setText(tab);
        tabView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                getResources().getDrawable(R.drawable.selector_up),
                null);

        tabView.setTextColor(new ColorStateList(
                new int[][]{
                        {android.R.attr.state_selected},
                        {}},
                new int[]{
                        Color.rgb(0xEA,0X56,0x22),
                        Color.BLACK}));

        tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // 文字大小设置为16sp
        tabView.setPadding(dp2px(5), dp2px(12), dp2px(5), dp2px(12));

        mTabs.add(tabView);

        mTabContainer.addView(tabView);

        tabView.setTag(mTabCount ++);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tag = (Integer) v.getTag();
                switchTag(tag);
            }
        });
    }

    private Drawable getTabDrawableSelector(){
        StateListDrawable listDrawable = new StateListDrawable();

        ColorDrawable normal = new ColorDrawable(Color.BLACK);
        ColorDrawable selected = new ColorDrawable(Color.rgb(0xEA,0X56,0X22));
        listDrawable.addState(new int[]{android.R.attr.state_selected},selected);
        listDrawable.addState(new int[]{},normal);
        return listDrawable;
    }

    private void switchTag(int tagPosition) {
        if(mCurTagPosition == tagPosition) return;

        if (mCurTagPosition != -1) {
            menuContent.getChildAt(mCurTagPosition).setVisibility(GONE);
            menuContent.getChildAt(tagPosition).setVisibility(VISIBLE);
            mTabs.get(mCurTagPosition).setSelected(false);
        } else {
            ListView listView = mMenus.get(tagPosition);
            listView.setVisibility(VISIBLE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.content_in));
            mMaskView.setVisibility(VISIBLE);
            mMaskView.setClickable(true);
            menuContent.setVisibility(VISIBLE);
            menuContent.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.pop_in));
        }

        mCurTagPosition = tagPosition;
        mTabs.get(tagPosition).setSelected(true);
    }

    private void addMenuList(List<String> menus,int tabPosition) {
        ListView menusListView = new ListView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        menusListView.setLayoutParams(params);
        menusListView.setDividerHeight(0);
        menusListView.setBackgroundColor(Color.WHITE);
        menusListView.setVisibility(GONE);
        menusListView.setTag(tabPosition);
        menusListView.setOnItemClickListener(this);
        mMenus.add(menusListView);

        menusListView.setAdapter(new MenuListAdapter(menus));
    }

    public void setCurTagText(String text){
        if(mCurTagPosition == -1) return;

        TextView tabTextView = (TextView) mTabs.get(mCurTagPosition);
        tabTextView.setText(text);
    }

    public void setMenuSelectListener(MenuSelectListener listener){
        mListener = listener;
    }

    public void setContentView(View view) {
        mFlContentContainer.addView(view, 0);

        menuContent = new FrameLayout(getContext());
        mMaskView = new View(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        menuContent.setLayoutParams(params);
        mMaskView.setLayoutParams(params);
        mMaskView.setBackgroundColor(Color.argb(0xAA, 0x00, 0x00, 0x00));
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: mMaskView");
                closeMenu();
            }
        });
        mMaskView.setVisibility(GONE);
        mFlContentContainer.addView(mMaskView);
        menuContent.setVisibility(GONE);
        mFlContentContainer.addView(menuContent);
        int index = 0;
        for (ListView menuList : mMenus) {
            menuContent.addView(menuList,index++);
        }
    }

    public void closeMenu() {
        if (mCurTagPosition != -1) {
            ListView listView = mMenus.get(mCurTagPosition);
            listView.setVisibility(GONE);
            mMaskView.setVisibility(GONE);
            mMaskView.setClickable(false);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.content_out));
            menuContent.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.pop_out));
            menuContent.setVisibility(GONE);
            mTabs.get(mCurTagPosition).setSelected(false);
        }
        mCurTagPosition = -1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer tabPosition = (Integer) parent.getTag();
        if (mListener != null) {
            mListener.onMenuSelect(tabPosition,position);
        }

        closeMenu();
    }

    private static class MenuListAdapter extends BaseAdapter {

        private List<String> menuList;

        MenuListAdapter(List<String> menuList) {
            this.menuList = menuList;
        }

        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 创建ViewHodler
            MenuHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_menu, null);
                holder = new MenuHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MenuHolder) convertView.getTag();
            }

            // 绑定数据
            holder.bindData(menuList.get(position));

            return convertView;
        }
    }

    private static class MenuHolder extends ViewHolder<String> {

        private TextView mTv;

        public MenuHolder(View root) {
            super(root);
            mTv = (TextView) findViewById(R.id.menu_tv);
        }

        @Override
        public void bindData(String s) {
            mTv.setText(s);
        }
    }

    private void toastLog(String msg) {
        if (toast == null)
            toast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);

        toast.setText(msg);
        toast.show();
    }

    public interface MenuSelectListener{

        void onMenuSelect(int tabPosition,int menuPosition);
    }
}
