package com.example.kuvalista;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Tiedostojenkasittely {

	private File temp;
	private String latitjalonit;
	private String timeStamp;
	private File chDir;
	private File mediaStorageDir;
	private static final String IMAGE_DIRECTORY_NAME = "Kamera1";


	public Tiedostojenkasittely() {
		// TODO Auto-generated constructor stub
	}


public boolean tutkitanOnkoOlemassa(File chDir, String latitjalonit){
	boolean onjo =false;
	String strLine = null;
	this.latitjalonit = latitjalonit;
	this.chDir =chDir;
	//T�m� tehd��n vain, jos on jo jotain ennest��n.
	if(chDir.listFiles()!=null && chDir.listFiles().length>0){			
		try {
			
			//Alustetaan lukijat ja luetaan viimeisin
			//*********************************************************************************************************//
			File[]lista;
			   lista = chDir.listFiles();
			//Aletaan lukemaan listan viimeist� vertailuun
			FileReader fReader = new FileReader(lista[lista.length-1]);
			BufferedReader bReader = new BufferedReader(fReader);
			StringBuilder stringBuilder =  new StringBuilder();
			String receiveString;
			//** Luetaan rivi *//*
			while ( (receiveString = bReader.readLine()) != null ) {	            		
				stringBuilder.append(receiveString);
        	}
			strLine = stringBuilder.toString();
			bReader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double erotus=10;			
	   StringBuilder builder = new StringBuilder();
	   //Jostain syyst� joka toinen tyhj��, joten k�ytet��n t�t� konstia	
	   for(int i=1;i<strLine.length();i++){
		   if(i % 2!=0)
			   builder.append(strLine.charAt(i));				
	   }			
	   strLine = builder.toString();
	   Log.d("STR",strLine);
	   // #############Otetaan my�s aikaleima vertailuun vanhasta tiedostosta ja verrataan sit� nykyhetkeen.##############
	   String katkaisija ="_";
	   String uusileima =  (new SimpleDateFormat("dd_HHmm",
				Locale.getDefault()).format(new Date()));
	   
	   String[] uusipaiva = uusileima.split(katkaisija);	   
	   File[]lista;	
	   lista = chDir.listFiles();		   	   
	   String[] vanhapaiva = new String[1];
	   String vanhaleima = null;	  
	   		   temp = lista[lista.length-1];
			   vanhaleima = temp.toString();	
			   Log.d("VANHALEIMA",vanhaleima);
			   Log.d("UUSILEIMA",uusileima);
			   char[] vanhal = vanhaleima.toCharArray();
			   int lkm=0;
			   int i=0;
			   StringBuilder sb = new StringBuilder();
			   while(lkm <5){
				   if(vanhal[i]=='/')
					   lkm++;
				   
				   if(lkm==5){
					   int k= i+8;
					   for(int j=i+1;j<k ;j++)
						   sb.append(vanhal[j]);
				   }
				   i++;
			   }
			   vanhaleima = sb.toString();
			   Log.d("VANHALEIMA",vanhaleima);
			   vanhapaiva[0] =vanhaleima.substring(0, 2);			   
			   
			   //Vertailu vain saman p�iv�n ajoille
			   Log.d(" VANHAPAIVA",vanhapaiva[0]);
			   Log.d(" UUSIPAIVA",uusipaiva[0]);
			   //Jos samoilla p�ivill�, niin katsotaan minuutut
			   if(uusipaiva[0].equals(vanhapaiva[0])){
				   String vertailuunuusi = uusileima.substring(3);
				   Log.d(" kellouusi",vertailuunuusi);
				   String vertailuunvanha = vanhaleima.substring(3, 7);
				   Log.d(" kellovanha",vertailuunvanha);
				   double leimavanha = Double.parseDouble(vertailuunvanha);				   
				   double leimauusi =  Double.parseDouble(vertailuunuusi);
				   erotus = leimauusi-leimavanha;
			   }
			   else
				   erotus =20;	 
	
		//Vain, jos on gps tiedot jo olemassa
		String str = String.valueOf((double) erotus);
		Log.d(" STRING",str);
		if(strLine!=null){
			//Jos GPS-tiedot olemassa sek� t�sm�� olemassa olevaan ja aikakin alle 10 min, niin palautetaan true
			if (strLine.equals(latitjalonit)&&erotus<10) {
				onjo =true;
				return onjo;
			
			}
		}				
      }		
	
	  return false;
	
	}
/*
 * Heitet��n temppiin uusi GPS-tiedosto
 * 
 */
public void tallennaGPS(String latitjalonit,File chDir) {
	// Luodaan uusi GPS-text-file
	  this.chDir = chDir;
	  this.latitjalonit = latitjalonit;
	  timeStamp = new SimpleDateFormat("dd_HHmm",
			Locale.getDefault()).format(new Date());				
		temp = new File(chDir.getPath() + File.separator
				 + timeStamp + ".txt");
		
		Log.d("AIKALEIMA",temp.toString()); 
	// T�ss� kameraa varten try-lohko
	try {	
		DataOutputStream out = new DataOutputStream(
				new FileOutputStream(temp.toString()));
		out.writeChars(latitjalonit);	
		out.close();
	} catch (IOException e) {
		Log.i("Data output", "I/O Error");
	  }
	
}
public boolean tyhjaa(String IMAGE_DIRECTORY_NAME, File chDir) {
	mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
	
	this.chDir =chDir;
	
	List<Boolean> bool = new ArrayList<Boolean>();
	File[] temp;
	List<File> fileList = new ArrayList<File>();
	List<File> listakuvat = new ArrayList<File>();
	//List<File> listagps = new ArrayList<File>();			
	
	//Hakee my�s kansiot, joten kikkaillaan ne pois
	temp = chDir.listFiles();
	
	for(int i=0; i<temp.length;i++)
		if(! temp[i].isDirectory())
			fileList.add(temp[i]);
	
	//poistetaan filet sijoitetaan tulos (true tai false)
	if (fileList.size()>0){
		for (File file: fileList) {						
			bool.add(file.delete());
		}
	}							
	
	
	//Listataan tiedoston, jos viite polkuun on olemassa ja postetaan filet sijoitetaan tulos (true tai false)
	//mediaStorageDir = new File(chDir.getPath()+"//Kamera");
	if(mediaStorageDir.exists())
	temp = mediaStorageDir.listFiles();
	if(temp!=null){
	for (int i = 0; i < temp.length; i++)
		listakuvat.add(temp[i]);	
		for (File file: listakuvat) {
			bool.add(file.delete());
		}
		
	}
	if(bool.contains(false))
		return false;
	
	else
		return true;
	
	
}


}

