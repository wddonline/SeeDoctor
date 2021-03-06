/*
 * Copyright 2016 Bit Cat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wdd.app.android.seedoctor.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by Bit Cat on 2016/12/28.
 */
class SettingExecutor implements SettingService {

    private Object object;

    SettingExecutor(@NonNull Object mObject) {
        object = mObject;
    }

    @Override
    public void execute() {
        Context context = PermissionUtils.getContext(object);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startAppSettings(object, intent);
    }

    @Override
    public void cancel() {
    }

    private static void startAppSettings(Object object, Intent intent) {
        if (object instanceof Activity) {
            ((Activity) object).startActivity(intent);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivity(intent);
        } else if (object instanceof android.app.Fragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ((android.app.Fragment) object).startActivity(intent);
            }
        }
    }

}