package eg.edu.guc.innovators;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Innovators";

    DatabaseReference car;
    DatabaseReference driverState;
    DatabaseReference electricCurrent;
    DatabaseReference battery;
    DatabaseReference pedestrian;
    DatabaseReference speed;
    DatabaseReference temperature;
    DatabaseReference indicators;
    DatabaseReference rain;
    DatabaseReference seatbelt;
    DatabaseReference duration;
    DatabaseReference waiting;
    DatabaseReference pulseRate;

    ArrayList<Car> cars;
    TextView myNull ;
    ScrollView myContent;
    ProgressBar mProgressBar;

    TextView txtSpeed ;
    TextView current ;
    TextView seat  ;
    TextView time ;
    TextView txtBattery ;
    TextView pulse ;
    RelativeLayout left_ind ;
    RelativeLayout relWaiting ;
    RelativeLayout right_ind ;
    RelativeLayout raining;
    RelativeLayout focus ;
    RelativeLayout sleep ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("QueryUtils","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        battery = FirebaseDatabase.getInstance().getReference("battery");
        car = FirebaseDatabase.getInstance().getReference("car");
        driverState = FirebaseDatabase.getInstance().getReference("driverState");
        pedestrian = FirebaseDatabase.getInstance().getReference("pedestrian");
        speed = FirebaseDatabase.getInstance().getReference("speed");
        temperature = FirebaseDatabase.getInstance().getReference("temperature");
        electricCurrent = FirebaseDatabase.getInstance().getReference("electricCurrent");
        indicators = FirebaseDatabase.getInstance().getReference("indicators");
        rain = FirebaseDatabase.getInstance().getReference("rain");
        seatbelt = FirebaseDatabase.getInstance().getReference("seatbelt");
        duration = FirebaseDatabase.getInstance().getReference("duration");
        waiting = FirebaseDatabase.getInstance().getReference("waiting");
        pulseRate = FirebaseDatabase.getInstance().getReference("pulseRate");

        txtSpeed = (TextView) findViewById(R.id.speed);
        current = (TextView) findViewById(R.id.current);
        seat = (TextView) findViewById(R.id.seat);
        time = (TextView) findViewById(R.id.time);
        txtBattery = (TextView) findViewById(R.id.battery);
        pulse = (TextView) findViewById(R.id.pulse);
        left_ind = (RelativeLayout) findViewById(R.id.left_ind);
        relWaiting = (RelativeLayout) findViewById(R.id.waiting);
        right_ind = (RelativeLayout) findViewById(R.id.right_ind);
        raining= (RelativeLayout) findViewById(R.id.raining);
        focus = (RelativeLayout) findViewById(R.id.focus);
        sleep = (RelativeLayout) findViewById(R.id.sleep);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        cars = new ArrayList<>();
        myContent = (ScrollView) findViewById(R.id.MyContent);
        myNull =(TextView)findViewById(R.id.textView);
        mProgressBar =(ProgressBar)findViewById(R.id.progressBar);

        myNull.setVisibility(View.GONE);
        myContent.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        ConnectivityManager connMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =connMgr.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()) {
            onStartApp();
            Log.v("QueryUtils", "initLoader");
            myNull.setVisibility(View.GONE);
            myContent.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }else{
            myNull.setText("no internet connection");
            myNull.setVisibility(View.VISIBLE);
            myContent.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }

    }
        public void updatePedestriansNumber(int i){
            TextView L = (TextView) findViewById(R.id.pedestrians);
            L.setText("");
            String s="";
            for(int c=0;c<i;c++){
                s+="(^_^)  ";
            }
            L.setText(s);
        }

        public void createCars( ArrayList<Car> carItem){
            TextView carNumber= (TextView) findViewById(R.id.car);
            carNumber.setText(carItem.size()+"");
            CarAdapter car =new CarAdapter(this,new ArrayList<Car>() );
            car.addAll(carItem);
            ListView L = (ListView) findViewById(R.id.car_item);
            L.setAdapter(car);
            setListViewHeightBasedOnChildren(L);
        }
        public static void setListViewHeightBasedOnChildren(ListView listView) {

            ListAdapter listAdapter = listView.getAdapter();

            if (listAdapter == null) return;

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                    View.MeasureSpec.UNSPECIFIED);

            int totalHeight = 0;
            View view = null;

            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);

                if (i == 0) view.setLayoutParams(new
                        ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();

            params.height = totalHeight + (listView.getDividerHeight() *
                    (listAdapter.getCount() - 1));

            listView.setLayoutParams(params);
            listView.requestLayout();
        }

    protected void onStartApp(){
        Log.v(TAG,"on start");
        super.onStart();
        battery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "battery");
               String bStatus = dataSnapshot.child("battery0").getValue(Battery.class).getStatus();
                Log.d(TAG, "Value is: " + bStatus);
                txtBattery.setText(bStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        car.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cars.clear();
                int i =1;
                for(DataSnapshot carsSnapshot : dataSnapshot.getChildren()){
                    Car c = carsSnapshot.getValue(Car.class);
                    c.setId_number(i);
                    cars.add(c);
                    Log.d(TAG, "Value is: " + c);
                    i++;
                }
                createCars(cars);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        driverState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean awake = dataSnapshot.child("DriverState0").getValue(DriverState.class).isAwake();
                boolean isfocus = dataSnapshot.child("DriverState0").getValue(DriverState.class).isNotFocused();
                Log.d(TAG, "Value is: " + awake +" "+isfocus );
                if(!isfocus)
                    focus.setBackgroundResource(R.drawable.focus);
                else
                    focus.setBackgroundResource(R.drawable.not_focus);
                if(!awake)
                    sleep.setBackgroundResource(R.drawable.sleep);
                else
                    sleep.setBackgroundResource(R.drawable.not_sleep);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        electricCurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double ec = dataSnapshot.child("ElectricCurrent0").getValue(ElectricCurrent.class).getCurrent();
                Log.d(TAG, "Value is: " + ec);
                current.setText(ec+" A");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        pedestrian.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int peds = dataSnapshot.child("Pedestrian").getValue(Pedestrian.class).getNumberOfPeople();
                Log.d(TAG, "Value is: " + peds);
                updatePedestriansNumber(peds);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        speed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sp = dataSnapshot.child("Speed").getValue(Speed.class).getSpeed();
                Log.d(TAG, "Value is: " + sp);
                txtSpeed.setText(sp+" Km/H");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        indicators.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean indLeft = dataSnapshot.child("Indicators").getValue(Indicators.class).isLeft();
                boolean indRight = dataSnapshot.child("Indicators").getValue(Indicators.class).isRight();
                Log.d(TAG, "Value is: " + indLeft+" "+indRight);
                if(indLeft)
                    left_ind.setBackgroundResource(R.drawable.left_yellow);
                else
                    left_ind.setBackgroundResource(R.drawable.left_black);
                if(indRight)
                    right_ind.setBackgroundResource(R.drawable.right_yellow);
                else
                    right_ind.setBackgroundResource(R.drawable.right_black);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        rain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean des = dataSnapshot.child("Rain0").getValue(Rain.class).isRainy();
                Log.d(TAG, "Value is: " + des);
                if(des)
                    raining.setBackgroundResource(R.drawable.raining);
                else
                    raining.setBackgroundResource(R.drawable.not_raining);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });
        waiting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean w = dataSnapshot.child("Waiting0").getValue(Waiting.class).isWaiting();
                Log.d(TAG, "Value is: " + w);
                if(w)
                    relWaiting.setBackgroundResource(R.drawable.waiting);
                else
                    relWaiting.setBackgroundResource(R.drawable.not_waiting);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        seatbelt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean sb = dataSnapshot.child("SeatBelt0").getValue(SeatBelt.class).isOn();
                Log.d(TAG, "Value is: " + sb);
                if(sb)
                    seat.setText("Connected");
                else
                    seat.setText("Not Connected");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        duration.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double td = dataSnapshot.child("TrialDuration0").getValue(TrialDuration.class).getMinutes();
                Log.d(TAG, "Value is: " + td);
                time.setText(td+" Minute");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        pulseRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int p = dataSnapshot.child("Pulse0").getValue(Pulse.class).getPulseRate();
                Log.d(TAG, "Value is: " + p);
                pulse.setText(p+" Pulse/Mn");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        Log.v(TAG,"end of start");

    }

}
