package il.co.rootsapp

import android.content.Context
import android.util.Log
import android.widget.Button
import androidx.core.view.size
import androidx.test.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.impl.utils.SynchronousExecutor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import junit.framework.TestCase
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SleepWorker(context: Context, parameters: WorkerParameters) :
    Worker(context, parameters) {

    override fun doWork(): Result {
        // Sleep on a background thread.
        Thread.sleep(1000)
        return Result.success()
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class UnitTests {

    private var mainActivityController: ActivityController<MainActivity>? = null
    private lateinit var context: Context
    private lateinit var executor: Executor

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mainActivityController = Robolectric.buildActivity(MainActivity::class.java)
    }

    @Test
    fun when_activityIsUpFirstTime_than_recycleView_should_beEmpty() {
        // setup
        mainActivityController!!.create().visible()
        val activityUnderTest = mainActivityController!!.get()
        val recycleView = activityUnderTest.rootsRecycleView
        assertEquals(0, recycleView.size)
    }

    @Test
    fun when_addButtonIsClicked_than_alert_should_show() {
        // setup
        mainActivityController!!.create().visible()
        val activityUnderTest = mainActivityController!!.get()

        val addButton = activityUnderTest.findViewById<FloatingActionButton>(R.id.addButton)
        addButton.performClick()
        assertTrue(activityUnderTest.alert.isShowing)
    }

    @Test
    fun addition_isCorresct() {
        // setup
        mainActivityController!!.create().visible()
        val activityUnderTest = mainActivityController!!.get()

        val addButton = activityUnderTest.findViewById<FloatingActionButton>(R.id.addButton)
        addButton.performClick()
        activityUnderTest.alert.cancel()
        assertFalse(activityUnderTest.alert.isShowing)
    }
}