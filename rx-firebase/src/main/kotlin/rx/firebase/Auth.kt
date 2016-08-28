@file:JvmName("Auth")

package rx.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import rx.Observable
import rx.Single
import rx.lang.kotlin.observable
import rx.lang.kotlin.single
import rx.lang.kotlin.singleOf

inline fun FirebaseAuth.logOut(): Single<Unit> =
  singleOf(signOut())

inline fun FirebaseAuth.currentUser(): Single<FirebaseUser> =
  single<FirebaseUser> {
    if (currentUser == null)
      it.onError(UnsignedAuthException())
    else
      it.onSuccess(currentUser)
  }

inline fun FirebaseAuth.logIn(email: String, password: String): Single<FirebaseUser> =
  signInWithEmailAndPassword(email, password).toSingle()

inline fun FirebaseAuth.createUser(email: String, password: String): Single<FirebaseUser> =
  createUserWithEmailAndPassword(email, password).toSingle()

inline fun FirebaseAuth.logInAnonymously(): Single<FirebaseUser> =
  signInAnonymously().toSingle()

inline fun FirebaseAuth.logIn(c: AuthCredential): Single<FirebaseUser> =
  signInWithCredential(c).toSingle()

inline fun FirebaseAuth.logInToken(token: String): Single<FirebaseUser> =
  signInWithCustomToken(token).toSingle()

inline fun Task<AuthResult>.toSingle(): Single<FirebaseUser> =
  single<FirebaseUser> {
    this
      .addOnSuccessListener { r -> it.onSuccess(r.user) }
      .addOnFailureListener { e -> it.onError(e) }
  }

inline fun FirebaseAuth.state(): Observable<FirebaseUser> {
  var listener: FirebaseAuth.AuthStateListener? = null

  return observable<FirebaseUser> {
    listener = FirebaseAuth.AuthStateListener { fa -> it.onNext(fa.currentUser) }
    listener?.let { l -> addAuthStateListener(l) }
  }.doOnCompleted {
    listener?.let { l -> removeAuthStateListener(l) }
  }
}
