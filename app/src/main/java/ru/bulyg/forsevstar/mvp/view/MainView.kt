package ru.bulyg.forsevstar.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface MainView : MvpView {
    fun init()
    fun setTemp(temp: String)
    fun setIcon(url: String)
    fun setDescription(desc: String)
    fun showAlertDialog()
}