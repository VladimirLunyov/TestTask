package com.lunyov.sidemenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lunyov.yalantis.R;

public class TestFragment extends ContentFragment {
	
	private TextView	txtTitle;
	private TextView	txtBody;
	private TextView	txtAddress;
	private ImageView	imgPicture;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.test_fragment, container, true);
		
		txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtBody = (TextView) v.findViewById(R.id.txtBody);
		txtAddress = (TextView) v.findViewById(R.id.txtAddress);
		imgPicture = (ImageView) v.findViewById(R.id.imgPicture);
		
		
		Button toogle = (Button) v.findViewById(R.id.toggle);
		toogle.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				toggleMenu();
				txtTitle.setVisibility(View.INVISIBLE);
				txtBody.setVisibility(View.INVISIBLE);
				txtAddress.setVisibility(View.INVISIBLE);
				imgPicture.setVisibility(View.INVISIBLE);
			}
		});
		return v;
	}

}
