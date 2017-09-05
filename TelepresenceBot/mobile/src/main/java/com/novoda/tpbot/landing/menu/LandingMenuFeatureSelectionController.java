package com.novoda.tpbot.landing.menu;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.novoda.tpbot.FeatureSelectionController;
import com.novoda.tpbot.FeatureSelectionPersistence;
import com.novoda.tpbot.R;
import com.novoda.tpbot.bot.service.ServiceConnectionSharedPreferencesPersistence;
import com.novoda.tpbot.bot.video.calling.VideoCallSharedPreferencesPersistence;

public final class LandingMenuFeatureSelectionController implements FeatureSelectionController<Menu, MenuItem> {

    @MenuRes
    private static final int FEATURE_MENU_RESOURCE = R.menu.feature_menu;

    private final MenuInflater menuInflater;
    private final SparseArray<FeatureSelectionPersistence> features;

    public static FeatureSelectionController<Menu, MenuItem> createFrom(MenuInflater menuInflater, Context context) {
        SparseArray<FeatureSelectionPersistence> features = new SparseArray<>();
        features.put(R.id.video_call_menu_item, VideoCallSharedPreferencesPersistence.newInstance(context));
        features.put(R.id.server_connection_menu_item, ServiceConnectionSharedPreferencesPersistence.newInstance(context));

        return new LandingMenuFeatureSelectionController(menuInflater, features);
    }

    private LandingMenuFeatureSelectionController(MenuInflater menuInflater, SparseArray<FeatureSelectionPersistence> features) {
        this.menuInflater = menuInflater;
        this.features = features;
    }

    @Override
    public void attachFeaturesTo(Menu componentToAttachTo) {
        menuInflater.inflate(FEATURE_MENU_RESOURCE, componentToAttachTo);

        for (int index = 0; index < features.size(); index++) {
            int key = features.keyAt(index);

            MenuItem menuItem = componentToAttachTo.findItem(key);
            FeatureSelectionPersistence featureSelectionPersistence = features.get(key);
            menuItem.setChecked(featureSelectionPersistence.isFeatureEnabled());
        }
    }

    @Override
    public void handleFeatureToggle(MenuItem featureRepresentation) {
        FeatureSelectionPersistence featureSelectionPersistence = features.get(featureRepresentation.getItemId());

        if (featureSelectionPersistence == null) {
            throw new IllegalStateException("You must check if data is present before using handleFeatureToggle().");
        }

        if (featureSelectionPersistence.isFeatureEnabled()) {
            featureRepresentation.setChecked(false);
            featureSelectionPersistence.setFeatureDisabled();
        } else {
            featureRepresentation.setChecked(true);
            featureSelectionPersistence.setFeatureEnabled();
        }
    }

    @Override
    public boolean contains(MenuItem featureRepresentation) {
        return features.get(featureRepresentation.getItemId()) != null;
    }

}
