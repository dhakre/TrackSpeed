package dhakre.uha.fr.trackspeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CarsListView extends AppCompatActivity {

    private String jsonstr;
    private JSONArray jsonResultArray;
    private CarsAdapter carsAdapter;
    private ListView listView;
    private String jsonNew;
    private final String TAG = this.getClass().getSimpleName();
    Cars cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_list_view);
        listView = (ListView) findViewById(R.id.listview);
        carsAdapter = new CarsAdapter(this, R.layout.raw_layout);
        listView.setAdapter(carsAdapter);
        jsonstr = getIntent().getExtras().getString("jsonString");
        /*if(jsonstr==null)
            Toast.makeText(getApplicationContext(),"json str is null", Toast.LENGTH_LONG);
        else
            Toast.makeText(getApplicationContext(),jsonstr, Toast.LENGTH_LONG).show();*/
        String crappyPrefix = "Closing:4000/";
        if (jsonstr != null) {
            jsonNew = jsonstr.substring(0, jsonstr.length() - (crappyPrefix.length() + 4));
        }
        //Toast.makeText(getApplicationContext(), jsonNew, Toast.LENGTH_LONG).show();
        //json data retrival
        jsonResultArray = null;
        try {
            jsonResultArray = new JSONArray(jsonNew.replaceAll("Cars", ""));
            Log.d(TAG, jsonResultArray.toString());
            //no name given by the server to the json array
           //Toast.makeText(getApplicationContext(), "json array here", Toast.LENGTH_LONG).show();
            //Log.d(TAG, jsonNew);
//            jsonArray = jsonResultArray.getJSONArray("Cars");
            int count = 0;
            String brand, name, speedmax, currentspeed;
            while (count < jsonResultArray.length()) {
                JSONObject json = jsonResultArray.getJSONObject(count);
                brand = json.getString("Brand");
                name = json.getString("Name");
                speedmax = json.getString("SpeedMax");
                currentspeed = json.getString("CurrentSpeed");
                //pass data to class
                cars = new Cars(brand, name, speedmax, currentspeed);
                carsAdapter.add(cars);//pass data to adapters
                count++;
            }
            Log.d(TAG,count+"");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "json in exception", Toast.LENGTH_LONG).show();
        }
        //clickable list view
      AdapterView.OnItemClickListener clickAdapter= new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Intent intent=new Intent(CarsListView.this,ShowSpeed.class);
             intent.putExtra("ListData",String.valueOf(id));
             startActivity(intent);
         }
     };
     listView.setOnItemClickListener(clickAdapter);

    }
}
