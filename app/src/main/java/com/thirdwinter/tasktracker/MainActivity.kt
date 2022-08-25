package com.thirdwinter.tasktracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.recyclerview_helper.GeneralAdapter
import com.data.recyclerview_helper.MainViewHolder
import com.google.android.material.snackbar.Snackbar
import com.thirdwinter.tasktracker.databinding.ActivityMainBinding
import com.thirdwinter.tasktracker.model.Task
import com.thirdwinter.tasktracker.ui.TaskViewHolder
import com.thirdwinter.tasktracker.ui.clickListener.TaskItemClickListener
import com.thirdwinter.tasktracker.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskItemClickListener, View.OnClickListener,
    GeneralAdapter.ViewHolderPlug {

    lateinit var binding: ActivityMainBinding
    private val generalAdapter = GeneralAdapter()

    val viewModel: TaskViewModel by viewModels()
    var currentTask: Task? = null
    var allTask: List<Task> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //  binding.notePage.root.visibility=View.GONE


        generalAdapter.viewHolderPlug = this
        generalAdapter.superClickListener = this

        binding.notePage.save.setOnClickListener(this)

        binding.actionButton.setOnClickListener(this)
        binding.filter.setOnClickListener(this)

        binding.notePage.edit.setOnClickListener(this)

        binding.taskRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = generalAdapter
        }

        viewModel.editing.observe(this){
            if(it) {
                binding.notePage.root.visibility = View.VISIBLE
                return@observe
            }
            binding.notePage.root.visibility=View.GONE
        }

        viewModel.currentTask.observe(this) {
            currentTask = it
            binding.notePage.title.setText(it?.title)
        }

        viewModel.descending.observe(this){
            displayTask(it)
        }

    }

    private fun displayTask(ascending:Boolean) {
        viewModel.getTask(ascending).observe(this) {
            if(it.isEmpty()){
                binding.notePage.root.visibility=View.VISIBLE
            }
            generalAdapter.items = it
            allTask = it
            generalAdapter.notifyDataSetChanged()
        }
    }

    override fun onDeleteClick(position: Int) {
        viewModel.deleteTask(allTask[position])
        generalAdapter.notifyItemRemoved(position)

        Snackbar.make(binding.root, "Task has been deleted", Snackbar.LENGTH_SHORT).show()

    }

    override fun onEditClick(position: Int) {
        val currentTask = allTask[position]
        viewModel.setCurrentTask(currentTask)
    }

    /**
     * Complete task on item clicked
     */
    override fun onClickItem(position: Int) {
        val task = allTask[position].apply {
            completed = !completed
        }
        viewModel.completeTask(task)
    }


    private fun saveTask() {
        val title = binding.notePage.title.text.toString()
        if (title.isEmpty()) {
            Snackbar.make(binding.root, "Task title can not be empty", Snackbar.LENGTH_SHORT).show()
            return
        }

        val task = if (currentTask == null)
            Task(title)
        else {
            Task(
                id = currentTask!!.id,
                title = title,
                timeStamp = currentTask!!.timeStamp
            )
        }

        viewModel.insertTask(task)

        binding.notePage.title.text.clear()
        if (currentTask == null) {
            generalAdapter.notifyItemInserted(allTask.size)
        }

        Snackbar.make(binding.root, "Task saved", Snackbar.LENGTH_SHORT).show()

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.actionButton -> {
                binding.notePage.root.visibility = View.VISIBLE
            }
            binding.filter->{
                viewModel.sort()
            }
            binding.notePage.edit->{
                viewModel.edit()
            }
            else -> {
                saveTask()
            }
        }
    }

    override fun setPlug(group: ViewGroup, viewType: Int): MainViewHolder {
        val contentView = LayoutInflater.from(this).inflate(R.layout.task_component, group, false)
        return TaskViewHolder(contentView)
    }
}
