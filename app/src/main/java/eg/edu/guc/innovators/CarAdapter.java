package eg.edu.guc.innovators;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CarAdapter extends ArrayAdapter {
    public CarAdapter(Context context,ArrayList<Car> CarItems) {
        super(context,0,CarItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Car myCar = (Car) getItem(position);
        View ViewList = convertView;
        if(ViewList==null){
            ViewList=LayoutInflater.from(getContext()).inflate(R.layout.car_list_item,parent,false);
        }
        TextView id=(TextView) ViewList.findViewById(R.id.car_id);
        TextView destance=(TextView)ViewList.findViewById(R.id.destance);
        TextView velocity=(TextView)ViewList.findViewById(R.id.velocity);
        id.setText(myCar.getId_number()+"");
        destance.setText(myCar.getDistance()+"");
        velocity.setText(myCar.getVelocity()+"");
        return ViewList;
    }

}
