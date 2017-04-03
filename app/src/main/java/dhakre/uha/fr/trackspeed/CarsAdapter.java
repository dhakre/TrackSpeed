package dhakre.uha.fr.trackspeed;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CarsAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public CarsAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void add(Cars object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View raw;
        raw=convertView;
        CarsHolder carsHolder;
        if(raw==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            raw=layoutInflater.inflate(R.layout.raw_layout,parent,false);
            carsHolder=new CarsHolder();
            carsHolder.tx_brand= (TextView) raw.findViewById(R.id.car_brand);
            carsHolder.tx_name= (TextView) raw.findViewById(R.id.car_name);
            carsHolder.tx_speedmax= (TextView) raw.findViewById(R.id.car_speedmax);
            carsHolder.tx_currentspeed= (TextView) raw.findViewById(R.id.tx_currentSpeed);
            raw.setTag(carsHolder);
        }
        else{
            carsHolder= (CarsHolder) raw.getTag();
        }
        Cars car= (Cars) this.getItem(position);
        carsHolder.tx_brand.setText("Brand:"+car.getBrand());
        carsHolder.tx_name.setText("Name:"+car.getName());
        carsHolder.tx_speedmax.setText("Max Speed:"+car.getSpeedmax());
        carsHolder.tx_currentspeed.setText("Current speed:"+car.getCurrentSpeed());
        return raw;
    }

    //to hold the data
    static class CarsHolder{
        TextView tx_brand;
        TextView tx_name;
        TextView tx_speedmax;
        TextView tx_currentspeed;
    }
}
