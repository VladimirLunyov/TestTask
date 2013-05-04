package com.lunyov.sidemenu;

import android.support.v4.app.Fragment;

public interface SideMenuListener {
	public void startFragment(Fragment fragment);

	public boolean toggleMenu();
}