package net.kibotu.android.deviceinfo.library.version;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.os.Build.VERSION_CODES.BASE;
import static android.os.Build.VERSION_CODES.BASE_1_1;
import static android.os.Build.VERSION_CODES.CUPCAKE;
import static android.os.Build.VERSION_CODES.DONUT;
import static android.os.Build.VERSION_CODES.ECLAIR;
import static android.os.Build.VERSION_CODES.ECLAIR_0_1;
import static android.os.Build.VERSION_CODES.ECLAIR_MR1;
import static android.os.Build.VERSION_CODES.FROYO;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static android.os.Build.VERSION_CODES.GINGERBREAD_MR1;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.HONEYCOMB_MR1;
import static android.os.Build.VERSION_CODES.HONEYCOMB_MR2;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.KITKAT_WATCH;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Nyaruhodo on 21.02.2016.
 *
 * @see <a href="http://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels">What is API Level?</a>
 */

@IntDef({BASE,
        BASE_1_1,
        CUPCAKE,
        DONUT,
        ECLAIR,
        ECLAIR_0_1,
        ECLAIR_MR1,
        FROYO,
        GINGERBREAD,
        GINGERBREAD_MR1,
        HONEYCOMB,
        HONEYCOMB_MR1,
        HONEYCOMB_MR2,
        ICE_CREAM_SANDWICH,
        ICE_CREAM_SANDWICH_MR1,
        JELLY_BEAN,
        JELLY_BEAN_MR1,
        JELLY_BEAN_MR2,
        KITKAT,
        KITKAT_WATCH,
        LOLLIPOP,
        LOLLIPOP_MR1,
        M})
@Retention(RetentionPolicy.SOURCE)
public @interface VersionCode {
}
