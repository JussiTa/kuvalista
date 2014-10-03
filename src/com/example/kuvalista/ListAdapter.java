package com.example.kuvalista;
/**
 * Perusadapteri, jota lista k‰ytt‰‰ 
 * 
 * 
 */


import com.example.kuvalista.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class ListAdapter extends ArrayAdapter {
	private final Context context;
	private String[] list;	
	private Bitmap[] kuvat;

	
	

	@SuppressWarnings("unchecked")
	public ListAdapter(Context context, String[] list, Bitmap[] kuvat2) {

		super(context, R.layout.rowlayout, list);

		this.context = context;
		this.list = list;
		this.kuvat=kuvat2;
	}
		
	
	/*
	 * Alla n‰ytet‰‰n rivi kerrallaan listaan
	 */

	@SuppressWarnings("unchecked")
	public ListAdapter(Context context,
			String[] list) {
		super(context, R.layout.rowlayout, list);
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Asetetaan n‰kym‰‰n layoutin mukainen rivi sis‰lt‰en tekstin ja kuvan.
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.txt);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		if (list == null)
			textView.setText("Ei GPS-tietoja");
		else {
			textView.setText(list[position]);
			// Ladataan oletuskuva, jos ei kameran kuvia
			if (kuvat[position]==null)
				imageView.setImageResource(R.drawable.ic_launcher);
			else
				imageView.setImageBitmap(kuvat[position]);

		}

		return rowView;
	}

	public Context getContext() {
		return this.context;
	}

}
