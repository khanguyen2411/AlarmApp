package com.example.alarmapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.alarmapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StopwatchFragment extends Fragment {
    Chronometer chronometer;
    FloatingActionButton stopButton, playPauseButton;
    //biến lưu trữ trạng thái có đang chạy đếm hay không
    boolean isPlaying = false;
    // lưu thời gian khi nhấn nút pause
    long timeWhenStopped = 0;

    public StopwatchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        chronometer = view.findViewById(R.id.chronometer);
        stopButton = view.findViewById(R.id.stopButton);
        playPauseButton = view.findViewById(R.id.playPauseButton);

        // đặt định dạng cho đồng hồ bấm giờ
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // nếu thời gian đếm đã được 1h (3.600.000 ms) thì format theo kiểu: 01:xx:xx ngược lại thì 00:xx:xx
                long elapsedMillis = SystemClock.elapsedRealtime() -chronometer.getBase();
                if(elapsedMillis > 3600000L){
                    chronometer.setFormat("0%s");
                }else{
                    chronometer.setFormat("00:%s");
                }
            }
        });

        // đặt sự kiện nút play/pause
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        // nếu đồng hồ đang không chạy
                if(!isPlaying){
                    // đặt thời gian đồng hồ tham chiếu đến = thời điểm bắt đầu chạy + thời gian dừng ở lần trước (nếu chưa dừng thì là bằng 0)
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    //bắt đầu chạy đếm
                    chronometer.start();
                    // đổi icon của button thành pause icon
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    // trạng thái đồng hồ đang chạy = true
                    isPlaying = true;
                } else {
                    // xử lý tạm dừng chạy
                    // thời điểm dừng = thời gian đồng hồ tham chiếu đến - thời điểm bắt đầu chạy
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    // dừng đồng hồ đếm
                    chronometer.stop();
                    // đổi cion button thành play icon
                    playPauseButton.setImageResource(R.drawable.ic_play);
                    // trạng thái đồng hồ đang chạy = false
                    isPlaying = false;
                }
            }
        });

        // đặt sự kiện cho nút stop
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dừng đồng hồ đếm
                chronometer.stop();
                // đặt lại thời gian đồng hồ tham chiếu đến là thời gian bắt đầu chạy
                chronometer.setBase(SystemClock.elapsedRealtime());
                // thời điểm dừng đặt về 0
                timeWhenStopped = 0;
                // đặt lại icon nút play về về play icon
                playPauseButton.setImageResource(R.drawable.ic_play);
                // trạng thái đồng hồ đang chạy trở về false
                isPlaying = false;
            }
        });

        return view;
    }
}