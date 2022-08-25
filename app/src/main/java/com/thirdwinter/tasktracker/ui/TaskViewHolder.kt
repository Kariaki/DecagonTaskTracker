package com.thirdwinter.tasktracker.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.data.recyclerview_helper.MainViewHolder
import com.data.recyclerview_helper.SuperClickListener
import com.kcoding.recyclerview_helper.SuperEntity
import com.thirdwinter.tasktracker.R
import com.thirdwinter.tasktracker.databinding.TaskComponentBinding
import com.thirdwinter.tasktracker.model.Task
import com.thirdwinter.tasktracker.ui.clickListener.TaskItemClickListener
import java.util.*
import kotlin.random.Random

class TaskViewHolder(private val itemView: View) : MainViewHolder(itemView) {

    private val binding: TaskComponentBinding = TaskComponentBinding.bind(itemView)
    private val colorSet = listOf(
        R.color.color1,
        R.color.color2,
        R.color.color4,
        R.color.color5
    )

    override fun bindPostType(
        types: SuperEntity,
        context: Context,
        clickListener: SuperClickListener
    ) {
        clickListener as TaskItemClickListener
        val task = types as Task
        binding.delete.setOnClickListener {
            clickListener.onDeleteClick(layoutPosition)
        }

        binding.content.text = task.title
        binding.checkBox.isChecked = task.completed

        val random = Random(colorSet.size)
        binding.root.setCardBackgroundColor(
            getColor(colorSet.random(), context)
        )


        binding.edit.setOnClickListener {
            clickListener.onEditClick(layoutPosition)
        }
        binding.root.setOnClickListener {
            clickListener.onClickItem(layoutPosition)
        }


    }

    private fun getColor(color: Int, context: Context) = ColorStateList.valueOf(
        ContextCompat.getColor(
            context,
           color
        )
    )
}