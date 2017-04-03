package dhakre.uha.fr.trackspeed;
//to show the current speed of the selected car
//Assuming that the if I query for example car Name=i8 then the server replay is of the format json={"Brand":"BMW","Name":"i8","SpeedMax":"250","CurrentSpeed":"96"}

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ShowSpeed extends AppCompatActivity {
    Button bnenter,showDetail;
    EditText et_name;
    String name="i8";  // taking the i8 as default value
    String jsonstr,recievedjson;
    JSONObject revievedJson;
    private final String TAG = this.getClass().getSimpleName();
    private OkHttpClient client;
    private JSONObject startSpeed = new JSONObject();
    private JSONObject startSpeedCar = new JSONObject();
    private JSONObject stopSpeed = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_speed);
        initializeOkHttp();

        bnenter= (Button) findViewById(R.id.enter);
        showDetail= (Button) findViewById(R.id.showDetail);
        et_name= (EditText) findViewById(R.id.et_name);


        bnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=et_name.getText().toString();
                Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
            }
        });

        //initionalize json query
        initializeRequestObject(name);
        //start json query
        start(startSpeed);

        //json processing
        recievedjson=jsonstr;

        //send the recieved car current speed to other SlectOtherCar Activity
        showDetail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              Intent intent= new Intent(ShowSpeed.this,SelectOtherCar.class);
               intent.putExtra("RecievedJsonStr",recievedjson);
               startActivity(intent);

           }
       });
        //check if jsonstr has value
        if(jsonstr==null)
            Toast.makeText(getApplicationContext(),"json str is null", Toast.LENGTH_LONG);
        else
            Toast.makeText(getApplicationContext(),"json str has data=="+jsonstr, Toast.LENGTH_LONG).show();
    }
    //server intionalisation method
    private void initializeOkHttp(){
        client=new OkHttpClient();
    }

    private void initializeRequestObject(String name) {
        try {
            //User car startSocket
            startSpeed.put("Type", "start");
            startSpeed.put("UserToken", 42);
            startSpeedCar.put("Name", name);
            startSpeed.put("Payload", startSpeedCar);

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
        ShowSpeed.EchoWebSocketListener listener = new ShowSpeed.EchoWebSocketListener(jsonObject);
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        jsonstr=jsonstr+txt; // recieved json here
    }
    //server code
    private final class EchoWebSocketListener extends WebSocketListener {

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
            output("Receiving : " + text);
            //output( text);
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
