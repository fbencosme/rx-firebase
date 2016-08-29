package rx.firebase.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.textChanges
import kotlinx.android.synthetic.main.activity_login.*
import rx.Observable
import rx.firebase.*


class LoginActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    anonymousBtn
      .clicks()
      .flatMap {
        FirebaseAuth
          .getInstance()
          .logInAnonymously()
          .doOnError { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
          .onErrorReturn<FirebaseUser> { null }
          .toObservable()
      }
      .filter { it != null }
      .subscribe {
        Toast.makeText(this, "Anonymous", Toast.LENGTH_LONG).show()
      }

    val form = Observable.combineLatest(email.textChanges(), password.textChanges(),
      { user, pwd -> Pair(user.toString(), pwd.toString()) })

    signInBtn
      .clicks()
      .flatMap { form }
      .filter {
        listOf(it.first, it.second).all { it.isNotEmpty() }
      }
      .flatMap {
        FirebaseAuth
          .getInstance()
          .logIn(it.first, it.second)
          .doOnError { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
          .onErrorReturn<FirebaseUser> { null }
          .toObservable()
      }
      .filter { it != null }
      .subscribe {
        Toast.makeText(this, "logIn", Toast.LENGTH_LONG).show()
      }

    FirebaseDatabase
      .getInstance()
      .reference
      .childEvents()
      .doOnError {   Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
      .onErrorReturn<Pair<ChildEvent, DataSnapshot>> { null  }
      .filter { it != null }
      .subscribe {
        Toast.makeText(this, "${it.first} ${it.second.value}", Toast.LENGTH_LONG).show()
      }

  }

}

