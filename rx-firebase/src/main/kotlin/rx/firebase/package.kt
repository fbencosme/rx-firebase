package rx.firebase

import com.google.firebase.auth.FirebaseAuthException

class UnsignedAuthException  : FirebaseAuthException("401", "no signed")

class DBException(msg: String) : Exception(msg)