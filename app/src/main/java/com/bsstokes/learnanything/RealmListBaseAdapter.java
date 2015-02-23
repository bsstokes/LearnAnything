package com.bsstokes.learnanything;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmObject;

public abstract class RealmListBaseAdapter<T extends RealmObject> extends BaseAdapter {

    protected LayoutInflater inflater;
    protected Realm realm;
    protected RealmList<T> realmList;
    protected Context context;

    public RealmListBaseAdapter(Context context, Realm realm, RealmList<T> realmList, boolean automaticUpdate) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        if (null == realm) {
            throw new IllegalArgumentException("Realm cannot be null");
        }

        if (realmList == null) {
            throw new IllegalArgumentException("RealmList cannot be null");
        }

        this.context = context;
        this.realmList = realmList;
        this.inflater = LayoutInflater.from(context);
        if (automaticUpdate) {
            realm.addChangeListener(new RealmChangeListener() {
                @Override
                public void onChange() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getCount() {
        return realmList.size();
    }

    @Override
    public T getItem(int i) {
        return realmList.get(i);
    }

    /**
     * Returns the current ID for an item. Note that item IDs are not stable so you cannot rely on
     * the item ID being the same after {@link #notifyDataSetChanged()} or
     * {@link #updateRealmList(RealmList)} has been called.
     *
     * @param i Index of item in the adapter
     * @return Current item ID.
     */
    @Override
    public long getItemId(int i) {
        // TODO: find better solution once we have unique IDs
        return i;
    }

    /**
     * Update the RealmResults associated to the Adapter. Useful when the query has been changed.
     * If the query does not change you might consider using the automaticUpdate feature
     *
     * @param realmList the new RealmResults coming from the new query.
     */
    public void updateRealmList(RealmList<T> realmList) {
        this.realmList = realmList;
        notifyDataSetChanged();
    }
}
