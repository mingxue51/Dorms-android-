package entity.User;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;

import entity.Generic.BaseMapper;


public class UserMapper extends BaseMapper {

	private final String LOG_TAG = UserMapper.class.getName();

	public UserMapper(Context context) {
		super(context);
	}

	public void saveEntity(User entity) {

		try {
			int id = super.user.extractId(entity);
			if (!super.user.idExists(id)) {
				int numRows = super.user.create(entity);
				/* return new CreateOrUpdateStatus(true, false, numRows); */
			} else {
				int numRows = super.user.update(entity);
				/* return new CreateOrUpdateStatus(false, true, numRows); */
			}
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Database exception", e);
		}
	}

	public void deleteAllEntities() {
		try {
			DeleteBuilder<User, Integer> deleteBuilder = super.user.deleteBuilder();
			int status = deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
