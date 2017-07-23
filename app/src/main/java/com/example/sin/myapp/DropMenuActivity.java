package com.example.sin.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sin.myapp.bean.Person;
import com.example.sin.myapp.utils.ExecutorsUtil;
import com.example.sin.myapp.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import base.ViewHolder;
import ui.BilibiliSearchLayout;
import ui.DropMenuLayout;

public class DropMenuActivity extends AppCompatActivity {

    String city = "城市";
    String[] citys = new String[]{
            "不限",
            "北京",
            "上海",
            "深圳",
            "广州",
            "重庆",
            "香港",
            "其他"
    };

    String age = "年龄";
    String[] ages = new String[]{
            "不限",
            "15~20岁",
            "21~26岁",
            "27~32岁",
            "33岁以上"
    };

    String sex = "性别";
    String[] sexs = new String[]{
            "不限",
            "女性",
            "男性"
    };

    private List<Person> originPersons = Util.createSome();
    private ArrayList<Person> persons = new ArrayList<>();

    private DropMenuLayout mDropMenu;
    private ListView conventView;
    private PersonAdapter personAdapter;

    private String mCurSexSelectCondition;
    private String mCurAgeSelectCondition;
    private String mCurCitySelectConditine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_menu);

        initView();

        initData();
    }

    private void initData() {
        HashMap<String, List<String>> map = new HashMap<>();
        map.put(city, Arrays.asList(citys));
        map.put(age, Arrays.asList(ages));
        map.put(sex, Arrays.asList(sexs));

        mDropMenu.setTabsAndMenu(map);
        mDropMenu.setContentView(conventView);
        mDropMenu.setMenuSelectListener(new DropMenuLayout.MenuSelectListener() {
            @Override
            public void onMenuSelect(int tabPosition, int menuPosition) {
                String tabText = null;
                switch (tabPosition) {
                    case 0:
                        mCurSexSelectCondition = sexs[menuPosition];
                        tabText = mCurSexSelectCondition;
                        break;
                    case 1:
                        mCurAgeSelectCondition = ages[menuPosition];
                        tabText = mCurAgeSelectCondition;
                        break;
                    case 2:
                        mCurCitySelectConditine = citys[menuPosition];
                        tabText = mCurCitySelectConditine;
                        break;
                    default:
                        return;
                }

                mDropMenu.setCurTagText(tabText);

                ExecutorsUtil.exec(new Runnable() {
                    @Override
                    public void run() {
                        int[] ageValue = getAgeValue(mCurAgeSelectCondition);
                        // Log.e(TAG, "run: " + Arrays.toString(ageValue));
                        int size = originPersons.size();
                        persons.clear();

                        for (int i = 0; i < size; i++) {
                            Person person = originPersons.get(i);

                            if (checkAge(person, ageValue)
                                    && checkSex(person, mCurSexSelectCondition)
                                    && checkCity(person, mCurCitySelectConditine))
                                persons.add(person);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                personAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    private boolean checkAge(Person person, int[] ageRange) {
                        return person.age >= ageRange[0]
                                && person.age <= ageRange[1];
                    }

                    private boolean checkSex(Person person, String sexCondition) {
                        return person.sex.equals(sexCondition)
                                || "不限".equals(sexCondition)
                                || sexCondition == null;

                    }

                    private boolean checkCity(Person person, String cityCondition) {
                        return person.city.equals(cityCondition)
                                || "不限".equals(cityCondition)
                                || cityCondition == null;
                    }

                    private int[] getAgeValue(String age) {
                        if (age == null) return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE};
                        int[] ages = null;
                        switch (age) {
                            case "33岁以上":
                                ages = new int[]{33, Integer.MAX_VALUE};
                                break;
                            case "不限":
                                ages = new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE};
                                break;
                            default:
                                String[] split = age.split("~");
                                ages = new int[2];
                                ages[0] = Integer.parseInt(split[0].trim());
                                ages[1] = Integer.parseInt(split[1].trim().replace("岁", ""));
                                break;
                        }

                        return ages;
                    }
                });
            }
        });

        persons.addAll(Util.createSome());
        personAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mDropMenu = (DropMenuLayout) findViewById(R.id.dropmenu_dropmenu);

        conventView = new ListView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        personAdapter = new PersonAdapter(persons);
        conventView.setAdapter(personAdapter);
        conventView.setLayoutParams(params);
    }

    static class PersonAdapter extends BaseAdapter {

        private ArrayList<Person> persons;

        PersonAdapter(ArrayList<Person> persons) {
            this.persons = persons;
        }

        @Override
        public int getCount() {
            return persons.size();
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
            PersonHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_person, null);
                holder = new PersonHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (PersonHolder) convertView.getTag();
            }
            holder.bindData(persons.get(position));
            return convertView;
        }
    }


    static class PersonHolder extends ViewHolder<Person> {


        private TextView mTv;

        public PersonHolder(View root) {
            super(root);
            mTv = (TextView) findViewById(R.id.person_tv);
        }

        @Override
        public void bindData(Person person) {
            mTv.setText(person.toString());
        }
    }
}
