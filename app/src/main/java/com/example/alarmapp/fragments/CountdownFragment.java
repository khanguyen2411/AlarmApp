package com.example.alarmapp.fragments;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.util.Calendar;
import java.util.Objects;


public class CountdownFragment extends Fragment {
    Chronometer chronometer;
    Button btnPickTime;
    int hour, minutePicked, secondPicked;
    long time;
    TextView tvCountdown;
    FloatingActionButton stopButton;
    CountDownTimer countDownTimer;

    public CountdownFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);
        chronometer = view.findViewById(R.id.chronometer);
        btnPickTime = view.findViewById(R.id.btnPickTime);
        tvCountdown = view.findViewById(R.id.tvCountDown);
        stopButton = view.findViewById(R.id.stopButton);

        // gán sự kiện cho nút đặt giờ
        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //khởi tạo dialog để pick time
                MyTimePickerDialog myTimePickerDialog = new MyTimePickerDialog(Objects.requireNonNull(getActivity()), new MyTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                        //lấy ra các giác trị giờ, phút, giây
                        hour = hourOfDay;
                        minutePicked = minute;
                        secondPicked = seconds;
                        //chuyển đổi sang ms
                        time = convertToMs(hour, minutePicked, secondPicked);

                        //sử dụng countdowntimer để đếm ngược
                        countDownTimer = new CountDownTimer(time, 1000 ) {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onTick(long l) {
                                //tính toán các giá trị giờ, phút, giây còn lại mỗi 1000ms
                                int h = (int) (l / 3600000);
                                int m = (int) ((int) (l % 3600000)/ 60000L);
                                int s = (int) (((l % 3600000)% 60000L)/1000);

                                //gán lại textview mỗi 1s
                                tvCountdown.setText(String.format("%02d:%02d:%02d", h, m, s));
                            }

                            @Override
                            public void onFinish() {
                                //hiển thị hoàn tất khi đếm ngược kết thúc
                                Toast.makeText(getActivity(), "Hoàn tất", Toast.LENGTH_LONG).show();
                                //hiển thị nút đặt giờ
                                btnPickTime.setVisibility(View.VISIBLE);
                                //ẩn nút stop
                                stopButton.setVisibility(View.GONE);
                                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.message);
                                mediaPlayer.start();
                            }
                        };

                        //ẩn nút đặt giờ khi bắt đầu đếm ngược
                        btnPickTime.setVisibility(View.INVISIBLE);
                        //bắt đầu đếm ngược
                        countDownTimer.start();
                        //hiển thị nút stop khi bắt đầu đếm ngược
                        stopButton.setVisibility(View.VISIBLE);
                    }
                }, 0, 0, 0, true);
                //hiển thị dialog
                myTimePickerDialog.show();
            }
        });

        //gán sự kiện khi nhấn nút stop khi đồng hồ đang đếm ngược
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dừng đếm ngược
                countDownTimer.cancel();
                //gán textview về 00:00:00
                tvCountdown.setText(getResources().getString(R.string.count_down_text_view));
                //hiển thị nút đặt giờ
                btnPickTime.setVisibility(View.VISIBLE);
                //ẩn nút stop
                stopButton.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Đã dừng đếm ngược", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    //hàm chuyển giờ, phút, giây sang ms
    //1h = 3,600,000ms
    //1m = 60,000ms
    //1s = 1,000s
    public static long convertToMs(int hour, int minute, int second){
        return hour * 3600000L + minute * 60000L + second * 1000L;
    }
}