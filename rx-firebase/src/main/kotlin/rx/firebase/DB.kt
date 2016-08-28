package rx.firebase

import com.google.firebase.database.*
import rx.Observable
import rx.lang.kotlin.observable

enum class ChildEvent {
  Moved, Changed, Added, Removed, Cancelled
}

class DB {

  val db = FirebaseDatabase.getInstance()

  inline fun buildRef(url: String): DatabaseReference =
    if (url.isEmpty()) db.reference else db.getReferenceFromUrl(url)

  inline fun <reified T : Any> DatabaseReference.childs(): Observable<Pair<ChildEvent, T>> =
    childEvents().map { Pair(it.first, it.second.getValue(T::class.java)) }

  inline fun DatabaseReference.childEvents(): Observable<Pair<ChildEvent, DataSnapshot>> {
    var listener: ChildEventListener? = null

    val childs = observable<Pair<ChildEvent, DataSnapshot>> {

      listener = object : ChildEventListener {
        override fun onChildMoved(ds: DataSnapshot?, p: String?) {
          ds?.let { d -> it.onNext(Pair(ChildEvent.Moved, d)) }
        }

        override fun onChildChanged(ds: DataSnapshot?, p: String?) {
          ds?.let { d -> it.onNext(Pair(ChildEvent.Changed, d)) }
        }

        override fun onChildAdded(ds: DataSnapshot?, p: String?) {
          ds?.let { d -> it.onNext(Pair(ChildEvent.Added, d)) }
        }

        override fun onChildRemoved(ds: DataSnapshot?) {
          ds?.let { d -> it.onNext(Pair(ChildEvent.Removed, d)) }
        }

        override fun onCancelled(error: DatabaseError?) {
          error?.let { e -> it.onError(DBException("${e.code} ${e.message} ${e.details}")) }
        }
      }

      addChildEventListener(listener)
    }

    childs.doOnCompleted { listener?.let { l -> removeEventListener(l) } }
    return childs
  }

  inline fun <reified T : Any> DatabaseReference.single(): Observable<T> =
    singleEvent().map { it.getValue(T::class.java) }

  inline fun DatabaseReference.singleEvent(): Observable<DataSnapshot> {

    var listener: ValueEventListener? = null

    val single = observable<DataSnapshot> {

      listener = object : ValueEventListener {
        override fun onDataChange(ds: DataSnapshot) = it.onNext(ds)

        override fun onCancelled(error: DatabaseError) =
          error?.let { e -> it.onError(e.exception()) }
      }

      addListenerForSingleValueEvent(listener)
    }

    single.doOnCompleted { listener?.let { l -> removeEventListener(l) } }

    return single
  }

  inline fun <reified T : Any> DatabaseReference.value(): Observable<T> =
    valueEvent().map { it.getValue(T::class.java) }

  inline fun DatabaseReference.valueEvent(): Observable<DataSnapshot> {

    var listener: ValueEventListener? = null

    val single = observable<DataSnapshot> {

      listener = object : ValueEventListener {
        override fun onDataChange(ds: DataSnapshot) = it.onNext(ds)

        override fun onCancelled(error: DatabaseError) =
          error?.let { e -> it.onError(e.exception()) }
      }

      addValueEventListener(listener)
    }

    single.doOnCompleted { listener?.let { l -> removeEventListener(l) } }

    return single
  }

  inline fun <reified T : Any> DatabaseReference.putValue(value: T) = rx.lang.kotlin.single<T> {

    push().setValue(value) { e: DatabaseError, ref: DatabaseReference ->
      if (e == null)
        it.onSuccess(value)
      else
        it.onError(e.exception())
    }
  }
}