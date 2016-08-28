@file:JvmName("Analytics")

package rx.firebase

import android.content.Context
import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics

import rx.lang.kotlin.singleOf

object Analytics {

  inline fun logEvent(ctx: Context, name: String, b: Bundle) =
    singleOf(FirebaseAnalytics.getInstance(ctx).logEvent(name, b))

  inline fun userId(ctx: Context, id: String) =
    singleOf(FirebaseAnalytics.getInstance(ctx).setUserId(id))

  inline fun userProperty(ctx: Context, property: Pair<String, String>) =
    singleOf(FirebaseAnalytics.getInstance(ctx).setUserProperty(property.first, property.second))

}