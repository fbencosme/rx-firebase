@file:JvmName("Auth")

package rx.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import rx.*
import rx.lang.kotlin.*

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
    addOnCompleteListener { t ->
      if (t.isSuccessful)
        it.onSuccess(t.result.user)
      else
        it.onError(t.exception)
    }
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
