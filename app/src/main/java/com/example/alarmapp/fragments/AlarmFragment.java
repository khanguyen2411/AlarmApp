package com.example.alarmapp.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.alarmapp.Alarm;
import com.example.alarmapp.AlarmReceiver;
import com.example.alarmapp.R;
import com.example.alarmapp.activities.AlarmActivity;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Objects;

public class AlarmFragment extends Fragment {

    NumberPicker npHour, npMinute;
    TextView tvTime;
    EditText etMessage;
    Button btSetAlarm, btTurnOffAlarm;
    Calendar alarmCalendar;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    public AlarmFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("DefaultLocale, SetTextI18n, UnspecifiedImmutableFlag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        //đối tượng SharedPreferences dùng để lưu dữ liệu báo thức
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //ánh xạ các view
        npHour = view.findViewById(R.id.npHour);
        npMinute = view.findViewById(R.id.npMinute);
        tvTime = view.findViewById(R.id.tvTime);
        etMessage = view.findViewById(R.id.etMessage);
        btSetAlarm = view.findViewById(R.id.btSetAlarm);
        btTurnOffAlarm = view.findViewById(R.id.btTurnOffAlarm);

        //khởi tạo một instance của calendar
        alarmCalendar = Calendar.getInstance();
        //khởi tạo alarm manager
        alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        //Intent trỏ đến AlarmReceiver
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //thiết lập cho number picker
        setupNumberPicker();

        //do đối tượng Alarm trong SharedPreferences được lưu dưới dạng json nên sử dụng thư viện gson để convert về object
        //lấy ra báo thức được lưu trong SharedPreferences và hiển thị lên khi fragment được render
        Gson gson = new Gson();
        String json = mPrefs.getString("alarm", "");
        Alarm alarm = gson.fromJson(json, Alarm.class);

        //nếu có báo thức được lưu và báo thức được bật thì gán dữ liệu lên tvTime
        if(alarm != null && alarm.isOn()){
            tvTime.setText(String.format("Báo thức vào lúc: %02d:%02d", alarm.getHour(), alarm.getMinute()));
        } else {
            tvTime.setText("Chưa đặt báo thức");
        }

        //sự kiện cho nút đặt báo thức
        btSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Khởi tạo đối tương Alarm mới
                Alarm alarm = new Alarm();
                //set các thuộc tính lấy từ number picker
                alarm.setHour(npHour.getValue());
                alarm.setMinute(npMinute.getValue());
                //toán tử 3 ngôi
                //nếu message trong etMessage là rỗng thì mặc đinh là "Đến giờ thức dậy rồi"
                alarm.setMessage(etMessage.getText().toString().isEmpty() ?
                        "Đến giờ thức dậy rồi" :
                        etMessage.getText().toString());
                //bật báo thức
                alarm.setOn(true);

                //xóa dữ liệu etMessage
                etMessage.setText("");

                //đặt thông tin báo thức lên tvTime
                tvTime.setText(String.format("Báo thức vào lúc: %02d:%02d", alarm.getHour(), alarm.getMinute()));

                //SharedPreferences.Editor để lưu dữ liệu vào SharedPreferences
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                //đối tượng gson để convert object sang json
                Gson gson = new Gson();
                String json = gson.toJson(alarm);
                //lưu object dưới dạng json vào SharedPreferences
                prefsEditor.putString("alarm", json);
                //lưu thay đổi
                prefsEditor.apply();

                //đặt thông tin báo thức vào đối tượng calendar
                alarmCalendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                alarmCalendar.set(Calendar.MINUTE, alarm.getMinute());
                //hiển thị thông báo
                Toast.makeText(getActivity(), "Đặt báo thức thành công", Toast.LENGTH_LONG).show();

                //kiểm tra nếu thời gian báo thức đã trôi qua (nhỏ hơn thời điểm hiện tại) thì tăng DATE thêm 1 ngày
                if(alarmCalendar.getTimeInMillis() < System.currentTimeMillis()){
                    alarmCalendar.add(Calendar.DATE, 1);
                }
                //đặt báo thức với alarmManager và pendingIntent đã tạo ở trên
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            }
        });

        //nút tắt báo thức
        btTurnOffAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lấy alarm đc lưu trong SharedPreferences
                Gson gson = new Gson();
                String json = mPrefs.getString("alarm", "");
                Alarm alarm = gson.fromJson(json, Alarm.class);

                //sửa isOn từ true -> false: tắt báo thức
                alarm.setOn(false);
                //lưu alarm vừa sửa vào trong SharedPreferences
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gsonSave = new Gson();
                String jsonSave = gsonSave.toJson(alarm);
                prefsEditor.putString("alarm", jsonSave);
                prefsEditor.apply();

                //sửa tvTime thành chưa đặt báo thức
                tvTime.setText("Chưa đặt báo thức");
                //hiển thị thông báo
                Toast.makeText(getActivity(), "Đã tắt báo thức", Toast.LENGTH_LONG).show();
                //hủy báo thức đến alarmManager với pendingIntent ở trên
                alarmManager.cancel(pendingIntent);
            }
        });

        return view;
    }

    void setupNumberPicker() {
        //khởi tạo calendar
        Calendar calendar = Calendar.getInstance();
        //khởi tạo formatter của number picker để hiển thị số 0 khi value < 10
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        };

        //đặt format cho number picker
        npHour.setFormatter(formatter);
        npMinute.setFormatter(formatter);
        //đặt giá trị lớn nhất, nhỏ nhất cho 2 NumberPicker
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);

        //đặt giá trị hiện tại của NumberPicker là giờ và phút hiện tại
        npHour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        npMinute.setValue(calendar.get(Calendar.MINUTE));
    }



}