package jp.cordea.sakemeter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ogaclejapan.rx.binding.RxEvent;

import rx.functions.Action1;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MeterFragment.newInstance();
            case 1:
                return EditFragment.newInstance();
            case 2:
                return DetailFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.meter_fragment_title);
            case 1:
                return context.getResources().getString(R.string.edit_fragment_title);
            case 2:
                return context.getResources().getString(R.string.detail_fragment_title);
        }
        return super.getPageTitle(position);
    }

}
