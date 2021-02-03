package andro.jf.androclassloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


// La méthode Download FileTask doit être asynchrone 
// car sinon le téléchargement gèle l'UI thread
public class LoadAsyncTask extends AsyncTask<Object, Void, Context> {
	
	// La fonction doInBackground exécute le téléchargement
	@Override
	protected Context doInBackground(Object... params) {
		Log.i("AsyncTask", "starting asynctack");
		String DownloadUrl = (String) params[0];
		String path = (String) params[1];
		Context context = (Context) params[3];
		
		DownloadFromUrl(DownloadUrl, path);
		return context;
    }
	
	// La métode suivante s'éxecute à la fin de la fonction doInBackground(téléchargement)
	// Elle prend en paramètre le contexte renvoyé par la méthode doInBackground
	@Override
	protected void onPostExecute(Context result) 
	{
		Log.i("AsyncTask", "finishing asynctack");
		MainActivity.loadExternalClass(result);
	}
	
	// Methode qui permet de télécharger un fichier filename à l'url DownloadUrl
	public void DownloadFromUrl(String DownloadUrl, String path) {
	//public Void DownloadFromUrl(String DownloadUrl, String path) {
		try {
			
			URL url = new URL(DownloadUrl); //you can write here any link
			Log.i("JFL", "Connecting to " + DownloadUrl);
	       	File file = new File(path);
			//File file = new File(path, name);
	       	long startTime = System.currentTimeMillis();
	       	Log.i("DownloadManager", "download begining");
	       	Log.i("DownloadManager", "download url:" + url);
	       	Log.i("DownloadManager", "downloaded file name:" + file);

	       	/* Open a connection to that URL. */
	       	URLConnection ucon = url.openConnection();
	       	InputStream is = ucon.getInputStream();
	       	BufferedInputStream bis = new BufferedInputStream(is);
	       	/*
	        * Read bytes to the Buffer until there is nothing more to read(-1).
	        */
	       	ByteArrayBuffer baf = new ByteArrayBuffer(5000);
	       	int current = 0;
	       	while ((current = bis.read()) != -1) {
	       		baf.append((byte) current);
	       	}

	       	/* Convert the Bytes read to a String. */
	       	FileOutputStream fos = new FileOutputStream(file);
	       	fos.write(baf.toByteArray());
	       	fos.flush();
	       	fos.close();
	       	Log.i("DownloadManager", "download ready in " + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
   	
		} catch (IOException e) {
			Log.i("DownloadManager", "Error: " + e);
			e.printStackTrace();
	   	}
	}
}