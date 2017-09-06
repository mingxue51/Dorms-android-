package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import entity.Generic.Country;
import entity.Generic.Currency;
import entity.Generic.Gender;
import entity.Generic.Language;
import entity.User.User;
import entity.User.UserProfile;


public class DatabaseHelperOrm extends SQLiteOpenHelper {

    protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

    private static final String DATABASE_NAME = "youthhostels.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    protected Dao<User, Integer> user = null;
    protected Dao<UserProfile, Integer> userProfile = null;
    protected Dao<Gender, Integer> gender = null;
    protected Dao<Country, Integer> country = null;


    public DatabaseHelperOrm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseConnection conn = connectionSource.getSpecialConnection();
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true);
            try {
                connectionSource.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException(
                        "Could not save special connection", e);
            }
        }
        try {
            onCreate();
        } finally {
            if (clearSpecial) {
                connectionSource.clearSpecialConnection(conn);
            }
        }
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseConnection conn = connectionSource.getSpecialConnection();
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true);
            try {
                connectionSource.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException(
                        "Could not save special connection", e);
            }
        }
        try {
            onUpgrade(oldVersion, newVersion);
        } finally {
            if (clearSpecial) {
                connectionSource.clearSpecialConnection(conn);
            }
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        user = null;
        userProfile = null;
        country = null;
        gender = null;
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It
     * will create it or just give the cached value.
     */
    public Dao<User, Integer> getUserDao() throws SQLException {
        if (user == null) {
            user = getDao(User.class);
        }
        return user;
    }

    public Dao<UserProfile, Integer> getUserProfileDao() throws SQLException {
        if (userProfile == null) {
            userProfile = getDao(UserProfile.class);
        }
        return userProfile;
    }

    public Dao<Country, Integer> getCountryDao() throws SQLException {
        if (country == null) {
            country = getDao(Country.class);
        }
        return country;
    }
    public Dao<Gender, Integer> getGenderDao() throws SQLException {
        if (gender == null) {
            gender = getDao(Gender.class);
        }
        return gender;
    }


    private void onCreate() {
        try {
            Log.i(DatabaseHelperOrm.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, UserProfile.class);
            TableUtils.createTable(connectionSource, Gender.class);
            TableUtils.createTable(connectionSource, Country.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelperOrm.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    private void onUpgrade(int oldVersion, int newVersion) {
/*		try {
            Log.i(DatabaseHelperOrm.class.getName(), "onUpgrade");
//			TableUtils.dropTable(connectionSource, Video.class, true);
			// after we drop the old databases, we create the new ones
			onCreate();
		} catch (SQLException e) {
			Log.e(DatabaseHelperOrm.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}*/
    }

    protected <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
            throws SQLException {
        // lookup the dao, possibly invoking the cached database config
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            // try to use our new reflection magic
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
            if (tableConfig == null) {
                /**
                 * TODO: we have to do this to get to see if they are using the
                 * deprecated annotations like {@link DatabaseFieldSimple}.
                 */
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
            } else {
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource,
                        tableConfig);
            }
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}
