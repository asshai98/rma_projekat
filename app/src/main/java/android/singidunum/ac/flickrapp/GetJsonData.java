package android.singidunum.ac.flickrapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {

    //dobijamo callbackove iz getraw data kada je skidanje podataka sa urla zavrseno i kada su oni spremni za parsiranje

    private List<Photo> photoList = null;
    private String baseURL;
    private String language;
    private boolean mathAll;

    private final OnDataAvilable callBack;
    private boolean runningOnSameThread = false;


    //preko njega salje callback Gallery fragmentu kako bi mogao da izrenderuje dohvacene podatke
   interface  OnDataAvilable {
        void onDataAvilable(List<Photo> data, DownloadStatus status);
    }

    public GetJsonData(String baseURL, String language, boolean mathAll, OnDataAvilable callBack) {
        this.baseURL = baseURL;
        this.language = language;
        this.mathAll = mathAll;
        this.callBack = callBack;
    }

   @Override
    protected void onPostExecute(List<Photo> photos) {
        
        if(callBack != null){
            callBack.onDataAvilable(photoList, DownloadStatus.OK);
        }

    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        String destinationUri = createUri(params[0], language, mathAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return photoList;
    }

    private String createUri(String searchCriteria, String lang, boolean mathAll){
        //dobijamo builder na osnovu buildUpon metoda nad kojim dodajemo uri parametre
        //querryparamter uvek vraca ispravan uri i posle svakog parametra dodaje sledeci u nizu i validira & ili ? znakove
        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", mathAll ? "ALL" : "ANY" )
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();

    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if(status == DownloadStatus.OK){
            photoList = new ArrayList<>();
            //parsiranje podataka, izbacuje json exeption, java.org.json

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i=0; i<itemsArray.length(); i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");
                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    String description = jsonPhoto.getString("description");

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl, description);
                    photoList.add(photoObject);

                }
            } catch (JSONException jsone){
                jsone.printStackTrace();
                status = DownloadStatus.FAILED_OR_EMPTY;
            }

        }

        if(runningOnSameThread && callBack != null){
            //informisemo caller klasu da je proces zavrsen
            callBack.onDataAvilable(photoList, status);

        }
    }
}
