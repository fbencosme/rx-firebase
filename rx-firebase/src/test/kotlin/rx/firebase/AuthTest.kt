package rx.firebase

import android.app.Application
import android.test.ApplicationTestCase
import org.junit.Test

class AuthTest : ApplicationTestCase<Application>(Application::class.java) {

  @Test
  fun logInAnonymously(): Unit = TODO()

  @Test
  fun signIn(): Unit = TODO()

  @Test
  fun signOut(): Unit = TODO()

  @Test
  fun createUserWithEmailAndPassword(): Unit = TODO()

  @Test
  fun currentUser(): Unit = TODO();

  @Test
  fun signInWithCredential() : Unit = TODO()

  @Test
  fun logInToken() : Unit = TODO()

  @Test
  fun state() : Unit = TODO()
}