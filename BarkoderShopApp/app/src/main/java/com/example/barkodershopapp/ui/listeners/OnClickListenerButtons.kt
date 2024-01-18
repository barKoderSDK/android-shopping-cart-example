package com.example.barkodershopapp.ui.listeners

import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity

interface OnClickListenerButtons {

    fun onClickPlus(list : ListDataEntity)
    fun onClickMinus(list : ListDataEntity)
}