/**
 * 
 */
package com.example.kuvalista;



import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;




public class SplashFragment extends Fragment {

	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		 
		 final View view = inflater.inflate(R.layout.activity_splash, container, false);
	        
	        return view;
	      
	    }

}
