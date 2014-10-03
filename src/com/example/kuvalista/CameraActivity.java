package com.example.kuvalista;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CameraActivity extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	private Button btnCapturePicture;	
	private GPSTracker gps;
	public boolean kuvaotettu;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "Kamera1";
	private Tiedostojenkasittely tk;
	private File chDir;
	private String latitjalonit;
	private View btntyhjays;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
		Button btnReturn1 = (Button) findViewById(R.id.btnback);
		btntyhjays = (Button) findViewById(R.id.btntyhjays);
		chDir = alustaCache();
		btnReturn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				paluu();
			}
		});
		btnCapturePicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// otetaan kuva
				captureImage();
			}
		});

		/**
		 * Välimuistin tyhjäys kuvineen ja paikkatietoineen sekä aikaleimoineen
		 * 
		 */
		btntyhjays.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tk = new Tiedostojenkasittely();
				boolean tyhjatty = false;
				tyhjatty = tk.tyhjaa(IMAGE_DIRECTORY_NAME, alustaCache());
				if (!tyhjatty) {
					Toast.makeText(getBaseContext(),
							"Tiedoston poistossa ongelmia", Toast.LENGTH_LONG)
							.show();
				}

				else {
					Toast.makeText(getBaseContext(),
							"Tiedostot poistettu onnistuneesti",
							Toast.LENGTH_LONG).show();

				}
			}

		});

		// Onko kamera saatavilla.
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Laitteessasi ei tue kameraa",
					Toast.LENGTH_LONG).show();
			// Suletaan ohjelma, jos ei kameraa saatailla.
			finish();
		}

	}

	/**
	 * Tarkistetaan onko kamera saatavilla
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// Laitteessa on kamera
			return true;
		} else {
			// Laitteessa ei ole kameraa
			return false;
		}
	}

	private void captureImage() {

		gps = new GPSTracker(CameraActivity.this);
		kuvaotettu = false;
		// Tarkistetaan onko GPS saatavilla
		if (gps.canGetLocation()) {
			// Haetaan palvelulta latit ja lonit
			double latitude = 0;
			double longitude = 0;
			StringBuffer bufferi = new StringBuffer();
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			bufferi.append(Double.toString(longitude));
			bufferi.append("-");
			bufferi.append(Double.toString(latitude));
			latitjalonit = bufferi.toString();
			boolean onjo = false;

			tk = new Tiedostojenkasittely();
			onjo = tk.tutkitanOnkoOlemassa(chDir, latitjalonit);
			if (onjo) {
				Toast.makeText(
						getBaseContext(),
						"Ei oteta kuvaa, koska GPS-tiedot samat ja aikaa kulunut alle 10 minuuttia",
						Toast.LENGTH_LONG).show();
				paluu();
				Log.d("VÄLITIETOJA", "Tsekattu tiedosto");

			}

			else {
				// Käynnistetään kamera, jonka jälkeen kontolli palautetaan
				// takaisin tähän luokkaan
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}

		}

		if (!gps.canGetLocation()) {
			// Ei saada paikkatietoja
			// GPS tai verkko ei saatavilla
			Toast.makeText(
					CameraActivity.this,
					"GPS-tietoja ei saatu. tarkista Gps-asetukset"
							+ latitjalonit, Toast.LENGTH_LONG).show();
			gps.showSettingsAlert();
		}

	}

	/**
	 * 
	 * Tätä metodia kutsutaan, kamerasta. Kuvanotto hyväksytty, peruttu tai
	 * epäonnistunut
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				Toast.makeText(this, "Kuva otettu ja tallennettu",
						Toast.LENGTH_LONG).show();
				kuvaOtettu();

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Perutaan...", Toast.LENGTH_LONG).show();

			}
		} else {
			Toast.makeText(this, "Kuvan otto ep�onnistui:" + data.getData(),
					Toast.LENGTH_LONG).show();

		}

	}

	private void kuvaOtettu() {
		tk = new Tiedostojenkasittely();
		chDir = getBaseContext().getCacheDir();
		// latitjalonit = getIntent().getStringExtra("ll");
		tk.tallennaGPS(latitjalonit, chDir);
		Toast.makeText(CameraActivity.this, "GPS-TIEDOT ladattu cacheen",
				Toast.LENGTH_LONG).show();
	}

	/** Polku kuvasta */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Luodaan polku, johon kuva tallennetaan */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);
		// Yleinen ja hyväksi havaittu paikka tallentaa. Löytyy SD-kortilta
		// Pictures ja sen alle
		// luodaan haluttu kansio. Tässä tapauksessa Kamera1

		// Kansio luodaan, jos ei ole olemassa
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "failed to create directory");
				return null;
			}
		}

		// Sitten varsinainen kuvatiedosto
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");

		} else {
			return null;
		}

		return mediaFile;
	}

	/*
	 * / Paluu laitettu metodin sisään
	 */
	private void paluu() {

		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);

	}

	/*
	 * Näitä käytetään put intent extroja varten, jotta kamerasta palattaessa
	 * saadaan käyttöön. Tässä ohjelmassa ei käyetä kuvan esikatselua, joten
	 * nämä urin osalta turhia. app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("file_uri", fileUri);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// uri saadaan takaisin
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	public File alustaCache() {

		return getBaseContext().getCacheDir();
	}

}
