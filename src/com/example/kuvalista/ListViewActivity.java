package com.example.kuvalista;

/**
 * Normaali listView t�ss� on
 * lis�n� vain tiedostosta luku. 
 * 
 * 
 */
//http://jmsliu.com/1431/download-images-by-asynctask-in-listview-android-example.html
//http://www.youtube.com/watch?v=WRANgDgM2Zg

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.kuvalista.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.view.*;

public class ListViewActivity extends Activity {

	public static final int MEDIA_TYPE_IMAGE = 1;
	private String[] texttaulukko;
	private File[] imageList;
	private Bitmap[] kuvat;
	private File[] tempList;
	private List<File> fileList = new ArrayList<File>();
	private File mediaStorageDir;
	private String IMAGE_DIRECTORY_NAME = "Kamera1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listviewexampleactivity);

		/** Haetaan cache hakemisto (Androidin oletus) */
		File chDir = getBaseContext().getCacheDir();

		// Lisätään listaan kaikki filet hakemistosta hakee myös kansiot, joten
		// tehtävä vähän lisätyötä
		tempList = chDir.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (!tempList[i].isDirectory())
				fileList.add(tempList[i]);
		}

		texttaulukko = new String[fileList.size()];

		// Jos gps tiedostoja löytyy
		if (fileList.size() > 0) {
			int i = 0;
			for (File item : fileList) {
				try {
					FileReader fReader = new FileReader(item);
					BufferedReader bReader = new BufferedReader(fReader);

					/** Luetaan rivi taulukon indeksiin */

					texttaulukko[i] = bReader.readLine();
					Log.d("STRLINE", texttaulukko[i]);

					bReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				i++;
			}

		}

		// Tehdään lista nakyma layoutin pohjalta
		final ListView listview = (ListView) findViewById(R.id.listview);

		// Menn��n t�h�n, jos tempList ei tyhj�
		if (fileList.size() > 0) {
			BitmapFactory.Options options = new BitmapFactory.Options();

			// Kuva pienenetään riittävästi
			options.inSampleSize = 8;
			// Luodaan olio viittammaan hakemistoon
			mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					IMAGE_DIRECTORY_NAME);

			// haetaan kuvat taulukkoon
			imageList = mediaStorageDir.listFiles();
			kuvat = new Bitmap[imageList.length];
			// K�yd��n yksitellen l�pi ja muutetaan bitmapiksi
			if (imageList != null && imageList.length > 0)
				for (int i = 0; i < imageList.length; i++) {
					Log.d("Image: " + i + ": path",
							imageList[i].getAbsolutePath());
					Bitmap b = BitmapFactory.decodeFile(
							imageList[i].getAbsolutePath(), options);
					kuvat[i] = b;
				}

			final ListAdapter adapter = new ListAdapter(this, texttaulukko,
					kuvat);
			listview.setAdapter(adapter);

		}
		// Muutoin käytetään korvaavaa taulukkoa
		else {
			String[] korvaava = { "ei ole dataa" };
			final ListAdapter adapter = new ListAdapter(this, korvaava);
			listview.setAdapter(adapter);
		}

		// Tämä kohta kuuntelee painallusta halutussa listan kohdassa ja toimii
		// avaamalla uuden aktiviteetin
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String kuvannimi;
				kuvannimi = imageList[position].toString();
				kuvannimi = hoidaKatakaisu(kuvannimi);

				Intent intent = new Intent(getApplicationContext(),
						PictureActivity.class);
				if (texttaulukko.length > 0) {
					intent.putExtra("nimi", texttaulukko[position]);
					if (kuvat[position] != null) {
						intent.putExtra("nimi2", kuvannimi);
						intent.putExtra("kuva", kuvat[position]);
					}
				} else {

					intent.putExtra("nimi", "Ei dataa");
					intent.putExtra("nimi2", "Ei dataa");

				}
				startActivity(intent);
			}

			/**
			 * Tässä kuvatiedoston nimi muokataan niin, että on vvvvkkdd_hh:mm
			 * 
			 * @param kuvannimi
			 * @return katkaistu tiedoston nimi
			 */
			private String hoidaKatakaisu(String kuvannimi) {
				char[] vanhal = kuvannimi.toCharArray();
				int lkm = 0;
				int i = 0;
				StringBuilder sb = new StringBuilder();
				while (lkm < 5) {
					if (vanhal[i] == '/')
						lkm++;

					if (lkm == 5) {
						int k = i + 18;
						for (int j = i + 5; j < k; j++) {
							sb.append(vanhal[j]);
							if (j == i + 15)
								sb.append(":");
						}
					}
					i++;
				}
				return sb.toString();
			}
		});

		// Paluu- nappula MainActivityyn
		Button btnReturn1 = (Button) findViewById(R.id.takaisin);
		btnReturn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Näytetään kierron tunnistus ei toimi
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

}