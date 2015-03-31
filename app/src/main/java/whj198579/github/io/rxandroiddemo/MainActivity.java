package whj198579.github.io.rxandroiddemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private SlidingPaneLayout mSlidingLayout;
    private Fragment mFragment;

    private ActionBarHelper mActionBarHelper;

    private final static String[] TITLES =
            {
                    "Hello world",
                    "Double Clicks",
                    "Http Asynchronous"

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        mSlidingLayout.openPane();
        mSlidingLayout.setPanelSlideListener(new SliderListener());

        ListView mList = (ListView) findViewById(R.id.left_pane);
        mList.setAdapter(new ArrayAdapter<>(this, R.layout.left_pannel_item, TITLES));
        mList.setOnItemClickListener(new ListItemClickListener());

        mActionBarHelper = createActionBarHelper();
        mActionBarHelper.init();

        mSlidingLayout.getViewTreeObserver().addOnGlobalLayoutListener(new FirstLayoutListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home && !mSlidingLayout.isOpen()) {
            mSlidingLayout.openPane();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBarHelper createActionBarHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new ActionBarHelperICS();
        } else {
            return new ActionBarHelper();
        }
    }

    private class SliderListener extends SlidingPaneLayout.SimplePanelSlideListener {
        @Override
        public void onPanelOpened(View panel) {
            mActionBarHelper.onPanelOpened();
        }

        @Override
        public void onPanelClosed(View panel) {
            mActionBarHelper.onPanelClosed();
        }
    }

    private class ListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActionBarHelper.setTitle(TITLES[position]);
            mSlidingLayout.closePane();

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (mFragment != null) {
                fragmentManager.beginTransaction().remove(mFragment).commit();
                mFragment = null;
            }

            switch (position) {
                case 0:
                    mFragment = new HelloWorldFragment();
                    break;
                case 1:
                    mFragment = new DoubleClicksFragment();
                    break;
            }

            if (mFragment != null) {
                fragmentManager.beginTransaction().add(R.id.right_pane, mFragment).commit();
            }
        }

    }

    @SuppressWarnings("deprecation")
    private class FirstLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            mActionBarHelper.onFirstLayout();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mSlidingLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                mSlidingLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    }

    private class ActionBarHelper {
        public void init() {
        }

        public void onPanelClosed() {
        }

        public void onPanelOpened() {
        }

        public void onFirstLayout() {
        }

        public void setTitle(CharSequence title) {
        }
    }

    private class ActionBarHelperICS extends ActionBarHelper {
        private final ActionBar mActionBar;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;

        ActionBarHelperICS() {
            mActionBar = getSupportActionBar();
        }

        @Override
        public void init() {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mTitle = mDrawerTitle = getTitle();
        }

        @Override
        public void onPanelClosed() {
            super.onPanelClosed();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setTitle(mTitle);
        }

        @Override
        public void onPanelOpened() {
            super.onPanelOpened();
            mActionBar.setHomeButtonEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle(mDrawerTitle);
        }

        @Override
        public void onFirstLayout() {
            if (mSlidingLayout.isSlideable() && !mSlidingLayout.isOpen()) {
                onPanelClosed();
            } else {
                onPanelOpened();
            }
        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
        }
    }
}
