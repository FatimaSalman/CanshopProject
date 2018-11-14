package com.project.creativly.canshopproject.fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booking.rtlviewpager.RtlViewPager;
import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.adapter.CategoryAdapter;
import com.project.creativly.canshopproject.callback.CategoriesCallback;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriesFragment_ extends Fragment {

    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_, container, false);
        connectionManager = new ConnectionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(Objects.requireNonNull(getActivity()).getString(R.string.loading));
        progressDialog.show();
        init(view);
//        setupViewPager(viewPager);

getCategories();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
        selectPage(0);
//        Objects.requireNonNull(tabLayout.getTabAt(0)).isSelected();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
    }

    public void getCategories() {
        connectionManager.getCategories(new CategoriesCallback() {
            @Override
            public void onCategories(Category category) {
                progressDialog.dismiss();
                ProductsFragment f = new ProductsFragment();
// Supply data input as an argument.
                Bundle args = new Bundle();
                args.putString("slug", category.getSlug());
                f.setArguments(args);
                adapter.addFrag(f, category.getName().replace("&amp;", "&"));                //viewPager.setCurrentItem(0);

                viewPager.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(getActivity(), error);
            }
        });
    }
}
