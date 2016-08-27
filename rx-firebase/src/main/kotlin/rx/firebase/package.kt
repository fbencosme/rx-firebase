package rx.firebase

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseError

class UnsignedAuthException  : FirebaseAuthException("401", "no signed")

class DBException(msg: String) : Exception(msg)

fun DatabaseError.exception() = DBException("${code} ${message} ${details}")