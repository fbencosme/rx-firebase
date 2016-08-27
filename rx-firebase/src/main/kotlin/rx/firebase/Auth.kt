package rx.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import rx.Observable
import rx.lang.kotlin.observable
import rx.lang.kotlin.single
import rx.lang.kotlin.singleOf

class Auth {

  val auth: FirebaseAuth = FirebaseAuth.getInstance()

  inline fun signOut() =
    singleOf(FirebaseAuth.getInstance().signOut())

  inline fun currentUser() =
    single<FirebaseUser> {
      val u = FirebaseAuth.getInstance().currentUser
      if (u == null)
        it.onError(UnsignedAuthException())
      else
        it.onSuccess(u)
    }

  inline fun signIn(email: String, password: String) =
    auth.signInWithEmailAndPassword(email, password).toObservable()

  inline fun signUp(email: String, password: String) =
    auth.createUserWithEmailAndPassword(email, password).toObservable()

  inline fun signInAnonymously() =
    auth.signInAnonymously().toObservable()

  inline fun signInCredentials(c: AuthCredential) =
    auth.signInWithCredential(c).toObservable()

  inline fun signInToken(token: String) =
    auth.signInWithCustomToken(token).toObservable()

  inline fun Task<AuthResult>.toObservable() = single<FirebaseUser> {
    this
      .addOnSuccessListener { r -> it.onSuccess(r.user) }
      .addOnFailureListener { e -> it.onError(e) }
  }

  inline fun state(): Observable<FirebaseUser> {
    var listener: FirebaseAuth.AuthStateListener? = null

    return observable<FirebaseUser> {
      listener = FirebaseAuth.AuthStateListener { fa -> it.onNext(fa.currentUser) }
      listener?.let { l -> auth.addAuthStateListener(l) }
    }.doOnCompleted {
      listener?.let { l -> auth.removeAuthStateListener(l) }
    }
  }
}