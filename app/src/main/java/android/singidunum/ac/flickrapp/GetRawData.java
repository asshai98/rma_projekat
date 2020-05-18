package android.singidunum.ac.flickrapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


enum DownloadStatus {IDLE, PROCESSING, NOT_INITALISED, FAILED_OR_EMPTY, OK} //status skidanja, globalna promenljiva dostupna svim klasama koje implementiraju interfejs onDownloadComplete, kako bi dobile status
                                                                            //get metoda

class GetRawData extends AsyncTask<String, Void, String> {

    //klasa koja ce u pozadinskoj niti skinuti sirove podatke u izvornom formatu sa navedenog urla-a
    //ne brine o formatu podatka, niti parsiranju, niti sa kog urla se konkretno skidaju podaci


    private static final String TAG = "GetRawData";
    private DownloadStatus downloadStatus;
    private final OnDownloadComplete callBack;


    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callBack) {
        this.downloadStatus = DownloadStatus.IDLE; //inicijalni status skidanja
        this.callBack = callBack;
    }

    void runInSameThread(String s){
        if(callBack != null){
            callBack.onDownloadComplete(doInBackground(s), downloadStatus);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(callBack != null){
            callBack.onDownloadComplete(s, downloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        //proveravamo da li je dodeljen uopste dodeljen bilo kakav url metodu
        if(strings == null){
            downloadStatus = DownloadStatus.NOT_INITALISED;
            return null;
        }

       try{
            downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]); //pravimo url preko string parametra

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); //get http request koristimo
            connection.connect(); //otvaramo konekciju

           StringBuilder result = new StringBuilder();

           reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //ucitavamo podatke sa inputstreama

           for(String line = reader.readLine(); line != null; line = reader.readLine()){ //linija po liniju bez novog reda
               result.append(line).append("\n"); //dodajemo novi red na liniju teksta
           }
           downloadStatus = DownloadStatus.OK;
           return result.toString();

        //obradjujemo sve potencijalne izuztke koji se mogu javiti i prikazujemo status

       } catch (MalformedURLException e){ //proveravamo dali je URL validan
           Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage()); //kompajlira se tokom runtime-a
       } catch (IOException e){
           Log.e(TAG, "doInBackground: IO Exeption reading data " + e.getMessage());
       } catch (SecurityException e){ //izuzetak koji se izbacuje ukoliko nemamo permisiju da pristupimo internetu
           Log.e(TAG, "doInBackground: Security exeption. Needs permission? " + e.getMessage());
       } finally { //zagarantovano se izvrsava bez obzira da li bacen izuzetak ili ne
           if(connection != null){
               connection.disconnect(); //ukoliko je sve dobro proslo zatvaramo konekciju
           }
           if(reader != null){
               try {
                   reader.close();
               } catch (IOException e) {
                   Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage() );
               }
           }
       }
       downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
       return null;
    }


}
