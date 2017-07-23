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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sin.myapp.R;
import com.example.sin.myapp.bean.ConditionMenu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import base.ViewHolder;

public class BilibiliSearchLayout extends LinearLayout implements AdapterView.OnItemClickListener {

    private static final String TAG = "BilibiliSearchLayout";

    private LinearLayout mTabContainer;         // 显示tab的布局
    private FrameLayout mFlContentContainer;    // 显示内容的布局

    private int mTabCount;                      // 记录有多少张tab标签
    private List<GridView> mMenus;              // 每一张Menu对应一个tab标签
    private List<View> mTabs;                   // 标签

    private int mCurTagPosition = -1;           // 当前显示tag的position
    private View mMaskView;
    private Toast toast;
    private FrameLayout menuContent;
    private MenuSelectListener mListener;

    private List<MenuListAdapter> adapters;
    private List<List<ConditionMenu>> tabMenus;
    private List<TriangleView> mTriangleView;
    private List<TabHolder> mTabHolders;
    private int[][] record = new int[][]{{0}, {0}, {0}};
    private LinearLayout mIndicators;

    private LinkedHashMap<List<String>, Integer> mSelectMenus;


    public BilibiliSearchLayout(Context context) {
        this(context, null);
    }

    public BilibiliSearchLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BilibiliSearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        mIndicators = new LinearLayout(getContext());
        mIndicators.setBackgroundColor(Color.WHITE);
        LayoutParams paramsIndicator = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mIndicators.setOrientation(HORIZONTAL);
        mIndicators.setLayoutParams(paramsIndicator);
        addView(mIndicators, 1);

        // 正文区
        mFlContentContainer = new FrameLayout(getContext());
        ViewGroup.LayoutParams contentParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFlContentContainer.setLayoutParams(contentParams);
        addView(mFlContentContainer, 2);

        mMenus = new ArrayList<>();
        mTabs = new ArrayList<>();
        adapters = new ArrayList<>();
        tabMenus = new ArrayList<>();
        mTriangleView = new ArrayList<>();

        mTabHolders = new ArrayList<>();
    }

    private static int dp2px(float dp, Context context) {
        // 获取屏幕分辨率
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        // 将值转换为设备独立像素转换为
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics) + 0.5f);
    }

    public void setMenuList(LinkedHashMap<List<String>, Integer> selectMenus) {
        mSelectMenus = selectMenus;
        int index = 0;
        for (Map.Entry<List<String>, Integer> entry : selectMenus.entrySet()) {
            List<String> key = entry.getKey();
            addTab(key.get(0));
            addConditionMenuList(trans2ConditionMenu(key), index++, entry.getValue());
        }
    }

    public void setMenuList2(LinkedHashMap<List<String>, Integer> selectMenus) {
        mSelectMenus = selectMenus;
        int index = 0;
        for (Map.Entry<List<String>, Integer> entry : selectMenus.entrySet()) {

            List<String> key = entry.getKey();
            TabHolder tabHolder = new TabHolder(
                    trans2ConditionMenu(key),
                    entry.getValue(),
                    index,
                    getContext());

            tabHolder.mTabLayout.setTag(index);
            tabHolder.mTabLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tab = (Integer) v.getTag();
                    switchTag2(tab);
                }
            });

            tabHolder.setOnMenuSelectListener(new TabHolder.MenuSelectListener() {
                @Override
                public void onMenuSelect(int tabPosition, int menuPosition) {
                    Toast.makeText(getContext(),
                            "" + tabPosition + " -- " + menuPosition,
                            Toast.LENGTH_SHORT).show();
                    closeMenu();
                }
            });
            mTabContainer.addView(tabHolder.mTabLayout);
            mIndicators.addView(tabHolder.mIndicatorLayout);
            mTabHolders.add(tabHolder);

            index++;

        }
    }

    private List<ConditionMenu> trans2ConditionMenu(List<String> stringMenus) {
        List<ConditionMenu> objects = new ArrayList<>();
        for (String str : stringMenus) {
            objects.add(new ConditionMenu(str, false));
        }
        return objects;
    }


    private void addTab(String tab) {

        RelativeLayout tabContainer = new RelativeLayout(getContext());

        LinearLayout.LayoutParams params = new LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f);                // -1表示weight, 权重

        params.topMargin = dp2px(12, getContext());
        params.leftMargin = dp2px(10, getContext());
        params.rightMargin = dp2px(10, getContext());
        tabContainer.setLayoutParams(params);

        TextView tabView = new TextView(getContext());
        RelativeLayout.LayoutParams tabParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tabParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tabView.setLayoutParams(
                tabParams);

        tabView.setGravity(Gravity.CENTER);
        tabView.setText(tab);
        tabView.setCompoundDrawablePadding(dp2px(5, getContext()));
        tabView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                getResources().getDrawable(R.drawable.selector_bilibili),
                null);

        tabView.setTextColor(new ColorStateList(
                new int[][]{
                        {android.R.attr.state_selected},
                        {}},
                new int[]{
                        Color.rgb(0xFB, 0X72, 0x99),      // bilibili色
                        Color.rgb(0xAA, 0xAA, 0xAA)}));   // 灰色

        tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // 文字大小设置为12sp

        mTabs.add(tabView);
        tabContainer.addView(tabView);
        tabContainer.setClickable(true);

        tabContainer.setTag(mTabCount++);
        tabContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long l = System.currentTimeMillis();
                Log.e(TAG, "onClick: " + l);
                Integer tag = (Integer) v.getTag();
                switchTag(tag);
            }
        });

        View indicatorView = getIndicatorView();
        mIndicators.addView(indicatorView);

        mTabContainer.addView(tabContainer);
    }

    private View getIndicatorView() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f);
        frameLayout.setLayoutParams(layoutParams);

        TriangleView triangleView = new TriangleView(getContext());
        FrameLayout.LayoutParams triangleParams = new FrameLayout.LayoutParams(
                dp2px(20, getContext()),
                dp2px(12, getContext()));
        triangleParams.gravity = Gravity.CENTER_HORIZONTAL;
        triangleParams.rightMargin = dp2px(7, getContext());
        triangleView.setLayoutParams(triangleParams);
        triangleView.setVisibility(INVISIBLE);
        triangleView.setTriangleColor(Color.rgb(0xF9, 0XF9, 0XF9));

        mTriangleView.add(triangleView);
        frameLayout.addView(triangleView);
        return frameLayout;
    }

    private void switchTag2(int tagPosition) {
        if (mCurTagPosition == tagPosition) return;

        if (mCurTagPosition != -1) {
            mTabHolders.get(mCurTagPosition).display(false);
        } else {
            mMaskView.setVisibility(VISIBLE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.content_in));
            mMaskView.setClickable(true);
            menuContent.setVisibility(VISIBLE);
            menuContent.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_in));
        }

        mCurTagPosition = tagPosition;
        mTabHolders.get(tagPosition).display(true);
    }

    private void switchTag(int tagPosition) {
        if (mCurTagPosition == tagPosition) return;

        if (mCurTagPosition != -1) {
            menuContent.getChildAt(mCurTagPosition).setVisibility(GONE);
            menuContent.getChildAt(tagPosition).setVisibility(VISIBLE);
            mTabs.get(mCurTagPosition).setSelected(false);
            // triangles.get(mCurTagPosition).setVisibility(INVISIBLE);
            // tabHolders.get(mCurTagPosition).onTabShow(false);
            mTriangleView.get(mCurTagPosition).setVisibility(INVISIBLE);
        } else {
            GridView listView = mMenus.get(tagPosition);
            listView.setVisibility(VISIBLE);
            mMaskView.setVisibility(VISIBLE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.content_in));
            mMaskView.setClickable(true);
            menuContent.setVisibility(VISIBLE);
            menuContent.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_in));

        }

        mCurTagPosition = tagPosition;
        mTabs.get(tagPosition).setSelected(true);
        mTriangleView.get(tagPosition).setVisibility(VISIBLE);
    }

    private void addConditionMenuList(List<ConditionMenu> menus, int tabPosition) {


        GridView menusListView = new GridView(getContext());
        menus.get(0).selected = true;
        switch (menus.get(0).name) {
            case "默认排序":
                menusListView.setNumColumns(4);
                break;
            case "全部分区":
                menusListView.setNumColumns(5);
                break;
            case "全部时长":
                menusListView.setNumColumns(5);
                break;
        }
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        menusListView.setLayoutParams(params);
        // menusListView.setDividerHeight(0);
        menusListView.setBackgroundColor(Color.rgb(0xF9, 0xF9, 0xF9));
        menusListView.setVisibility(GONE);
        menusListView.setTag(tabPosition);
        menusListView.setOnItemClickListener(this);
        mMenus.add(menusListView);

        MenuListAdapter adapter = new MenuListAdapter(menus);
        menusListView.setAdapter(adapter);
        adapters.add(adapter);
    }

    private void addConditionMenuList(List<ConditionMenu> menus, int tabPosition, int columnsNum) {


        GridView menusListView = new GridView(getContext());
        menus.get(0).selected = true;
        menusListView.setNumColumns(columnsNum);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        menusListView.setLayoutParams(params);
        // menusListView.setDividerHeight(0);
        menusListView.setBackgroundColor(Color.rgb(0xF9, 0xF9, 0xF9));
        menusListView.setVisibility(GONE);
        menusListView.setTag(tabPosition);
        menusListView.setOnItemClickListener(this);
        mMenus.add(menusListView);

        MenuListAdapter adapter = new MenuListAdapter(menus);
        menusListView.setAdapter(adapter);
        adapters.add(adapter);
    }

    public void setCurTagText(String text) {
        if (mCurTagPosition == -1) return;

        TextView tabTextView = (TextView) mTabs.get(mCurTagPosition);
        tabTextView.setText(text);
    }

    public void setMenuSelectListener(MenuSelectListener listener) {
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
        mMaskView.setBackgroundColor(Color.rgb(0xAB, 0xAB, 0xAB));
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        mMaskView.setVisibility(GONE);
        mFlContentContainer.addView(mMaskView);
        menuContent.setVisibility(GONE);
        mFlContentContainer.addView(menuContent);
        int index = 0;
        for (TabHolder holder : mTabHolders) {
            menuContent.addView(holder.mGvDisplay, index++);
        }
    }

    public void closeMenu() {
        if (mCurTagPosition != -1) {
            mTabHolders.get(mCurTagPosition).display(false);
            mMaskView.setVisibility(GONE);
            mMaskView.setClickable(false);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.content_out));
            menuContent.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_out));
            menuContent.setVisibility(GONE);

        }
        mCurTagPosition = -1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer tabPosition = (Integer) parent.getTag();

        if (mListener != null) {
            mListener.onMenuSelect(tabPosition, position);
        }
        setCurTagText(tabMenus.get(tabPosition).get(position).name);
        tabMenus.get(tabPosition).get(position).selected = true;
        tabMenus.get(tabPosition).get(record[tabPosition][0]).selected = false;
        record[tabPosition][0] = position;
        adapters.get(tabPosition).notifyDataSetChanged();
        closeMenu();
    }

    private static class MenuListAdapter extends BaseAdapter {

        private List<ConditionMenu> menuList;

        MenuListAdapter(List<ConditionMenu> menuList) {
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
                convertView = View.inflate(parent.getContext(), R.layout.item_gmenu, null);
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

    private static class MenuHolder extends ViewHolder<ConditionMenu> {

        private TextView mTv;

        public MenuHolder(View root) {
            super(root);
            mTv = (TextView) findViewById(R.id.menu_tv);
        }

        @Override
        public void bindData(ConditionMenu s) {
            mTv.setText(s.name);
            if (!s.selected) {
                mTv.setTextColor(Color.GRAY);
                mTv.setSelected(false);
            } else {
                mTv.setSelected(true);
                mTv.setTextColor(Color.WHITE);
            }
        }
    }

    private void toastLog(String msg) {
        if (toast == null)
            toast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);

        toast.setText(msg);
        toast.show();
    }

    static class TabHolder {
        private int columns;
        private int tabPosition;
        private View mIndicatorLayout;
        private View mTabLayout;
        private Context context;
        // 显示tab文本的textView
        TextView mTvTab;
        // 显示三角形的指示布局
        TriangleView mTv;
        // 表示当前菜单选中了拿一项
        int mPosition;
        // 展示菜单的GridView
        GridView mGvDisplay;
        // GridView的数据适配器
        MenuListAdapter mAdapter;
        // 表示菜单的Bean集合
        List<ConditionMenu> mConditionMenus;

        MenuSelectListener listener;

        TabHolder(List<ConditionMenu> conditionMenu, int columns, int tabPosition, Context context) {
            this.context = context;
            this.columns = columns;
            mConditionMenus = conditionMenu;
            this.tabPosition = tabPosition;

            mTabLayout = initTabLayout();

            mIndicatorLayout = initIndicatorLayout();

            initMenuList();

        }

        private View initIndicatorLayout() {
            FrameLayout frameLayout = new FrameLayout(context);
            LinearLayout.LayoutParams layoutParams = new
                    LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f);
            frameLayout.setLayoutParams(layoutParams);

            TriangleView triangleView = new TriangleView(context);
            FrameLayout.LayoutParams triangleParams = new FrameLayout.LayoutParams(
                    dp2px(20, context),
                    dp2px(12, context));
            triangleParams.gravity = Gravity.CENTER_HORIZONTAL;
            triangleParams.rightMargin = dp2px(7, context);
            triangleView.setLayoutParams(triangleParams);
            triangleView.setVisibility(VISIBLE);
            triangleView.setTriangleColor(Color.rgb(0xF9, 0XF9, 0XF9));
            mTv = triangleView;
            frameLayout.addView(triangleView);
            frameLayout.setVisibility(INVISIBLE);
            return frameLayout;
        }

        private void initMenuList() {
            GridView menusListView = new GridView(context);
            mConditionMenus.get(0).selected = true;
            menusListView.setNumColumns(columns);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            menusListView.setLayoutParams(params);
            menusListView.setBackgroundColor(Color.rgb(0xF9, 0xF9, 0xF9));
            menusListView.setVisibility(GONE);
            menusListView.setTag(tabPosition);
            mAdapter = new MenuListAdapter(mConditionMenus);
            menusListView.setAdapter(mAdapter);
            menusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mConditionMenus.get(mPosition).selected = false;
                    mConditionMenus.get(position).selected = true;
                    mPosition = position;
                    mTvTab.setText(mConditionMenus.get(position).name);
                    if(position > 0){
                        mTvTab.setSelected(true);
                    }else{
                        mTvTab.setSelected(false);
                    }
                    if (listener != null) {
                        listener.onMenuSelect(tabPosition, position);
                    }
                }
            });
            mGvDisplay = menusListView;
        }

        private View initTabLayout() {
            RelativeLayout tabLayout = new RelativeLayout(context);

            LinearLayout.LayoutParams params = new LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f);

            params.topMargin = dp2px(12, context);
            params.leftMargin = dp2px(10, context);
            params.rightMargin = dp2px(10, context);
            tabLayout.setLayoutParams(params);

            TextView tabView = new TextView(context);
            RelativeLayout.LayoutParams tabParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tabParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            tabView.setLayoutParams(
                    tabParams);

            tabView.setGravity(Gravity.CENTER);
            tabView.setText(mConditionMenus.get(0).name);
            tabView.setCompoundDrawablePadding(dp2px(5, context));
            tabView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    context.getResources().getDrawable(R.drawable.selector_bilibili),
                    null);

            tabView.setTextColor(new ColorStateList(
                    new int[][]{
                            {android.R.attr.state_selected},
                            {}},
                    new int[]{
                            Color.rgb(0xFB, 0X72, 0x99),      // bilibili色
                            Color.rgb(0xAA, 0xAA, 0xAA)}));   // 灰色

            tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // 文字大小设置为12sp

            mTvTab = tabView;
            tabLayout.addView(tabView);
            tabLayout.setClickable(true);
            return tabLayout;
        }

        void display(boolean display) {
            if (display) {
                mIndicatorLayout.setVisibility(VISIBLE);
                mGvDisplay.setVisibility(VISIBLE);
                mAdapter.notifyDataSetChanged();

            } else {
                mGvDisplay.setVisibility(INVISIBLE);
                mIndicatorLayout.setVisibility(INVISIBLE);
            }
        }

        public void setOnMenuSelectListener(MenuSelectListener listener) {
            this.listener = listener;
        }

        static interface MenuSelectListener {
            void onMenuSelect(int tabPosition, int menuPosition);
        }
    }

    public interface MenuSelectListener {

        void onMenuSelect(int tabPosition, int menuPosition);
    }
}
