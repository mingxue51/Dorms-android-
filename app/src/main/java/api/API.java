package api;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.mc.youthhostels.entity.Order;
import com.mc.youthhostels.entity.UserReview;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Property.Properties;
import entity.Property.Property;
import entity.Property.Search.SearchCity;
import entity.Property.Search.SearchProperty;
import entity.User.User;
import entity.User.UserProfile;
import helper.App;
import helper.H;
import helper.IGetRequest;
import helper.Root;
import rx.Observable;
import rx.Subscriber;

public class API extends Root {


    public static String API_KEY = "123456";

    private static API mSingleton = null;

    public static API getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new API(context);
        }
        mSingleton.setContext(context);
        return mSingleton;
    }

    public static API getInstance() {
        return getInstance(App.getInstance());
    }

    protected API(Context context) {
        super(context);
    }

    private void run(Runnable run, boolean isAsync) {
        try {
            run.run();
        } catch (Exception e) {
            H.logE("Run API thread error.", e.getMessage());
            H.logE(e);
        } finally {
        }
    }

    public void login(String login, String password, IGetRequest callback) {
        run(new Login(this, login, password, callback), true);
    }

    public void contactUs(String firstName, String lastName, String email,
                          String message, String subject, IGetRequest callback) {
        run(new ContactUs(this,firstName, lastName, email, message, subject, callback), true);
    }

    public void forgetPassword(String email, IGetRequest callabck) {
        run(new ForgetPassword(this, email, callabck), true);
    }

    public void signUp(String firstName, String lastName,
                         String email, String password,IGetRequest callback) {
        run(new SignUp(this, firstName, lastName, email, password, callback), true);
    }

    public void Register(UserProfile profile, IGetRequest callback) {
        run(new Register(this, profile, callback), true);
    }

    public void Signout(IGetRequest callback) {
        run(new Signout(this, callback), true);
    }

    public void cancelAccount(IGetRequest callback) {
        run(new CancelAccount(this, callback), true);
    }

    public void ChangePassword(String oldPassword, String newPassword, IGetRequest callback) {
        run(new ChangePassword(this, oldPassword, newPassword, callback), true);
    }

    public void GetUserProfile(IGetRequest callback) {
        run(new GetUserProfile(this, callback), true);
    }

    public void updateProfile(UserProfile userProfile, IGetRequest callback) {
        run(new UpdateProfile(this, userProfile, callback), true);
    }

    public void GetCurrencyList(IGetRequest callback) {
        run(new GetCurrencyList(this, callback), true);
    }

    public void GetCitiyNCountryList(IGetRequest callback) {
        run(new GetCitiyNCountryList(this, callback), true);
    }

    public void GetLanguageList(IGetRequest callback) {
        run(new GetLanguageList(this, callback), true);
    }

    public void GetGenderList(IGetRequest callback) {
        run(new GetGenderList(this, callback), true);
    }

    public void GetSuggestions(SearchCity srchCity, IGetRealTime cbRTData) {
        run(new GetSuggestions(this, srchCity, cbRTData), true);
    }

    public void getPropertyDetails(String propertyNumber, IGetRealTimeObject callback) {
        run(new GetPropertyDetails(this, propertyNumber, callback), true);
    }

    public void GetContent(String query, IGetRealTimeObject callback) {
        run(new GetContent(this, query, callback), true);
    }

    public void GetPropertyImages(Property pProperty, IGetRealTimeObject callback) {
        run(new GetPropertyImages(this, pProperty, callback), true);
    }

    public void GetPropertyReviews(String propertyNumber, IGetRealTimeObject callback) {
        run(new GetPropertyReviews(this, propertyNumber, callback), true);
    }

    public void getPropertyAvailability(SearchProperty query, IGetRealTimeObject callback) {
        run(new GetPropertyAvailability(this, query, callback), true);
    }

    public void bookProperty(Order order, IGetRealTimeObject callback) {
        run(new BookProperty(this, order, callback), true);
    }

    public void getUserBookings(IGetRealTimeObject callback) {
        run(new GetUserBookings(this, callback), true);
    }

    public void getUserReviews(IGetRealTimeObject callback) {
        run(new GetUserReviews(this, callback), true);
    }

    public void addToFavourite(Property property, IGetRealTimeObject callback) {
        run(new AddToFavourite(this,
                               property.getFavouriteId(),
                               property.getArrivalDate(),
                               property.getDepartureDate(),
                               property.getPropertyNumber(),
                               property.getFavouriteNote(),
                               callback),
            true);
    }

    public void addToFavourite(String favouriteId,
                               Date arrivalDate,
                               Date departureDate,
                               String propertyNumber,
                               String favouriteNote,
                               IGetRealTimeObject callback) {
        run(new AddToFavourite(this,
                               favouriteId,
                               arrivalDate,
                               departureDate,
                               propertyNumber,
                               favouriteNote,
                               callback),
            true);
    }

    public void getFavourites(IGetRealTimeObject callback) {
        run(new GetFavourites(this, callback), true);
    }

    public void deleteFavorite(Property property, IGetRequest callback) {
        run(new DeleteFavorite(this, property, callback), true);
    }

    public void postPropertyReview(UserReview userReview, IGetRealTimeObject callback) {
        run(new PostPropertyReview(this, userReview, callback), true);
    }

    public void GetPropertiesByLocationAndDate(SearchProperty query, IGetRealTimeObject callback) {
        run(new GetPropertiesByLocationAndDate(this, query, callback), true);
    }

    public void GetPropertiesByGeocodes(SearchProperty query, IGetRealTimeObject callback) {
        run(new GetPropertiesByGeocodes(this, query, callback), true);
    }

    public void GetCurrentLocationByGeocodes(LatLng latlng, IGetRealTimeObject callback) {
        run(new GetCurrentLocationByGeocodes(this, latlng, callback), true);
    }

    public void GetCountryList(IGetRequest callback) {
        run(new GetCountryList(this, callback), true);
    }

    public void doFacebookLogin(String accessToken, IGetRequest callback) {
        run(new FacebookLogin(this, accessToken, callback), true);
    }

    public void doGooglePlusLogin(String accessToken,IGetRequest callback) {
        run(new GooglePlusLogin(this, accessToken, callback), true);
    }

    public void getDistrictCoords(String districtId, IGetRequest callback) {
        run(new GetDistrictPolygon(this, districtId, callback), true);
    }

    public abstract static interface IMultiRequest {
        public JSONObject getParams(String hashName);

        public void onCurrent(Object obj);

        public void onStart();

        public void onFinish();

        public void onError(String message);
    }

    public abstract static interface IGetRealTime {
        public void getData(List<?> list);//public void getData(Properties properties);//

        public void onError(String message);
    }

    public abstract static interface IGetRealTimeObject {
        public void getData(Object object);

        public void onError(String message);
    }


    public Observable<Property> searchByProperty(String propertyId) {
        return Observable.create(new Observable.OnSubscribe<Property>() {
            @Override
            public void call(Subscriber<? super Property> subscriber) {
                API.getInstance(App.getInstance()).getPropertyDetails(propertyId, new API.IGetRealTimeObject() {
                    @Override
                    public void getData(final Object properties) {
                        subscriber.onNext((Property) properties);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(String message) {
                        // todo on handle this if need
                    }
                });
            }
        });
    }

    public Observable<Properties> searchByLocation(SearchProperty searchProperty) {
        return Observable.create(new Observable.OnSubscribe<Properties>() {
            @Override
            public void call(Subscriber<? super Properties> subscriber) {
                API.getInstance(App.getInstance()).GetPropertiesByLocationAndDate(searchProperty, new API.IGetRealTimeObject() {
                    @Override
                    public void getData(final Object properties) {
                        subscriber.onNext((Properties) properties);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(String message) {
                        // todo on handle this if need
                    }
                });
            }
        });
    }

    public Observable<Properties> searchByGeocodes(SearchProperty searchProperty) {
        return Observable.create(new Observable.OnSubscribe<Properties>() {
            @Override
            public void call(Subscriber<? super Properties> subscriber) {
                API.getInstance(App.getInstance()).GetPropertiesByGeocodes(searchProperty, new API.IGetRealTimeObject() {
                    @Override
                    public void getData(final Object properties) {
                        subscriber.onNext((Properties) properties);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(String message) {
                        // todo on handle this if need
                    }
                });
            }
        });
    }

    public Observable<List<Property>> getFavourites() {
        return Observable.create(new Observable.OnSubscribe<List<Property>>() {
            @Override
            public void call(Subscriber<? super List<Property>> subscriber) {
                if (!User.isLogged()) {
                    subscriber.onNext(new ArrayList<Property>());
                    subscriber.onCompleted();
                    return;
                }

                API.getInstance(App.getInstance()).getFavourites(new API.IGetRealTimeObject() {
                    @Override
                    public void getData(final Object properties) {
                        subscriber.onNext((List<Property>) properties);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(String message) {
                        H.logE("error whilte fetching favourites");
                        subscriber.onNext(new ArrayList<Property>());
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }


}
