package dhakre.uha.fr.trackspeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    //private TextView outputText;
    private OkHttpClient client;
    private JSONObject userAuth = new JSONObject();
    private JSONObject startSpeed = new JSONObject();
    private JSONObject startSpeedCar = new JSONObject();
    private JSONObject stopSpeed = new JSONObject();
    Button carsList,currentSpeed;
    String jsonstr="Cars";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeOkHttp();
        initializeRequestObject();
        carsList=(Button) findViewById(R.id.cars);
        currentSpeed=(Button) findViewById(R.id.enter);

        start(userAuth);
        //for seeing the list of cars
        carsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CarsListView.class);
                intent.putExtra("jsonString",jsonstr);
                startActivity(intent);
            }
        });
        // for checking the current speed of the a selected car
        currentSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // start(startSpeed);
                Intent intent=new Intent(MainActivity.this,ShowSpeed.class);
                intent.putExtra("jsonString",jsonstr);
                startActivity(intent);
            }
        });
    }

    private void initializeOkHttp(){
        client=new OkHttpClient();
    }

    private void initializeRequestObject() {
        try {
            //User auth token
            userAuth.put("Type", "infos");
            userAuth.put("UserToken", 42);
            //User car startSocket
            startSpeed.put("Type", "start");
            startSpeed.put("UserToken", 42);
            startSpeedCar.put("Name", "911");
            startSpeed.put("Payload", startSpeedCar);
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
        EchoWebSocketListener listener = new EchoWebSocketListener(jsonObject);
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
       /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputText.setText(outputText.getText().toString() + "\n\n" + txt);
            }
        });*/
       jsonstr=jsonstr+txt;
    }

    public  final class EchoWebSocketListener extends WebSocketListener {

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
            output( text);
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
