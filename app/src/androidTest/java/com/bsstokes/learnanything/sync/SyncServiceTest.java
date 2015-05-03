package com.bsstokes.learnanything.sync;

import android.test.ServiceTestCase;
import android.test.mock.MockApplication;
import android.test.mock.MockContext;

import com.bsstokes.learnanything.sync.SyncService;

import junit.framework.TestCase;

public class SyncServiceTest extends ServiceTestCase<SyncService> {

    public SyncServiceTest() {
        super(SyncService.class);
    }

    public void testSmoke() {

//        setApplication(new MockApplication());
//        setContext(new MockContext());

        SyncService.startActionSyncTopicTree(getContext());
        assertTrue(true);
    }
}