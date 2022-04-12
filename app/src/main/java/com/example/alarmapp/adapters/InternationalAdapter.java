package com.example.alarmapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmapp.R;

import java.util.List;

public class InternationalAdapter extends RecyclerView.Adapter<InternationalAdapter.InternationalViewHolder> {

    //danh sách các địa điểm và múi giờ tương ứng
    List<String> locations;
    List<String> timezones;

    //phương thức khởi tạo adapter
    public InternationalAdapter(List<String> locations, List<String> timezones) {
        this.locations = locations;
        this.timezones = timezones;
    }

    //phương thức tạo viewholder
    @NonNull
    @Override
    public InternationalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view chứa layout của một item trong RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.internation_time_item, parent, false);
        //trả về InternationalViewHolder với tham số truyền vào là view ở trên
        return new InternationalViewHolder(view);
    }

    //đổ dữ liệu cho các view
    @Override
    public void onBindViewHolder(@NonNull InternationalViewHolder holder, int position) {
        //set timezone cho textClock
        holder.textClock.setTimeZone(timezones.get(position));
        //set text cho tvLocation
        holder.tvLocation.setText(locations.get(position));
    }

    //lấy ra số lượng item trong recycler view, toán tử 3 ngôi: nếu timezones hoặc locations có kích
    //thước là 0 thì trả về 0, ngược lại trả về kích thước của timezone
    @Override
    public int getItemCount() {
        return (timezones.size() == 0 || locations.size() == 0) ? 0 : timezones.size();
    }

    //ViewHolder của item view (ở đây là giờ quốc tế), kế thừa vừa ViewHolder của RecyclerView
    public static class InternationalViewHolder extends RecyclerView.ViewHolder {
        //khai báo textview hiển thị vị trí và textclock hiển thị thời gian
        public TextView tvLocation;
        public TextClock textClock;

        //phương thức khởi tạo InternationalViewHolder, tham số truyền vào là 1 view
        public InternationalViewHolder(@NonNull View itemView) {
            super(itemView);
            //ánh xạ các thành phần qua itemview truyền vào
            tvLocation = itemView.findViewById(R.id.tvLocation);
            textClock = itemView.findViewById(R.id.textClock);
        }
    }
}
