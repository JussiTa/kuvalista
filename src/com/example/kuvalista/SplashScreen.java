/**T�ss� luokassa kutsutaan fragment xml tiedostoa, josta viite sen omaan ragmentti-luokkaan, joka taas asettaa n�kyv�n
 * layoutin.
 * T�m� Aktiviteetti k�ynnist�� my�skin tuon mainAktiviteetin
 * http://www.verious.com/article/splash-screen-for-your-android-application-using-fragment-activity-rotation-working/
 * 
 * 
 * 
 */

package com.example.kuvalista;
import com.example.kuvalista.R;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


public class SplashScreen extends FragmentActivity {
 
    // Asetettu aika, jolloin alustuskuva n�kyy
    private static int SPLASH_TIME_OUT = 3000;   
	private MyStateSaver data;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);       
            this.data = (MyStateSaver) getLastCustomNonConfigurationInstance();
            if (this.data == null) {
                this.data = new MyStateSaver();

            }

            if (this.data.doInit) {
                doInit();

            }
            
            
        }
    protected void doInit() {

        this.data.doInit = false;
    
        new Handler().postDelayed(new Runnable() {
 
 
            @Override
            public void run() {
                // T�m� suoritetaan, kun asetettuaika ylitetty
                // k�ynnistet��n p��luokka
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    
    
    
    
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// N�yt�n kierron tunnistus 
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}
    
    
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return this.data;

    }
    
    
    private class MyStateSaver {
        public boolean doInit = true;

    }

    
}    
    
 
