package rx.firebase

import android.app.Application
import android.test.ApplicationTestCase
import com.google.firebase.auth.FirebaseAuth
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by fbencosme on 8/28/2016.
 */

inline fun String.klk() = "${this}klk"
class MyTest  : ApplicationTestCase<Application>(Application::class.java) {

  @Test
  fun testKlk() {
    var s = FirebaseAuth.getInstance()
    assertEquals("holaklk", "hola".klk())
  }
}