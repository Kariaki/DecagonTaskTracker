package com.thirdwinter.tasktracker.ui.clickListener

import com.data.recyclerview_helper.SuperClickListener

interface TaskItemClickListener :SuperClickListener {

    fun onDeleteClick(position:Int)

    fun onEditClick(position: Int)

}