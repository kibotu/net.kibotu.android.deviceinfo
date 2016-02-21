package net.kibotu.android.deviceinfo.library;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.os.Build.VERSION_CODES.*;

/**
 * Created by Nyaruhodo on 21.02.2016.
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
