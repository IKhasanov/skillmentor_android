package com.skillmentor.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.skillmentor.R;
import com.skillmentor.R.id;
import com.skillmentor.R.layout;
import com.skillmentor.R.menu;
import com.skillmentor.R.string;
import com.skillmentor.dialogs.LogInDialogFragment;
import com.skillmentor.dialogs.LogInDialogFragment.LogInDialogListener;
import com.skillmentor.models.Advert;
import com.skillmentor.utils.Consts;
import com.skillmentor.utils.net.ApiSession;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, LogInDialogListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	int menuId = R.menu.mainlogin;
	
	protected final static String PREFERENCES_NAME = "Skillmentor";
	protected SharedPreferences mSettings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSettings = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(menuId, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_log_in:
			showLogInDialog();
			return true;
		case R.id.action_log_out:
			menuId = R.menu.mainlogin;
			Editor editor = mSettings.edit();
			editor.remove(Consts.loginArg);
			editor.remove(Consts.passwordArg);
			editor.commit();
			
			invalidateOptionsMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();

			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private AdvertListAdapter adapter;
		private List<Advert> advertModelList = new ArrayList<Advert>();


		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			
			adapter = new AdvertListAdapter(getActivity(), R.layout.list_item);
			ListView listView = (ListView) rootView.findViewById(R.id.lvList);
			Log.d("ListView", "listview = " + listView);
			listView.setAdapter(adapter);
			
			int param = getArguments().getInt(ARG_SECTION_NUMBER);
			new GetAdvertListTask().execute(param);
			
			return rootView;
		}
		
		public final class GetAdvertListTask extends AsyncTask<Integer, Void, List<Advert>> {

			@Override
			protected List<Advert> doInBackground(Integer... params) {
				List<Advert> advertsList = new ArrayList<Advert>();
				ApiSession apiSession = new ApiSession();
				if (params[0] == 2) {
					advertsList = apiSession.requestList();
				} else {
					advertsList = apiSession.advertList();
				}

				return advertsList;
			}
			
			@Override
			protected void onPostExecute(final List<Advert> advertList) {
				super.onPostExecute(advertList);
				
				advertModelList = advertList;
				adapter.clear();
				if (advertModelList != null) {
					for (Advert advert : advertModelList) {
						adapter.add(advert);
					}
				}
				adapter.notifyDataSetChanged();
				System.out.println("Adapter notified");
			}
			
		}
		
		public class AdvertListAdapter extends ArrayAdapter<Advert> {
			private final LayoutInflater mInflater;
			
			public AdvertListAdapter(Context context, int resource) {
				super(context, resource);
				mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			
			public View getView(final int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.list_item, null);
					
					holder.tvListItemHeader = (TextView) convertView.findViewById(R.id.tvListItemHeader);
					holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
					holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
					holder.rlNameDivider = (RelativeLayout) convertView.findViewById(R.id.rlNameDivider);
					holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrce);
					holder.rlPriceDivider = (RelativeLayout) convertView.findViewById(R.id.rlPriceDivider);
					holder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
					holder.rlLocationDivider = (RelativeLayout) convertView.findViewById(R.id.rlLocationDivider);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				
				Advert advert = advertModelList.get(position);
				holder.tvListItemHeader.setText(advert.getTitle());
				holder.tvDescription.setText(advert.getDescription());
				if (advert.getUser() == null) {
					holder.tvName.setVisibility(View.GONE);
					holder.rlNameDivider.setVisibility(View.GONE);
				} else {
					holder.tvName.setText(advert.getUser().getName());
					holder.tvName.setVisibility(View.VISIBLE);
					holder.rlNameDivider.setVisibility(View.VISIBLE);
				}
				if (advert.getReward() == null) {
					holder.tvPrice.setVisibility(View.GONE);
					holder.rlPriceDivider.setVisibility(View.GONE);
				} else {
					holder.tvPrice.setText(advert.getReward());
					holder.tvPrice.setVisibility(View.VISIBLE);
					holder.rlPriceDivider.setVisibility(View.VISIBLE);
				}
				if (advert.getLocation() == null) {
					holder.tvLocation.setVisibility(View.GONE);
					holder.rlLocationDivider.setVisibility(View.GONE);
				} else {
					holder.tvLocation.setVisibility(View.VISIBLE);
					holder.tvLocation.setText(advert.getLocation());
					holder.rlLocationDivider.setVisibility(View.VISIBLE);
				}
				
				return convertView;
			}
			
		}
	}
	
	private static class ViewHolder {
		TextView tvListItemHeader;
		TextView tvDescription;
		TextView tvPrice;
		RelativeLayout rlPriceDivider;
		TextView tvName;
		RelativeLayout rlNameDivider;
		TextView tvLocation;
		RelativeLayout rlLocationDivider;
	}
	
	public void showLogInDialog() {
		LogInDialogFragment dialog = new LogInDialogFragment();
		dialog.show(getFragmentManager(), "LogInDialogFragment");
	}

	@Override
	public void onDialogLogInClick(DialogFragment dialog, Bundle args) {
		Editor editor = mSettings.edit();
		Log.d("MainActivity onDialogLogInClick", "Login = " + args.getString(Consts.loginArg));
		Log.d("MainActivity onDialogLogInClick", "Password = " + args.getString(Consts.passwordArg));
		editor.putString(Consts.loginArg, args.getString(Consts.loginArg));
		editor.putString(Consts.passwordArg, args.getString(Consts.passwordArg));
		editor.commit();
		
		menuId = R.menu.mainlogout;
		invalidateOptionsMenu();
		dialog.dismiss();
	}

	@Override
	public void onDialogCancelClick(DialogFragment dialog) {
		dialog.dismiss();		
	}

}
