package dhakre.uha.fr.trackspeed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class SelectOtherCar extends AppCompatActivity {
    String jsonstr,jsonString;
    String brand,name,spmax,currspd;
    Button othercar;
    TextView tx_brand,tx_name,tx_speedmax,tx_currspeed;
    private final String TAG = this.getClass().getSimpleName();
    private OkHttpClient client;
    private JSONObject stopSpeed = new JSONObject();
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_other_car);
        othercar= (Button) findViewById(R.id.othercar);
        tx_brand= (TextView) findViewById(R.id.tx_Brand);
        tx_name= (TextView) findViewById(R.id.tx_name);
        tx_speedmax= (TextView) findViewById(R.id.tx_speeedmax);
        tx_currspeed= (TextView) findViewById(R.id.tx_currentSpeed);

        jsonString=getIntent().getExtras().getString("RecievedJsonStr");
        try {
            //json=new JSONObject(jsonString.replaceAll("Cars"," "));
            JSONArray jsonResultArray = new JSONArray(jsonString.replaceAll("Cars", ""));
            int i=0;
            while (i<jsonResultArray.length()) {
                json=jsonResultArray.getJSONObject(i);
                brand = json.getString("Brand");
                name = json.getString("Name");
                spmax = json.getString("SpeedMax");
                currspd = json.getString("CurrentSpeed");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),jsonstr, Toast.LENGTH_LONG).show();
        }
        tx_brand.setText(brand);
        tx_name.setText(name);
        tx_speedmax.setText(spmax);
        tx_currspeed.setText(currspd);

        initializeOkHttp();
        initializeRequestObject();

        othercar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // start(stopSpeed);//send the stop json request to server
                Intent intent=new Intent(SelectOtherCar.this,CarsListView.class);
                startActivity(intent);
            }
        });
    }

    //server code
    private void initializeOkHttp() {
        client = new OkHttpClient();
    }

    private void initializeRequestObject() {
        try {
            //User car stop
            stopSpeed.put("Type", "stop");
            stopSpeed.put("UserToken", 42);

        } catch (@NonNull JSONException exception) {
            if (exception.getMessage() != null) {
                Log.d(TAG, exception.getMessage());
            } else {
                Log.d(TAG, "JSON Error");
            }
        }
    }

    private void start(@NonNull JSONObject jsonObject) {
        Request request = new Request.Builder().url("wss://pbbouachour.fr/openSocket").build();
        SelectOtherCar.EchoWebSocketListener listener = new SelectOtherCar.EchoWebSocketListener(jsonObject);
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        jsonstr = jsonstr + txt;
    }

    public final class EchoWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 4000;
        private JSONObject jsonObject;

        public EchoWebSocketListener(@NonNull JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            webSocket.send(jsonObject.toString());
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, String text) {
            //output("Receiving : " + text);
            output(text);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
}
