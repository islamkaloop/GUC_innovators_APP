package eg.edu.guc.innovators;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    Battery bStatus;
    DriverState ds;
    ElectricCurrent ec;
    Pedestrian peds;
    Speed sp;
    Temperature t;
    Indicators ind;
    Rain des;
    Waiting w;
    SeatBelt sb;
    TrialDuration td;
    Pulse p;

    ArrayList<Car> cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cars = new ArrayList<>();
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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                callall();
            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second

    }

        public void callall(){
            Object[] my_data =new Object[14];
            my_data[0]=car;
            my_data[1]=peds.getNumberOfPeople();
            my_data[2]=sp.getSpeed();
            my_data[3]=ec.getCurrent();
            my_data[4]=sb.isOn();
            my_data[5]=ind.isLeft();
            my_data[6]=w.isWaiting();
            my_data[7]=ind.isRight();
            my_data[8]=td.getMinutes();
            my_data[9]=des.isRainy();
            my_data[10]=bStatus.getStatus();
            my_data[11]=ds.isNotFocused();
            my_data[12]=p.getPulseRate();
            my_data[13]=ds.isAwake();
            loadall(my_data);
        }

        public void loadall(Object[] data){
            createCars((ArrayList<Car>)data[0]);
            updatePedestriansNumber((int)data[1]);
            TextView speed = (TextView) findViewById(R.id.speed);
            TextView current = (TextView) findViewById(R.id.current);
            TextView seat = (TextView) findViewById(R.id.seat);
            TextView time = (TextView) findViewById(R.id.time);
            TextView battery = (TextView) findViewById(R.id.battery);
            TextView pulse = (TextView) findViewById(R.id.pulse);
            RelativeLayout left_ind = (RelativeLayout) findViewById(R.id.left_ind);
            RelativeLayout waiting = (RelativeLayout) findViewById(R.id.waiting);
            RelativeLayout right_ind = (RelativeLayout) findViewById(R.id.right_ind);
            RelativeLayout raining= (RelativeLayout) findViewById(R.id.raining);
            RelativeLayout focus = (RelativeLayout) findViewById(R.id.focus);
            RelativeLayout sleep = (RelativeLayout) findViewById(R.id.sleep);
            speed.setText(data[2]+" Km/H");
            current.setText(data[3]+" A");
            if((boolean)data[4])
                seat.setText("Connected");
            else
                seat.setText("Not Connected");
            if((boolean)data[5])
                left_ind.setBackgroundResource(R.drawable.left_yellow);
            else
                left_ind.setBackgroundResource(R.drawable.left_black);
            if((boolean)data[6])
                waiting.setBackgroundResource(R.drawable.waiting);
            else
                waiting.setBackgroundResource(R.drawable.not_waiting);
            if((boolean)data[7])
                right_ind.setBackgroundResource(R.drawable.right_yellow);
            else
                right_ind.setBackgroundResource(R.drawable.right_black);
            time.setText(data[8]+" Minute");
            if((boolean)data[9])
                raining.setBackgroundResource(R.drawable.raining);
            else
                raining.setBackgroundResource(R.drawable.not_raining);
            battery.setText(data[10]+"");
            if(!(boolean)data[11])
                focus.setBackgroundResource(R.drawable.focus);
            else
                focus.setBackgroundResource(R.drawable.not_focus);
            pulse.setText(data[12]+" Pulse/Mn");
            if(!(boolean)data[13])
                sleep.setBackgroundResource(R.drawable.sleep);
            else
                sleep.setBackgroundResource(R.drawable.not_sleep);
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

    protected void onStart(){
        super.onStart();
        battery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               bStatus = dataSnapshot.getValue(Battery.class);
                Log.d(TAG, "Value is: " + bStatus);
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
                for(DataSnapshot carsSnapshot : dataSnapshot.getChildren()){
                    Car c = dataSnapshot.getValue(Car.class);
                    cars.add(c);
                    Log.d(TAG, "Value is: " + c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        driverState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ds = dataSnapshot.getValue(DriverState.class);
                Log.d(TAG, "Value is: " + ds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        electricCurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ec = dataSnapshot.getValue(ElectricCurrent.class);
                Log.d(TAG, "Value is: " + ec);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        pedestrian.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                peds = dataSnapshot.getValue(Pedestrian.class);
                Log.d(TAG, "Value is: " + peds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        speed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sp = dataSnapshot.getValue(Speed.class);
                Log.d(TAG, "Value is: " + sp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                t = dataSnapshot.getValue(Temperature.class);
                Log.d(TAG, "Value is: " + t);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        indicators.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ind = dataSnapshot.getValue(Indicators.class);
                Log.d(TAG, "Value is: " + ind);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        rain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                des = dataSnapshot.getValue(Rain.class);
                Log.d(TAG, "Value is: " + des);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });
        waiting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                w = dataSnapshot.getValue(Waiting.class);
                Log.d(TAG, "Value is: " + w);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        seatbelt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sb = dataSnapshot.getValue(SeatBelt.class);
                Log.d(TAG, "Value is: " + sb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        duration.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                td = dataSnapshot.getValue(TrialDuration.class);
                Log.d(TAG, "Value is: " + td);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });

        pulseRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(Pulse.class);
                Log.d(TAG, "Value is: " + p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.",databaseError.toException());
            }
        });
    }

}
