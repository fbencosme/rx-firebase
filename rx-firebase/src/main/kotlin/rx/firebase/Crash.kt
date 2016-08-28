@file:JvmName("Crash")

package rx.firebase

import com.google.firebase.crash.FirebaseCrash
import rx.lang.kotlin.singleOf

object Crash {

  inline fun crash(s: String) =
    singleOf(FirebaseCrash.log(s))

  inline fun report(e: Exception) =
    singleOf(FirebaseCrash.report(e))

  inline fun logcat(i: Int, s: String, d: String) =
    singleOf(FirebaseCrash.logcat(i, s, d))

}