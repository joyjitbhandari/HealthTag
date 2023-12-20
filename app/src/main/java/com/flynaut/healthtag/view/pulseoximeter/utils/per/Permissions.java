package com.flynaut.healthtag.view.pulseoximeter.utils.per;

import android.app.Activity;

import com.flynaut.healthtag.databinding.FragmentAddDeciveBinding;
import com.flynaut.healthtag.view.addDevice.AddDeviceFragment;
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.BluetoothManager;
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceListDialog;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

/*
 * @deprecated Permission
 * @author zl
 * @date 2022/12/2 13:43
 */
public class Permissions {
    public static void all(Activity activity, BluetoothManager ble, DeviceListDialog dialog, FragmentAddDeciveBinding binding) {
        XXPermissions.with(activity)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.Group.BLUETOOTH)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (ble != null) ble.isOpen(dialog,binding);

                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) XXPermissions.startPermissionActivity(activity, permissions);
                    }
                });
    }
}
