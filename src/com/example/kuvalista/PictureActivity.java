package com.example.kuvalista;

/**
 * 
 * Luokka, joka hallitsee yksitt�ist� listan kohtaa
 */



import com.example.kuvalista.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureActivity extends Activity {
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		  //ekavaihe, kun luodaan list layout
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_picture);
	    //activity_listviewexampleactivity= t�m�n luokan layoyt tiedoston nimi
	    
	    //Haetaan listaluokassa laitettu ekstra, mik� on klikattu kohta.
	     
	    String sana= getIntent().getStringExtra("nimi");
	    Log.d("sana",sana);
	    String sana2= getIntent().getStringExtra("nimi2");
	    Bitmap kuva=getIntent().getParcelableExtra("kuva");	 
	    
	    if(kuva!=null)
	    kuva = suurennetaan(kuva);
		  
        
        TextView textView = (TextView) findViewById(R.id.secondLine);
        TextView textView2 = (TextView) findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) findViewById(R.id.icon);
        textView.setText(sana);
        textView2.setText(sana2);
        if(kuva!=null)
        imageView.setImageBitmap(kuva);
        else
        	imageView.setImageResource(R.drawable.ic_launcher);
        	
	    
	    Button btnReturn1 = (Button) findViewById(R.id.back);
	    
	    btnReturn1.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	            Intent i=new Intent(getApplicationContext(),  ListViewActivity.class);
	            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(i);
	        }
	    });
	    
	
	}
	/**
	 * Bitmapin suurennukseen
	 * @param kuva
	 * @return
	 */

	private Bitmap suurennetaan(Bitmap kuva) {		 
		
		int width = kuva.getWidth();
		int height = kuva.getHeight();
		
		float scaleWidth = ((float) kuva.getWidth()*2) / width;
		float scaleHeight = ((float) kuva.getHeight()*2) / height;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);			 
		
		Bitmap resizedBitmap	=	 Bitmap.createBitmap(kuva, 0, 0, width, height, matrix, false);					  
				
		return resizedBitmap;
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// N�yt�n kierron tunnistus ei toimi
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	

}
