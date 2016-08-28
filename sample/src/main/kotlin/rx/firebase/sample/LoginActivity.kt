package rx.firebase.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import rx.firebase.logInAnonymously

class LoginActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    val r = FirebaseAuth.getInstance()
    r.logInAnonymously()
  }

}

