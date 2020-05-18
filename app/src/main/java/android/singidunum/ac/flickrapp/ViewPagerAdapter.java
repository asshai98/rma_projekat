package android.singidunum.ac.flickrapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {
    //adapter neophodan za rad sa fragmentima u okviru tablayouta kroz koje ce korisnik moci da swipe-uje
    //dve liste za cuvanje fragmenata i nazive nasih fragmenata

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitle = new ArrayList<>(); //vidljivi na tablayout-u

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        fragmentTitle.add(title);
        //dodajemo u listu naziv i fragment
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position); //vraca fragment na odredjenoj poziciji u listi
    }

    //broj fragmenata u listi

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position); //preuzece sve nazive iz liste fragmentTitle i setovati kao nazive tabova
    }
}
