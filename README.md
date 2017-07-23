#### 说明

Library-FliterLayout是一个模仿Bilibili 筛选的布局;

Module-test是测试FilterLayout库的

#### 使用

* 在布局文件中声明FilterLayout
    
		<com.example.filterlayout.FilterLayout
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:id="@+id/main_filterlayout"
	        />

* 设置筛选菜单列表
	
		mFilterLayout = (FilterLayout) findViewById(R.id.main_filterlayout);
		LinkedHashMap<List<String>, Integer> map = Util.getMap();
        Set<List<String>> lists = map.keySet();
        arrayLists = lists.toArray(new ArrayList[0]);

        mFilterLayout.setMenuList(map);

* 设置选择监听

		mFilterLayout.setMenuSelectListener(this);
		
* 设置展示的ListView

		ListView textView = new ListView(this);
        final ArrayList<String> objects = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                objects);
        textView.setAdapter(arrayAdapter);
        mFilterLayout.setContentView(textView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    objects.add("item num " + i);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

* 菜单选择回调

		@Override
	    public void onMenuSelect(int tabPosition, int menuPosition) {
	        String s = (String) arrayLists[tabPosition].get(menuPosition);
	        Toast.makeText(this, "s: " + s, Toast.LENGTH_SHORT).show();
	    }

* setMenuList说明
	* 参数是LinkedHashMap<List<String>,Integer>
	* 集合需要是有序的, 即我们怎么给控件传(顺序), 就怎么显示出来;
	* List<String>是要显示的菜单, 默认显示集合的索引为0的元素
	* Integer是该菜单栏要显示多少列(使用的是GridView来显示的)

#### 备注

本Demo仅提供筛选菜单的实现思路, 实际运用中需要灵活多变,需要自己按照需求去修改成合适自己使用的布局控件
