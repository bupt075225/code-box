package com.hebao.ccliu.testfactory;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[] mNavItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        //toolbar的标题要在setSupportActionBar之前设置，否则会无效
        //mToolbar.setTitle("邻里");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);

        mNavItems = getResources().getStringArray(R.array.nav_item_array);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        //设置adapter,list view的内容由adapter产生
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                mNavItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // App bar上的图标按钮与导航抽屉的滑动交互通过ActionBarToggle关联起来
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawer_view) {
                super.onDrawerOpened(drawer_view);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //将事件传递给ActionBarDrawerToggle，处理点击app图标打开导航抽屉的事件
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 监听点击左滑导航抽屉中的导航项
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        //创建新的fragment,通过替换fragment来更新主界面内容

        //导航抽屉中选中的导航项高亮
        mDrawerList.setItemChecked(position, true);
        //关闭抽屉
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    //使用了ActionBarDrawerToggle后要覆盖以下两个方法
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //onRestoreInstanceState变化时，同步toggle状态
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //将配置更新传递给drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);

        onTitleChanged(getTitle(), getTitleColor());
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if(toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }
}
