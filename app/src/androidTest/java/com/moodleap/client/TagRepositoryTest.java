package com.moodleap.client;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.dao.TagDao;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.repository.TagRepository;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TagRepositoryTest {
    private AppDatabase db;
    private TagRepository tagRepo;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        tagRepo = new TagRepository(db);
    }

    @Test
    public void testInsertTag() throws Exception {
        Tag tag = new Tag();
        tag.name = "study";
        tagRepo.insert(tag);
        Thread.sleep(200);
        tagRepo.getTags().observeForever(tags -> {
            assertNotNull(tags);
            assertEquals(1, tags.size());
            assertEquals("study", tags.get(0).name);
        });

    }

    @After
    public void closeDb() {
        db.close();
    }
}