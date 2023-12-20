package com.flynaut.healthtag.view.pulseoximeter.utils.device_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import com.clj.fastble.data.BleDevice;
import com.flynaut.healthtag.R;
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.BluetoothManager;
public class DeviceListDialog {
    protected Dialog dialog;
    protected Context context;
    protected DeviceAdapter adapter;
    protected BluetoothManager ble;

    public DeviceListDialog(Context context, BluetoothManager ble, DeviceAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        this.ble = ble;
        dialog = new Dialog(context, R.style.my_dialog);
    }

    public void show() {
        if (dialog != null) {
            dialog.setContentView(R.layout.dialog_device_list);
            dialog.setCancelable(false);
            event(dialog);
            dialog.show();
            size(dialog);
        }
    }

    private void event(Dialog dialog) {
        ble.scan();
        ListView listView = dialog.findViewById(R.id.list_view);

        ImageButton close = dialog.findViewById(R.id.close);
        ImageButton refresh = dialog.findViewById(R.id.refresh);
        close.setOnClickListener(v -> dismiss());
        refresh.setOnClickListener(v -> ble.scan());
        listView.setSelector(R.color.transparent);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            BleDevice bleDevice = adapter.getDevices().get(position);

            if (bleDevice != null) {
                ble.conn(bleDevice);
                dismiss();
            }
        });
    }

    public void dismiss() {
        if (dialog != null) dialog.dismiss();
    }

    private void size(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width();
        lp.height = height();
        window.setAttributes(lp);
    }

    private int width() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (displayMetrics.widthPixels * 0.9f);
    }

    private int height() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (displayMetrics.heightPixels * 0.6f);
    }
}

