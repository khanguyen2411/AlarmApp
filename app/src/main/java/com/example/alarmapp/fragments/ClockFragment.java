package com.example.alarmapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmapp.R;
import com.example.alarmapp.adapters.InternationalAdapter;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClockFragment extends Fragment {
    //textview giúp hiển thị ngày tháng hiện tại
    TextView tvCurrentDate;
    //list string lưu trữ các địa điểm
    List<String> locations = Arrays.asList("London", "New York", "Tokyo", "Amsterdam");
    //list string lưu trữ các múi giờ
    List<String> timezones = Arrays.asList("Europe/London", "America/New_York", "Asia/Tokyo", "Europe/Amsterdam");
    //recycler view hiển thị giờ quốc tế
    RecyclerView rcvInternationTime;
    //IntentFilter: Intent Filter là thành phần giúp cho hệ thống Android biết được ứng dụng của bạn
    // có thể làm được những gì. Activity, Service và BroadCast Receiver sử dụng Intent Filter để
    // thông báo cho hệ thống biết các dạng Implicit Intent mà nó có thể xử lý. Nói cách khác,
    // Intent Filter là bộ lọc Intent, chỉ cho những Intent phù hợp đi qua nó.
    private IntentFilter filter;
    //BroadcastReceiver: xử lý sự kiện khi có intent từ hệ thống
    private BroadcastReceiver listener;

    //phương thức khởi tạo của fragment (bỏ đi cũng đc thì phải)
    public ClockFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //view chứa toàn bộ layout của fragment
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        //ánh xạ các view
        tvCurrentDate = view.findViewById(R.id.current_date);
        rcvInternationTime = view.findViewById(R.id.rcvInternationTime);

        //khởi tạo adapter cho giờ quốc tế, tham số là danh sách địa điểm và múi giờ
        InternationalAdapter internationalAdapter = new InternationalAdapter(locations, timezones);

        //LinearLayoutManager cho RecyclerView - mặc định là theo chiều dọc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //thiết lập adapter cho RecyclerView
        rcvInternationTime.setAdapter(internationalAdapter);
        //thiest lập layout manager
        rcvInternationTime.setLayoutManager(linearLayoutManager);

        //set ngày hiện tại
        //lấy thông tin này hiện tại set cho tvCurrentDate
        tvCurrentDate.setText(DateFormat.getDateInstance().format(new Date()));

        //setup BroadcastReceiver để thay đổi ngày khi có ngày của hệ thống thay đổi
        //khởi tạo intentFilter
        filter = new IntentFilter();
        //thêm action cho internFilter là khi ngày của hệ thống thay đổi
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        //listener xử lý khi có intent được gửi từ hệ thống
        listener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String ct = DateFormat.getDateInstance().format(new Date());
                tvCurrentDate.setText(ct);
            }
        };

        //trả về view của fragment
        return view;
    }

    // đăng kí listener với intentFilter
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager
                .getInstance(Objects
                        .requireNonNull(getContext())).registerReceiver(listener, filter);
    }

    // Hủy đăng kí listener
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(Objects
                        .requireNonNull(getContext())).unregisterReceiver(listener);
    }
}