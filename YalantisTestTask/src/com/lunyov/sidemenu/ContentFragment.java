package com.lunyov.sidemenu;

import android.support.v4.app.Fragment;

public class ContentFragment extends Fragment {

	protected void startFragment(Fragment fragment) {
		((SideMenuListener) getActivity()).startFragment(fragment);
	}

	protected boolean toggleMenu() {
		return ((SideMenuListener) getActivity()).toggleMenu();
	}

}
