package net.kibotu.android.deviceinfo.library.version;

import android.os.Build;

import java.lang.reflect.Field;

/**
 * Created by Nyaruhodo on 06.03.2016.
 */
public class Version {

    public static boolean isAtLeastVersion(@VersionCode final int version) {
        return Build.VERSION.SDK_INT >= version;
    }

        public static String getOsAsString(final int sdk) {
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (sdk == fieldValue)
                return fieldName;
        }
        return "";
    }
}
