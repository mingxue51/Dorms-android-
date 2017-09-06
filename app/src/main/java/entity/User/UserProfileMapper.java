package entity.User;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import entity.Generic.BaseMapper;


public class UserProfileMapper extends BaseMapper {

    private final String LOG_TAG = UserProfileMapper.class.getName();

    public UserProfileMapper(Context context) {
        super(context);
    }

    public void saveEntity(UserProfile entity) {
        try {
            int id = super.userProfile.extractId(entity);
            if (!super.userProfile.idExists(id)) {
                int numRows = super.userProfile.create(entity);
                /* return new CreateOrUpdateStatus(true, false, numRows); */
            } else {
                int numRows = super.userProfile.update(entity);
				/* return new CreateOrUpdateStatus(false, true, numRows); */
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Database exception", e);
        }

    }

    public void deleteAllEntities() {
        try {
            DeleteBuilder<UserProfile, Integer> deleteBuilder = super.userProfile.deleteBuilder();
            int status = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public UserProfile getUserProfile() {
    	UserProfile entity = new UserProfile();
        try{
            QueryBuilder<UserProfile, Integer> queryBuilder = userProfile.queryBuilder();
            PreparedQuery<UserProfile> preparedQuery = queryBuilder.prepare();
        	entity = this.userProfile.queryForFirst(preparedQuery);
        }catch (SQLException e){
        	e.printStackTrace();
        }
        return entity;
    }
}
