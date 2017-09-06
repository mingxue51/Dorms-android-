/*

Copyright 2014 Marcin Polak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

package routedrawer.model;

import android.content.Context;
import android.support.annotation.Nullable;

import com.mc.youthhostels.R;

public enum TravelMode {
    DRIVING(R.string.method_driving, R.drawable.icon_type_car),
    WALKING(R.string.method_walking, R.drawable.icon_type_foot),
    BICYCLING(R.string.method_bicycling, R.drawable.icon_type_bike),
    TRANSIT(R.string.method_transit,  R.drawable.icon_type_transport);

    private int nameResource;
    private int iconResource;

    TravelMode(int nameResource, int iconRecource) {
       this.nameResource = nameResource;
       this.iconResource = iconRecource;
    }

    @Nullable
    public static TravelMode getBy(Context context, String name) {
        for (TravelMode travelMode : values()) {
            if (context.getResources().getString(travelMode.getNameResource()).equals(name)) {
                return travelMode;
            }
        }

        return null;
    }

    public int getIconResource() {
        return iconResource;
    }

    public int getNameResource() {
        return nameResource;
    }

    public void setNameResource(int nameResource) {
        this.nameResource = nameResource;
    }
}
