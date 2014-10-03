package com.example.kuvalista;


import com.example.kuvalista.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button kameranappula;
	private Button listanappula;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

			listanappula = (Button) findViewById(R.id.Listaan);
			kameranappula = (Button) findViewById(R.id.Kamera);			
			Button exit = (Button) findViewById(R.id.Ulos);
			
			
			//p��st��n ohjelmasta pois
		    exit.setOnClickListener(new View.OnClickListener()
		    {
		        @Override
		        public void onClick(View v)
		        {
		            // TODO Auto-generated method stub
		            finish();
		            System.exit(0);
		        }
		    });

		
		kameranappula.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
			

				Intent intent = new Intent(getApplicationContext(),
						CameraActivity.class);

				startActivity(intent);

			}
		});	


		// K�ynnistet��n lista Activiteetti
		// ja laitetaan sinne tarvittavat kuvat la Lat Lon putextran kautta

		listanappula.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),
						ListViewActivity.class);

				startActivity(intent);

			}
		});		
		
	}
	
	
	
	public void AppExit()
	{

	    this.finish();
	    Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_HOME);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);

	}

	
	
	
	
	
	
	
	

}
