package p.gorden.pdalibrary.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by gorden on 2018/1/4.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragment;

    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragment = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}
