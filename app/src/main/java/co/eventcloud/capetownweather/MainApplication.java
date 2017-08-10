package co.eventcloud.capetownweather;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import co.eventcloud.capetownweather.realm.RealmMigrationHandler;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import timber.log.Timber;

/**
 * The main application class. This is subclassed in order to initialise various libraries used in the app.
 *
 * <p/>
 *
 * Created by Laurie on 2017/08/10.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialise libs only meant to be used in debug builds
        if (BuildConfig.DEBUG) {
            // Init Timber, for logging
            Timber.plant(new Timber.DebugTree());

            // Init Stetho, for debugging using Chrome tools
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build());
        }

        // Initialise Realm
        Realm.init(this);
        RealmMigrationHandler.init(getApplicationContext());

        // Enable vector graphics as resources
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Initialise Fabric Crashlytics
        Fabric.with(this, new Crashlytics());
    }
}
