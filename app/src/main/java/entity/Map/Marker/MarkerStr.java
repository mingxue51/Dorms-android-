package entity.Map.Marker;


import com.google.android.gms.maps.model.MarkerOptions;


public  class MarkerStr {
    private MarkerI mMarker;

    public MarkerStr(MarkerI marker)
    {
        mMarker = marker;
    }

    public MarkerOptions getMarker() {
        return new MarkerOptions();
    }
}