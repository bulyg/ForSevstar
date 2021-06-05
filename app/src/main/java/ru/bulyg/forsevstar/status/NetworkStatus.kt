package ru.bulyg.forsevstar.status

import io.reactivex.Observable

interface NetworkStatus {
    fun isOnline(): Observable<Boolean>
}